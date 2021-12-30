package com.luxuryadmin.controller.product;

import com.luxuryadmin.biz.BizLeaguerBaseController;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.weixxin.mpsdk.WxEntity;
import com.luxuryadmin.common.utils.weixxin.mpsdk.WxService;
import com.luxuryadmin.config.WxPayProperties;
import com.luxuryadmin.param.api.ParamWxJsCode;
import com.luxuryadmin.param.biz.ParamLeaguerProductQuery;
import com.luxuryadmin.param.pro.ParamUnionBizId;
import com.luxuryadmin.service.BasicsService;
import com.luxuryadmin.service.biz.BizShopUnionService;
import com.luxuryadmin.service.shp.ShpWechatService;
import com.luxuryadmin.vo.pro.VoLeaguerProduct;
import com.luxuryadmin.vo.shp.VoShpWechat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author sanjin145
 * @date 2020-09-22
 */
@Slf4j
@RestController
@RequestMapping(value = "/product")
@Api(tags = "MP.商品接口", description = "商品接口 ")
public class ProductController extends BizLeaguerBaseController {

    @Autowired
    private ShpWechatService shpWechatService;

    @ApiOperation(
            value = "获取联盟商品详情;",
            notes = "获取联盟商品详情;",
            httpMethod = "GET")
    @RequestRequire
    @GetMapping("/getUnionProductDetail")
    public BaseResult<VoLeaguerProduct> getUnionProductDetail(@Valid ParamUnionBizId unionBizId, BindingResult result) throws Exception {
        servicesUtil.validControllerParam(result);
        String bizId = unionBizId.getBizId();
        Integer leaguerShopId = Integer.parseInt(unionBizId.getShopId());
        VoLeaguerProduct leaProDetail = proProductService.getUnionProductDetailByBizId(bizId, leaguerShopId);
        if (LocalUtils.isEmptyAndNull(leaProDetail)) {
            return BaseResult.okResultNoData();
        }
        new BigDecimal(leaProDetail.getTradePrice());
        DecimalFormat df = new DecimalFormat(",##0.##");
        leaProDetail.setTradePrice(df.format(LocalUtils.calcNumber(leaProDetail.getTradePrice(), "*", 0.01)));
        String productImg = leaProDetail.getProductImg();
        if (!LocalUtils.isEmptyAndNull(productImg)) {
            String[] productImgArray = productImg.split(";");
            for (int i = 0; i < productImgArray.length; i++) {
                if (!productImgArray[i].contains("http")) {
                    productImgArray[i] = servicesUtil.formatImgUrl(productImgArray[i], false);
                }
            }
            leaProDetail.setProductImgList(productImgArray);
            leaProDetail.setProductImg(null);
        }
        String videoUrl = leaProDetail.getVideoUrl();
        leaProDetail.setVideoUrl(servicesUtil.formatImgUrl(videoUrl));
        //设置友商微信列表
        List<VoShpWechat> voShpWechatList = shpWechatService.listShpWechat(leaProDetail.getShopId());
        leaProDetail.setVoShpWechatList(voShpWechatList);
        return LocalUtils.getBaseResult(leaProDetail);
    }

}