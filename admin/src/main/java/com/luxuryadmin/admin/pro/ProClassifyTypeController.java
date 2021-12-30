package com.luxuryadmin.admin.pro;

import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.service.pro.ProClassifyService;
import com.luxuryadmin.service.pro.ProClassifyTypeService;
import com.luxuryadmin.service.pro.ProClassifyTypeShopService;
import com.luxuryadmin.vo.pro.VoClassifyTypeList;
import com.luxuryadmin.vo.pro.VoClassifyTypeSonList;
import com.luxuryadmin.vo.pro.VoClassifyTypeSonPage;
import com.luxuryadmin.vo.pro.VoProClassify;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


/**
 * 商品补充信息分类表 controller
 *
 * @author zhangsai
 * @Date 2021-08-03 10:45:15
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user", method = RequestMethod.POST)
@Api(tags = {"1.4.【二级分类】模块----2.6.2"}, description = "/shop/user |店铺分类相关")
public class ProClassifyTypeController extends BaseController {

    @Autowired
    private ProClassifyTypeService proClassifyTypeService;

    @Autowired
    private ProClassifyService proClassifyService;
    /**
     * 店铺设置--补充信息设置-- 集合显示
     *
     * @param classifyTypeSearch 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "店铺设置--补充信息设置-- 集合显示 --2.6.2;",
            notes = "店铺设置--补充信息设置-- 集合显示;",
            httpMethod = "POST")
    @RequestMapping("/getClassifyTypeList")
    @RequiresPerm(value = "pro:classify")
    public BaseResult<VoClassifyTypeSonPage> getClassifyTypeList(@Valid ParamClassifyTypeSearch classifyTypeSearch, BindingResult result) {
        servicesUtil.validControllerParam(result);
        VoClassifyTypeSonPage classifyTypeSonLists = proClassifyTypeService.getClassifyListForAdmin(classifyTypeSearch);
        return BaseResult.okResult(classifyTypeSonLists);
    }


    /**
     * 店铺设置--补充信息设置-- 分类集合显示
     *
     * @return Result
     */
    @ApiOperation(
            value = "店铺设置--补充信息设置-- 分类集合显示 --2.6.2;",
            notes = "店铺设置--补充信息设置-- 分类集合显示;",
            httpMethod = "POST")
    @RequestMapping("/listSysProClassifyByState")
    public BaseResult<List<VoProClassify>> listSysProClassifyByState() {

        List<VoProClassify> classifyList = proClassifyService.listSysProClassifyByState(null);
        if (LocalUtils.isEmptyAndNull(classifyList)) {
            return BaseResult.okResultNoData();
        }
        return BaseResult.okResult(classifyList);
    }

    /**
     * 店铺设置--补充信息设置-- 新增
     *
     * @param classifyTypeAdd 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "店铺设置--补充信息设置-- 新增 --2.6.2;",
            notes = "店铺设置--补充信息设置-- 新增;",
            httpMethod = "POST")
    @RequestMapping("/addClassifyType")
    public BaseResult addClassifyType(@Valid ParamClassifyTypeAdd classifyTypeAdd, BindingResult result) {
        servicesUtil.validControllerParam(result);
        //方法废弃 暂不用
//        proClassifyTypeService.addClassifyType(classifyTypeAdd);

        return BaseResult.okResult();
    }

    /**
     * 店铺设置--补充信息设置-- 子模块新增
     *
     * @param classifyTypeAdd 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "店铺设置--补充信息设置 -- 子模块新增 --2.6.2;",
            notes = "店铺设置--补充信息设置-- 子模块新增;",
            httpMethod = "GET")
    @RequestMapping("/addClassifyTypeSun")
    public BaseResult addClassifyTypeSun(@Valid ParamClassifyTypeAdd classifyTypeAdd, BindingResult result) {
        servicesUtil.validControllerParam(result);

        proClassifyTypeService.addClassifyTypeSun(classifyTypeAdd);

        return BaseResult.okResult();
    }

    /**
     * 店铺设置--补充信息设置-- 编辑
     *
     * @param classifyTypeUpdate 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "店铺设置--补充信息设置 -- 编辑 --2.6.2;",
            notes = "店铺设置--补充信息设置-- 编辑;",
            httpMethod = "POST")
    @RequestMapping("/updateClassifyType")
    public BaseResult updateClassifyType(@Valid ParamClassifyTypeUpdate classifyTypeUpdate, BindingResult result) {
        servicesUtil.validControllerParam(result);
        proClassifyTypeService.updateClassifyType(classifyTypeUpdate);
        return BaseResult.okResult();
    }

    /**
     * 店铺设置--补充信息设置-- 删除接口
     *
     * @param shareDelete 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "店铺设置--补充信息设置 -- 删除接口 --2.6.2;",
            notes = "店铺设置--补充信息设置-- 删除接口;",
            httpMethod = "POST")
    @RequestMapping("/deleteClassifyType")
    public BaseResult deleteClassifyType(@Valid ParamShareDelete shareDelete, BindingResult result) {
        servicesUtil.validControllerParam(result);
        proClassifyTypeService.deleteClassifyType(Integer.parseInt(shareDelete.getId()));
        return BaseResult.okResult();
    }
}
