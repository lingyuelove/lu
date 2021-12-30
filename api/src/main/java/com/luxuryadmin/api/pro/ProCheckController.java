package com.luxuryadmin.api.pro;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.exception.ControllerException;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.entity.pro.ProCheck;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.service.pro.ProCheckService;
import com.luxuryadmin.service.pro.ProClassifyService;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.vo.pro.VoCheckListForApi;
import com.luxuryadmin.vo.pro.VoProClassify;
import com.luxuryadmin.vo.shp.VoEmployee;
import com.luxuryadmin.vo.shp.VoShpWechat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping(value = "/shop/user/pro/check")
@Api(tags = {"C006.【盘点】模块--2.6.5❤"}, description = "/shop/user/pro/check |盘点】模块相关")
@RequiresPermissions("checkProduct:list")
public class ProCheckController  extends ProProductBaseController {

    @Autowired
    private ProCheckService checkService;
    @Autowired
    private ProClassifyService proClassifyService;
    /**
     * 前端新增盘点
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "新增盘点--2.6.5❤",
            notes = "新增盘点--2.6.5❤",
            httpMethod = "POST")

    @PostMapping(value = "/addCheck")
    public BaseResult addCheck(@Valid ParamCheckAddList paramCheckAddList, BindingResult result) {
        servicesUtil.validControllerParam(result);
        Integer shopId = getShopId();
        Integer userId = getUserId();
        try {
            //判断版本控制是否在2.6.5或以上版本
            BasicParam basicParam = getBasicParam();
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.5") >= 0) {
                if (LocalUtils.isEmptyAndNull(paramCheckAddList.getType())){
                    return BaseResult.defaultErrorWithMsg("盘点类型不为空");
                }
                if ("temp".equals(paramCheckAddList.getType())){
                    if (LocalUtils.isEmptyAndNull(paramCheckAddList.getTempId())){
                        return BaseResult.defaultErrorWithMsg("临时仓id不为空");
                    }
                    ParamCheckProductAddList checkProductAddList = new ParamCheckProductAddList();
                    BeanUtils.copyProperties(paramCheckAddList, checkProductAddList);
                    checkProductAddList.setFkShpUserId(userId);
                    checkProductAddList.setFkShpShopId(shopId);
                    checkService.addCheckForTemp(checkProductAddList);
                }else {
                    //默认走原来的盘点
                    paramCheckAddList.setType("warehouse");
                    addCheck(paramCheckAddList);
                }
            }else {
                paramCheckAddList.setType("warehouse");
                //判断版本控制是否在2.6.5或以下版本
               addCheck(paramCheckAddList);
            }
        } catch (Exception e) {
            throw new MyException("新增盘点错误" + e);
        }
        return BaseResult.okResult();
    }

    public BaseResult addCheck( ParamCheckAddList paramCheckAddList){
        if (LocalUtils.isEmptyAndNull(paramCheckAddList.getFkProAttributeCodes())){
            return BaseResult.defaultErrorWithMsg("商品属性不为空");
        }
        Integer shopId = getShopId();
        Integer userId = getUserId();
        ParamCheckProductAddList checkProductAddList = new ParamCheckProductAddList();
        BeanUtils.copyProperties(paramCheckAddList, checkProductAddList);
        checkProductAddList.setFkShpUserId(userId);
        checkProductAddList.setFkShpShopId(shopId);
        checkService.addCheck(checkProductAddList);
        return BaseResult.okResult();
    }
    /**
     * 修改盘点
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "修改盘点",
            notes = "修改盘点",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token")
    })
    @PostMapping(value = "/updateCheckState")
    public BaseResult updateCheckState(@Valid ParamCheckUpdateForApi checkUpdateForApi, BindingResult result) {
        servicesUtil.validControllerParam(result);
        ProCheck check = checkService.getById(checkUpdateForApi.getId());
        if (check == null ){
            return BaseResult.errorResult("此盘点不存在");
        }
        checkService.updateCheckState(checkUpdateForApi);
        return BaseResult.okResult();
    }

    /**
     * 初始化【商品】页面查询参数;
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "创建【盘点】--商品分类;",
            notes = "创建【盘点】--商品分类;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token")
    })
    @PostMapping("/initQueryParam")
    public BaseResult initQueryParam() {
        Integer shopId = getShopId();
        List<VoProClassify> voProClassifyList = proClassifyService.listProClassifyByState(shopId, "1");

        return BaseResult.okResult(voProClassifyList);
    }

    /**
     * 商户盘点集合显示
     * @param checkListForApiBySearch
     * @return
     */
    @ApiOperation(
            value = "获取商户盘点集合--2.6.5❤",
            notes = "获取商户盘点集合--2.6.5❤",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token登录标识符"),
    })
    @RequestRequire
    @GetMapping("/getCheckListForApi")
    public BaseResult<PageInfo<VoCheckListForApi>> getCheckListForApi(@Valid ParamCheckListForApiBySearch checkListForApiBySearch,String token) {
        Integer shopId = getShopId();
        if (LocalUtils.isEmptyAndNull(checkListForApiBySearch.getPageNum())) {
            checkListForApiBySearch.setPageNum("1");
        }
        PageHelper.startPage(Integer.parseInt(checkListForApiBySearch.getPageNum()), checkListForApiBySearch.getPageSize());
        checkListForApiBySearch.setFkShpShopId(shopId);
        List<VoCheckListForApi> checkListForApi = checkService.getCheckListForApi(checkListForApiBySearch);
        PageInfo pageInfo = new PageInfo(checkListForApi);
        return BaseResult.okResult(pageInfo);
    }

    /**
     * 初始化【商品】页面查询参数;
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "盘点商品分类2.5.1--商品分类;",
            notes = "盘点商品分类--商品分类;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "checkId", value = "分类id")
    })
    @PostMapping("/getClassifyList")
    public BaseResult getClassifyList(Integer checkId) {
        Integer shopId = getShopId();
        List<VoProClassify> voProClassifyList = checkService.getClassifyList(shopId, checkId);

        return BaseResult.okResult(voProClassifyList);
    }

    @ApiOperation(
            value = "删除此次盘点",
            notes = "删除此次盘点",
            httpMethod = "POST")
    @RequestRequire
    @PostMapping("/deleteCheck")
    public BaseResult deleteCheck(@Valid ParamCheckDelete checkDelete) {

        try {
            //判断版本控制是否在2.6.5或以上版本
            BasicParam basicParam = getBasicParam();
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.5") >= 0) {
                List<String> idList = Arrays.asList(checkDelete.getId().split(","));
                idList.forEach(s -> {
                    checkService.deleteCheck(Integer.parseInt(s));
                });

            }else {
                ProCheck check = checkService.getById(Integer.parseInt(checkDelete.getId()));
                if (check == null ){
                    return BaseResult.errorResult("此盘点不存在");
                }
                if (!"20".equals(check.getCheckState())){
                    return BaseResult.errorResult("此批次的盘点状态不为已取消，不可再进行盘点");
                }
                checkService.deleteCheck(Integer.parseInt(checkDelete.getId()));
            }
        } catch (Exception e) {
            throw new MyException("删除盘点错误" + e);
        }
        return BaseResult.okResult();
    }
}
