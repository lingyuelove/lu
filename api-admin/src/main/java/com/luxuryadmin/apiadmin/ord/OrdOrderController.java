package com.luxuryadmin.apiadmin.ord;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.entity.ord.OrdAddress;
import com.luxuryadmin.entity.ord.OrdOrder;
import com.luxuryadmin.entity.ord.OrdType;
import com.luxuryadmin.entity.pro.ProDetail;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.enums.fin.EnumFinShopRecordInoutType;
import com.luxuryadmin.enums.fin.EnumFinShopRecordType;
import com.luxuryadmin.enums.pro.EnumDynamicProductState;
import com.luxuryadmin.enums.pro.EnumProAttribute;
import com.luxuryadmin.enums.pro.EnumProState;
import com.luxuryadmin.enums.shp.EnumShpOperateLogModule;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.fin.ParamFinShopRecordAdd;
import com.luxuryadmin.param.fin.ParamFundRecordAdd;
import com.luxuryadmin.param.ord.ParamOrdReceipt;
import com.luxuryadmin.param.ord.ParamOrderDelete;
import com.luxuryadmin.param.ord.ParamOrderQuery;
import com.luxuryadmin.param.ord.ParamOrderUpload;
import com.luxuryadmin.param.pro.ParamProductUpload;
import com.luxuryadmin.param.pro.ParamProductUploadQuick;
import com.luxuryadmin.param.shp.ParamAddShpOperateLog;
import com.luxuryadmin.service.fin.FinShopRecordService;
import com.luxuryadmin.service.op.OpPushService;
import com.luxuryadmin.service.ord.OrdOrderService;
import com.luxuryadmin.service.ord.OrdReceiptService;
import com.luxuryadmin.service.ord.OrdTypeService;
import com.luxuryadmin.service.pro.*;
import com.luxuryadmin.service.shp.ShpAfterSaleGuaranteeService;
import com.luxuryadmin.service.shp.ShpOperateLogService;
import com.luxuryadmin.service.shp.ShpUserShopRefService;
import com.luxuryadmin.service.util.OrdOrderBaseController;
import com.luxuryadmin.vo.data.VoSaleRankHomePageData;
import com.luxuryadmin.vo.ord.VoOrdReceipt;
import com.luxuryadmin.vo.ord.VoOrderLoad;
import com.luxuryadmin.vo.ord.VoOrderModifyRecord;
import com.luxuryadmin.vo.pro.VoProAttribute;
import com.luxuryadmin.vo.pro.VoProClassify;
import com.luxuryadmin.vo.pro.VoProSaleChannel;
import com.luxuryadmin.vo.shp.VoEmployee;
import com.luxuryadmin.vo.shp.VoShpAfterSaleGuarantee;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author monkey king
 * @Date 2019/12/01 4:07
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/admin/ord", method = RequestMethod.POST)
@Api(tags = {"C002.【订单】模块"}, description = "/shop/user/ord |用户【订单】模块相关")
public class OrdOrderController extends OrdOrderBaseController {

    @Autowired
    private ProClassifyService proClassifyService;

    @Autowired
    private ProAttributeService proAttributeService;

    @Autowired
    private OrdOrderService ordOrderService;

    @Autowired
    private OrdTypeService ordTypeService;

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    @Autowired
    protected ProProductService proProductService;

    @Autowired
    protected ProSaleChannelService proSaleChannelService;

    @Autowired
    private ShpAfterSaleGuaranteeService shpAfterSaleGuaranteeService;

    @Autowired
    private OpPushService opPushService;

    @Autowired
    private FinShopRecordService finShopRecordService;

    @Autowired
    private ShpOperateLogService shpOperateLogService;

    @Autowired
    private ProLockRecordService proLockRecordService;

    @Autowired
    private OrdReceiptService ordReceiptService;

    @Autowired
    private ProDeliverService proDeliverService;

    @Autowired
    private ProDynamicProductService proDynamicProductService;

    /**
     * 初始化销售分析查询条件
     *
     * @param paramToken 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "初始化销售分析查询条件;",
            notes = "初始化销售分析查询条件",
            httpMethod = "POST")
    @PostMapping("/initSaleAnalyse")
    public BaseResult initSaleAnalyse(@Valid ParamToken paramToken, BindingResult result) {
        servicesUtil.validControllerParam(result);
        return BaseResult.okResult(initSaleAnalyseQuery(getShopId()));
    }

    /**
     * 初始化销售分析的查询条件
     *
     * @param shopId
     * @return
     */
    private HashMap<String, Object> initSaleAnalyseQuery(int shopId) {
        List<OrdType> ordTypeList = ordTypeService.listOrdTypeByShopId(shopId);
        List<VoEmployee> voEmployeeList = shpUserShopRefService.getSalesmanByShopId(shopId);
        List<VoProClassify> voProClassifyList = proClassifyService.listProClassifyByState(shopId, "1");
        List<VoProAttribute> voProAttributeList = proAttributeService.listProAttributeByShopId(shopId);
        List<VoProAttribute> voProAttributeListNew = new ArrayList<>();
        //去掉质押商品
        for (VoProAttribute voProAttribute : voProAttributeList) {
            if (!(EnumProAttribute.PAWN.getCode() + "").equals(voProAttribute.getCode())) {
                voProAttributeListNew.add(voProAttribute);
            }
        }
        HashMap<String, Object> hashMap = LocalUtils.getHashMap("ordTypeList", ordTypeList);
        //商品属性
        hashMap.put("attributeList", voProAttributeListNew);
        //商品分类
        hashMap.put("classifyList", voProClassifyList);
        List<VoEmployee> salesmanOrderList;
        //是否有查看全部订单的权限
        boolean hasPerm = hasPermWithCurrentUser(ConstantPermission.CHK_ALL_ORDER);
        String name = shpUserShopRefService.getNameFromShop(getShopId(), getUserId());
        if (hasPerm) {
            salesmanOrderList = new ArrayList<>(voEmployeeList);
        } else {
            //只能查看个人订单
            salesmanOrderList = new ArrayList<>();
            VoEmployee ve = new VoEmployee();
            ve.setUserId(getUserId());

            ve.setNickname(name);
            salesmanOrderList.add(ve);
        }
        //销售订单筛选人员
        hashMap.put("salesmanOrderList", salesmanOrderList);
        return hashMap;
    }


    /**
     * 加载【订单管理】列表;
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载【订单管理】列表;",
            notes = "加载【订单管理】列表;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/listOrder")
    @RequiresPermissions("ord:listOrder")
    public BaseResult listOrder(@RequestParam Map<String, String> params,
                                @Valid ParamOrderQuery orderQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        //独立编码不支持模糊查询
        orderQuery.setUniqueCode(orderQuery.getProName());
        //是否有查看全部订单的权限
        boolean hasPerm = hasPermWithCurrentUser(ConstantPermission.CHK_ALL_ORDER);
        if (!hasPerm) {
            //没有查看全部订单的权限,只能查看个人订单
            orderQuery.setSaleUserId(getUserId() + "");
        }
        formatQueryParam(orderQuery);
        PageHelper.startPage(getPageNum(), getPageSize());
        List<VoOrderLoad> voOrderLoadList = ordOrderService.listOrderByCondition(orderQuery);
        formatVoOrderLoad(voOrderLoadList);
        //统计开单数据, 只针对已开单;
        orderQuery.setState("20");

        //是否有查看订单数据汇总的权限
        VoSaleRankHomePageData saleData = new VoSaleRankHomePageData();
        saleData.setTotalSaleCount("******");
        saleData.setTotalInitPrice("******");
        saleData.setTotalGrossProfit("******");
        saleData.setGrossProfitRate("******");
        saleData.setTotalSaleAmount(null);
        if (hasPerm) {
            saleData = ordOrderService.getOrderNumAndPrice(orderQuery);
            try {
                DecimalFormat df = new DecimalFormat(",##0.##");
                //销售数量
                BigDecimal totalSaleCount = new BigDecimal(saleData.getTotalSaleCount());
                //销售总额(元)
                String totalSaleAmount = saleData.getTotalSaleAmount();
                //成本总额(元)
                String totalInitPrice = saleData.getTotalInitPrice();
                //毛利润
                BigDecimal totalGrossProfit = LocalUtils.calcNumber(totalSaleAmount, "-", totalInitPrice);
                BigDecimal grossProfitRate = new BigDecimal(0);
                if (totalGrossProfit.longValue() != 0) {
                    //毛利率
                    grossProfitRate = LocalUtils.calcNumber(totalGrossProfit.multiply(new BigDecimal(100)), "/", totalSaleAmount);
                }

                saleData.setTotalSaleCount(df.format(totalSaleCount.stripTrailingZeros()));
                saleData.setTotalInitPrice(df.format(new BigDecimal(totalInitPrice).stripTrailingZeros()));
                saleData.setTotalGrossProfit(df.format(totalGrossProfit.stripTrailingZeros()));
                saleData.setGrossProfitRate(df.format(grossProfitRate.stripTrailingZeros()) + "%");
                saleData.setTotalSaleAmount(null);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }


        HashMap<String, Object> hashMap = LocalUtils.getHashMap(voOrderLoadList);
        //HashMap<String, Object> hashMap = new HashMap<>(16);
        hashMap.putAll(LocalUtils.convertBeanToMap(saleData));
        PageInfo pageInfo = new PageInfo(voOrderLoadList);
        pageInfo.setList(null);
        hashMap.putAll(LocalUtils.convertBeanToMap(pageInfo));

        //删除订单
        String deleteOrderPerm = ConstantPermission.MOD_DELETE_ORDER;
        String hasDeleteOrderPerm = hasPermToPageWithCurrentUser(deleteOrderPerm);
        hashMap.put("uPermDeleteOrder", hasDeleteOrderPerm);

        return BaseResult.okResult(hashMap);
    }

    /**
     * 初始化开单页面;-订单类型;-销售人员列表
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "初始化开单页面;",
            notes = "初始化开单页面;-订单类型;-销售人员列表;<br/>" +
                    "vid需要在商品开单时,传上来",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
    })
    @RequestRequire
    @PostMapping("/initOrderParam")
    public BaseResult initOrderParam(@RequestParam Map<String, String> params) {
        int shopId = getShopId();
        List<OrdType> ordTypeList = ordTypeService.listOrdTypeByShopId(shopId);
        List<VoEmployee> voEmployeeList = shpUserShopRefService.getSalesmanByShopId(shopId);
        List<VoProSaleChannel> saleChannelList = proSaleChannelService.listProSaleChannel(shopId);
        List<VoProClassify> voProClassifyList = proClassifyService.listProClassifyByState(shopId, "1");
        List<VoProAttribute> voProAttributeList = proAttributeService.listProAttributeByShopId(shopId);
        List<VoEmployee> listRecycleUser = proProductService.listRecycleUser(shopId);
        List<VoProAttribute> voProAttributeListNew = new ArrayList<>();
        //去掉质押商品
        for (VoProAttribute voProAttribute : voProAttributeList) {
            if (!(EnumProAttribute.PAWN.getCode() + "").equals(voProAttribute.getCode())) {
                voProAttributeListNew.add(voProAttribute);
            }
        }
        String vid = LocalUtils.getUUID();
        HashMap<String, Object> hashMap = LocalUtils.getHashMap("ordTypeList", ordTypeList);
        hashMap.put("salesmanList", voEmployeeList);
        //回收人员列表
        hashMap.put("listRecycleUser", listRecycleUser);
        //商品属性
        hashMap.put("attributeList", voProAttributeListNew);
        //商品分类
        hashMap.put("classifyList", voProClassifyList);
        List<VoEmployee> salesmanOrderList;
        //是否有查看全部订单的权限
        boolean hasPerm = hasPermWithCurrentUser(ConstantPermission.CHK_ALL_ORDER);
        String name = shpUserShopRefService.getNameFromShop(getShopId(), getUserId());
        if (hasPerm) {
            salesmanOrderList = new ArrayList<>(voEmployeeList);
        } else {
            //只能查看个人订单
            salesmanOrderList = new ArrayList<>();
            VoEmployee ve = new VoEmployee();
            ve.setUserId(getUserId());

            ve.setNickname(name);
            salesmanOrderList.add(ve);
        }
        hashMap.put("salesmanOrderList", salesmanOrderList);
        hashMap.put("saleChannelList", saleChannelList);
        hashMap.put("vid", vid);
        hashMap.put("currentName", name);
        hashMap.put("currentId", getUserId());

        List<VoShpAfterSaleGuarantee> afterSaleGuaranteeList = shpAfterSaleGuaranteeService.listAfterSaleGuarantee(shopId);
        hashMap.put("afterSaleGuaranteeList", afterSaleGuaranteeList);
        String key = ConstantRedisKey.getInitIssueOrderKey(shopId, getUserId());
        redisUtil.setExMINUTES(key, vid, 60);
        return BaseResult.okResult(hashMap);
    }


    /**
     * 获取订单详情
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "获取订单详情;",
            notes = "获取订单详情",
            httpMethod = "GET")
    @ApiImplicitParams({

            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "orderBizId", value = "订单流水号"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @GetMapping("/getOrderDetail")
    public BaseResult getOrderDetail(@RequestParam Map<String, String> params) {
        String orderBizId = params.get("orderBizId");
        VoOrderLoad orderLoad = ordOrderService.getOrderDetailByNumber(getShopId(), orderBizId);
        if (LocalUtils.isEmptyAndNull(orderLoad)) {
            throw new MyException("订单不存在");
        }
        formatVoOrderLoad(orderLoad);
        //销售数量,销售总额
        // Map<String, Object> numAndPrice = ordOrderService.getOrderNumAndPrice(orderQuery);
        //numAndPrice.put("objList", voOrderLoadList);
        return LocalUtils.getBaseResult(orderLoad);
    }


    /**
     * 加载【订单修改记录】列表;
     *
     * @return Result
     */
    @ApiOperation(
            value = "加载【订单修改记录】列表;",
            notes = "加载【订单修改记录】列表;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "orderNo", value = "订单编号")
    })
    @RequestRequire
    @RequestMapping("/listOrderModifyRecordByOrderNo")
    public BaseResult<List<VoOrderModifyRecord>> listOrderModifyRecordByOrderNo(@RequestParam String orderNo) {
        PageHelper.startPage(getPageNum(), PAGE_SIZE_50);
        Integer shopId = getShopId();
        //是否有查看成交价的权限
        boolean hasSeeSalePricePerm = hasPermWithCurrentUser(ConstantPermission.CHK_PRICE_FINISH);
        List<VoOrderModifyRecord> voOrderModifyRecordList = ordOrderService.listOrderModifyRecordById(shopId, orderNo, hasSeeSalePricePerm);

        return BaseResult.okResult(voOrderModifyRecordList);
    }

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
    @GetMapping("/getOrderReceipt")
    public BaseResult getOrderReceipt(@Valid ParamOrdReceipt params, BindingResult bindingResult) {
        servicesUtil.validControllerParam(bindingResult);
        String orderBizId = params.getOrderBizId();
        VoOrdReceipt ordReceipt = ordReceiptService.getOrdReceiptByOrderNumber(getShopId(), orderBizId);
        return LocalUtils.getBaseResult(ordReceipt);
    }

    /**
     * 确认开单;
     *
     * @param orderUpload 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "确认开单;--2.6.5❤",
            notes = "确认开单;vid从initOrderParam接口获取--2.6.5❤",
            httpMethod = "POST")
    @RequestRequire
    @PostMapping("/confirmOrder")
    @RequiresPermissions("ord:confirmOrd")
    public BaseResult confirmOrder(@Valid ParamOrderUpload orderUpload, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        int shopId = getShopId();
        String bizId = orderUpload.getBizId();
        String vid = orderUpload.getVid();
        String key = ConstantRedisKey.getInitIssueOrderKey(shopId, getUserId());
        //锁单记录表id;v262(含)版本以后才有此字段,因此可以用判空来做版本控制
        String lockId = orderUpload.getLockId();
        //判断成交价格是否为空版本大于2.6.3
        if (LocalUtils.isEmptyAndNull(orderUpload.getFinalPrice())) {
            throw new MyException("成交价格不能为空");
        }
//        //2020-09-02 sanjin145 成交价格：改成选填,不填则为0
//        if (null == orderUpload.getFinalPrice()) {
//            orderUpload.setFinalPrice("0");
//        }
        //2020-10-22 sanjin145 销售日期不能大于当前日期
        Date saleTime = orderUpload.getSaleTime();
        Date now = new Date();
        if (now.before(saleTime)) {
            throw new MyException("销售时间不可大于当前时间");
        }
        servicesUtil.validVid(key, vid);
        ProProduct pro = proProductService.getProProductByShopIdBizId(shopId, bizId);

        if ("20".equals(pro.getFkProAttributeCode()) && (LocalUtils.isEmptyAndNull(orderUpload.getEntrustState()))) {
            throw new MyException("寄卖商品结款状态不能为空");
        }
        String totalNumStr = orderUpload.getTotalNum();
        int openTotalNum = Integer.parseInt(totalNumStr);
        //锁单商品开单
        if (!LocalUtils.isEmptyAndNull(lockId)) {
            lockProConfirmV262(request, lockId, pro);
        }
        try {
            //判断版本控制是否在2.6.6或以上版本
            BasicParam basicParam = getBasicParam();
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.6") >= 0) {
                if (pro.getTotalNum() - openTotalNum <= 0) {
                    proDynamicProductService.updateStateByProId(pro.getId(), EnumDynamicProductState.SOLD_OUT.getCode(), shopId);
                }
            }
        } catch (Exception e) {
            throw new MyException("商品修改动态错误" + e);
        }
        //ProProduct pro = proProductService.getProProductByShopIdBizId(shopId, bizId);
        OrdOrder order = pickOrdOrder(orderUpload, pro);
        OrdAddress address = pickOrdAddress(orderUpload);
        String orderNumber = ordOrderService.confirmOrder(pro, order, address);
        redisUtil.delete(key);

        //添加开单商品记录
        addProModifyRecord(pro.getId(), "开单", "", "开单数量为: " + openTotalNum);


        //后台线程刷新薪资
        //ordOrderService.refreshSalaryByOrderId(order.getId());

        //减去缓存的可用库存数量
        proProductService.subtractProRedisLeftNum(shopId, bizId, order.getTotalNum());
        //开单,删除new标识的redis缓存
        String newRedisKey = ConstantRedisKey.getNotDownloadProductKey(getShopId(), bizId);
        redisUtil.delete(newRedisKey);

        //添加账单流水-【商品售出】
        ParamFinShopRecordAdd paramFinShopRecordAdd = new ParamFinShopRecordAdd();
        paramFinShopRecordAdd.setInoutType(EnumFinShopRecordInoutType.IN.getCode());
        paramFinShopRecordAdd.setChangeAmount(order.getFinishPrice().divide(new BigDecimal(100.00)).toString());
        paramFinShopRecordAdd.setType("商品售出");
        paramFinShopRecordAdd.setHappenTime(DateUtil.format(new Date()));
        String note = "【" + pro.getName() + "】售出成交价";
        paramFinShopRecordAdd.setNote(note);
        String imgUrl = pro.getSmallImg();
        finShopRecordService.addFinShopRecord(shopId, getUserId(), paramFinShopRecordAdd, EnumFinShopRecordType.SYSTEM, imgUrl, order.getNumber());
        //推送开单消息
        opPushService.pushOrderMsg(shopId, order, pro);

        //添加【店铺操作日志】-【商品开单】
        if (null != pro) {
            ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
            paramAddShpOperateLog.setShopId(shopId);
            paramAddShpOperateLog.setOperateUserId(getUserId());
            paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.ORDER.getName());
            paramAddShpOperateLog.setOperateName("商品开单");
            paramAddShpOperateLog.setOperateContent(pro.getName());
            paramAddShpOperateLog.setProdId(pro.getId());
            paramAddShpOperateLog.setOrderId(order.getId());
            paramAddShpOperateLog.setRequest(request);

            shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        }
        //添加到发货列表
        OrdOrder ordOrder = ordOrderService.getOrderInfoByNumber(orderNumber);
        if (LocalUtils.isEmptyAndNull(lockId)) {
            ordOrderService.savProDeliver(ordOrder);
        } else {
            proDeliverService.updateSourceByLockId(Integer.parseInt(lockId), ordOrder);
        }
        return LocalUtils.getBaseResult(orderNumber);
    }

    /**
     * 快速开单
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "快速开单--2.6.5❤;",
            notes = "快速开单;vid从initOrderParam接口获取--2.6.5❤",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @PostMapping("/confirmQuickOrder")
    @RequiresPermissions("ord:confirmOrd")
    public BaseResult confirmQuickOrder(@RequestParam Map<String, String> params,
                                        @Valid ParamOrderUpload orderUpload,
                                        @Valid ParamProductUploadQuick paramProductUploadQuick,
                                        BindingResult result,
                                        HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        int shopId = getShopId();
        int userId = getUserId();
        String vid = orderUpload.getVid();
        String key = ConstantRedisKey.getInitIssueOrderKey(shopId, getUserId());
        servicesUtil.validVid(key, vid);

        //2020-10-22 sanjin145 销售日期不能大于当前日期
        Date saleTime = orderUpload.getSaleTime();
        Date now = new Date();
        if (now.before(saleTime)) {
            throw new MyException("销售时间不可大于当前时间");
        }

        //新增快速开单商品信息
        ParamProductUpload paramProductUpload = new ParamProductUpload();
        BeanUtils.copyProperties(paramProductUploadQuick, paramProductUpload);
        paramProductUpload.setAttribute(paramProductUploadQuick.getAttribute());
        paramProductUpload.setState(EnumProState.RELEASE_20.getCode() + "");
        paramProductUpload.setTotalNum(orderUpload.getTotalNum());
        paramProductUpload.setVid(orderUpload.getVid());
        ProProduct pro = proProductService.pickProProduct(paramProductUpload, true, shopId, userId);
        //增加2.4.2的需求判断;
        //判断是否为寄卖商品 判断结款状态是否为空
        if ("20".equals(pro.getFkProAttributeCode()) && (LocalUtils.isEmptyAndNull(orderUpload.getEntrustState()))) {
            throw new MyException("寄卖商品结款状态不能为空");
        }
        paramProductUpload.setBizId(pro.getBizId());
        ProDetail proDetail = proProductService.pickProDetail(paramProductUpload, true);
        pro.setSaleNum(0);
        proProductService.saveProProductReturnId(pro, proDetail, null);

        //新增快速开单订单信息
        orderUpload.setBizId(pro.getBizId());
        OrdOrder order = pickOrdOrder(orderUpload, pro);
        OrdAddress address = pickOrdAddress(orderUpload);
        order.setOpenType("quick");
        String orderNumber = ordOrderService.confirmOrder(pro, order, address);
        redisUtil.delete(key);
        //是否有锁单数量,目前只针对孤品;
        Integer leftNum = proProductService.getProRedisNum(shopId, pro.getBizId()).getLeftNum();
        if (leftNum > 0) {
            JSONObject lockJson;
            int lockNum = 0;
            String lockRedisKey = ConstantRedisKey.getLockProductByBizId(shopId, pro.getBizId());
            String lockRedisValue = redisUtil.get(lockRedisKey);
            if (!LocalUtils.isEmptyAndNull(lockRedisValue)) {
                lockJson = JSONObject.parseObject(lockRedisValue);
                leftNum = Integer.parseInt(lockJson.get(ConstantRedisKey.LEFT_NUM).toString());
                lockNum = Integer.parseInt(lockJson.get(ConstantRedisKey.LOCK_NUM).toString());
                if (leftNum >= 1) {
                    leftNum -= leftNum;
                }
                lockJson = new JSONObject();
                lockJson.put(ConstantRedisKey.LEFT_NUM, leftNum);
                lockJson.put(ConstantRedisKey.LOCK_NUM, lockNum);
                redisUtil.setEx(lockRedisKey, lockJson.toString(), 10);
            }
        }
        //后台线程刷新薪资
        // ordOrderService.refreshSalaryByOrderId(order.getId());

        //开单,删除new标识的redis缓存
        String newRedisKey = ConstantRedisKey.getNotDownloadProductKey(getShopId(), pro.getBizId());
        redisUtil.delete(newRedisKey);

        //添加账单流水-【商品售出】
        ParamFinShopRecordAdd paramFinShopRecordAdd = new ParamFinShopRecordAdd();
        paramFinShopRecordAdd.setInoutType(EnumFinShopRecordInoutType.IN.getCode());
        paramFinShopRecordAdd.setChangeAmount(order.getFinishPrice().divide(new BigDecimal(100.00)).toString());
        paramFinShopRecordAdd.setType("商品售出");
        paramFinShopRecordAdd.setHappenTime(DateUtil.format(new Date()));
        String note = "【" + pro.getName() + "】售出成交价";
        paramFinShopRecordAdd.setNote(note);
        String imgUrl = pro.getSmallImg();
        finShopRecordService.addFinShopRecord(shopId, getUserId(), paramFinShopRecordAdd, EnumFinShopRecordType.SYSTEM, imgUrl, order.getNumber());


//
//        //推送开单消息
        //opPushService.pushOrderMsg(shopId, order, pro);

        //添加【店铺操作日志】-【商品开单(快速开单)】
        if (null != pro) {
            ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
            paramAddShpOperateLog.setShopId(shopId);
            paramAddShpOperateLog.setOperateUserId(getUserId());
            paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.ORDER.getName());
            paramAddShpOperateLog.setOperateName("商品开单");
            paramAddShpOperateLog.setOperateContent(pro.getName());
            paramAddShpOperateLog.setProdId(pro.getId());
            paramAddShpOperateLog.setOrderId(order.getId());
            paramAddShpOperateLog.setRequest(request);

            shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        }
        //添加到发货列表
        OrdOrder ordOrder = ordOrderService.getOrderInfoByNumber(orderNumber);
        ordOrderService.savProDeliver(ordOrder);
        return LocalUtils.getBaseResult(orderNumber);
    }


    /**
     * 删除订单
     *
     * @param delete 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "删除订单;❤",
            notes = "删除订单",
            httpMethod = "POST")

    @PostMapping("/deleteOrder")
    @RequiresPermissions("ord:deleteOrder")
    public BaseResult deleteOrder(@Valid ParamOrderDelete delete, HttpServletRequest request, BindingResult result) {
//        String orderBizId = params.get("orderBizId");
        servicesUtil.validControllerParam(result);
        delete.setShopId(getShopId());
        delete.setUserId(getUserId());
        ordOrderService.deleteOrderForFalse(delete, request);
        return BaseResult.okResult();
    }
}
