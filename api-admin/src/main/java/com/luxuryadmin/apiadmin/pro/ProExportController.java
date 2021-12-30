package com.luxuryadmin.apiadmin.pro;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.op.OpUploadDownload;
import com.luxuryadmin.enums.pro.EnumProAttribute;
import com.luxuryadmin.excel.ExVoProduct;
import com.luxuryadmin.excel.ExVoTempProduct;
import com.luxuryadmin.param.pro.ParamProTempProductQuery;
import com.luxuryadmin.param.pro.ParamProductQuery;
import com.luxuryadmin.service.op.OpUploadDownloadService;
import com.luxuryadmin.service.pro.ProTempProductService;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.vo.pro.VoProductLoad;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author monkey king
 * @date 2021-09-08 22:20:22
 */
@Slf4j
@RestController
@Api(tags = {"H002.【商品导出】模块"})
@RequestMapping(value = "/shop/admin/pro")
public class ProExportController extends ProProductBaseController {

    @Autowired
    private OpUploadDownloadService opUploadDownloadService;
    @Autowired
    private ProTempProductService proTempProductService;

    /**
     * 导出在售商品
     *
     * @param queryParam 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "导出在售商品",
            notes = "导出在售商品",
            httpMethod = "POST")
    @RequestRequire
    @PostMapping("/exportOnSale")
    public BaseResult exportOnSale(@Valid ParamProductQuery queryParam, BindingResult result) {
        servicesUtil.validControllerParam(result);
        queryParam.setStateCode("onSale");
        String taskName = "在售商品导出";
        List<ExVoProduct> list = listPublicProduct(queryParam, result);
        if (LocalUtils.isEmptyAndNull(list)) {
            return BaseResult.okResultNoData();
        }
        OpUploadDownload opUploadDownload = packObject(taskName, queryParam,"商品模块");
        int id = opUploadDownloadService.saveOrUpdateObjectAndReturnId(opUploadDownload);
        opUploadDownloadService.uploadFileToOss(opUploadDownload, list);
        return LocalUtils.getBaseResult(id);
    }

    /**
     * 仓库商品导出
     *
     * @param queryParam 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "仓库商品导出",
            notes = "仓库商品导出",
            httpMethod = "POST")
    @RequestRequire
    @PostMapping("/exportStore")
    public BaseResult exportStore(@Valid ParamProductQuery queryParam, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String taskName = "仓库商品导出";
        List<ExVoProduct> list = listPublicProduct(queryParam, result);
        if (LocalUtils.isEmptyAndNull(list)) {
            return BaseResult.okResultNoData();
        }
        OpUploadDownload opUploadDownload = packObject(taskName, queryParam,"商品模块");
        int id = opUploadDownloadService.saveOrUpdateObjectAndReturnId(opUploadDownload);
        opUploadDownloadService.uploadFileToOss(opUploadDownload, list);
        return LocalUtils.getBaseResult(id);
    }

    /**
     * 质押商品导出
     *
     * @param queryParam 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "质押商品导出",
            notes = "质押商品导出",
            httpMethod = "POST")
    @RequestRequire
    @PostMapping("/exportPawn")
    public BaseResult exportPawn(@Valid ParamProductQuery queryParam, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String taskName = "质押商品导出";
        queryParam.setAttributeCode(EnumProAttribute.PAWN.getCode().toString());
        List<ExVoProduct> list = listPublicProduct(queryParam, result);
        if (LocalUtils.isEmptyAndNull(list)) {
            return BaseResult.okResultNoData();
        }
        OpUploadDownload opUploadDownload = packObject(taskName, queryParam,"商品模块");
        int id = opUploadDownloadService.saveOrUpdateObjectAndReturnId(opUploadDownload);
        opUploadDownloadService.uploadFileToOss(opUploadDownload, list);
        return LocalUtils.getBaseResult(id);
    }

    private OpUploadDownload packObject(String taskName, ParamProductQuery queryParam, String module) {
        String st = queryParam.getUploadStDateTime();
        String et = queryParam.getUploadEtDateTime();
        validTimeScope(st, et);
        int shopId = getShopId();
        int userId = getUserId();
        String exportType = "out";
//        String module = "商品模块";
        Date st1 = null;
        Date et2 = null;
        try {
            st1 = DateUtil.parse(st);
            et2 = DateUtil.parse(et);
        } catch (ParseException ignored) {
        }
        return opUploadDownloadService.packOpUploadDownload(shopId, userId, exportType, module, taskName, st1, et2);
    }


    private List<ExVoProduct> listPublicProduct(ParamProductQuery queryParam, BindingResult result) {
        servicesUtil.validControllerParam(result);
        if (!shopIsMember()) {
            throw new MyException("导出功能仅限【正式会员】可用~");
        }
        if (LocalUtils.isEmptyAndNull(queryParam.getAttributeCode())) {
            queryParam.setAttributeCode(getAllProAttributeCodeExcludePawnPro(";"));
        }
        //独立编码不支持模糊查询
        queryParam.setUniqueCode(queryParam.getProName());
        formatQueryParam(queryParam);
        List<ExVoProduct> proProductList = proProductService.listProProductByVoProductQueryParamExcelExport(queryParam);
        if (!LocalUtils.isEmptyAndNull(proProductList)) {
            int shopId = queryParam.getShopId();
            int userId = queryParam.getUserId();
            String chkPriceInit = ConstantPermission.CHK_PRICE_INIT;
            String chkPriceTrade = ConstantPermission.CHK_PRICE_TRADE;
            String chkPriceAgency = ConstantPermission.CHK_PRICE_AGENCY;
            String chkPriceSale = ConstantPermission.CHK_PRICE_SALE;
            boolean hasPriceInit = servicesUtil.hasPermission(shopId, userId, chkPriceInit);
            boolean hasPriceTrade = servicesUtil.hasPermission(shopId, userId, chkPriceTrade);
            boolean hasPriceAgency = servicesUtil.hasPermission(shopId, userId, chkPriceAgency);
            boolean hasPriceSale = servicesUtil.hasPermission(shopId, userId, chkPriceSale);
            for (ExVoProduct vo : proProductList) {
                try {
                    vo.setUrl(new URL(servicesUtil.formatImgUrl(vo.getSmallImg())));
                    //商品状态
                    vo.setState(servicesUtil.getStateCn(vo.getState()));
                    //商品分类
                    vo.setClassify(servicesUtil.getClassifyCn(vo.getClassify()));
                    vo.setAttribute(servicesUtil.getAttributeCn(vo.getAttribute(), true));
                    vo.setRepairCard("1".equals(vo.getRepairCard()) ? "有" : "没有");
                    vo.setInitPrice(LocalUtils.formatPriceSpilt(LocalUtils.calcNumber(vo.getInitPrice(), "*", 0.01).toString()));
                    vo.setTradePrice(LocalUtils.formatPriceSpilt(LocalUtils.calcNumber(vo.getTradePrice(), "*", 0.01).toString()));
                    vo.setAgencyPrice(LocalUtils.formatPriceSpilt(LocalUtils.calcNumber(vo.getAgencyPrice(), "*", 0.01).toString()));
                    vo.setSalePrice(LocalUtils.formatPriceSpilt(LocalUtils.calcNumber(vo.getSalePrice(), "*", 0.01).toString()));
                    if (!hasPriceInit) {
                        vo.setInitPrice("无权限查看");
                    }
                    if (!hasPriceTrade) {
                        vo.setTradePrice("无权限查看");
                    }
                    if (!hasPriceAgency) {
                        vo.setAgencyPrice("无权限查看");
                    }
                    if (!hasPriceSale) {
                        vo.setSalePrice("无权限查看");
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return proProductList;
    }

    @ApiOperation(
            value = "临时仓商品导出",
            notes = "临时仓商品导出",
            httpMethod = "POST")
    @RequestRequire
    @PostMapping("/exportTempProduct")
    public BaseResult exportTempProduct(@Valid ParamProTempProductQuery queryParam, BindingResult result) {
        servicesUtil.validControllerParam(result);
        if (!shopIsMember()) {
            throw new MyException("导出功能仅限【正式会员】可用~");
        }
        String taskName = "临时仓商品";
        int shopId = getShopId();
        queryParam.setShopId(shopId);
        //独立编码不支持模糊查询
        queryParam.setUniqueCode(queryParam.getProName());
        List<ExVoTempProduct> tempProducts = listTempProduct(queryParam);
        if (LocalUtils.isEmptyAndNull(tempProducts)) {
            return BaseResult.okResultNoData();
        }
        OpUploadDownload opUploadDownload = packObject(taskName, queryParam, "临时仓商品");
        int id = opUploadDownloadService.saveOrUpdateObjectAndReturnId(opUploadDownload);
        opUploadDownloadService.uploadFileToOss(opUploadDownload, tempProducts);
        return LocalUtils.getBaseResult(id);
    }

    public List<ExVoTempProduct> listTempProduct(ParamProTempProductQuery queryParam) {
        List<VoProductLoad> proList = proTempProductService.listVoProductLoadByPrice(queryParam);
        //获取接收参数---临时仓商品列表价格类型检索字段
        String priceType = queryParam.getDefaultPrice();
        if (LocalUtils.isEmptyAndNull(proList)) {
            return null;
        }
//        formatVoProductLoad(null, proList, queryParam.getSortKey());
        List<ExVoTempProduct> tempProducts = new ArrayList<>();
        String userPerms = servicesUtil.getUserPerms(getShopId(), getUserId());
        //查看成本价
        String showInitPrice = ConstantPermission.CHK_PRICE_INIT;
        boolean initPrice = hasPermWithCurrentUser(userPerms, showInitPrice);
        //查看销售价
        String showSalePrice = ConstantPermission.CHK_PRICE_SALE;
        boolean salePrice = hasPermWithCurrentUser(userPerms, showSalePrice);
            proList.forEach(vo -> {

                try {
                    ExVoTempProduct tempProduct = new ExVoTempProduct();
                    tempProduct.setUrl(new URL(servicesUtil.formatImgUrl(vo.getSmallImg())));
                    String name =vo.getName();
                    tempProduct.setName(name);
                    tempProduct.setAttributeUs(servicesUtil.getAttributeCn(vo.getAttributeUs(), true));
                    tempProduct.setClassifyCn(servicesUtil.getClassifyCn(vo.getClassifyUs()));
                    switch (priceType) {
                        case "initPrice":
                            vo.setShowPrice(initPrice ? vo.getInitPrice() : null);
                            break;
                        case "tradePrice":
                            //查看友商价
                            String showTradePrice = ConstantPermission.CHK_PRICE_TRADE;
                            vo.setShowPrice(hasPermWithCurrentUser(userPerms, showTradePrice) ? vo.getTradePrice() : null);
                            break;
                        case "agencyPrice":
                            //查看代理价
                            String showAgencyPrice = ConstantPermission.CHK_PRICE_AGENCY;
                            vo.setShowPrice(hasPermWithCurrentUser(userPerms, showAgencyPrice) ? vo.getAgencyPrice() : null);
                            break;
                        case "salePrice":
                            vo.setShowPrice(salePrice ? vo.getSalePrice() : null);
                            break;
                        default://无价格类型时默认为0
                            vo.setShowPrice("0.00");
                            break;
                    }
                    BigDecimal initNewPrice = new BigDecimal(vo.getShowInitPrice()).divide(new BigDecimal(100));
                    BigDecimal showNewPrice = new BigDecimal(vo.getShowPrice()).divide(new BigDecimal(100));
                    tempProduct.setInitPrice(initNewPrice.toString());
                    tempProduct.setShowPrice(showNewPrice.toString());
                    tempProduct.setTotalNum(vo.getTotalNum());
                    tempProduct.setStateCn(servicesUtil.getStateCn(vo.getStateUs()));
                    tempProduct.setShowTime(DateUtil.format(vo.getUpdateTime()));
                    tempProduct.setDescription(vo.getDescription());
                    tempProduct.setTargetUser(vo.getTargetUser());
                    tempProducts.add(tempProduct);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            });

        return tempProducts;
    }
}
