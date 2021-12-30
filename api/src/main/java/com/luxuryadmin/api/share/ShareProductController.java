package com.luxuryadmin.api.share;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.ShpShopConfig;
import com.luxuryadmin.entity.sys.SysEnum;
import com.luxuryadmin.param.pro.ParamShareProductForApplets;
import com.luxuryadmin.param.pro.ParamShareProductQuery;
import com.luxuryadmin.vo.pro.VoShareProductForApplets;
import com.luxuryadmin.service.pro.ProClassifyService;
import com.luxuryadmin.service.pro.ProShareService;
import com.luxuryadmin.service.pro.ProTempProductService;
import com.luxuryadmin.service.shp.ShpShopConfigService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.service.sys.SysEnumService;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.vo.pro.VoProClassify;
import com.luxuryadmin.vo.pro.VoProductLoad;
import com.luxuryadmin.vo.pro.VoShareProduct;
import com.luxuryadmin.vo.shp.VoUserShopBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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
@Api(tags = {"Z001.【分享】模块"}, description = "/shop/share | 外部分享,不需要登录")
public class ShareProductController extends ProProductBaseController {


    @Autowired
    private ProShareService proShareService;

    @Autowired
    private ShpShopService shpShopService;

    @Autowired
    private ProClassifyService proClassifyService;

    @Autowired
    private SysEnumService sysEnumService;

    @Autowired
    private ProTempProductService proTempProductService;
    @Autowired
    private ShpShopConfigService shopConfigService;

    private static final String appVersion = "2.4.1";


    /**
     * 获取分享产品的店铺信息和分类列表
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "获取分享产品的店铺信息和分类列表",
            notes = "获取分享产品的分类列表",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @GetMapping("/getShopInfo")
    public BaseResult getShopInfo(@RequestParam Map<String, String> params,
                                  @Valid ParamShareProductQuery shareProduct, BindingResult result) {
        servicesUtil.validControllerParam(result);
        VoUserShopBase shopBase = shpShopService.getShareShopInfoByShopNumber(shareProduct.getShopNumber());
        if (LocalUtils.isEmptyAndNull(shopBase)) {
            return BaseResult.okResultNoData();
        }
        List<VoProClassify> voProClassifyList = proClassifyService.listProClassifyByState(shopBase.getShopId(), "1");

        HashMap<String, Object> hashMap = LocalUtils.getHashMap(shopBase);
        hashMap.put("classifyList", voProClassifyList);
        shopBase.setShopHeadImgUrl(servicesUtil.formatImgUrl(shopBase.getShopHeadImgUrl()));
        return BaseResult.okResult(hashMap);
    }


    /**
     * 根据分享批次获取分享信息
     * @return Result
     */
    @ApiOperation(
            value = "根据分享批次获取分享信息",
            notes = "根据分享批次获取分享信息",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "shareBatch", value = "分享批号")
    })
    @RequestRequire
    @GetMapping("/getByShareBatch")
    public BaseResult<VoShareProductForApplets> getByShareBatch(ParamShareProductForApplets shareProductForApplets) {

        VoShareProductForApplets shareProduct = proShareService.getByShareBatch( shareProductForApplets);

        return BaseResult.okResult(shareProduct);
    }
    /**
     * 查看分享出去的产品列表
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "查看--分享出去的产品列表---2.5.2---mong",
            notes = "查看分享出去的产品列表",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @GetMapping("/showProduct")
    public BaseResult showProduct(@RequestParam Map<String, String> params,
                                  @Valid ParamShareProductQuery shareProduct, BindingResult result) {
        servicesUtil.validControllerParam(result);
        formatQueryParam(shareProduct);
        shareProduct.setStateCode("store");
        String shopNumber = shareProduct.getShopNumber();
        //友商分享过来的商品, 不需要知道具体的店铺信息
        List<VoProClassify> voProClassifyList = new ArrayList<>();
        Integer shopId = 0;
        if ("leaguer".equals(shopNumber)) {
            shareProduct.setShopNumber("0");
            List<SysEnum> enumProClassify = sysEnumService.getEnumProClassify();
            int i = 0;
            for (SysEnum sysEnum : enumProClassify) {
                VoProClassify classify = new VoProClassify();
                classify.setCode(sysEnum.getCode());
                classify.setName(sysEnum.getName());
                classify.setState(sysEnum.getState());
                classify.setId(++i);
                voProClassifyList.add(classify);
            }
        } else {
            shopId = shpShopService.getShopIdByShopNumber(shopNumber);
            //没找到店铺
            if (LocalUtils.isEmptyAndNull(shopId)) {
                return BaseResult.okResultNoData();
            }
             voProClassifyList = proShareService.listProClassifyByShareBatch(shareProduct.getShareBatch());
            if (LocalUtils.isEmptyAndNull(voProClassifyList)){
                voProClassifyList = proClassifyService.listProClassifyByState(shopId, "1");
            }
//            voProClassifyList = proClassifyService.listProClassifyByState(shopId, "1");
        }
        HashMap<String, Object> hashMap = LocalUtils.getHashMap("classifyList", voProClassifyList);

        VoShareProduct voShareProduct = proShareService.getProIdsByShareBatch(shareProduct);
        if (LocalUtils.isEmptyAndNull(voShareProduct)) {
            return BaseResult.okResult(hashMap);
        }


        shareProduct.setShopId(voShareProduct.getShopId());
        String proId = voShareProduct.getProId();
        List<VoProductLoad> shareProductList;


        //临时仓商品详情,需要优先显示临时仓修改的内容;
        String shareName = voShareProduct.getShareName();
        int tempId = 0;
        if (shareName.startsWith("tempPro")) {
            tempId = Integer.parseInt(shareName.split("_")[1]);

            //临时仓的判断
            if (ConstantCommon.ALL.equals(proId)) {
                //分享整个临时仓的商品, 把临时仓的所有商品id,找出来;
                List<Integer> proIdList = proTempProductService.listProIdFromTempProduct(shopId, tempId);
                if (LocalUtils.isEmptyAndNull(proIdList)) {
                    return BaseResult.okResultNoData();
                }
                proId = LocalUtils.packString(proIdList.toArray());
            }

        } else {
            //非临时仓的判断
            if (ConstantCommon.ALL.equals(proId)) {
                //分享整个店铺时,只分享已上架和锁单中商品
                shareProduct.setStateCode("onRelease");
            }
        }


        //分享具体商品
        shareProduct.setProIds(proId);
        //对多选的参数(品牌分类)进行逗号分开;
        if (!LocalUtils.isEmptyAndNull(voShareProduct.getClassifyCode())){
//          String  classifyCode=voShareProduct.getClassifyCode().replace(",",";");
            shareProduct.setClassifyCodes(LocalUtils.formatParamForSqlInQuery(voShareProduct.getClassifyCode(), ","));
        }

        PageHelper.startPage(getPageNum(), PAGE_SIZE_10);
        shareProductList = proShareService.listShareProductByProId(shareProduct);
        if (!LocalUtils.isEmptyAndNull(shareProductList)) {

            for (VoProductLoad vo : shareProductList) {
                if (tempId > 0) {
                    formatTempProNew(shopId, tempId, vo);
                }
                formatVoProductLoad(appVersion, vo, true, null);
                //是否显示价格
                showPrice(voShareProduct.getShowPrice(), vo);
            }
        }
        ShpShopConfig shopConfig =shopConfigService.getShopConfigByShopId(shopId);
        hashMap.put("proList", shareProductList);
        hashMap.put("shopId", shopId);
       // 是否开启小程序访客功能 0未开启 1已开启
        if (shopConfig == null){
            hashMap.put("openShareUser", "0");
        }else {
            hashMap.put("openShareUser", shopConfig.getOpenShareUser());
        }
        return BaseResult.okResult(hashMap);
    }


    /**
     * 获取分享商品详情;根据shopId和bizId
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "获取分享商品详情;根据shopId和bizId;",
            notes = "获取分享商品详情;根据shopId和bizId",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "bizId", value = "商品业务id"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "shopId", value = "店铺id"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @GetMapping("/getShareProductDetail")
    public BaseResult getShareProductDetail(@RequestParam Map<String, String> params,
                                            @Valid ParamShareProductQuery shareProduct, BindingResult result) {

        servicesUtil.validControllerParam(result);
        if ("leaguer".equals(shareProduct.getShopNumber())) {
            shareProduct.setShopNumber("0");
        }
        VoShareProduct voShareProduct = proShareService.getProIdsByShareBatch(shareProduct);
        if (LocalUtils.isEmptyAndNull(voShareProduct)) {
            return BaseResult.okResultNoData();
        }
        String shopId = params.get("shopId");
        String bizId = params.get("bizId");
        VoProductLoad voPro = proProductService.getShareProductDetailByShopIdBizId(shopId, bizId);
        if (voPro == null) {
            return BaseResult.okResultNoData();
        }
        //临时仓商品详情,需要优先显示临时仓修改的内容;
        String shareName = voShareProduct.getShareName();
        int tempId = 0;
        if (shareName.startsWith("tempPro")) {
            tempId = Integer.parseInt(shareName.split("_")[1]);
        }
        if (tempId > 0) {
            shopId = LocalUtils.isEmptyAndNull(shopId) ? "0" : shopId;
            formatTempProNew(Integer.parseInt(shopId), tempId, voPro);
        }
        //是否显示价格
        showPrice(voShareProduct.getShowPrice(), voPro);
        formatVoProductLoad(appVersion, voPro, true, null);
        return LocalUtils.getBaseResult(voPro);
    }

    /**
     * h5获取分享商品详情;
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "获取分享商品详情;根据bizId;",
            notes = "获取分享商品详情;根据bizId;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "bizId", value = "商品业务id"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "tp", value = "商品业务id"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "tid", value = "商品业务id"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "scope", value = "价格权限;隐藏所有价格时,传值为no"),

    })
    @RequestRequire
    @GetMapping("/getScannerProductDetail")
    public BaseResult getScannerProductDetail(@RequestParam Map<String, String> params) {
        String bizId = params.get("bizId");
        String tidStr = params.get("tid");
        String scope = params.get("scope");
        VoProductLoad voPro = proProductService.getShareProductDetailByShopIdBizId(bizId);
        if (voPro == null) {
            return BaseResult.okResultNoData();
        }
        //是否显示价格
        VoShareProduct voShareProduct = new VoShareProduct();
        //价格可看
        if (!LocalUtils.isEmptyAndNull(scope) && !"no".equalsIgnoreCase(scope)) {
            String[] scopeArray = scope.split(",");
            StringBuilder showPrice = new StringBuilder();
            for (String s : scopeArray) {
                switch (s) {
                    case "1":
                        showPrice.append(",initPrice");
                        break;
                    case "2":
                        showPrice.append(",tradePrice");
                        break;
                    case "3":
                        showPrice.append(",agencyPrice");
                        break;
                    case "4":
                        showPrice.append(",salePrice");
                        break;
                    default:
                        break;
                }
            }
            voShareProduct.setShowPrice(showPrice.toString());
        }

        if (LocalUtils.isEmptyAndNull(scope)) {
            voShareProduct.setShowPrice("salePrice");
        }
        showPrice(voShareProduct.getShowPrice(), voPro);

        //临时仓商品详情,需要优先显示临时仓修改的内容;
        int tempId = 0;
        if (!LocalUtils.isEmptyAndNull(tidStr)) {
            tempId = Integer.parseInt(tidStr);
        }
        if (tempId > 0) {
            formatTempProNew(voPro.getShopId(), tempId, voPro);
        }

        formatVoProductLoad(appVersion, voPro, true, null);
        return LocalUtils.getBaseResult(voPro);
    }

    private void showPrice(String showPrice, VoProductLoad productLoad) {
        //不显示价格
        if (LocalUtils.isEmptyAndNull(showPrice)) {
            productLoad.setTradePrice(null);
            productLoad.setAgencyPrice(null);
            productLoad.setSalePrice(null);
            productLoad.setInitPrice(null);
        } else {
            if (!showPrice.contains("salePrice")) {
                productLoad.setSalePrice(null);
            }
            if (!showPrice.contains("agencyPrice")) {
                productLoad.setAgencyPrice(null);
            }
            if (!showPrice.contains("tradePrice")) {
                productLoad.setTradePrice(null);
            }
            if (!showPrice.contains("initPrice")) {
                productLoad.setInitPrice(null);
            }
        }
    }


    /**
     * 2.5.2分享商品显示价格
     *
     * @param showPrice
     * @param productLoad
     */
    private void newShowPrice(String showPrice, VoProductLoad productLoad) {
        //不显示价格
        if (LocalUtils.isEmptyAndNull(showPrice)) {
            productLoad.setTradePrice(null);
            productLoad.setAgencyPrice(null);
            productLoad.setSalePrice(null);
            productLoad.setInitPrice(null);
        } else {
            switch (showPrice) {
                case "initPrice"://成本价
                    productLoad.setShowPrice(productLoad.getInitPrice());
                    break;
                case "tradePrice"://友商价
                    productLoad.setShowPrice(productLoad.getTradePrice());
                    break;
                case "agencyPrice"://代理价
                    productLoad.setShowPrice(productLoad.getAgencyPrice());
                    break;
                case "salePrice"://销售价
                    productLoad.setShowPrice(productLoad.getSalePrice());
                    break;
                default://无价格类型时默认为0
                    productLoad.setShowPrice("000.00");
                    break;
            }
        }
    }

}