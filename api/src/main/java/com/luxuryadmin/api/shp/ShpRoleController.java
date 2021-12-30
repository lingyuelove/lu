package com.luxuryadmin.api.shp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.ShpRole;
import com.luxuryadmin.param.shp.ParamShpRole;
import com.luxuryadmin.param.shp.ParamShpRoleUpdate;
import com.luxuryadmin.service.shp.ShpPermissionService;
import com.luxuryadmin.service.shp.ShpRolePermissionRefService;
import com.luxuryadmin.service.shp.ShpRoleService;
import com.luxuryadmin.vo.shp.VoRolePermissionRel;
import com.luxuryadmin.vo.shp.VoShpPermission;
import com.luxuryadmin.vo.shp.VoShpRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改于2020-09-04 17:30:54
 * 店铺角色控制层(店铺角色已弃用)
 *
 * @author monkey king
 * @date 2019-12-30 15:09:24
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user", method = RequestMethod.POST)
@Api(tags = {"F002.【角色】模块"}, description = "/shop/user |【角色权限】模块的接口 ")
@ApiIgnore
public class ShpRoleController extends BaseController {


    @Autowired
    private ShpRoleService shpRoleService;

    @Autowired
    private ShpRolePermissionRefService shpRolePermissionRefService;

    @Autowired
    private ShpPermissionService shpPermissionService;


    /**
     * 加载店铺角色
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载店铺角色",
            notes = "加载店铺角色",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "pageNum", value = "当前页数"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token")
    })
    @RequestRequire
    @RequestMapping("/listShopRole")
    @RequiresPermissions("role:check")
    public BaseResult listShopRole(@RequestParam Map<String, String> params) {
        int shopId = getShopId();
        PageHelper.startPage(getPageNum(), PAGE_SIZE_30);
        List<VoShpRole> roleList = shpRoleService.listShpRole(shopId);
        HashMap<String, Object> hashMap = LocalUtils.getHashMap("shopRoleList", roleList);
        return BaseResult.okResult(hashMap);
    }

    /**
     * 进入【创建角色】页面
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "进入【创建角色】页面",
            notes = "进入【创建角色】页面",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token")
    })
    @RequestRequire
    @GetMapping("/goAddShpRolePage")
    public BaseResult goAddShpRolePage(@RequestParam Map<String, String> params) {
        List<VoShpPermission> shpPermissionList = shpPermissionService.groupByShpPermission(null);
        return LocalUtils.getBaseResult(shpPermissionList);
    }


    /**
     * 进入【修改角色】页面
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "【进入【修改角色】页面",
            notes = "进入【修改角色】页面",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "roleId", value = "角色id"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token")
    })
    @RequestRequire
    @GetMapping("/goUpdateShpRolePage")
    public BaseResult goUpdateShpRolePage(@RequestParam Map<String, String> params) {
        String roleIdStr = params.get("roleId");
        int roleId = Integer.parseInt(roleIdStr);
        List<VoShpPermission> shpPermissionList = shpPermissionService.groupByShpPermission(null);
        HashMap<String, Object> hashMap = LocalUtils.getHashMap("shopPermList", shpPermissionList);
        List<VoRolePermissionRel> rolePermList = shpRolePermissionRefService.listRolePermsRelByRoleId(getShopId(), roleId);
        hashMap.put("rolePermList", rolePermList);
        return LocalUtils.getBaseResult(hashMap);
    }


    /**
     * 创建(店铺)角色
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "创建(店铺)角色",
            notes = "创建(店铺)角色",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestMapping("/addShpRole")
    @RequiresPermissions("role:addRole")
    public BaseResult addShpRole(@RequestParam Map<String, String> params,
                                 @Valid ParamShpRole shpRole1, BindingResult result) {
        servicesUtil.validControllerParam(result);
        int roleNum = shpRoleService.countRoleNum(getShopId());
        if (roleNum >= 100) {
            return BaseResult.defaultErrorWithMsg("最多只能创建100个角色!");
        }
        int row = packShpRole(shpRole1, true);
        return BaseResult.okResult(row);
    }


    /**
     * 修改角色
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "修改角色",
            notes = "修改角色",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/updateShpRole")
    @RequiresPermissions("role:updateRole")
    public BaseResult updateShpRole(@RequestParam Map<String, String> params,
                                    @Valid ParamShpRoleUpdate update, BindingResult result) {
        servicesUtil.validControllerParam(result);
        //查看角色是否存在
        boolean existsShpRole = shpRoleService.existsShpRole(getShopId(), Integer.parseInt(update.getRoleId()));
        if (!existsShpRole) {
            return BaseResult.defaultErrorWithMsg("该角色不存在!");
        }
        String roleId = update.getRoleId();
        isSysCreateRole(Integer.parseInt(roleId));
        int row = packShpRole(update, false);
        return BaseResult.okResult(row);
    }

    private int packShpRole(ParamShpRole paramShpRole, boolean isSave) {
        String roleId = paramShpRole.getRoleId();
        String roleName = paramShpRole.getRoleName();
        String permStrIds = paramShpRole.getPermIds();
        //角色附带着权限;
        String[] ids = permStrIds.split(";");
        Integer[] permIds = new Integer[ids.length];
        for (int i = 0; i < ids.length; i++) {
            permIds[i] = Integer.parseInt(ids[i]);
        }
        ShpRole shpRole = new ShpRole();

        Date date = new Date();
        if (isSave) {
            shpRole.setInsertTime(date);
            shpRole.setInsertAdmin(getUserId());
        } else {
            shpRole.setId(Integer.parseInt(roleId));
            shpRole.setUpdateAdmin(getUserId());
            shpRole.setUpdateTime(date);
        }
        shpRole.setFkShpShopId(getShopId());
        shpRole.setRoleName(roleName);
        shpRole.setRemark(paramShpRole.getRemark());
        return shpRoleService.saveOrUpdateShpRole(shpRole, permIds,isSave);
    }

    /**
     * 删除(店铺)角色
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "删除(店铺)角色",
            notes = "删除(店铺)角色",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "int",
                    name = "roleId", value = "角色Id"),
    })
    @RequestRequire
    @RequestMapping("/deleteShopRole")
    @RequiresPermissions("role:deleteRole")
    public BaseResult deleteShopRole(@RequestParam Map<String, String> params) {
        String roleIdStr = params.get("roleId");
        int roleId = LocalUtils.isEmptyAndNull(roleIdStr) ? 0 : Integer.parseInt(roleIdStr);
        isSysCreateRole(roleId);
        int rows = shpRoleService.deleteShopRole(getShopId(), getUserId(), roleId);
        return BaseResult.okResult(rows);
    }


    /**
     * 获取角色对应的权限
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "获取角色对应的权限",
            notes = "获取角色对应的权限",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "int",
                    name = "roleId", value = "角色Id"),
    })
    @RequestRequire
    @RequestMapping("/loadPermissionFromRole")
    public BaseResult loadPermissionFromRole(@RequestParam Map<String, String> params) {
        String roleId = params.get("roleId");
        List<VoRolePermissionRel> rolePermissionRelList = shpRolePermissionRefService.listRolePermsRelByRoleId(getShopId(), Integer.parseInt(roleId));
        return LocalUtils.getBaseResult(rolePermissionRelList);
    }

    /**
     * 系统创建的角色不允许更改
     *
     * @param roleId
     */
    private void isSysCreateRole(int roleId) {
        //系统创建角色不允许删除;
        boolean sysCreateRole = shpRoleService.isSysCreateRole(getShopId(), roleId);
        if (sysCreateRole) {
            throw new MyException("系统创建的角色不允许更改!");
        }
    }


}
