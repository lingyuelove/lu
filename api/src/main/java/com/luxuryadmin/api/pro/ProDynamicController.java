package com.luxuryadmin.api.pro;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProDynamic;
import com.luxuryadmin.param.pro.ParamDynamicDelete;
import com.luxuryadmin.param.pro.ParamDynamicQuery;
import com.luxuryadmin.param.pro.ParamDynamicSave;
import com.luxuryadmin.param.pro.ParamProductQuery;
import com.luxuryadmin.service.pro.ProDynamicService;

import com.luxuryadmin.vo.pro.VoDynamicList;
import com.luxuryadmin.vo.pro.VoDynamicShow;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 商品位置列表 controller
 *
 * @author zhangsai
 * @Date 2021-11-05 11:45:33
 */
@Api(tags = {"C003.1【商品位置】模块 --2.6.6"}, description = "商品位置列表")
@RestController
@RequestMapping(value = "/shop/user/pro/dynamic")
public class ProDynamicController extends BaseController {


    @Autowired
    private ProDynamicService proDynamicService;


    @ApiOperation(
            value = "获取动态列表",
            httpMethod = "POST")
    @RequestRequire
    @PostMapping("/listDynamic")
    @RequiresPermissions("dynamic:list")
    public BaseResult<VoDynamicShow> listDynamic(@Valid ParamDynamicQuery param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        param.setShopId(getShopId());
        PageHelper.startPage(getPageNum(), 50);
        List<VoDynamicList> voDynamics = proDynamicService.listDynamic(param);
        String uPermDynamicDel = ConstantPermission.DYNAMIC_DELETE;
        VoDynamicShow voDynamicShow =new VoDynamicShow();
        voDynamicShow.setVoDynamics(voDynamics);
        voDynamicShow.setUPermDynamicDel(hasPermToPageWithCurrentUser(uPermDynamicDel));
        return BaseResult.okResult(voDynamicShow);
    }

    @ApiOperation(
            value = "新增动态",
            httpMethod = "POST")
    @RequestRequire
    @PostMapping("/saveDynamic")
    public BaseResult saveDynamic(@Valid ParamDynamicSave param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        param.setShopId(getShopId());
        param.setUserId(getUserId());
        proDynamicService.saveDynamic(param);
        return BaseResult.okResult();
    }

    @ApiOperation(
            value = "删除动态",
            httpMethod = "POST")
    @RequestRequire
    @PostMapping("/deleteDynamic")
    public BaseResult deleteDynamic(@Valid ParamDynamicDelete param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        proDynamicService.deleteDynamic(param);
        return BaseResult.okResult();
    }
}
