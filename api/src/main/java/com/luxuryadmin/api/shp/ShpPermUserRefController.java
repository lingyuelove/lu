package com.luxuryadmin.api.shp;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.ShpPermIndex;
import com.luxuryadmin.entity.shp.ShpPermUserRef;
import com.luxuryadmin.entity.shp.ShpUser;
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
import org.apache.commons.collections.ListUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * APP权限2.0
 * 用户权限--控制层
 *
 * @author monkey king
 * @date 2021-12-05 00:14:52
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user", method = RequestMethod.POST)
@Api(tags = {"E003.【员工】模块"}, description = "/shop/user |员工列表,身份,权限的接口 ")
@RequiresPermissions("employee:check")
public class ShpPermUserRefController extends BaseController {

    @Autowired
    private ShpUserService shpUserService;

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    @Autowired
    private ShpPermUserRefService shpPermUserRefService;

    @Autowired
    private FinSalaryService finSalaryService;

    @Autowired
    private ShpOperateLogService shpOperateLogService;

    @Autowired
    private ShpUserTypeService shpUserTypeService;

    @Autowired
    private ShpPermIndexService shpPermIndexService;

    /**
     * 添加员工(分配身份)<br/>
     * 判断该帐号是否存在;<br/>
     * 判断该帐号是否已经是店铺员工;<br/>
     * 分配权限
     *
     * @return Result
     */
    @ApiOperation(
            value = "添加员工(分配权限)",
            notes = "添加员工(分配权限)",
            httpMethod = "POST")
    @RequestMapping("/addEmployeeNew")
    public BaseResult addEmployeeNew(@Valid ParamEmployeePermAdd param,
                                     BindingResult result, HttpServletRequest request) throws Exception {
        servicesUtil.validControllerParam(result);
        int shopId = getShopId();
        shpUserShopRefService.isEmployeeLimit(shopId);
        String username = param.getUsername();
        String permIds = param.getPermIds();
        String name = param.getName();
        String addType = param.getAddType();
        Integer userId;
        VoEmployee voEmployee = shpUserService.geVoEmployeeByUsername(username);
        int userTypeId = Integer.parseInt(param.getUserTypeId());
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
        List<ShpPermUserRef> list = pickShpPermUserRef(userId, permIds);
        //让新员工重新登录
        String tokenKey = ConstantRedisKey.getShpTokenKey(username);
        String token = redisUtil.get(tokenKey);
        shpUserService.exitLogin(token);
        shpPermUserRefService.saveOrUpdateBatchShpPermUserRef(userId, shopId, name, userTypeId, list, getUserId());
        //shpPermUserRefService.refreshOldPerm(shopId, userId, list);
        if (isNewUser) {
            //为新注册的用户修改昵称名称
            ShpUser newUser = new ShpUser();
            newUser.setId(userId);
            newUser.setNickname(name);
            newUser.setUpdateTime(new Date());
            shpUserService.updateShpUser(newUser);
        }
        //创建薪资0:不创建 | 1:创建
        if (ConstantCommon.ONE.equals(param.getCreateSalary())) {
            //为新员工创建本月的薪资
            finSalaryService.initFinSalary(shopId, userId, new Date());
        }
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
    @RequestMapping("/updateEmployeeNew")
    public BaseResult updateEmployeeNew(@RequestParam Map<String, String> params,
                                        @Valid ParamEmployeePerm employeePerm, BindingResult result) throws Exception {
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
            List<ShpPermUserRef> list = pickShpPermUserRef(userId, permIds);
            shpPermUserRefService.saveOrUpdateBatchShpPermUserRef(userId, getShopId(), name, paramUserTypeId, list, getUserId());
            //shpPermUserRefService.refreshOldPerm(getShopId(), userId, list);
        }
        return BaseResult.okResult();
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
    private List<ShpPermUserRef> pickShpPermUserRef(int userId, String permIds) {
        if (!LocalUtils.isEmptyAndNull(permIds)) {
            List<ShpPermUserRef> list = new ArrayList<>();
            String[] ids = permIds.split(";");
            String shareModule = "share:check:module";
            // 小程序店铺,分享商品,拼图制作,文本制作
            String[] sharePerms = new String[]{"share:check:miniProgramShop", "share:check:shareProd", "share:check:friendBatchImg", "share:check:makeText"};
            ShpPermIndex shpPermIndex = shpPermIndexService.getShpPermissionByPermission(shareModule);
            Integer permIdDb = -1;
            if (!LocalUtils.isEmptyAndNull(shpPermIndex)) {
                permIdDb = shpPermIndex.getId();
            }
            List<String> newList = new ArrayList<>(Arrays.asList(ids));
            //是否包含整个分享模块
            if (newList.contains(permIdDb + "")) {
                List<String> permIdList = shpPermIndexService.listPermIndexByPermission(sharePerms);
                newList.addAll(permIdList);
            }
            for (String permId : newList) {
                int permIdInt = Integer.parseInt(permId);
                ShpPermUserRef shpUserPerm = shpPermUserRefService.packShpPermUserRef(getShopId(), userId, permIdInt, getUserId());
                list.add(shpUserPerm);
            }
            return list;
        }
        return null;
    }

}
