package com.luxuryadmin.api.pro;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.*;
import com.luxuryadmin.entity.pro.ProCheck;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.enums.pro.EnumDynamicProductState;
import com.luxuryadmin.enums.pro.EnumProAttribute;
import com.luxuryadmin.enums.shp.EnumShpOperateLogModule;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.pro.ParamProductBizId;
import com.luxuryadmin.param.pro.ParamProductQuery;
import com.luxuryadmin.param.shp.ParamAddShpOperateLog;
import com.luxuryadmin.service.op.OpPushService;
import com.luxuryadmin.service.ord.OrdOrderService;
import com.luxuryadmin.service.pro.ProCheckService;
import com.luxuryadmin.service.pro.ProDynamicProductService;
import com.luxuryadmin.service.pro.ProDynamicService;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.service.shp.ShpOperateLogService;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.vo.pro.*;
import io.swagger.annotations.*;
import com.luxuryadmin.vo.pro.VoProClassifyForAnalysis;
import com.luxuryadmin.vo.pro.VoProForAnalysisShow;
import com.luxuryadmin.vo.pro.VoProductAnalyzeDetail;
import com.luxuryadmin.vo.pro.VoProductLoad;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 商品管理-controller层;
 *
 * @author monkey king
 * @Date 2019/12/01 4:07
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/pro", method = RequestMethod.POST)
@Api(tags = {"C001.【仓库】模块--2.6.2"}, description = "/shop/user/pro |用户【仓库】模块相关--2.6.4❤")
public class ProProductStorehouseController extends ProProductBaseController {

    @Autowired
    private ProProductService proProductService;
    @Autowired
    private ProDynamicProductService proDynamicProductService;

    @Autowired
    private OpPushService opPushService;

    @Autowired
    private ShpOperateLogService shpOperateLogService;

    @Autowired
    private OrdOrderService ordOrderService;
    @Autowired
    private ProCheckService checkService;

    @Autowired
    private ProDynamicService proDynamicService;


    /**
     * 加载【全部商品仓库】页面
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载【全部商品仓库】页面",
            notes = "加载在售商品；支持模糊查询；分页；条件筛选--2.6.4❤",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @PostMapping("/listAllProductStorehouse")
    @RequiresPermissions(value = {"pro:check:ownProduct", "pro:check:entrustProduct", "pro:check:otherProduct"}, logical = Logical.OR)
    public BaseResult listAllProductStorehouse(@RequestParam Map<String, String> params,
                                               @Valid ParamProductQuery queryParam, BindingResult result) {
        queryParam.setAttributeCode(getAllProAttributeCodeExcludePawnPro(";"));
        return listPublicProductStorehouse(queryParam, result);
    }

    /**
     * 加载【自有商品仓库】页面
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载【自有商品仓库】页面",
            notes = "加载在售商品；支持模糊查询；分页；条件筛选",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @PostMapping("/listOwnProductStorehouse")
    @RequiresPermissions(value = {"pro:check:ownProduct", "pro:check:ownProduct2"}, logical = Logical.OR)
    public BaseResult listOwnProductStorehouse(@RequestParam Map<String, String> params,
                                               @Valid ParamProductQuery queryParam, BindingResult result) {

        //BasicParam basicParam = getBasicParam();
        //try {
        //    int i = VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.0");
        //    if (i >= 0) {
        //        boolean hasPerm = hasPermWithCurrentUser("pro:check:ownProduct2");
        //        if (!hasPerm) {
        //            return BaseResult.errorResult(EnumCode.ERROR_NO_SHOP_PERMISSION);
        //        }
        //    }
        //
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}

        queryParam.setAttributeCode(EnumProAttribute.OWN.getCode().toString());
        return listPublicProductStorehouse(queryParam, result);
    }

    /**
     * 加载【寄卖商品仓库】页面
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载【寄卖商品仓库】页面",
            notes = "加载在售商品；支持模糊查询；分页；条件筛选",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @PostMapping("/listEntrustProductStorehouse")
    @RequiresPermissions(value = {"pro:check:entrustProduct", "pro:check:entrustProduct2"}, logical = Logical.OR)
    public BaseResult listEntrustProductStorehouse(@RequestParam Map<String, String> params,
                                                   @Valid ParamProductQuery queryParam, BindingResult result) {
        queryParam.setAttributeCode(EnumProAttribute.ENTRUST.getCode().toString());
        return listPublicProductStorehouse(queryParam, result);
    }

    /**
     * 加载【其它商品仓库】页面
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载【其它商品仓库】页面",
            notes = "加载在售商品；支持模糊查询；分页；条件筛选",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @PostMapping("/listOtherProductStorehouse")
    @RequiresPermissions(value = {"pro:check:otherProduct", "pro:check:otherProduct2"}, logical = Logical.OR)
    public BaseResult listOtherProductStorehouse(@RequestParam Map<String, String> params,
                                                 @Valid ParamProductQuery queryParam, BindingResult result) {
        queryParam.setAttributeCode(EnumProAttribute.OTHER.getCode().toString());
        return listPublicProductStorehouse(queryParam, result);
    }


    /**
     * 获取仓库商品列表
     *
     * @param queryParam
     * @param result
     * @return
     */
    private BaseResult listPublicProductStorehouse(ParamProductQuery queryParam, BindingResult result) {
        servicesUtil.validControllerParam(result);
        //独立编码不支持模糊查询
        queryParam.setUniqueCode(queryParam.getProName());
        formatQueryParam(queryParam);
        PageHelper.startPage(getPageNum(), PAGE_SIZE_10);
        List<VoProductLoad> proProductList = proProductService.listProProductByVoProductQueryParam(queryParam);
        String appVersion = getBasicParam().getAppVersion();

        for (VoProductLoad voProductLoad : proProductList) {
            formatVoProductLoad(appVersion, voProductLoad, queryParam.getSortKey());
            //hidePrice(voProductLoad);
            //2.6.6添加判断是否是锁单商品
            VoProRedisNum proRedisNum = proProductService.getProRedisNum(voProductLoad.getShopId(), voProductLoad.getBizId());
            //判断是否锁单商品
            voProductLoad.setLockState(proRedisNum.getLeftNum() == 0 ? "1" : "0");
        }

        try {
            //2.6.6版本加动态名称
            BasicParam basicParam = getBasicParam();
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.6") >= 0 && proProductList != null && proProductList.size() > 0) {
                List<Integer> proIds = proProductList.stream().map(VoProductLoad::getProId).collect(Collectors.toList());
                int shopId = getShopId();
                List<VoDynamicAndProductInfoList> voDynamicAndProductInfoLists = proDynamicService.listInfoByProId(proIds, shopId);
                if (voDynamicAndProductInfoLists != null && voDynamicAndProductInfoLists.size() > 0) {
                    Map<Integer, VoDynamicAndProductInfoList> dynamicInfoMap = voDynamicAndProductInfoLists.stream().
                            collect(Collectors.toMap(VoDynamicAndProductInfoList::getProId, Function.identity()));
                    proProductList.stream().forEach(pp -> {
                        if (dynamicInfoMap.get(pp.getProId()) != null) {
                            VoDynamicAndProductInfoList voDynamicAndProductInfoList = dynamicInfoMap.get(pp.getProId());
                            if (dynamicInfoMap.get(pp.getProId()) != null) {
                                pp.setDynamicName("商品位置：" + dynamicInfoMap.get(pp.getProId()).getName());
                                pp.setDynamicId(dynamicInfoMap.get(pp.getProId()).getDynamicId().toString());
                            } else {
                                pp.setDynamicName("商品位置：无");
                            }
                            pp.setDynamicId(voDynamicAndProductInfoList.getDynamicId().toString());
                            if (StringUtil.isBlank(queryParam.getDynamicId())) {
                                pp.setExists("0");
                            } else {
                                if (voDynamicAndProductInfoList.getDynamicId().toString().equals(queryParam.getDynamicId())) {
                                    pp.setExists("1");
                                } else {
                                    pp.setExists("0");
                                }
                            }
                        }else {
                            pp.setDynamicName("商品位置：无");
                            pp.setExists("0");
                        }
                    });
                } else {
                    proProductList.stream().forEach(pp -> {
                        pp.setDynamicName("商品位置：无");
                        pp.setExists("0");
                    });
                }
            }
            Boolean ture =!LocalUtils.isEmptyAndNull(queryParam.getConveyId());
            proProductList.stream().forEach(pp -> {
                //商品位置添加的状态去除
                if (LocalUtils.isEmptyAndNull(queryParam.getDynamicId())){
                    pp.setExists(null);
                }
                //寄卖传送设置是否存在状态
               if (ture){
                   if (!LocalUtils.isEmptyAndNull(pp.getId())){
                       pp.setExists("1");
                   }else {
                       pp.setExists("0");
                   }
                   pp.setTotalNum(pp.getConveyNum());
               }

            });
        } catch (Exception e) {
            throw new MyException("商品位置加载错误" + e);
        }
        HashMap<String, Object> hashMap = LocalUtils.getHashMap(proProductList);
        //统计不涉及独立编码
        queryParam.setUniqueCode(null);
        //统计商品总数,商品总成本
        Map<String, String> totalNumMap = proProductService.countLeftNumAndInitPrice(queryParam);

        //v2.6.2(含)版本以后,总价需要自己除以100
        try {
            int version = VersionUtils.compareVersion(appVersion, "2.6.2");
            if (version >= 0) {
                Object totalPrice = totalNumMap.get("totalPrice");
                totalPrice = LocalUtils.calcNumber(totalPrice, "*", 0.01).toString();
                String newPrice = LocalUtils.formatPriceSpilt(totalPrice);
                totalNumMap.put("totalPrice", newPrice);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        //是否有查看仓库资产权限
        String chkStoreTotalPrice = ConstantPermission.CHK_OWN_PRODUCT;

        //统计今日上传,今日上架
        String today = DateUtil.formatShort(new Date());
        queryParam.setTodayDate(today);
        //只统计今天的上传上架数据, 需要把查询时间设置为空,把状态设置为all;才不会因为状态和查询时间而改变查询结果;
        queryParam.setUploadStDateTime(null);
        queryParam.setUploadEtDateTime(null);
        queryParam.setStateCode("all");
        Integer upload = proProductService.countTodayUpload(queryParam);
        Integer release = proProductService.countTodayOnRelease(queryParam);
        hashMap.put("todayUpload", upload);
        hashMap.put("todayOnRelease", release);
        if (!hasPermWithCurrentUser(ConstantPermission.CHK_PRICE_INIT)) {
            totalNumMap.put("totalPrice", "******");
        }
        String uPerm = hasPermToPageWithCurrentUser(chkStoreTotalPrice);

        //兼容安卓版本，没有查看仓库资产权限时，不返回资产的统计；
        if ("0".equals(uPerm)) {
            hashMap.put("todayUpload", "******");
            hashMap.put("todayOnRelease", "******");
            totalNumMap.put("totalNum", "******");
            totalNumMap.put("totalPrice", "******");
        }
        hashMap.putAll(totalNumMap);
        hashMap.put("uPermStoreTotalPrice", uPerm);
        return BaseResult.okResult(hashMap);
    }


    /**
     * 加载【质押】商品
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载【质押】商品",
            notes = "请参数填写固定值: state=store ",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/listProductPawn")
    @RequiresPermissions("pro:check:pawnProduct")
    public BaseResult listProductPawn(@RequestParam Map<String, String> params,
                                      @Valid ParamProductQuery queryParam, BindingResult result) {
        servicesUtil.validControllerParam(result);
        //系统编码不支持模糊查询,独立编码支持模糊查询;修改于2021-09-18 16:30:42
        queryParam.setUniqueCode(queryParam.getProName());
        queryParam.setAttributeCode(EnumProAttribute.PAWN.getCode().toString());
        formatQueryParam(queryParam);
        PageHelper.startPage(getPageNum(), PAGE_SIZE_10);
        List<VoProductLoad> proProductList = proProductService.listProProductByVoProductQueryParam(queryParam);
        String appVersion = getBasicParam().getAppVersion();
        formatVoProductLoad(appVersion, proProductList, queryParam.getSortKey());
        //统计不涉及独立编码
        queryParam.setUniqueCode(null);
        //统计商品总数,商品总成本,为了统计时,不分页,把条数置为null
        //queryParam.setPageSize(null);
        Map<String, String> totalNumMap = proProductService.countLeftNumAndInitPrice(queryParam);
        HashMap<String, Object> hashMap = LocalUtils.getHashMap((proProductList));


        try {
            //v2.6.3(含)版本以后,总价需要自己除以100
            int version = VersionUtils.compareVersion(appVersion, "2.6.3");
            //仅对262的安卓版,另外处理;
            boolean androidBoolean = VersionUtils.compareVersion(appVersion, "2.6.2") == 0
                    && "android".equalsIgnoreCase(getBasicParam().getPlatform());
            if (androidBoolean || version >= 0) {
                Object totalPrice = totalNumMap.get("totalPrice");
                totalPrice = LocalUtils.calcNumber(totalPrice, "*", 0.01).toString();
                String newPrice = LocalUtils.formatPriceSpilt(totalPrice);
                totalNumMap.put("totalPrice", newPrice);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }


        //统计今日上传,今日到期
        String today = DateUtil.formatShort(new Date());
        queryParam.setTodayDate(today);
        //只统计今天的上传和到期的数据, 需要把查询时间设置为空,把状态设置为all;才不会因为状态和查询时间而改变查询结果;
        queryParam.setUploadStDateTime(null);
        queryParam.setUploadEtDateTime(null);
        queryParam.setStateCode("all");
        //今日上传
        Integer upload = proProductService.countTodayUpload(queryParam);
        //今日到期
        Integer todayExpire = proProductService.countTodayExpire(queryParam);
        hashMap.put("todayUpload", upload);
        hashMap.put("todayExpire", todayExpire);
        hashMap.putAll(totalNumMap);
        return BaseResult.okResult(hashMap);
    }


    /**
     * 获得【仓库】商品分类、属性分析
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "获得【仓库】商品分类、属性分析 --2.6.2",
            notes = "商品分类分析;按分类统计;按属性统计;返回名称,数量,总成本--2.6.2",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
    })
    @RequestMapping(value = "/listProductAnalyze")
    @RequiresPermissions("pro:check:ownProduct")
    public BaseResult listProductAnalyze(@RequestParam Map<String, String> params) {
        int shopId = getShopId();
        String attribute = "10,20,40";
        Boolean flag = hasPermWithCurrentUser(ConstantPermission.CHK_PRICE_INIT);
        VoProForAnalysisShow classify = proProductService.countClassifyNumAndPrice(shopId, attribute, flag);
        List<VoProClassifyForAnalysis> attributeList = proProductService.countAttributeNumAndPrice(shopId, flag, classify.getShowTotalPrice());
        classify.setAttributeList(attributeList);
        String totalPrice = classify.getShowTotalPrice();
        if (!LocalUtils.isEmptyAndNull(totalPrice) && !"*****".equals(totalPrice)) {
            String showTotalPrice = DecimalFormatUtil.formatString(new BigDecimal(totalPrice), null);
            classify.setShowTotalPrice(showTotalPrice);
        }

        return BaseResult.okResult(classify);
    }

    /**
     * 获得【仓库】商品分类、属性分析
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "获得【仓库】商品分类、属性横屏二维表分析",
            notes = "获得【仓库】商品分类、属性横屏二维表分析",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
    })
    @RequestMapping(value = "/listProductAnalyzeDetail")
    @RequiresPermissions("pro:check:ownProduct")
    public BaseResult<List<VoProductAnalyzeDetail>> listProductAnalyzeDetail(@RequestParam Map<String, String> params) {
        int shopId = getShopId();
        //String attribute = "10,20,40";
        List<VoProductAnalyzeDetail> countList = proProductService.countProductAnalyzeDetail(shopId);
        //权限控制成本是否可看;
        if (!hasPermWithCurrentUser(ConstantPermission.CHK_PRICE_INIT)) {
            if (!LocalUtils.isEmptyAndNull(countList)) {
                for (VoProductAnalyzeDetail vo : countList) {
                    vo.setCountDataAll(tempFormat(vo.getCountDataAll()) + "***");
                    vo.setCountDataConsignment(tempFormat(vo.getCountDataConsignment()) + "***");
                    vo.setCountDataOther(tempFormat(vo.getCountDataOther()) + "***");
                    vo.setCountDataSelf(tempFormat(vo.getCountDataSelf()) + "***");
                }
            }
        }


        return BaseResult.okResult(countList);
    }

    /**
     * 处理统计数据,成本根据权限来选择是否显示星号
     *
     * @param string
     * @return
     */
    private String tempFormat(String string) {
        if (!LocalUtils.isEmptyAndNull(string)) {
            string = string.substring(0, string.indexOf("￥") + 1);
        }
        return string;
    }


    /**
     * 获得【质押】商品的分类分析;
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "获得【质押】商品的分类分析--2.6.2",
            notes = "获得【质押】商品的分类分析;按分类统计;返回名称,数量,总成本--2.6.2",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
    })
    @RequestMapping(value = "/listProductPawnAnalyze")
    public BaseResult<VoProForAnalysisShow> listProductPawnAnalyze(@RequestParam Map<String, String> params) {
        int shopId = getShopId();
        String attributeCode = "30";
        Boolean flag = hasPermWithCurrentUser(ConstantPermission.CHK_PRICE_INIT);
        VoProForAnalysisShow classify = proProductService.countClassifyNumAndPrice(shopId, attributeCode, flag);
        String totalPrice = classify.getShowTotalPrice();
        if (!LocalUtils.isEmptyAndNull(totalPrice) && !"*****".equals(totalPrice)) {
            String showTotalPrice = DecimalFormatUtil.formatString(new BigDecimal(totalPrice), null);
            classify.setShowTotalPrice(showTotalPrice);
        }

        return BaseResult.okResult(classify);
    }


    /**
     * 根据业务id;上架商品;(支持批量;)
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "根据业务id;上架商品;(支持批量;)",
            notes = "根据业务id;上架商品;支持批量上架;多个bizId,用分号隔开",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestMapping(value = "/releaseProduct")
    @RequiresPermissions("pro:releaseProduct")
    public BaseResult releaseProduct(@RequestParam Map<String, String> params,
                                     @Valid ParamProductBizId paramProduct, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        int userId = getUserId();
        int shopId = getShopId();
        String bizIdParam = paramProduct.getBizId();
        String[] split = bizIdParam.split(";");
        if (LocalUtils.isEmptyAndNull(split)) {
            return BaseResult.defaultErrorWithMsg("请选择需要上架的商品!");
        }
        List<String> list = Arrays.asList(split);
        int rows = proProductService.releaseBatchProProductByShopIdBizId(shopId, userId, list);


        //发送上架消息
        opPushService.pushReleaseProductMsg(shopId, list);

        //添加【店铺操作日志】-【批量上架商品】
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.PROD.getName());
        paramAddShpOperateLog.setOperateName("商品上架");
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(request);

        if (rows == 1) {
            String singleBizId = split[0];
            ProProduct proProduct = proProductService.getProProductByShopIdBizId(shopId, singleBizId);
            if (null != proProduct) {
                paramAddShpOperateLog.setOperateContent(proProduct.getName());
                paramAddShpOperateLog.setProdId(proProduct.getId());
                //添加商品操作记录
                addProModifyRecord(proProduct.getId(), "上架", proProduct.getName(), proProduct.getName(), paramProduct.getPlatform());
            }
        } else {
            paramAddShpOperateLog.setOperateContent("批量操作");
            paramAddShpOperateLog.setProdId(null);
        }
        shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);

        //构造返回值
        StringBuilder sb = new StringBuilder();
        sb.append("上架 ");
        sb.append(split.length);
        sb.append(" 个,共成功: ");
        sb.append(rows);
        sb.append(" 个.");
        return BaseResult.okResult(sb.toString());
    }

    /**
     * 根据业务id;下架商品;(支持批量;)
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "根据业务id;下架商品;(支持批量;)",
            notes = "根据业务id;下架商品;支持批量下架商品;多个bizId,用分号隔开",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestMapping(value = "/backOffProduct")
    @RequiresPermissions("pro:releaseProduct")
    public BaseResult backOffProduct(@RequestParam Map<String, String> params,
                                     @Valid ParamProductBizId paramProduct, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        int userId = getUserId();
        int shopId = getShopId();
        String bizIdParam = paramProduct.getBizId();
        String[] split = bizIdParam.split(";");
        if (LocalUtils.isEmptyAndNull(split)) {
            return BaseResult.defaultErrorWithMsg("请选择需要下架的商品!");
        }
        if (!LocalUtils.isEmptyAndNull(paramProduct.getCheckId())) {
            ProCheck check = checkService.getById(paramProduct.getCheckId());
            if (!"10".equals(check.getCheckState())) {

                return BaseResult.defaultErrorWithMsg("该商品不在盘点中!");
            }
            //盘点进行中不在时间范围内
            if (check.getStartTime() != null && check.getEndTime() != null && check.getEndTime().before(new Date())) {
                return BaseResult.defaultErrorWithMsg("该商品不在盘点时间范围内!");
            }

        }
        int rows = proProductService.backOffBatchProProductByShopIdBizId(shopId, userId, Arrays.asList(split));

        //添加【店铺操作日志】-【批量上架商品】
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.PROD.getName());
        paramAddShpOperateLog.setOperateName("商品下架");
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(request);

        if (rows == 1) {
            String singleBizId = split[0];
            ProProduct proProduct = proProductService.getProProductByShopIdBizId(shopId, singleBizId);
            if (null != proProduct) {
                paramAddShpOperateLog.setOperateContent(proProduct.getName());
                paramAddShpOperateLog.setProdId(proProduct.getId());

                //添加商品操作记录
                addProModifyRecord(proProduct.getId(), "下架", proProduct.getName(), proProduct.getName(), paramProduct.getPlatform());
            }
        } else {
            paramAddShpOperateLog.setOperateContent("批量操作");
            paramAddShpOperateLog.setProdId(null);
        }
        shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);

        //构造返回值
        StringBuilder sb = new StringBuilder();
        sb.append("下架 ");
        sb.append(split.length);
        sb.append(" 个,共成功: ");
        sb.append(rows);
        sb.append(" 个.");
        //删除商品. 把对应的锁单记录, 全部清空掉;2.6.5 下架依旧可以锁单开单 所以去掉此方法
//        for (String bizId : split) {
//            proProductService.clearProLockNumByBizId(shopId, bizId);
//        }
        BasicParam basicParam = getBasicParam();
        try {
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.5") < 0) {
                //删除商品. 把对应的锁单记录, 全部清空掉;;2.6.5 下架依旧可以锁单开单 所以去掉此方法
                for (String bizId : split) {
                    proProductService.clearProLockNumByBizId(shopId, bizId);
                }
            }
        } catch (Exception e) {
            log.info("下架锁单关联错误" + e);
        }
        return BaseResult.okResult(sb.toString());
    }

    /**
     * 一键全店【全部上架】
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "一键全店【全部上架】",
            notes = "一键全店【全部上架】,把所有未上架的商品【全部上架】",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequiresPermissions("pro:releaseProduct")
    @RequestMapping(value = "/oneKeyReleaseProduct")
    public BaseResult oneKeyReleaseProduct(@RequestParam Map<String, String> params,
                                           @Valid ParamToken token, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        int userId = getUserId();
        int shopId = getShopId();
        proProductService.oneKeyReleaseProduct(shopId, userId);

        try {
            //发送上架消息
            opPushService.pushReleaseProductMsg(shopId);

            //添加【店铺操作日志】-【批量上架商品】
            ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
            paramAddShpOperateLog.setShopId(shopId);
            paramAddShpOperateLog.setOperateUserId(userId);
            paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.PROD.getName());
            paramAddShpOperateLog.setOperateName("商品上架");
            paramAddShpOperateLog.setOrderId(null);
            paramAddShpOperateLog.setRequest(request);
            paramAddShpOperateLog.setOperateContent("全店上架");
            paramAddShpOperateLog.setProdId(null);
            shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return BaseResult.okResult();
    }

    /**
     * 一键全店【全部下架】
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "一键全店【全部下架】",
            notes = "一键全店【全部下架】,把所有已上架的商品【全部下架】",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequiresPermissions("pro:releaseProduct")
    @RequestMapping(value = "/oneKeyBackOffProduct")
    public BaseResult oneKeyBackOffProduct(@RequestParam Map<String, String> params,
                                           @Valid ParamToken token, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        int userId = getUserId();
        int shopId = getShopId();

        //把当前已上架的bizId全部找出来;
        List<String> bizIdList = proProductService.listOnSaleProductBizId(shopId);
        proProductService.oneKeyBackOffProduct(shopId, userId);

        try {
            //发送上架消息
            opPushService.pushReleaseProductMsg(shopId);

            //添加【店铺操作日志】-【批量上架商品】
            ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
            paramAddShpOperateLog.setShopId(shopId);
            paramAddShpOperateLog.setOperateUserId(userId);
            paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.PROD.getName());
            paramAddShpOperateLog.setOperateName("商品下架");
            paramAddShpOperateLog.setOrderId(null);
            paramAddShpOperateLog.setRequest(request);
            paramAddShpOperateLog.setOperateContent("全店下架");
            paramAddShpOperateLog.setProdId(null);
            shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }


        //删除商品. 把对应的锁单记录, 全部清空掉;
        for (String bizId : bizIdList) {
            proProductService.clearProLockNumByBizId(shopId, bizId);
        }
        return BaseResult.okResult();
    }


    /**
     * 根据业务id;删除商品;
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "根据业务id;删除商品;(支持批量;)",
            notes = "根据业务id;删除商品;支持批量删除商品;多个bizId,用分号隔开",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestMapping(value = "/deleteProduct")
    @RequiresPermissions("pro:deleteProduct")
    public BaseResult deleteProduct(@RequestParam Map<String, String> params,
                                    @Valid ParamProductBizId paramProduct, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        int userId = getUserId();
        int shopId = getShopId();
        String bizIdParam = paramProduct.getBizId();
        String[] split = bizIdParam.split(";");
        if (LocalUtils.isEmptyAndNull(split)) {
            return BaseResult.defaultErrorWithMsg("请选择需要删除的商品!");
        }
        List<String> list = Arrays.asList(split);

        //刷新薪资-【商品里的回收人员】
        //for (String bizId : split) {
        //    ProProduct proProduct = proProductService.getProProductByShopIdBizId(shopId,bizId);
        //    ordOrderService.refreshSalaryForUser(shopId, proProduct.getRecycleAdmin(), userId,
        //            proProduct.getInsertTime());
        //}
        //执行删除操作
        int proId = proProductService.deleteBatchProProductByShopIdBizId(shopId, userId, list, paramProduct.getDeleteRemark());
        try {
            //判断版本控制是否在2.6.6或以上版本
            BasicParam basicParam = getBasicParam();
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.6") >= 0) {
                list.stream().forEach(s -> {
                    ProProduct proProduct = proProductService.getProProductForDeleteByShopIdBizId(shopId, s);
                    proDynamicProductService.updateStateByProId(proProduct.getId(), EnumDynamicProductState.ALREADY_DELETE.getCode(), shopId);
                });
            }
        } catch (Exception e) {
            throw new MyException("商品修改动态错误" + e);
        }
        //删除商品. 把对应的锁单记录, 全部清空掉;
        for (String bizId : split) {
            proProductService.clearProLockNumByBizId(shopId, bizId);
        }
        //添加商品操作记录
        addProModifyRecord(proId, "删除", "", "", paramProduct.getPlatform());
        //发送商品删除消息
        opPushService.pushDeleteProductMsg(shopId, list);

        //添加【店铺操作日志】-【增加订单类型】
        Integer delProdNum = list.size();
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.PROD.getName());
        paramAddShpOperateLog.setOperateName("删除商品");

        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(request);

        if (delProdNum.equals(1)) {
            ProProduct proProduct = proProductService.getProProductForDeleteByShopIdBizId(shopId, split[0]);
            if (proProduct == null) {
                paramAddShpOperateLog.setOperateContent("暂未获取商品名称");
                paramAddShpOperateLog.setProdId(0);
            } else {
                paramAddShpOperateLog.setOperateContent(proProduct.getName());
                paramAddShpOperateLog.setProdId(proProduct.getId());
            }

        } else {
            paramAddShpOperateLog.setOperateContent("批量操作");
            paramAddShpOperateLog.setProdId(null);
        }

        shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        return BaseResult.okResult(proId);
    }

    /**
     * 根据业务id;把【质押】商品转移到仓库;(支持批量;)
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "根据业务id;把【质押】商品转移到仓库;",
            notes = "根据业务id;把【质押】商品转移到仓库;支持批量操作商品;多个bizId,用分号隔开",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestMapping(value = "/movePrivateProToStore")
    public BaseResult movePrivateProToStore(@RequestParam Map<String, String> params,
                                            @Valid ParamProductBizId paramProduct, BindingResult result) {
        servicesUtil.validControllerParam(result);
        int userId = getUserId();
        int shopId = getShopId();
        String bizIdParam = paramProduct.getBizId();
        String[] split = bizIdParam.split(";");
        int proId = proProductService.movePrivateProToStore(shopId, userId, Arrays.asList(split));
        return BaseResult.okResult(proId);
    }
}
