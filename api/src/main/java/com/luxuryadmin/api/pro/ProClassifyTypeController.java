package com.luxuryadmin.api.pro;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProClassifyType;
import com.luxuryadmin.param.pro.ParamClassifyTypeAdd;
import com.luxuryadmin.param.pro.ParamClassifyTypeSearch;
import com.luxuryadmin.param.pro.ParamClassifyTypeShopAdd;
import com.luxuryadmin.param.pro.ParamProductOrOrderForDeleteSearch;
import com.luxuryadmin.service.pro.ProClassifyTypeService;
import com.luxuryadmin.service.pro.ProClassifyTypeShopService;
import com.luxuryadmin.vo.pro.VoClassify;
import com.luxuryadmin.vo.pro.VoClassifyTypeList;
import com.luxuryadmin.vo.pro.VoProClassify;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 商品补充信息分类表 controller
 *
 * @author zhangsai
 * @Date 2021-08-03 10:45:15
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user", method = RequestMethod.POST)
@Api(tags = {"E002.4【分类】模块----2.6.2"}, description = "/shop/user |店铺分类相关")
public class ProClassifyTypeController extends BaseController {

    @Autowired
    private ProClassifyTypeService proClassifyTypeService;

    @Autowired
    private ProClassifyTypeShopService proClassifyTypeShopService;

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
    public BaseResult<List<VoClassify>> getClassifyTypeList(@Valid ParamClassifyTypeSearch classifyTypeSearch, BindingResult result) {
        servicesUtil.validControllerParam(result);
        classifyTypeSearch.setShopId(getShopId());
        List<VoClassify> voProClassifyList = proClassifyTypeService.getClassifyListForApp(classifyTypeSearch);
        if (LocalUtils.isEmptyAndNull(voProClassifyList)) {
            return BaseResult.okResultNoData();
        }
        return BaseResult.okResult(voProClassifyList);
    }

    /**
     * 店铺设置--补充信息设置-- 新增
     *
     * @param classifyTypeShopAdd 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "店铺设置--补充信息设置-- 禁用 --2.6.2;",
            notes = "店铺设置--补充信息设置-- 禁用;",
            httpMethod = "POST")
    @RequestMapping("/addClassifyType")
    public BaseResult addClassifyType(@Valid ParamClassifyTypeShopAdd classifyTypeShopAdd, BindingResult result) {
        servicesUtil.validControllerParam(result);
        if (LocalUtils.isEmptyAndNull(classifyTypeShopAdd.getClassifyTypeId())){
            return BaseResult.okResult();
        }
        classifyTypeShopAdd.setShopId(getShopId());
        classifyTypeShopAdd.setUserId(getUserId());
        proClassifyTypeShopService.addRemoveClassifyType(classifyTypeShopAdd);
        return BaseResult.okResult();
    }
}
