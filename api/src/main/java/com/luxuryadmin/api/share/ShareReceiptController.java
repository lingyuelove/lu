package com.luxuryadmin.api.share;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.param.ord.ParamPcOrdReceipt;
import com.luxuryadmin.param.pro.ParamDeliverLogisticsDetail;
import com.luxuryadmin.param.pro.ParamDeliverShare;
import com.luxuryadmin.service.ord.OrdOrderService;
import com.luxuryadmin.service.pro.ProDeliverLogisticsService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.param.ord.ParamShareReceiptQuery;
import com.luxuryadmin.service.ord.OrdReceiptService;
import com.luxuryadmin.service.ord.OrdShareReceiptService;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.vo.ord.VoOrdReceipt;
import com.luxuryadmin.vo.ord.VoShareReceipt;
import com.luxuryadmin.vo.pro.VoDeliver;
import com.luxuryadmin.vo.pro.VoProductLoad;
import com.luxuryadmin.vo.share.VoShareShopProduct;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author monkey king
 * @date 2020-01-13 17:07:36
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/share", method = RequestMethod.GET)
@Api(tags = {"Z001.【分享】模块"}, description = "/shop/share | 外部分享,不需要登录--2.6.7")
public class ShareReceiptController extends ProProductBaseController {


    @Autowired
    private OrdShareReceiptService ordShareReceiptService;

    @Autowired
    private OrdReceiptService ordReceiptService;

    @Autowired
    private ProProductService proProductService;

    @Autowired
    private OrdOrderService ordOrderService;

    @Autowired
    private ShpShopService shopService;
    @Autowired
    private ProDeliverLogisticsService proDeliverLogisticsService;

    /**
     * 查看电子凭证
     *
     * @return Result
     */
    @ApiOperation(
            value = "查看电子凭证",
            httpMethod = "GET")
    @RequestRequire
    @GetMapping("/showReceipt")
    public BaseResult showReceipt(@Valid ParamShareReceiptQuery shareReceipt, BindingResult result) {
        servicesUtil.validControllerParam(result);
        VoShareReceipt voShareReceipt = ordShareReceiptService.getOrderNoByShareBatch(shareReceipt);
        if (LocalUtils.isEmptyAndNull(voShareReceipt)) {
            return BaseResult.defaultErrorWithMsg("找不到凭证记录!");
        }
        String orderNo = voShareReceipt.getOrderNo();
        String showProduct = voShareReceipt.getShowProduct();
        VoOrdReceipt orderReceipt = ordReceiptService.getOrdReceiptByOrderNumber(voShareReceipt.getShopId(), orderNo);
        HashMap<String, Object> hashMap = LocalUtils.getHashMap(orderReceipt);
        //显示店铺商品
        hashMap.put("showProduct", showProduct);
        //ParamProductQuery queryParam = new ParamProductQuery();
        //queryParam.setStateCode("onSale");
        //queryParam.setAttributeCode("10;20;40");
        //queryParam.setShopId(voShareReceipt.getShopId());
        if (LocalUtils.isEmptyAndNull(voShareReceipt.getShowDeliver())|| (!LocalUtils.isEmptyAndNull(voShareReceipt.getShowDeliver()) && "0".equals(voShareReceipt.getShowDeliver()))){

            return BaseResult.okResult(hashMap);
        }
        VoDeliver deliver= proDeliverLogisticsService.getDeliverByOrderNumber(voShareReceipt.getShopId(), orderNo);
        hashMap.put("deliver", deliver);
        return BaseResult.okResult(hashMap);
    }


    /**
     * 从pc端扫码查看电子凭证
     *
     * @return Result
     */
    @ApiOperation(
            value = "从pc端扫码查看电子凭证",
            httpMethod = "GET")
    @RequestRequire
    @GetMapping("/showPcReceipt")
    public BaseResult showPcReceipt(@Valid ParamPcOrdReceipt param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        //电子凭证分享批号为空，从pc端电子凭证扫码过来；根据店铺编号和订单编号去查询订单；
        Integer shopId = shopService.getShopIdByShopNumber(param.getShopNumber());
        if (shopId == null) {
            return BaseResult.defaultErrorWithMsg("找不到凭证记录!");
        }
        String orderNo = param.getOrderBizId();
        VoOrdReceipt orderReceipt = ordReceiptService.getOrdReceiptByOrderNumber(shopId, orderNo);
        HashMap<String, Object> hashMap = LocalUtils.getHashMap(orderReceipt);
        //显示店铺商品
        hashMap.put("showProduct", "0");
        return BaseResult.okResult(hashMap);
    }

    /**
     * 查看分享出去的产品列表
     *
     * @param shopNumber
     * @param pageNum
     * @return
     */
    @ApiOperation(
            value = "查看--分享出去的产品列表六个循环",
            notes = "查看分享出去的产品列表六个循环",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "pageNum", value = "当前页数"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "shopNumber", value = "店铺id"),
    })
    @RequestRequire
    @GetMapping("/wxShareProduct")
    public BaseResult<Map<String, Object>> getShareShopProductListByShopId(@RequestParam(name = "shopNumber", required = true) String shopNumber, @RequestParam(name = "pageNum", required = true) String pageNum) {

        Map<String, Object> objectMap = new HashMap<>();
        PageHelper.startPage(Integer.parseInt(pageNum), 6);
        List<VoShareShopProduct> voShareShopProducts = proProductService.getShareShopProductListByShopId(Integer.parseInt(shopNumber));
        PageInfo pageInfo = new PageInfo(voShareShopProducts);
        objectMap.put("total", pageInfo.getTotal());
        objectMap.put("list", voShareShopProducts);
        return BaseResult.okResult(objectMap);
    }


    @ApiOperation(
            value = "查看--分享出去的物流信息--2.6.7",
            notes = "查看--分享出去的物流信息--2.6.7",
            httpMethod = "GET")
    @GetMapping("/getShareDeliver")
    public BaseResult<VoDeliver> getShareDeliver(@Valid ParamDeliverShare deliverShare, BindingResult result)  throws UnsupportedEncodingException {
        servicesUtil.validControllerParam(result);
        ParamDeliverLogisticsDetail params =new ParamDeliverLogisticsDetail();
        BeanUtils.copyProperties(deliverShare,params);
        VoDeliver voProDeliverDetail = proDeliverLogisticsService.getDeliverByLogisticsNumAndPhone(params);
        VoProductLoad vo=proProductService.getProductDetailByShopIdBizId(10684, deliverShare.getBizId());
        //判断商品是否为空
        if (LocalUtils.isEmptyAndNull(vo)){
            return LocalUtils.getBaseResult(voProDeliverDetail);
        }
        voProDeliverDetail.setProductName(vo.getName());
        if (LocalUtils.isEmptyAndNull(voProDeliverDetail)){
            voProDeliverDetail =new VoDeliver();
            voProDeliverDetail.setLogisticsNumber(deliverShare.getLogisticsNumber());

            return LocalUtils.getBaseResult(voProDeliverDetail);
        }


        return LocalUtils.getBaseResult(voProDeliverDetail);
    }
}