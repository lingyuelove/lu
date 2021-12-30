package com.luxuryadmin.api.pro;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.data.ParamDataSaleRankQuery;
import com.luxuryadmin.param.pro.ParamProductOrOrderForDelete;
import com.luxuryadmin.param.pro.ParamProductOrOrderForDeleteSearch;
import com.luxuryadmin.param.pro.ParamProductOrOrderForUpdate;
import com.luxuryadmin.param.pro.ParamShare;
import com.luxuryadmin.service.pro.ProClassifyService;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.vo.biz.VoBizLeaguerShop;
import com.luxuryadmin.vo.pro.VoProClassify;
import com.luxuryadmin.vo.pro.VoProductOrOrderForDeletePage;
import com.luxuryadmin.vo.pro.VoSharePage;
import com.luxuryadmin.vo.shp.VoShpWechat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.api.pro
 * @ClassName: ProProductOrOrderForDeleteController
 * @Author: ZhangSai
 * Date: 2021/7/1 17:58
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/delete")
@Api(tags = {"I001.删除模块--2.6.2"}, description = "/shop/user/delete |删除模块")
public class ProProductOrOrderForDeleteController extends ProProductBaseController {
    @Autowired
    protected ProProductService proProductService;
    @Autowired
    private ProClassifyService proClassifyService;

    @ApiOperation(
            value = "删除列表 --删除订单/商品接口--2.6.2",
            notes = "删除列表 --删除删除订单/商品接口 --2.6.2;",
            httpMethod = "POST")
    @RequestMapping("/getProductOrOrderForDeletePage")
    @RequiresPermissions("proOrOrd:list:deleteHistory")
    public BaseResult<VoProductOrOrderForDeletePage> getProductOrOrderForDeletePage(@Valid ParamProductOrOrderForDeleteSearch productOrOrderForDeleteSearch, BindingResult result) {
        servicesUtil.validControllerParam(result);
        if (LocalUtils.isEmptyAndNull(productOrOrderForDeleteSearch.getPageNum())) {
            productOrOrderForDeleteSearch.setPageNum("1");
        }
        productOrOrderForDeleteSearch.setShopId(getShopId());
        productOrOrderForDeleteSearch.setUserId(getUserId());
        setDefaultQueryDateRange(productOrOrderForDeleteSearch);

        VoProductOrOrderForDeletePage productOrOrderForDeletePage = proProductService.getProductOrOrderForDeletePage(productOrOrderForDeleteSearch);
        //已删除订单/商品删除权限
        String uPermDeleteHistory = ConstantPermission.PROORORD_DELETE_DELETEHISTORY;
        productOrOrderForDeletePage.setUPermHistoryDelete(hasPermToPageWithCurrentUser(uPermDeleteHistory));
        return BaseResult.okResult(productOrOrderForDeletePage);
    }
    /**
     * 设置默认查询时间范围
     * @param productOrOrderForDeleteSearch
     */
    private void setDefaultQueryDateRange(ParamProductOrOrderForDeleteSearch productOrOrderForDeleteSearch) {

        //对多选的参数(删除人员)进行逗号分开;
        productOrOrderForDeleteSearch.setDeleteUserId(LocalUtils.formatParamForSqlInQuery(productOrOrderForDeleteSearch.getDeleteUserId()));
        //对多选的参数(属性编码)进行逗号分开;
        productOrOrderForDeleteSearch.setAttributeCode(LocalUtils.formatParamForSqlInQuery(productOrOrderForDeleteSearch.getAttributeCode()));
        //对多选的参数(分类编码)进行逗号分开;
        productOrOrderForDeleteSearch.setClassifyCode(LocalUtils.formatParamForSqlInQuery(productOrOrderForDeleteSearch.getClassifyCode()));
        //对多选的参数(订单类型)进行逗号分开;
        productOrOrderForDeleteSearch.setOrderType(LocalUtils.formatParamForSqlInQuery(productOrOrderForDeleteSearch.getOrderType()));

    }
    /**
     * 删除订单顶部分类接口;
     */
    @ApiOperation(
            value = "删除订单顶部分类接口 --2.5.5 --zs;",
            notes = "删除订单顶部分类接口 --2.5.5 --zs;",
            httpMethod = "GET")
//    @RequiresPermissions("proOrOrd:delete:deleteHistory")
    @GetMapping("/getShopClassify")
    public BaseResult<List<VoProClassify>> getShopClassify(@Valid ParamToken token, BindingResult result) {
        servicesUtil.validControllerParam(result);
        Integer shopId = getShopId();
        List<VoProClassify> voProClassifyList = proClassifyService.listProClassifyByState(shopId, "1");

        return BaseResult.okResult(voProClassifyList);
    }

    @ApiOperation(
            value = "删除订单/商品接口",
            notes = "删除订单/商品接口;",
            httpMethod = "POST")
    @RequestMapping("/deleteProductOrOrder")
    public BaseResult deleteProductOrOrderForDelete(@Valid ParamProductOrOrderForDelete productOrOrderForDelete, HttpServletRequest request, BindingResult result) {
        servicesUtil.validControllerParam(result);
        productOrOrderForDelete.setShopId(getShopId());
        productOrOrderForDelete.setUserId(getUserId());
        proProductService.deleteProductOrOrder(productOrOrderForDelete,request);
        return BaseResult.okResult();
    }


    @ApiOperation(
            value = "编辑 --删除订单/商品接口",
            notes = "编辑 --删除订单/商品接口;",
            httpMethod = "POST")

    @RequestMapping("/updateProductOrOrder")
    public BaseResult updateProductOrOrder(@Valid ParamProductOrOrderForUpdate productOrOrderForUpdate, HttpServletRequest request, BindingResult result) {
        servicesUtil.validControllerParam(result);
        productOrOrderForUpdate.setShopId(getShopId());
        productOrOrderForUpdate.setUserId(getUserId());
        proProductService.updateProductOrOrder(productOrOrderForUpdate,request);
        return BaseResult.okResult();
    }
}
