package com.luxuryadmin.apiadmin.ord;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.op.OpUploadDownload;
import com.luxuryadmin.enums.pro.EnumProAttribute;
import com.luxuryadmin.excel.ExVoOrder;
import com.luxuryadmin.excel.ExVoProduct;
import com.luxuryadmin.param.ord.ParamOrderQuery;
import com.luxuryadmin.service.op.OpUploadDownloadService;
import com.luxuryadmin.service.ord.OrdOrderService;
import com.luxuryadmin.service.util.OrdOrderBaseController;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.vo.ord.VoOrderLoad;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author monkey king
 * @date 2021-09-08 22:20:22
 */
@Slf4j
@RestController
@Api(tags = {"H003.【订单导出】模块"})
@RequestMapping(value = "/shop/admin/ord")
public class OrdExportController extends OrdOrderBaseController {

    @Autowired
    private OpUploadDownloadService opUploadDownloadService;

    @Autowired
    private OrdOrderService ordOrderService;

    /**
     * 订单列表导出
     *
     * @param orderQuery 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "订单列表导出",
            notes = "订单列表导出",
            httpMethod = "POST")
    @RequestRequire
    @PostMapping("/exportOrder")
    public BaseResult exportOrder(@Valid ParamOrderQuery orderQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        if (!shopIsMember()) {
            throw new MyException("导出功能仅限【正式会员】可用~");
        }
        String taskName = "订单列表导出";
        //独立编码不支持模糊查询
        orderQuery.setUniqueCode(orderQuery.getProName());
        formatQueryParam(orderQuery);
        List<ExVoOrder> list = ordOrderService.listOrderByConditionExcelExport(orderQuery);
        if (LocalUtils.isEmptyAndNull(list)) {
            return BaseResult.okResultNoData();
        }
        if (!LocalUtils.isEmptyAndNull(list)) {
            int shopId = getShopId();
            int userId = getUserId();
            String chkPriceInit = ConstantPermission.CHK_PRICE_INIT;
            String chkPriceTrade = ConstantPermission.CHK_PRICE_TRADE;
            String chkPriceAgency = ConstantPermission.CHK_PRICE_AGENCY;
            String chkPriceSale = ConstantPermission.CHK_PRICE_SALE;
            String chkPriceFinish = ConstantPermission.CHK_PRICE_FINISH;
            boolean hasPriceInit = servicesUtil.hasPermission(shopId, userId, chkPriceInit);
            boolean hasPriceTrade = servicesUtil.hasPermission(shopId, userId, chkPriceTrade);
            boolean hasPriceAgency = servicesUtil.hasPermission(shopId, userId, chkPriceAgency);
            boolean hasPriceSale = servicesUtil.hasPermission(shopId, userId, chkPriceSale);
            boolean hasPriceFinish = servicesUtil.hasPermission(shopId, userId, chkPriceFinish);
            for (ExVoOrder vo : list) {
                try {
                    vo.setUrl(new URL(servicesUtil.formatImgUrl(vo.getSmallImg())));
                    //商品状态
                    vo.setState(servicesUtil.getStateCn(vo.getState()));
                    //商品分类
                    vo.setClassify(servicesUtil.getClassifyCn(vo.getClassify()));
                    vo.setAttribute(servicesUtil.getAttributeCn(vo.getAttribute(), true));
                    vo.setRepairCard("1".equals(vo.getRepairCard()) ? "有" : "没有");
                    vo.setInitPrice(LocalUtils.formatPrice(LocalUtils.calcNumber(vo.getInitPrice(), "*", 0.01)).toString());
                    vo.setTradePrice(LocalUtils.formatPrice(LocalUtils.calcNumber(vo.getTradePrice(), "*", 0.01)).toString());
                    vo.setAgencyPrice(LocalUtils.formatPrice(LocalUtils.calcNumber(vo.getAgencyPrice(), "*", 0.01)).toString());
                    vo.setSalePrice(LocalUtils.formatPrice(LocalUtils.calcNumber(vo.getSalePrice(), "*", 0.01)).toString());
                    vo.setFinishPrice(LocalUtils.formatPrice(LocalUtils.calcNumber(vo.getFinishPrice(), "*", 0.01)).toString());
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
                    if (!hasPriceFinish) {
                        vo.setFinishPrice("无权限查看");
                    }

                    String state = vo.getOrderState();
                    if ("20".equals(state)) {
                        state = "已开单";
                    } else if ("-20".equals(state)) {
                        state = "已退货";
                    } else if ("-90".equals(state)) {
                        state = "已删除";
                    }
                    vo.setOrderState(state);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        OpUploadDownload opUploadDownload = packObject(taskName, orderQuery);
        int id = opUploadDownloadService.saveOrUpdateObjectAndReturnId(opUploadDownload);
        opUploadDownloadService.uploadFileToOss(opUploadDownload, list);
        return LocalUtils.getBaseResult(id);
    }


    private OpUploadDownload packObject(String taskName, ParamOrderQuery queryParam) {
        String st = queryParam.getSaleStDateTime();
        String et = queryParam.getSaleEtDateTime();
        validTimeScope(st, et);
        int shopId = getShopId();
        int userId = getUserId();
        String exportType = "out";
        String module = "订单模块";
        Date st1 = null;
        Date et2 = null;
        try {
            st1 = DateUtil.parse(st);
            et2 = DateUtil.parse(et);
        } catch (ParseException ignored) {
        }
        return opUploadDownloadService.packOpUploadDownload(shopId, userId, exportType, module, taskName, st1, et2);
    }


}
