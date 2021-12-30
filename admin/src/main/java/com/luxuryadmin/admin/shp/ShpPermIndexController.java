package com.luxuryadmin.admin.shp;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.ShpPermIndex;
import com.luxuryadmin.param.common.ParamIds;
import com.luxuryadmin.param.shp.ParamShpPermIndexAdd;
import com.luxuryadmin.param.shp.ParamShpPermIndexUpdate;
import com.luxuryadmin.service.shp.ShpPermIndexService;
import com.luxuryadmin.vo.shp.VoShpPermIndex;
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
 * APP员工权限2.0
 *
 * @author monkey king
 * @Date 2021-12-02 02:33:41
 */
@Slf4j
@RestController
@RequestMapping(value = "/admin/shp/perm")
@Api(tags = {"B001.【APP权限】模块"}, description = "/admin/shp/perm | 【APP权限】模块 ")
public class ShpPermIndexController extends BaseController {

    @Autowired
    private ShpPermIndexService shpPermIndexService;


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
    @RequestMapping("/listShpPermIndex")
    @RequiresPerm(value = "system:shop")
    public BaseResult listShpPermIndex(@RequestParam Map<String, String> params) throws Exception {
        List<VoShpPermIndex> voShpPermissions = shpPermIndexService.groupByShpPermIndex(null, null, null);
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
    @RequestMapping("/addShpPermIndex")
    public BaseResult addShpPermIndex(@Valid ParamShpPermIndexAdd permIndex, BindingResult result) {
        servicesUtil.validControllerParam(result);
        int parentId = Integer.parseInt(permIndex.getParentId());
        String permName = permIndex.getName();
        boolean exists = shpPermIndexService.existsShpPermIndex(parentId, permName);
        if (exists) {
            return BaseResult.defaultErrorWithMsg("【" + permName + "】已存在该模块!");
        }
        String code = permIndex.getCode();
        String perms = permIndex.getPermission();
        validPermission(code, perms);
        ShpPermIndex perm = pickShpPermIndex(permIndex, true);
        refreshPerm();
        return BaseResult.okResult(shpPermIndexService.saveShpPermIndex(perm));
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
    @RequestMapping("/updateShpPermIndex")
    public BaseResult updateShpPermIndex(@Valid ParamShpPermIndexUpdate paramPerm, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String id = paramPerm.getId();
        ShpPermIndex permissionDb = shpPermIndexService.getShpPermIndexById(Integer.parseInt(id));
        if (LocalUtils.isEmptyAndNull(permissionDb)) {
            return BaseResult.defaultErrorWithMsg("该权限不存在!id = " + id);
        }
        //允许code值重复,修改于2021-12-10 22:58:37;为了兼容权限页面跳转(个人和全部)
        //String oldCode = permissionDb.getCode();
        String oldPerm = permissionDb.getPermission();
        //String newCode = paramPerm.getCode();
        String newPerm = paramPerm.getPermission();
        //如果新旧code值不一样, 则需要判断新值是否已经存在;
        //if (LocalUtils.isEmptyAndNull(newCode) || !newCode.equals(oldCode)) {
        //    validPermission(newCode, null);
        //}
        //如果新旧perm值不一样, 则需要判断新值是否已经存在;
        if (LocalUtils.isEmptyAndNull(newPerm) || !newPerm.equals(oldPerm)) {
            validPermission(null, newPerm);
        }
        ShpPermIndex perm = pickShpPermIndex(paramPerm, false);
        refreshPerm();
        return BaseResult.okResult(shpPermIndexService.updateShpPermIndex(perm));
    }

    /**
     * 校验权限是否存在
     *
     * @param code
     * @param perms
     */
    private void validPermission(String code, String perms) {
        if (!LocalUtils.isEmptyAndNull(code)) {
            boolean existsCode = shpPermIndexService.existsShpPermIndexCode(code);
            if (existsCode) {
                throw new MyException("code值: 【" + code + "】已存在!");
            }
        }
        if (!LocalUtils.isEmptyAndNull(perms)) {
            boolean existsCode = shpPermIndexService.existsPerms(perms);
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
    private ShpPermIndex pickShpPermIndex(ParamShpPermIndexUpdate perm, boolean isSave) {
        ShpPermIndex permIndex = new ShpPermIndex();

        int parenId = Integer.parseInt(perm.getParentId());
        int type = Integer.parseInt(perm.getType());
        if (type > 0 && parenId <= 0) {
            //不是根目录菜单,则必须要有父节点
            throw new MyException("不是根目录菜单,则必须要有父节点!");
        }
        Date date = new Date();
        if (isSave) {
            //新增
            permIndex.setInsertTime(date);
            permIndex.setInsertAdmin(getAdminUserId());
        } else {
            permIndex.setId(Integer.parseInt(perm.getId()));
            permIndex.setUpdateTime(date);
            permIndex.setUpdateAdmin(getAdminUserId());
        }
        String sort = perm.getSort();
        permIndex.setParentId(parenId);
        permIndex.setType(type);
        permIndex.setDisplay(LocalUtils.strParseInt(perm.getDisplay()));
        permIndex.setIsPrivate(perm.getIsPrivate());
        permIndex.setName(perm.getName());
        permIndex.setCode(perm.getCode());
        permIndex.setUrl(perm.getUrl());
        permIndex.setPermission(perm.getPermission());
        permIndex.setIconUrl(perm.getIconUrl());
        permIndex.setSort(LocalUtils.isEmptyAndNull(sort) ? 0 : Integer.parseInt(sort));
        permIndex.setRemark(perm.getRemark());
        permIndex.setIosVersion(Integer.parseInt(perm.getIosVersion()));
        permIndex.setAndroidVersion(Integer.parseInt(perm.getAndroidVersion()));
        permIndex.setColor(perm.getColor());
        permIndex.setOnlyShopId(perm.getOnlyShopId());
        permIndex.setCostState(perm.getCostState());
        permIndex.setNewState(perm.getNewState());
        permIndex.setHttpUrl(perm.getHttpUrl());
        return permIndex;
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
    @RequestMapping("/deleteShpPermIndex")
    public BaseResult deleteShpPermIndex(@Valid ParamIds perm, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String ids = perm.getIds();
        String inSql = LocalUtils.packString(LocalUtils.splitString(ids, ","));
        shpPermIndexService.deleteShpPermIndex(inSql);
        refreshPerm();
        return BaseResult.okResult();
    }
}
