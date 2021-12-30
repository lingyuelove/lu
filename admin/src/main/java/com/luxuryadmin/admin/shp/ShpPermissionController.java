package com.luxuryadmin.admin.shp;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.ShpPermission;
import com.luxuryadmin.param.shp.ParamShpPermission;
import com.luxuryadmin.param.shp.ParamShpPermissionUpdate;
import com.luxuryadmin.service.shp.ShpPermissionService;
import com.luxuryadmin.vo.shp.VoShpPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author monkey king
 * @Date 2019/12/01 4:07
 */
@Slf4j
@RestController
@RequestMapping(value = "/sys")
@Api(tags = {"10.【PC权限】模块", "10.【PC权限】模块"}, description = "/sys/admin | 平台后台系统模块 ")
public class ShpPermissionController extends BaseController {

    @Autowired
    private ShpPermissionService shpPermissionService;


    /**
     * 加载APP权限列表
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载APP权限列表",
            notes = "加载APP权限列表",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
    })
    @RequestRequire
    @RequestMapping("/loadShpPermission")
    @RequiresPerm(value = "system:shop")
    public BaseResult loadShpPermission(@RequestParam Map<String, String> params) {
        List<VoShpPermission> voShpPermissions = shpPermissionService.groupByShpPermission("-1");
        return LocalUtils.getBaseResult(voShpPermissions);
    }


    /**
     * 添加app端权限
     *
     * @return Result
     */
    @ApiOperation(
            value = "添加app端权限",
            notes = "添加app端权限",
            httpMethod = "POST")
    @RequestMapping("/addShpPermission")
    public BaseResult addShpPermission(@Valid ParamShpPermission permission, BindingResult result) {
        servicesUtil.validControllerParam(result);
        ShpPermission perm = pickShpPermission(permission, true);
        String permName = perm.getName();
        boolean exists = shpPermissionService.existsShpPermission(perm.getParentId(), permName);
        if (exists) {
            return BaseResult.defaultErrorWithMsg("【" + permName + "】已存在该模块!");
        }

        String code = permission.getCode();
        String perms = permission.getPermission();
        validPermission(code, perms);
        Integer uid = getAdminUserId();
        perm.setInsertAdmin(uid);
        refreshPerm();
        return BaseResult.okResult(shpPermissionService.saveShpPermission(perm));
    }

    /**
     * 更新app端权限
     *
     * @return Result
     */
    @ApiOperation(
            value = "更新app端权限",
            notes = "更新app端权限",
            httpMethod = "POST")
    @RequestMapping("/updateShpPermission")
    public BaseResult updateShpPermission(@Valid ParamShpPermissionUpdate permission, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String id = permission.getId();
        ShpPermission permissionDb = shpPermissionService.getShpPermissionById(Integer.parseInt(id));
        if (LocalUtils.isEmptyAndNull(permissionDb)) {
            return BaseResult.defaultErrorWithMsg("该权限不存在!id = " + id);
        }
        String oldCode = permissionDb.getCode();
        String oldPerm = permissionDb.getPermission();

        String newCode = permission.getCode();
        String newPerm = permission.getPermission();

        //如果新旧code值不一样, 则需要判断新值是否已经存在;
        if (LocalUtils.isEmptyAndNull(newCode) || !newCode.equals(oldCode)) {
            validPermission(newCode, null);
        }

        //如果新旧perm值不一样, 则需要判断新值是否已经存在;
        if (LocalUtils.isEmptyAndNull(newPerm) || !newPerm.equals(oldPerm)) {
            validPermission(null, newPerm);
        }


        ShpPermission perm = pickShpPermission(permission, false);

        Integer uid = getAdminUserId();
        perm.setUpdateAdmin(uid);
        refreshPerm();
        return BaseResult.okResult(shpPermissionService.updateShpPermission(perm));
    }

    private void validPermission(String code, String perms) {
        if (!LocalUtils.isEmptyAndNull(code)) {
            boolean existsCode = shpPermissionService.existsShpPermissionCode(code);
            if (existsCode) {
                throw new MyException("code值: 【" + code + "】已存在!");
            }
        }

        if (!LocalUtils.isEmptyAndNull(perms)) {
            boolean existsCode = shpPermissionService.existsPerms(new String[]{perms});
            if (existsCode) {
                throw new MyException("授权标识: 【" + perms + "】已存在!");
            }
        }
    }


    /**
     * 封装ShpPermission实体</br>
     * 判断权限类型与父节点id
     *
     * @param perm
     * @return
     */
    private ShpPermission pickShpPermission(ParamShpPermissionUpdate perm, boolean isSave) {
        ShpPermission shpPermission = new ShpPermission();

        int parenId = Integer.parseInt(perm.getParentId());
        int type = Integer.parseInt(perm.getType());
        if (type > 0 && parenId <= 0) {
            //不是根目录菜单,则必须要有父节点
            throw new MyException("不是根目录菜单,则必须要有父节点!");
        }
        Date date = new Date();
        if (isSave) {
            //新增
            shpPermission.setInsertTime(date);
            shpPermission.setInsertAdmin(getUserId());
        } else {
            shpPermission.setId(Integer.parseInt(perm.getId()));
            shpPermission.setUpdateTime(date);
            shpPermission.setUpdateAdmin(getUserId());
        }
        String sort = perm.getSort();
        shpPermission.setParentId(parenId);
        shpPermission.setType(type);
        shpPermission.setDisplay(LocalUtils.strParseInt(perm.getDisplay()));
        shpPermission.setName(perm.getName());
        shpPermission.setCode(perm.getCode());
        shpPermission.setUrl(perm.getUrl());
        shpPermission.setPermission(perm.getPermission());
        shpPermission.setIconUrl(perm.getIconUrl());
        shpPermission.setSort(LocalUtils.isEmptyAndNull(sort) ? 0 : Integer.parseInt(sort));
        shpPermission.setRemark(perm.getRemark());
        shpPermission.setVersions(Integer.parseInt(perm.getVersions()));
        shpPermission.setCostState(perm.getCostState());
        shpPermission.setNewState(perm.getNewState());
        shpPermission.setHttpUrl(perm.getHttpUrl());
        return shpPermission;
    }

    /**
     * 刪除app端权限
     *
     * @return Result
     */
    @ApiOperation(
            value = "刪除app端权限",
            notes = "刪除app端权限",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
    })
    @RequestRequire
    @RequestMapping("/deleteShpPermission")
    public BaseResult deleteShpPermission(@Valid ParamShpPermissionUpdate permission, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String ids = permission.getId();
        String inSql = LocalUtils.packString(LocalUtils.splitString(ids, ","));
        refreshPerm();
        return BaseResult.okResult(shpPermissionService.deleteShpPermissions(inSql));
    }
}
