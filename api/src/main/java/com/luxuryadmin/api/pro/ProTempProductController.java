package com.luxuryadmin.api.pro;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.entity.pro.ProTempProduct;
import com.luxuryadmin.enums.pro.EnumProRequest;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.service.pro.ProTempProductService;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.vo.pro.VoProRedisNum;
import com.luxuryadmin.vo.pro.VoProTempProduct;
import com.luxuryadmin.vo.pro.VoProductLoad;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 临时仓
 *
 * @author monkey king
 * @Date 2019/12/01 4:07
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/pro")
@Api(tags = {"C004.【临时仓】模块 --2.6.4❤--zs"})
public class ProTempProductController extends ProProductBaseController {

    @Autowired
    private ProTempProductService proTempProductService;

    /**
     * 加载【临时仓商品】页面
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = " 加载【临时仓商品】页面",
            notes = " 加载【临时仓商品】页面 --2.6.4❤--zs",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @PostMapping("/listProTempProduct")
    public BaseResult listProTempProduct(@RequestParam Map<String, String> params,
                                         @Valid ParamProTempProductQuery queryParam, BindingResult result) {
        servicesUtil.validControllerParam(result);
        try {
            Integer shopId = getShopId();
            formatQueryParam(queryParam);
            //独立编码不支持模糊查询
            queryParam.setUniqueCode(queryParam.getProName());
            PageHelper.startPage(getPageNum(), PAGE_SIZE_10);
            BasicParam basicParam = getBasicParam();
            //判断版本控制是否在2.5.2或以上版本
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.5.2") < 0) {//2.5.2以前版本
                List<VoProductLoad> proList = proTempProductService.listVoProductLoad(queryParam);
                String appVersion = getBasicParam().getAppVersion();
                formatVoProductLoad(appVersion, proList, queryParam.getSortKey());
                return LocalUtils.getBaseResult(proList);
            } else {//2.5.2之后版本
                String priceType = queryParam.getDefaultPrice();//获取接收参数---临时仓商品列表价格类型检索字段
                //判断redis是否有默认价格类型
                String defaultRequest = "";
                String queryPriceType = queryParam.getPriceType();
                String priceMin = queryParam.getPriceMin();
                String priceMax = queryParam.getPriceMax();
                //判断redis是否有key值
                if (redisUtil.hasKey(ConstantRedisKey.getProDefaultRequest(queryParam.getProTempId()))) {
                    defaultRequest = redisUtil.get(ConstantRedisKey.getProDefaultRequest(queryParam.getProTempId()));//获取接收参数---是否为登录后初次请求接口
                } else {
                    defaultRequest = "tradePrice";
                }
                //判断redis默认价格类型与请求价格类型是否一致
                if (defaultRequest != null && !defaultRequest.equals(queryParam.getDefaultPrice())) {
                    proTempProductService.updateSalePriceByTempId(queryParam.getProTempId());//根据临时仓id修改商品销售价格为空
                    redisUtil.set(ConstantRedisKey.getProDefaultRequest(queryParam.getProTempId()), priceType);
                }
                if (StringUtil.isNotBlank(queryParam.getProName()) && queryParam.getProName().contains("#")) {
                    String uniqueCode = queryParam.getProName().replace("#", "");
                    queryParam.setUniqueCode(uniqueCode);
                }

                //修改临时仓默认价格类型
                proTempProductService.updateTempPriceTypeById(priceType, queryParam.getProTempId());
                //查询临时仓商品列表
                List<VoProductLoad> proList = proTempProductService.listVoProductLoadByPrice(queryParam);
                //查询临时仓价格汇总2.6.4新增需求
                Map<String, Object> objectMap = proTempProductService.getProductLoadByPriceByPrice(queryParam);
                Map<String, Object> objectMapResult = new HashMap<>();
                String chkPriceInit = ConstantPermission.CHK_PRICE_INIT;
                boolean hasInitPricePerm = hasPermWithCurrentUser(chkPriceInit);
                String userPerms = servicesUtil.getUserPerms(getShopId(), getUserId());
                //查看成本价
                String showInitPrice = ConstantPermission.CHK_PRICE_INIT;
                boolean initPrice = hasPermWithCurrentUser(userPerms, showInitPrice);
                //判断获取商品列表是否为空
                if (!LocalUtils.isEmptyAndNull(proList)) {

                    //查看销售价
                    String showSalePrice = ConstantPermission.CHK_PRICE_SALE;
                    boolean salePrice = hasPermWithCurrentUser(userPerms, showSalePrice);
                    //便利商品列表，判断检索价格类型，设置显示价格
                    for (VoProductLoad voProductLoad : proList) {
                        ////是否有查看成本价的权限;
                        if (!hasInitPricePerm) {
                            voProductLoad.setShowInitPrice(null);
                        }

                        if (!LocalUtils.isEmptyAndNull(priceType)) {
                            switch (priceType) {
                                case "initPrice":
                                    voProductLoad.setShowPrice(initPrice ? voProductLoad.getInitPrice() : null);
                                    break;
                                case "tradePrice":
                                    //查看友商价
                                    String showTradePrice = ConstantPermission.CHK_PRICE_TRADE;
                                    voProductLoad.setShowPrice(hasPermWithCurrentUser(userPerms, showTradePrice) ? voProductLoad.getTradePrice() : null);
                                    break;
                                case "agencyPrice":
                                    //查看代理价
                                    String showAgencyPrice = ConstantPermission.CHK_PRICE_AGENCY;
                                    voProductLoad.setShowPrice(hasPermWithCurrentUser(userPerms, showAgencyPrice) ? voProductLoad.getAgencyPrice() : null);
                                    break;
                                case "salePrice":

                                    voProductLoad.setShowPrice(salePrice ? voProductLoad.getSalePrice() : null);
                                    break;
                                default://无价格类型时默认为0
                                    voProductLoad.setShowPrice("0.00");
                                    break;
                            }
                        }
                        if (voProductLoad.getTotalNum() <= 0) {
                            voProductLoad.setStateUs("40");
                        }

                        String tempProState = proTempProductService.getTempProState(voProductLoad.getShopId(), Integer.parseInt(queryParam.getProTempId()), voProductLoad.getProId());
                        voProductLoad.setTempProState(tempProState);
                        if ("1".equals(tempProState)) {
                            voProductLoad.setTempProStateName("本仓已售罄");
                        }
                        if ("2".equals(tempProState)) {
                            voProductLoad.setTempProStateName("仓库无库存");
                        }
                        //2.6.6添加判断是否是锁单商品
                        if (LocalUtils.isEmptyAndNull(voProductLoad.getTempProStateName())){
                            VoProRedisNum proRedisNum = proProductService.getProRedisNum(voProductLoad.getShopId(), voProductLoad.getBizId());
                            //判断是否锁单商品
                            voProductLoad.setLockState(proRedisNum.getLeftNum() == 0 ? "1" : "0");
                        }

                    }

                }

                String appVersion = getBasicParam().getAppVersion();
                formatVoProductLoad(appVersion, proList, queryParam.getSortKey());
                objectMapResult.put("objList", proList);
                objectMapResult.put("uPermTotalPrice", hasPermToPageWithCurrentUser(showInitPrice));

                //临时仓商品汇总权限全部走成本价权限
                if (initPrice) {
                    //总数量
                    objectMapResult.put("totalNum", objectMap.get("totalNum"));
                    objectMapResult.put("totalNumName", "总数量");
                    objectMapResult.put("totalInitPriceName", "总成本");
                    objectMapResult.put("totalSalePriceName", "总销售");
                    //总成本 成本价权限判断
                    String totalInitPrice = StringUtil.removeEnd(objectMap.get("totalInitPrice").toString(), ".00");
                    objectMapResult.put("totalInitPrice", totalInitPrice);
                    //总销售 销售价权限判断
                    String totalSalePrice = StringUtil.removeEnd(objectMap.get("totalSalePrice").toString(), ".00");
                    objectMapResult.put("totalSalePrice", totalSalePrice);
                }
                return BaseResult.okResult(objectMapResult);
            }
        } catch (Exception e) {
            throw new MyException("临时仓商品列表异常" + e);
        }
    }


    /**
     * 添加商品到临时仓
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "添加商品到临时仓",
            notes = "添加商品到临时仓",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @PostMapping("/addProTempProduct")
    @RequiresPermissions("pro:temp:addProTempProduct")
    public BaseResult addProTempProduct(@RequestParam Map<String, String> params,
                                        @Valid ParamProTempProductAdd paramAdd, BindingResult result) {
        servicesUtil.validControllerParam(result);
        int tempId = Integer.parseInt(paramAdd.getProTempId());
        String proIds = paramAdd.getProIds();
        proTempProductService.addProTempProduct(getShopId(), getUserId(), tempId, proIds);
        return BaseResult.okResult();
    }


    /**
     * 显示临时仓商品详情的编辑信息
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "显示临时仓商品详情的编辑信息---2.5.2--mong",
            notes = "显示临时仓商品详情的编辑信息---2.5.2--mong",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @GetMapping("/showProTempProductEditInfo")
    public BaseResult showProTempProductEditInfo(@RequestParam Map<String, String> params,
                                                 @Valid ParamProTempShowEditInfo edit, BindingResult result) {
        servicesUtil.validControllerParam(result);
        BasicParam basicParam = getBasicParam();
        int shopId = getShopId();
        int tempId = Integer.parseInt(edit.getProTempId());
        int proId = Integer.parseInt(edit.getProId());
        //判断版本控制是否在2.5.2或以上版本
        try {
            //2.5.2以前版本
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.5.2") < 0) {
                ProTempProduct proTempProduct = proTempProductService.getProTempProduct(shopId, tempId, proId);
                return LocalUtils.getBaseResult(proTempProduct);
            } else {
                String priceType = "";
                if (redisUtil.hasKey(ConstantRedisKey.getProDefaultRequest(String.valueOf(tempId)))) {
                    priceType = redisUtil.get(ConstantRedisKey.getProDefaultRequest(String.valueOf(tempId)));//获取接收参数---是否为登录后初次请求接口
                } else {
                    priceType = "tradePrice";
                }

                //查询临时仓编辑商品信息
                VoProTempProduct voProTempProduct = proTempProductService.getNewProTempProduct(shopId, tempId, proId);
                if (voProTempProduct != null) {
                    if (!LocalUtils.isEmptyAndNull(priceType)) {
                        switch (priceType) {
                            case "initPrice"://成本价
                                voProTempProduct.setTradePrice(voProTempProduct.getInitPrice());
                                break;
                            case "tradePrice"://友商价
                                voProTempProduct.setTradePrice(voProTempProduct.getTradePrice());
                                break;
                            case "agencyPrice"://代理价
                                voProTempProduct.setTradePrice(voProTempProduct.getAgencyPrice());
                                break;
                            case "salePrice"://销售价
                                voProTempProduct.setTradePrice(voProTempProduct.getSalePrice());
                                break;
                            default://无价格类型时默认为0
                                voProTempProduct.setTradePrice("000.00");
                                break;
                        }
                    }
                    voProTempProduct.setInitPrice(voProTempProduct.getShowInitPrice());
                }
                return LocalUtils.getBaseResult(voProTempProduct);
            }
        } catch (Exception e) {
            throw new MyException("临时仓获取修改商品信息异常" + e);
        }

    }

    /**
     * 修改临时仓商品详情
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "修改临时仓商品详情 2.5.2--mong",
            notes = "修改临时仓商品详情(执行此接口前,先从showProTempProductEditInfo接口获取所填充信息)",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @PostMapping("/updateProTempProduct")
    @RequiresPermissions("pro:temp:addProTempProduct")
    public BaseResult updateProTempProduct(@RequestParam Map<String, String> params,
                                           @Valid ParamProTempUpdate paramUpdate, BindingResult result) {
        servicesUtil.validControllerParam(result);
        try {
            int shopId = getShopId();
            int id = Integer.parseInt(paramUpdate.getId());
            BasicParam basicParam = getBasicParam();
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.5.2") >= 0) {//判断当前版本是否为2.5.2或以上
                if (paramUpdate.getBizId() == null) {
                    throw new MyException("商品唯一标识不允许为空");
                }
                String num = paramUpdate.getNum();//获取临时仓修改商品数量
                String bizId = paramUpdate.getBizId();//获取接收参数商品标识
                int shopNum = proTempProductService.getShopNumById(bizId);//查询原商品总数
                if (!LocalUtils.isEmptyAndNull(num)) {//判断修改数量是否为空
                    if (Integer.parseInt(num) > shopNum) {//判断修改数量是否大于原商品数量
                        return BaseResult.defaultErrorWithMsg("临时仓数量不能超过原商品数量!");
                    }
                }
                /*String salePrice=paramUpdate.getSalePrice();
                if (salePrice!=null && !salePrice.equals("")){
                    String redisKey=ConstantRedisKey.getProDefaultRequest(paramUpdate.getTempId());
                    redisUtil.set(redisKey,paramUpdate.getPriceType());
                }*/
            }

            ProTempProduct proTempProduct = proTempProductService.getProTempProductById(shopId, id);
            if (LocalUtils.isEmptyAndNull(proTempProduct)) {
                return BaseResult.defaultErrorWithMsg("临时仓不存在此商品!");
            }
            paramUpdate.setShopId(shopId);
            proTempProductService.updateProTempProduct(paramUpdate);
            return BaseResult.okResult();
        } catch (Exception e) {
            throw new MyException("修改临时仓商品信息异常" + e);
        }
    }

    /**
     * 删除临时仓商品
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "删除临时仓商品",
            notes = "删除临时仓商品",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @PostMapping("/deleteProTempProduct")
    @RequiresPermissions("pro:temp:addProTempProduct")
    public BaseResult deleteProTempProduct(@RequestParam Map<String, String> params,
                                           @Valid ParamProTempProductAdd paramAdd, BindingResult result) {
        servicesUtil.validControllerParam(result);
        int tempId = Integer.parseInt(paramAdd.getProTempId());
        String proIds = paramAdd.getProIds();
        proTempProductService.deleteProTempProduct(getShopId(), tempId, proIds);
        return BaseResult.okResult();
    }


    /**
     * 一键清空临时仓
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "一键清空临时仓",
            notes = "一键清空临时仓",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @PostMapping("/oneKeyDeleteProTemp")
    @RequiresPermissions("pro:temp:addProTempProduct")
    public BaseResult oneKeyDeleteProTemp(@RequestParam Map<String, String> params,
                                          @Valid ParamProTempId proTempId, BindingResult result) {
        servicesUtil.validControllerParam(result);
        proTempProductService.deleteAllProTempProductByTempId(getShopId(), Integer.parseInt(proTempId.getProTempId()));
        return BaseResult.okResult();
    }


}
