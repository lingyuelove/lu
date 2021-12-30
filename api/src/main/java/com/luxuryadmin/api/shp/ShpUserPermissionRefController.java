package com.luxuryadmin.api.shp;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.ShpUser;
import com.luxuryadmin.entity.shp.ShpUserPermissionRef;
import com.luxuryadmin.entity.shp.ShpUserShopRef;
import com.luxuryadmin.enums.shp.EnumShpOperateLogModule;
import com.luxuryadmin.enums.shp.EnumShpUserType;
import com.luxuryadmin.param.shp.ParamAddShpOperateLog;
import com.luxuryadmin.param.shp.ParamEmployeePerm;
import com.luxuryadmin.param.shp.ParamEmployeePermAdd;
import com.luxuryadmin.service.fin.FinSalaryService;
import com.luxuryadmin.service.shp.*;
import com.luxuryadmin.vo.shp.VoEmployee;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户权限--控制层
 *
 * @author monkey king
 * @date 2019-12-30 15:09:24
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user", method = RequestMethod.POST)
@Api(tags = {"E003.【员工】模块"}, description = "/shop/user |员工列表,身份,权限的接口 ")
public class ShpUserPermissionRefController extends BaseController {

    @Autowired
    private ShpUserService shpUserService;

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    @Autowired
    private ShpUserPermissionRefService shpUserPermissionRefService;

    @Autowired
    private FinSalaryService finSalaryService;

    @Autowired
    private ShpOperateLogService shpOperateLogService;

    @Autowired
    private ShpUserTypeService shpUserTypeService;

    /**
     * 添加员工(分配身份)<br/>
     * 判断该帐号是否存在;<br/>
     * 判断该帐号是否已经是店铺员工;<br/>
     * 分配权限
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "添加员工(分配权限)",
            notes = "添加员工(分配权限)",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestMapping("/addEmployee")
    @RequiresPermissions("employee:check")
    public BaseResult addEmployee(@RequestParam Map<String, String> params,
                                  @Valid ParamEmployeePermAdd employeePerm, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        int shopId = getShopId();
        shpUserShopRefService.isEmployeeLimit(shopId);
        String username = employeePerm.getUsername();
        String permIds = employeePerm.getPermIds();
        String name = employeePerm.getName();
        String addType = employeePerm.getAddType();
        Integer userId;
        VoEmployee voEmployee = shpUserService.geVoEmployeeByUsername(username);
        int userTypeId = Integer.parseInt(employeePerm.getUserTypeId());
        boolean isNewUser = false;
        //帐号不存在,注册新账号并绑定员工关系
        if (ConstantCommon.ONE.equals(addType)) {
            //为当前的手机号码注册新帐号;
            userId = shpUserService.registerShpUserWithRandomPwd(getShopName(), username, getIpAddr());
            isNewUser = true;
        } else {
            if (LocalUtils.isEmptyAndNull(voEmployee)) {
                return BaseResult.errorResult(EnumCode.ERROR_NOT_EXIST_USER);
            }
            userId = voEmployee.getUserId();
            voEmployee.setUsername(username);
            validVoEmployee(voEmployee);
        }
        List<ShpUserPermissionRef> list = pickShpUserPermissionRef(userId, permIds);
        //让新员工重新登录
        String tokenKey = ConstantRedisKey.getShpTokenKey(username);
        String token = redisUtil.get(tokenKey);
        shpUserService.exitLogin(token);
        shpUserPermissionRefService.saveOrUpdateBatchShpUserPermissionRef(userId, shopId, name, userTypeId, list, getUserId());
        if (isNewUser) {
            //为新注册的用户修改昵称名称
            ShpUser newUser = new ShpUser();
            newUser.setId(userId);
            newUser.setNickname(name);
            newUser.setUpdateTime(new Date());
            shpUserService.updateShpUser(newUser);
        }

        //为新员工创建本月的薪资
        finSalaryService.initFinSalary(shopId, userId, new Date());

        //添加【店铺操作日志】-【添加员工】
        Integer operateUserId = getUserId();
        String usernameEmp = shpUserShopRefService.getNameFromShop(shopId, userId);
        String userType = shpUserShopRefService.getUserTypeRoleNameByShopIdAndUserId(shopId, userId);
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(getShopId());
        paramAddShpOperateLog.setOperateUserId(operateUserId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.STAFF.getName());
        paramAddShpOperateLog.setOperateName("添加员工");
        //获取userType
        paramAddShpOperateLog.setOperateContent(usernameEmp + "-" + userType);
        paramAddShpOperateLog.setProdId(null);
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(request);
        shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);

        return BaseResult.okResult();
    }

    /**
     * 修改员工(分配权限)
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "修改员工(分配权限)",
            notes = "修改员工(分配权限)",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/updateEmployee")
    @RequiresPermissions("employee:check")
    public BaseResult updateEmployee(@RequestParam Map<String, String> params,
                                     @Valid ParamEmployeePerm employeePerm, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String name = employeePerm.getName();
        String permIds = employeePerm.getPermIds();
        int userId = Integer.parseInt(employeePerm.getUserId());
        int boss = EnumShpUserType.BOSS.getCode();
        ShpUserShopRef shpUserShopRef = shpUserShopRefService.getShpUserShopRefByUserId(getShopId(), userId);
        int paramUserTypeId = Integer.parseInt(employeePerm.getUserTypeId());
        if (LocalUtils.isEmptyAndNull(shpUserShopRef) || !ConstantCommon.ONE.equals(shpUserShopRef.getState())) {
            return BaseResult.defaultErrorWithMsg("员工帐号为禁用状态，请先启用帐号！");
        }
        int userTypeId = shpUserShopRef.getFkShpUserTypeId();
        if (boss == userTypeId && paramUserTypeId != boss) {
            return BaseResult.defaultErrorWithMsg("该帐号身份为【经营者】，不允许通过该方式进行变更！");
        }
        if (boss == userTypeId) {
            //经营者, 只允许更改在店铺的昵称
            shpUserShopRef.setName(name);
            shpUserShopRef.setUpdateTime(new Date());
            shpUserShopRefService.updateUserShopRef(shpUserShopRef);
        } else {
            List<ShpUserPermissionRef> list = pickShpUserPermissionRef(userId, permIds);
            shpUserPermissionRefService.saveOrUpdateBatchShpUserPermissionRef(userId, getShopId(), name, paramUserTypeId, list, getUserId());
        }
        return BaseResult.okResult();
    }


    /**
     * 被添加员工是否已注册帐号
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "被添加员工是否已注册帐号",
            notes = "被添加员工是否已注册帐号",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "username", value = "帐号"),
    })
    @RequestRequire
    @RequestMapping("/existsUsername")
    public BaseResult existsUsername(@RequestParam Map<String, String> params) {
        String username = params.get("username");
        VoEmployee voEmployee = shpUserService.geVoEmployeeByUsername(username);
        if (LocalUtils.isEmptyAndNull(voEmployee)) {
            return BaseResult.errorResult(EnumCode.OK_NOT_EXIST_USER_SHOP);
        }
        voEmployee.setUsername(username);
        validVoEmployee(voEmployee);
        return BaseResult.okResult(voEmployee);
    }

    private void validVoEmployee(VoEmployee voEmployee) {
        Integer userId = voEmployee.getUserId();
        if (userId == getUserId()) {
            throw new MyException("不允许添加自己为员工!");
        }
        if (!ConstantCommon.ONE.equals(voEmployee.getState())) {
            throw new MyException("帐号：" + voEmployee.getUsername() + " 已被系统禁用!");
        }
        boolean existsShpUserRoleRef = shpUserShopRefService.existsShpUserShopRef(getShopId(), userId);
        if (existsShpUserRoleRef) {
            throw new MyException("该帐号已是该店铺员工!");
        }
    }

    /**
     * 封装ShpUserPermissionRef实体
     *
     * @param userId
     * @param permIds
     * @return
     */
    private List<ShpUserPermissionRef> pickShpUserPermissionRef(int userId, String permIds) {
        if (!LocalUtils.isEmptyAndNull(permIds)) {
            List<ShpUserPermissionRef> list = new ArrayList<>();
            String[] ids = permIds.split(";");
            Date date = new Date();
            for (String permId : ids) {
                int permIdInt = Integer.parseInt(permId);
                ShpUserPermissionRef shpUserPerm = shpUserPermissionRefService.packShpUserPermissionRef(getShopId(), userId, permIdInt, getUserId());
                list.add(shpUserPerm);
                //2.6.0之前的自有商品权限; 临时做兼容. 2.6.1版本之后, 可以去掉这段代码;
                int newPermId = 0;
                if (permIdInt == 10085) {
                    //自有商品的新版id
                    newPermId = 10311;
                } else if (permIdInt == 10087) {
                    //寄卖商品的新版id
                   newPermId = 10312;
                } else if (permIdInt == 10088) {
                    //其它商品的新版id
                    newPermId = 10313;
                }
                if (newPermId > 0) {
                    ShpUserPermissionRef shpUserPerm2 = shpUserPermissionRefService.packShpUserPermissionRef(getShopId(), userId, newPermId, getUserId());
                    list.add(shpUserPerm2);
                }

                int oldPermId = 0;
                if (permIdInt == 10311) {
                    //自有商品的新版id
                    oldPermId = 10085;
                } else if (permIdInt == 10312) {
                    //寄卖商品的新版id
                    oldPermId = 10087;
                } else if (permIdInt == 10313) {
                    //其它商品的新版id
                    oldPermId = 10088;
                }
                if (oldPermId > 0) {
                    ShpUserPermissionRef shpUserPerm2 = shpUserPermissionRefService.packShpUserPermissionRef(getShopId(), userId, oldPermId, getUserId());
                    list.add(shpUserPerm2);
                }
            }
            return list;
        }
        return null;
    }

}
