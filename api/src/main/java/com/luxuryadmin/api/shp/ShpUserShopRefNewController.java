package com.luxuryadmin.api.shp;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.ShpUser;
import com.luxuryadmin.entity.shp.ShpUserType;
import com.luxuryadmin.enums.shp.EnumShpOperateLogModule;
import com.luxuryadmin.enums.shp.EnumShpUserType;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.shp.*;
import com.luxuryadmin.service.shp.*;
import com.luxuryadmin.vo.shp.VoEmployee;
import com.luxuryadmin.vo.shp.VoShpPermIndex;
import com.luxuryadmin.vo.shp.VoUserPermission;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工权限入口
 *
 * @author monkey king
 * @date 2021-12-01 23:34:26
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user", method = RequestMethod.POST)
@Api(tags = {"E004.【员工权限】模块"}, description = "/shop/user | 员工列表,删除员工")
@RequiresPermissions("employee:check")
public class ShpUserShopRefNewController extends ShpUserBaseController {

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    @Autowired
    private ShpUserTypeService shpUserTypeService;

    @Autowired
    private ShpUserService shpUserService;


    @Autowired
    private ShpPermIndexService permIndexService;

    //@Autowired
    //private ShpUserPermissionTplService shpUserPermissionTplService;

    @Autowired
    private ShpPermTplService shpPermTplService;

    @Autowired
    private ShpPermUserRefService shpPermUserRefService;

    @Autowired
    private ShpOperateLogService shpOperateLogService;

    /**
     * 员工管理
     *
     * @return Result
     */
    @ApiOperation(
            value = "员工管理",
            notes = "员工管理",
            httpMethod = "POST")
    @RequestMapping("/listEmployee")
    public BaseResult listEmployee(@Valid ParamEmployee paramEmployee, BindingResult result) {
        servicesUtil.validControllerParam(result);
        paramEmployee.setShopId(getShopId());
        String state = paramEmployee.getState();
        formatParamEmployee(paramEmployee);
        PageHelper.startPage(getPageNum(), PAGE_SIZE_100);
        List<VoEmployee> voEmployeeList = shpUserShopRefService.listUserShopRefByShopId(paramEmployee);
        //在职员工才做置顶处理;
        List<VoEmployee> newEmployee = new ArrayList<>();
        VoEmployee employee = null;
        if (!LocalUtils.isEmptyAndNull(voEmployeeList)) {
            for (VoEmployee voEmployee : voEmployeeList) {
                formatVoEmployee(voEmployee);
                if (voEmployee.getUserId() == getUserId()) {
                    employee = voEmployee;
                }
            }
            if (ConstantCommon.ONE.equals(state) && !LocalUtils.isEmptyAndNull(employee)) {
                //把当前用户置顶;
                voEmployeeList.remove(employee);
                newEmployee.add(employee);
            }
        }
        newEmployee.addAll(voEmployeeList);
        Map<String, String> countMap = shpUserShopRefService.countEmployee(getShopId());
        HashMap<String, Object> hashMap = LocalUtils.getHashMap(newEmployee);
        hashMap.putAll(countMap);
        return BaseResult.okResult(hashMap);
    }

    private void formatParamEmployee(ParamEmployee paramEmployee) {
        String queryKey = paramEmployee.getQueryKey();
        if (!LocalUtils.isEmptyAndNull(queryKey)) {
            paramEmployee.setUsername(DESEncrypt.encodeUsername(queryKey));
        }
        //排序字段;normal(默认排序);time(加入时间);name(昵称排序);
        String sortValue = paramEmployee.getSortValue();
        final String desc = "DESC";
        String sortKey = paramEmployee.getSortKey();
        String sortKeyDb;
        switch (sortKey + "") {
            //按照加入时间排序或者按照默认排序;
            case "time":
                if (desc.equalsIgnoreCase(sortValue)) {
                    sortKeyDb = "uShopR.`insert_time` DESC, uShopR.`id` DESC";
                } else {
                    sortKeyDb = "uShopR.`insert_time` ASC, uShopR.`id` ASC";
                }
                break;
            case "name":
                if (desc.equalsIgnoreCase(sortValue)) {
                    sortKeyDb = "CONVERT( uShopR.`name` USING gbk) DESC, uShopR.`id` DESC";
                } else {
                    sortKeyDb = "CONVERT( uShopR.`name` USING gbk) ASC, uShopR.`id` ASC";
                }
                break;
            default:
                sortKeyDb = "IFNULL(sutype.`sort`,99) ASC,sutype.`id` ASC, us.`id` ASC";
                break;
        }
        paramEmployee.setSortKeyDb(sortKeyDb);
    }


    /**
     * 初始化【添加】员工权限页面
     *
     * @param paramToken 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "初始化【添加】员工权限页面",
            notes = "初始化【添加】员工权限页面,初始化用户类型，店铺角色",
            httpMethod = "POST")
    @RequestMapping("/initAddEmployee")
    public BaseResult initAddEmployee(@Valid ParamToken paramToken, BindingResult result) {
        servicesUtil.validControllerParam(result);
        //员工上限判断;
        shpUserShopRefService.isEmployeeLimit(getShopId());
        return BaseResult.okResult(initPermParam());
    }


    /**
     * 进入【修改】员工权限页面
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "进入【修改】员工权限页面",
            notes = "进入【修改】员工权限页面,加载员工已有的角色",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestMapping("/initUpdateEmployee")
    public BaseResult initUpdateEmployee(@RequestParam Map<String, String> params,
                                         @Valid ParamTokenUserId tokenUserId, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String userIdStr = tokenUserId.getUserId();
        int userId = Integer.parseInt(userIdStr);
        HashMap<String, Object> hashMap = initPermParam();
        //获取用户对应的权限;
        List<VoUserPermission> userPermList;
        int bossUserId = getBossUserId();
        //如果经营者
        if (userId == bossUserId) {
            List<Integer> permIds = permIndexService.listAppShpPermissionId();
            userPermList = new ArrayList<>();
            for (Integer permId : permIds) {
                VoUserPermission perm = new VoUserPermission();
                perm.setPermId(permId.toString());
                userPermList.add(perm);
            }
            //经营者默认全部权限
            VoUserPermission perm = new VoUserPermission();
            perm.setPermId("1");
            userPermList.add(perm);
        } else {
            userPermList = shpPermUserRefService.listUserPermByShopIdAndUserId(getShopId(), userId);
        }
        hashMap.put("userPermList", userPermList);
        return BaseResult.okResult(hashMap);
    }

    /**
     * 添加员工和修改员工界面所需要的初始化数据
     *
     * @return HashMap
     */
    private HashMap<String, Object> initPermParam() {
        HashMap<String, Object> hashMap = new HashMap<>(16);
        try {
            String showOnApp = "showOnApp";
            BasicParam bp = getBasicParam();
            String appVersion = bp.getAppVersion();
            //用户身份
            List<ShpUserType> userTypeList = shpUserTypeService.listShpUserTypeByShopId(getShopId());
            hashMap = LocalUtils.getHashMap("userTypeList", userTypeList);
            //店铺所有权限 String version99
            List<VoShpPermIndex> shpPermIndexList = permIndexService.groupByShpPermIndex(showOnApp, bp.getPlatform(), appVersion);
            for (VoShpPermIndex vo : shpPermIndexList) {
                List<VoShpPermIndex> permissionList = vo.getPermIndexList();
                if (!LocalUtils.isEmptyAndNull(permissionList)) {
                    permissionList.removeIf(vo1 -> "-9".equals(vo1.getOnlyShopId()) && getBossUserId() != getUserId());
                    permissionList.removeIf(vo1 -> !LocalUtils.isEmptyAndNull(vo1.getOnlyShopId()) && vo1.getOnlyShopId().length()
                            >= 5 && !vo1.getOnlyShopId().contains(getShopId() + ""));
                }
            }
            hashMap.put("shopPermList", shpPermIndexList);
            return hashMap;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return hashMap;
    }

    /**
     * 根据身份去获取对应的模板权限;
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "根据身份去获取对应的模板权限;",
            notes = "根据身份去获取对应的模板权限;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestMapping("/getUserTypePerm")
    public BaseResult getUserTypePerm(@RequestParam Map<String, String> params,
                                      @Valid ParamUserTypeId paramUserTypeId, BindingResult result) {
        servicesUtil.validControllerParam(result);
        int userTypeId = Integer.parseInt(paramUserTypeId.getUserTypeId());
        List<VoUserPermission> permTpl = shpPermTplService.listPermTplByTplName(getShopId(), userTypeId);
        HashMap<String, Object> hashMap = LocalUtils.getHashMap("permTplList", permTpl);
        return BaseResult.okResult(hashMap);
    }


    /**
     * 修改员工状态
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "修改员工状态",
            notes = "修改员工状态,可停用员工帐号,删除员工帐号;(仅针对本店铺)",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestMapping("/updateEmployeeState")
    public BaseResult updateEmployeeState(@RequestParam Map<String, String> params,
                                          @Valid ParamModifyEmployee employee, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        int operateUserId = getUserId();
        int shopId = getShopId();
        String userIdStr = employee.getUserId();
        String refIdStr = employee.getRefId();
        int userId = Integer.parseInt(userIdStr);
        int refId = Integer.parseInt(refIdStr);
        String state = employee.getState();
        //判断权限;后期单独拆开接口

        if (ConstantCommon.ONE.equals(state)) {
            shpUserShopRefService.isEmployeeLimit(getShopId());
        }
        boolean updatePerm = hasPermWithCurrentUser("employee:check");
        boolean deletePerm = hasPermWithCurrentUser("employee:check");
        //状态 -9删除 0：禁用(或辞职)   1：正常
        if ("-9".equals(state)) {
            //需要删除权限
            if (!deletePerm) {
                return BaseResult.errorResult(EnumCode.ERROR_NO_SHOP_PERMISSION);
            }
        } else {
            if (!updatePerm) {
                return BaseResult.errorResult(EnumCode.ERROR_NO_SHOP_PERMISSION);
            }
        }

        String userType = shpUserShopRefService.getUserTypeByShopIdAndUserId(shopId, userId);
        if (EnumShpUserType.BOSS.getCode().toString().equals(userType)) {
            //经营者身份不允许改变员工状态;
            return BaseResult.defaultErrorWithMsg("【经营者】类型帐号不允许更改状态！");
        }


        if ("0".equals(state) || "-9".equals(state)) {
            //员工被停用或者从店铺删除之后,更新选择登录的店铺id到用户表
            ShpUser shpUser = shpUserService.packShpUserForLogin(getUserId(), 0, "0");
            shpUserService.updateShpUser(shpUser);
        }

        shpUserShopRefService.modifyEmployeeState(shopId, userId, refId, state);

        //让新员工重新登录
        String username = shpUserService.getUsernameByUserId(userId);

        String tokenKey = ConstantRedisKey.getShpTokenKey(username);
        String token = redisUtil.get(tokenKey);
        shpUserService.exitLogin(token);

        if ("-9".equals(state)) {
            //添加【店铺操作日志】-【删除员工】
            String usernameEmp = shpUserShopRefService.getNameFromShop(shopId, userId);
            String userTypeEmp = shpUserShopRefService.getUserTypeRoleNameByShopIdAndUserId(shopId, userId);
            ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
            paramAddShpOperateLog.setShopId(shopId);
            paramAddShpOperateLog.setOperateUserId(operateUserId);
            paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.STAFF.getName());
            paramAddShpOperateLog.setOperateName("删除员工");
            paramAddShpOperateLog.setOperateContent(usernameEmp + "-" + userTypeEmp);
            paramAddShpOperateLog.setProdId(null);
            paramAddShpOperateLog.setOrderId(null);
            paramAddShpOperateLog.setRequest(request);

            shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        }
        return BaseResult.okResult();
    }


}
