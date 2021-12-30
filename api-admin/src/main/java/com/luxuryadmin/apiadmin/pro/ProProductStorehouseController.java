package com.luxuryadmin.apiadmin.pro;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.DecimalFormatUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.enums.pro.EnumProAttribute;
import com.luxuryadmin.enums.shp.EnumShpOperateLogModule;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.fin.ParamFundRecordAdd;
import com.luxuryadmin.param.pro.ParamProductBizId;
import com.luxuryadmin.param.pro.ParamProductQuery;
import com.luxuryadmin.param.shp.ParamAddShpOperateLog;
import com.luxuryadmin.service.op.OpPushService;
import com.luxuryadmin.service.ord.OrdOrderService;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.service.shp.ShpOperateLogService;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.vo.pro.VoProClassifyForAnalysis;
import com.luxuryadmin.vo.pro.VoProForAnalysisShow;
import com.luxuryadmin.vo.pro.VoProductAnalyzeDetail;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

/**
 * 商品管理-controller层;
 *
 * @author monkey king
 * @Date 2019/12/01 4:07
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/admin/pro", method = RequestMethod.POST)
@Api(tags = {"C001.【仓库】模块--2.6.2"}, description = "/shop/admin/pro |用户【仓库】模块相关--2.6.2")
public class ProProductStorehouseController extends ProProductBaseController {

    @Autowired
    private ProProductService proProductService;

    @Autowired
    private OpPushService opPushService;

    @Autowired
    private ShpOperateLogService shpOperateLogService;

    @Autowired
    private OrdOrderService ordOrderService;

    /**
     * 加载【全部商品仓库】页面
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载【全部商品仓库】页面",
            notes = "加载在售商品；支持模糊查询；分页；条件筛选",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @PostMapping("/listAllProductStorehouse")
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
    @RequiresPermissions("pro:check:ownProduct")
    public BaseResult listOwnProductStorehouse(@RequestParam Map<String, String> params,
                                               @Valid ParamProductQuery queryParam, BindingResult result) {
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
    @RequiresPermissions("pro:check:entrustProduct")
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
    @RequiresPermissions("pro:check:otherProduct")
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
        PageHelper.startPage(getPageNum(), getPageSize());
        List<VoProductLoad> proProductList = proProductService.listProProductByVoProductQueryParam(queryParam);

        String appVersion = getBasicParam().getAppVersion();
        for (VoProductLoad voProductLoad : proProductList) {
            formatVoProductLoad(appVersion, voProductLoad, queryParam.getSortKey());
            //hidePrice(voProductLoad);
        }
        HashMap<String, Object> hashMap = LocalUtils.getHashMap(proProductList);
        //统计不涉及独立编码
        queryParam.setUniqueCode(null);
        //统计商品总数,商品总成本,为了统计时,不分页,把条数置为null
        //queryParam.setPageSize(null);
        Map<String, String> totalNumMap = proProductService.countLeftNumAndInitPrice(queryParam);
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
            totalNumMap.put("totalPrice", "0");
        }
        hashMap.putAll(totalNumMap);
        //是否有查看仓库资产权限
        String chkStoreTotalPrice = ConstantPermission.CHK_OWN_PRODUCT;
        hashMap.put("uPermStoreTotalPrice", hasPermToPageWithCurrentUser(chkStoreTotalPrice));
        hashMap.put("totalSize", new PageInfo(proProductList).getTotal());
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
        //独立编码不支持模糊查询
        queryParam.setUniqueCode(queryParam.getProName());
        queryParam.setAttributeCode(EnumProAttribute.PAWN.getCode().toString());
        formatQueryParam(queryParam);
        PageHelper.startPage(getPageNum(), PAGE_SIZE_10);
        List<VoProductLoad> proProductList = proProductService.listProProductByVoProductQueryParam(queryParam);
        String appVersion = getBasicParam().getAppVersion();
        formatVoProductLoad(appVersion, proProductList, queryParam.getSortKey());
        //统计不涉及独立编码
        queryParam.setUniqueCode(null);
        //统计商品总数,商品总成本
        //queryParam.setPageSize(null);
        Map<String, String> totalNumMap = proProductService.countLeftNumAndInitPrice(queryParam);
        HashMap<String, Object> hashMap = LocalUtils.getHashMap((proProductList));

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
        hashMap.put("totalSize", new PageInfo(proProductList).getTotal());
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
    public BaseResult<VoProForAnalysisShow> listProductAnalyze(@RequestParam Map<String, String> params) {
        int shopId = getShopId();
        String attribute = "10,20,40";
        Boolean flag =hasPermWithCurrentUser(ConstantPermission.CHK_PRICE_INIT);
        VoProForAnalysisShow classify = proProductService.countClassifyNumAndPrice(shopId, attribute,flag);
        List<VoProClassifyForAnalysis> attributeList = proProductService.countAttributeNumAndPrice(shopId,flag,classify.getShowTotalPrice());
//        HashMap<String, Object> hashMap = LocalUtils.getHashMap("classifyList", classify);
//        hashMap.put("attributeList", attributeList);
        classify.setAttributeList(attributeList);
        String totalPrice = classify.getShowTotalPrice();
        if (!LocalUtils.isEmptyAndNull(totalPrice) &&  !"*****".equals(totalPrice)){
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
        return BaseResult.okResult(countList);
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
        Boolean flag =hasPermWithCurrentUser(ConstantPermission.CHK_PRICE_INIT);
        VoProForAnalysisShow classify = proProductService.countClassifyNumAndPrice(shopId, attributeCode,flag);
        String totalPrice = classify.getShowTotalPrice();
        if (!LocalUtils.isEmptyAndNull(totalPrice) &&  !"*****".equals(totalPrice)){
            String showTotalPrice = DecimalFormatUtil.formatString(new BigDecimal(totalPrice), null);
            classify.setShowTotalPrice(showTotalPrice);
        }

//        HashMap<String, Object> hashMap = LocalUtils.getHashMap("classifyList", classify);
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
                addProModifyRecord(proProduct.getId(), "上架", proProduct.getName(), proProduct.getName(),"pc");
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
                addProModifyRecord(proProduct.getId(), "下架", proProduct.getName(), proProduct.getName(),"pc");
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
        //删除商品. 把对应的锁单记录, 全部清空掉;
        for (String bizId : split) {
            proProductService.clearProLockNumByBizId(shopId, bizId);
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
        ProProduct proProduct = proProductService.getProProductByShopIdBizId(shopId, split[0]);
        //刷新薪资-【商品里的回收人员】
        //for (String bizId : split) {
        //    ProProduct proProduct = proProductService.getProProductByShopIdBizId(shopId,bizId);
        //    ordOrderService.refreshSalaryForUser(shopId, proProduct.getRecycleAdmin(), userId,
        //            proProduct.getInsertTime());
        //}
        //执行删除操作
        int proId = proProductService.deleteBatchProProductByShopIdBizId(shopId, userId, list,null);
        //删除商品. 把对应的锁单记录, 全部清空掉;
        for (String bizId : split) {
            proProductService.clearProLockNumByBizId(shopId, bizId);
        }
        //添加商品操作记录
        addProModifyRecord(proId, "删除", "", "","pc");
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
        if (delProdNum.equals(1) && !LocalUtils.isEmptyAndNull(proProduct)) {
            paramAddShpOperateLog.setOperateContent(proProduct.getName());
            paramAddShpOperateLog.setProdId(proProduct.getId());
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
