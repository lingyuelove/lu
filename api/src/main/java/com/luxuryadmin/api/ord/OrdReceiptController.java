package com.luxuryadmin.api.ord;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.entity.ord.OrdShareReceipt;
import com.luxuryadmin.entity.pro.ProShare;
import com.luxuryadmin.param.ord.ParamShareReceiptSave;
import com.luxuryadmin.service.ord.OrdReceiptService;
import com.luxuryadmin.service.ord.OrdShareReceiptService;
import com.luxuryadmin.service.pro.ProShareService;
import com.luxuryadmin.vo.ord.VoOrdReceipt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 订单收据
 *
 * @author monkey king
 * @date 2020-01-20 23:00:26
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/ord", method = RequestMethod.POST)
@Api(tags = {"C002.【订单】模块"}, description = "/shop/user/ord |用户【订单】模块相关")
public class OrdReceiptController extends BaseController {

    @Autowired
    private OrdReceiptService ordReceiptService;

    @Autowired
    private OrdShareReceiptService ordShareReceiptService;


    @Autowired
    private ProShareService proShareService;

    /**
     * 查看电子凭证
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "查看电子凭证",
            notes = "查看电子凭证",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "orderBizId", value = "订单编号"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @GetMapping("/getOrderReceipt")
    public BaseResult getOrderReceipt(@RequestParam Map<String, String> params) {
        String orderBizId = params.get("orderBizId");
        VoOrdReceipt ordReceipt = ordReceiptService.getOrdReceiptByOrderNumber(getShopId(), orderBizId);
        return LocalUtils.getBaseResult(ordReceipt);
    }


    /**
     * 保存分享【电子凭证】的记录
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "保存分享【电子凭证】的记录",
            notes = "保存分享【电子凭证】的记录",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/saveShareReceipt")
    public BaseResult saveShareReceipt(@RequestParam Map<String, String> params,
                                       @Valid ParamShareReceiptSave save, BindingResult result)  {
        servicesUtil.validControllerParam(result);

        String showProduct = save.getShowProduct();
        OrdShareReceipt receipt = new OrdShareReceipt();
        receipt.setFkShpShopId(getShopId());
        receipt.setFkShpUserId(getUserId());
        receipt.setShopNumber(getShopNumber());
        receipt.setUserNumber(getUserNumber());
        receipt.setShowDeliver(save.getShowDeliver());
        receipt.setShowProduct(showProduct);
        receipt.setOrderNo(save.getOrderNo());
        String receiveType = save.getReceiptType();
        receipt.setReceiptType(receiveType);
        receipt.setShowType(save.getShowType());
        String saveBatch = ordShareReceiptService.saveShareReceipt(receipt);
        if (ConstantCommon.ONE.equals(showProduct)) {
            //分享电子凭证且分享店铺所有商品,只显示销售价
            ProShare share = new ProShare();
            share.setFkShpShopId(getShopId());
            share.setFkShpUserId(getUserId());
            share.setShopNumber(getShopNumber());
            share.setUserNumber(getUserNumber());
            share.setShowPrice("salePrice");
            share.setProId("all");
            share.setShareBatch(saveBatch);
            proShareService.saveShareProduct(share);
        }
        return LocalUtils.getBaseResult(saveBatch);
    }
}
