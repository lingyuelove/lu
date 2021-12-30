package com.luxuryadmin.admin.shp;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.ShpIndex;
import com.luxuryadmin.param.common.ParamIds;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.shp.ParamShpIndexAdd;
import com.luxuryadmin.param.shp.ParamShpIndexUpdate;
import com.luxuryadmin.param.shp.ParamShpPermIndexDelete;
import com.luxuryadmin.service.shp.ShpIndexService;
import com.luxuryadmin.service.shp.ShpPermIndexService;
import com.luxuryadmin.vo.shp.VoShpIndex;
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
 * APP首页功能
 *
 * @author monkey king
 * @Date 2021-12-03 00:30:24
 */
@Slf4j
@RestController
@RequestMapping(value = "/admin/shp/perm")
@Api(tags = {"B002.【APP首页】模块"}, description = "/admin/shp/perm | 【APP首页】模块 ")
public class ShpIndexController extends BaseController {

    @Autowired
    private ShpIndexService shpIndexService;

    @Autowired
    private ShpPermIndexService shpPermIndexService;


    /**
     * 加载app首页功能
     *
     * @return Result
     */
    @ApiOperation(
            value = "加载app首页功能",
            notes = "加载app首页功能",
            httpMethod = "POST")
    @RequestMapping("/listShpIndex")
    @RequiresPerm(value = "system:shop")
    public BaseResult listShpIndex(@Valid ParamToken paramToken, BindingResult result) {
        servicesUtil.validControllerParam(result);
        List<VoShpIndex> voShpPermissions = shpIndexService.groupByShpPermIndex();
        return LocalUtils.getBaseResult(voShpPermissions);
    }

    /**
     * 预览app首页功能
     *
     * @return Result
     */
    @ApiOperation(
            value = "预览app首页功能",
            notes = "预览app首页功能",
            httpMethod = "POST")
    @RequestMapping("/previewAppIndex")
    @RequiresPerm(value = "system:shop")
    public BaseResult previewAppIndex(@Valid ParamToken paramToken, BindingResult result) {
        servicesUtil.validControllerParam(result);
        List<VoShpIndex> voShpPermissions = shpIndexService.listAppIndexFunction(null, null);
        return LocalUtils.getBaseResult(voShpPermissions);
    }


    /**
     * 添加app首页功能
     *
     * @return Result
     */
    @ApiOperation(
            value = "添加app首页功能",
            notes = "添加app首页功能",
            httpMethod = "POST")
    @RequestMapping("/addShpIndex")
    public BaseResult addShpIndex(@Valid ParamShpIndexAdd permIndex, BindingResult result) {
        servicesUtil.validControllerParam(result);
        //String permName = permIndex.getName();
        //boolean exists = shpIndexService.existsShpIndex(permName);
        //if (exists) {
        //    return BaseResult.defaultErrorWithMsg("【" + permName + "】已存在该名称!");
        //}
        ShpIndex perm = pickShpIndex(permIndex, true);
        shpIndexService.saveShpPermIndex(perm);
        refreshPerm();
        return BaseResult.okResult();
    }

    /**
     * 更新app首页功能
     *
     * @return Result
     */
    @ApiOperation(
            value = "更新app首页功能",
            notes = "更新app首页功能",
            httpMethod = "POST")
    @RequestMapping("/updateShpIndex")
    public BaseResult updateShpIndex(@Valid ParamShpIndexUpdate param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String id = param.getId();
        ShpIndex shpIndexDb = shpIndexService.getShpIndexById(Integer.parseInt(id));
        if (LocalUtils.isEmptyAndNull(shpIndexDb)) {
            return BaseResult.defaultErrorWithMsg("该权限不存在!id = " + id);
        }
        //String oldName = shpIndexDb.getName();
        //if (!oldName.equals(param.getName())) {
        //    boolean exists = shpIndexService.existsShpIndex(param.getName());
        //    if (exists) {
        //        return BaseResult.defaultErrorWithMsg("【" + param.getName() + "】已存在该名称!");
        //    }
        //}
        ShpIndex perm = pickShpIndex(param, false);
        shpIndexService.updateShpIndex(perm);
        refreshPerm();
        return BaseResult.okResult();
    }


    /**
     * 获取权限列表里面可跳转的功能模块;
     *
     * @return Result
     */
    @ApiOperation(
            value = "获取权限列表里面可跳转的功能模块",
            notes = "获取权限列表里面可跳转的功能模块",
            httpMethod = "POST")
    @RequestMapping("/listAppFunction")
    public BaseResult listAppFunction(@Valid ParamToken paramToken, BindingResult result) {
        servicesUtil.validControllerParam(result);
        List<VoShpPermIndex> list = shpPermIndexService.listAppFunction();
        return LocalUtils.getBaseResult(list);
    }


    /**
     * 刪除首页app的功能
     *
     * @return Result
     */
    @ApiOperation(
            value = "刪除首页app的功能",
            notes = "刪除首页app的功能",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
    })
    @RequestRequire
    @RequestMapping("/deleteShpIndex")
    public BaseResult deleteShpIndex(@Valid ParamIds perm, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String ids = perm.getIds();
        String inSql = LocalUtils.packString(LocalUtils.splitString(ids, ","));
        shpIndexService.deleteShpIndex(inSql);
        refreshPerm();
        return BaseResult.okResult();
    }

    /**
     * 封装ShpPermission实体</br>
     * 判断权限类型与父节点id
     *
     * @param perm
     * @return
     */
    private ShpIndex pickShpIndex(ParamShpIndexUpdate perm, boolean isSave) {
        ShpIndex permIndex = new ShpIndex();

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
        permIndex.setName(perm.getName());
        permIndex.setType(Integer.parseInt(perm.getType()));
        permIndex.setSort(LocalUtils.isEmptyAndNull(sort) ? 0 : Integer.parseInt(sort));
        int permId = LocalUtils.isEmptyAndNull(perm.getPermId()) ? 0 : Integer.parseInt(perm.getPermId());
        permIndex.setFkShpPermIndexId(permId);
        return permIndex;
    }

}
