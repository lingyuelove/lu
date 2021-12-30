package com.luxuryadmin.api.ord;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.entity.ord.OrdAddress;
import com.luxuryadmin.entity.ord.OrdOrder;
import com.luxuryadmin.entity.ord.OrdType;
import com.luxuryadmin.entity.pro.*;
import com.luxuryadmin.enums.fin.EnumFinShopRecordInoutType;
import com.luxuryadmin.enums.fin.EnumFinShopRecordType;
import com.luxuryadmin.enums.pro.EnumDynamicProductState;
import com.luxuryadmin.enums.pro.EnumProAttribute;
import com.luxuryadmin.enums.pro.EnumProState;
import com.luxuryadmin.enums.shp.EnumShpOperateLogModule;
import com.luxuryadmin.param.fin.ParamFinShopRecordAdd;
import com.luxuryadmin.param.fin.ParamFundRecordAdd;
import com.luxuryadmin.param.ord.*;
import com.luxuryadmin.param.pro.ParamProductUpload;
import com.luxuryadmin.param.pro.ParamProductUploadQuick;
import com.luxuryadmin.param.shp.ParamAddShpOperateLog;
import com.luxuryadmin.service.fin.FinFundRecordService;
import com.luxuryadmin.service.fin.FinShopRecordService;
import com.luxuryadmin.service.op.OpPushService;
import com.luxuryadmin.service.ord.OrdOrderService;
import com.luxuryadmin.service.ord.OrdTypeService;
import com.luxuryadmin.service.pro.*;
import com.luxuryadmin.service.shp.ShpAfterSaleGuaranteeService;
import com.luxuryadmin.service.shp.ShpOperateLogService;
import com.luxuryadmin.service.shp.ShpUserShopRefService;
import com.luxuryadmin.service.util.OrdOrderBaseController;
import com.luxuryadmin.vo.data.VoSaleRankHomePageData;
import com.luxuryadmin.vo.ord.VoOrderForTempPage;
import com.luxuryadmin.vo.ord.VoOrderLoad;
import com.luxuryadmin.vo.ord.VoOrderModifyRecord;
import com.luxuryadmin.vo.pro.*;
import com.luxuryadmin.vo.shp.VoEmployee;
import com.luxuryadmin.vo.shp.VoShpAfterSaleGuarantee;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
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
@RequestMapping(value = "/shop/user/ord", method = RequestMethod.POST)
@Api(tags = {"C002.【订单】模块 --2.5.2--zs"}, description = "/shop/user/ord |用户【订单】模块相关2.5.2")
public class OrdOrderController extends OrdOrderBaseController {

    @Autowired
    private ProClassifyService proClassifyService;

    @Autowired
    private ProAttributeService proAttributeService;

    @Autowired
    private OrdOrderService ordOrderService;

    @Autowired
    private ProDeliverService proDeliverService;

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
    private FinFundRecordService fundRecordService;

    @Autowired
    private ProTempProductService tempProductService;

    @Autowired
    private ProModifyRecordService proModifyRecordService;

    @Autowired
    private ProDynamicProductService proDynamicProductService;
    @Autowired
    private ProConveyProductService proConveyProductService;
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
    @RequiresPermissions(value = {"ord:listOrder", "ord:listAllOrder"}, logical = Logical.OR)
    public BaseResult listOrder(@RequestParam Map<String, String> params,
                                @Valid ParamOrderQuery orderQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        //系统编码不支持模糊查询,独立编码支持模糊查询;修改于2021-09-18 16:30:42
        orderQuery.setUniqueCode(orderQuery.getProName());
        //是否有查看全部订单的权限
        boolean hasPerm = hasPermWithCurrentUser(ConstantPermission.CHK_ALL_ORDER);
        if (!hasPerm) {
            //没有查看全部订单的权限,只能查看个人订单
            orderQuery.setSaleUserId(getUserId() + "");
        }
        formatQueryParam(orderQuery);
        PageHelper.startPage(getPageNum(), PAGE_SIZE_10);
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
            if (LocalUtils.isEmptyAndNull(orderQuery.getSaleStDateTime())
                    && LocalUtils.isEmptyAndNull(orderQuery.getSaleEtDateTime())) {
                //默认为今天
                Calendar calendar = Calendar.getInstance();
                String today = DateUtil.formatShort(calendar.getTime());
                orderQuery.setSaleStDateTime(today + " 00:00:00");
                orderQuery.setSaleEtDateTime(today + " 23:59:59");
            }
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
                if (Double.parseDouble(totalSaleAmount) != 0) {
                    //毛利率
                    grossProfitRate = LocalUtils.calcNumber(totalGrossProfit.multiply(new BigDecimal(100)), "/", totalSaleAmount);
                }
                saleData.setTotalSaleCount(df.format(totalSaleCount.stripTrailingZeros()));
                //saleData.setTotalInitPrice(df.format(new BigDecimal(totalInitPrice).stripTrailingZeros()));
                //该字段改成放销售额2021-12-16 20:32:14
                saleData.setTotalInitPrice(df.format(new BigDecimal(totalSaleAmount).stripTrailingZeros()));
                saleData.setTotalSaleAmount(totalSaleAmount);
                boolean hasPriceInit = hasPermWithCurrentUser(ConstantPermission.CHK_PRICE_INIT);
                if (hasPriceInit) {
                    saleData.setTotalGrossProfit(df.format(totalGrossProfit.stripTrailingZeros()));
                    saleData.setGrossProfitRate(df.format(grossProfitRate.stripTrailingZeros()) + "%");
                }else{
                    saleData.setTotalGrossProfit("******");
                    saleData.setGrossProfitRate("******");
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }


        HashMap<String, Object> hashMap = LocalUtils.getHashMap(voOrderLoadList);
        hashMap.putAll(LocalUtils.convertBeanToMap(saleData));
        //订单数据汇总权限
        String orderDataTotalPerm = ConstantPermission.CHK_ALL_ORDER;
        String hasOrderDataTotalPerm = hasPermToPageWithCurrentUser(orderDataTotalPerm);
        hashMap.put("uPermOrderDataTotal", hasOrderDataTotalPerm);
        //查看销售分析权限
        String saleAllPerm = ConstantPermission.CHK_SALE_ALL;
        String hasSaleAllPerm = hasPermToPageWithCurrentUser(saleAllPerm);
        hashMap.put("uPermSaleAll", hasSaleAllPerm);
        hashMap.put("text1", "销量");
        hashMap.put("text2", "销售额");
        hashMap.put("text3", "毛利润");
        hashMap.put("text4", "毛利率");

        String uPermOrdUpdate = ConstantPermission.ORD_UPDATE_ORDER;
        hashMap.put("uPermOrdUpdate", hasPermToPageWithCurrentUser(uPermOrdUpdate));
        return BaseResult.okResult(hashMap);
    }

    /**
     * 加载【订单管理】列表;
     *
     * @param ordForTempSearch 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "临时仓 --开单记录--2.5.2--zs;",
            notes = "临时仓 --开单记录--2.5.2--zs;",
            httpMethod = "POST")

    @RequestRequire
    @RequestMapping("/getOrderForTemp")
    public BaseResult<VoOrderForTempPage> getOrderForTemp(@Valid ParamOrdForTempSearch ordForTempSearch, BindingResult result) {
        servicesUtil.validControllerParam(result);

        ordForTempSearch.setShopId(getShopId());
        VoOrderForTempPage voOrderLoadList = ordOrderService.getOrderForTemp(ordForTempSearch);

        return BaseResult.okResult(voOrderLoadList);
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
        int userId = getUserId();
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
        //商品属性
        hashMap.put("attributeList", voProAttributeListNew);
        //商品分类
        hashMap.put("classifyList", voProClassifyList);
        List<VoEmployee> salesmanOrderList;
        //是否有查看全部订单的权限
        boolean hasPerm = hasPermWithCurrentUser(ConstantPermission.CHK_ALL_ORDER);
        String name = shpUserShopRefService.getNameFromShop(getShopId(), userId);
        if (hasPerm) {
            salesmanOrderList = ordOrderService.listSaleUserByShopId(shopId);
        } else {
            //只能查看个人订单
            salesmanOrderList = new ArrayList<>();
            VoEmployee ve = new VoEmployee();
            ve.setUserId(userId);
            ve.setNickname(name);
            salesmanOrderList.add(ve);
        }
        hashMap.put("salesmanOrderList", salesmanOrderList);
        hashMap.put("saleChannelList", saleChannelList);
        hashMap.put("vid", vid);
        hashMap.put("currentName", name);
        hashMap.put("currentId", userId);
        hashMap.put("listRecycleUser", listRecycleUser);
        //开单人员默认当前登录用户;开单类型不需要记住上一次选择;修改于2021-09-16 22:22:29
        hashMap.put("saleNickname", name);
        hashMap.put("saleUserId", userId);
        ////获取最近的开单记录
        //VoOrder lastOrderInfo = ordOrderService.getLastOrderInfo(shopId, getUserId());
        ////自己上一次并没有开单;
        //if (lastOrderInfo == null || lastOrderInfo.getFkShpUserId() == null) {
        //    //查找该店铺最近一次的开单记录
        //    lastOrderInfo = ordOrderService.getLastOrderInfo(shopId, null);
        //}
        //if (lastOrderInfo != null) {
        //    hashMap.put("saleNickname", lastOrderInfo.getSaleNickname());
        //    hashMap.put("saleUserId", lastOrderInfo.getFkShpUserId());
        //    for (OrdType ordType : ordTypeList) {
        //        if (ordType.getName().equals(lastOrderInfo.getType())) {
        //            hashMap.put("type", lastOrderInfo.getType());
        //            break;
        //        }
        //    }
        //}
        List<VoShpAfterSaleGuarantee> afterSaleGuaranteeList = shpAfterSaleGuaranteeService.listAfterSaleGuarantee(shopId);
        hashMap.put("afterSaleGuaranteeList", afterSaleGuaranteeList);
        String key = ConstantRedisKey.getInitIssueOrderKey(shopId, userId);
        redisUtil.setExMINUTES(key, vid, 60);
        return BaseResult.okResult(hashMap);
    }


    /**
     * 确认开单;
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "确认开单2.5.1;",
            notes = "确认开单;vid从initOrderParam接口获取",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @PostMapping("/confirmOrder")
    @RequiresPermissions("ord:confirmOrd")
    public BaseResult confirmOrder(@RequestParam Map<String, String> params,
                                   @Valid ParamOrderUpload orderUpload, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        int shopId = getShopId();
        String bizId = orderUpload.getBizId();
        String vid = orderUpload.getVid();
        String key = ConstantRedisKey.getInitIssueOrderKey(shopId, getUserId());
        //锁单记录表id;v262(含)版本以后才有此字段,因此可以用判空来做版本控制
        String lockId = orderUpload.getLockId();
        //判断成交价格是否为空2.6.3
        uploadFinalPrice(orderUpload);
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

        BasicParam basicParam = getBasicParam();
        String appVersion = basicParam.getAppVersion();

        String totalNumStr = orderUpload.getTotalNum();
        int openTotalNum = Integer.parseInt(totalNumStr);

        try {
            //2.4.2(含)增加了寄卖结款的状态; 2.6.6之后编辑订单不传结款状态
            int compareVersion = VersionUtils.compareVersion(appVersion, "2.6.6");
            if (compareVersion < 0) {
                //增加2.4.2的需求判断;
                //判断是否为寄卖商品 判断结款状态是否为空
                if ("20".equals(pro.getFkProAttributeCode()) && (LocalUtils.isEmptyAndNull(orderUpload.getEntrustState()))) {
                    throw new MyException("寄卖商品结款状态不能为空");
                }
            } else {
                if ("20".equals(pro.getFkProAttributeCode()) && (LocalUtils.isEmptyAndNull(orderUpload.getEntrustState()))) {
                    orderUpload.setEntrustState("0");
                }
                if (pro.getTotalNum() - openTotalNum <= 0) {
                    proDynamicProductService.updateStateByProId(pro.getId(), EnumDynamicProductState.SOLD_OUT.getCode(), shopId);
                }
            }

        } catch (Exception e) {
            throw new MyException("寄卖商品的状态异常" + e);
        }


        //锁单商品开单
        if (LocalUtils.isEmptyAndNull(lockId)) {
            try {
                int compareVersion = VersionUtils.compareVersion(appVersion, "2.6.2");
                if (compareVersion < 0) {
                    //262版本之前没有预付定金,所以默认尾款就是总额;
                    orderUpload.setPreMoney("0");
                    orderUpload.setLastMoney(orderUpload.getFinalPrice());
                    lockProConfirm(request, shopId, bizId, pro, openTotalNum);
                }
            } catch (Exception e) {
                throw new MyException("开单版本错误: " + e);
            }
        } else {
            lockProConfirmV262(request, lockId, pro);
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
        //新增订单添加账单记录
        ParamFundRecordAdd paramFundRecordAdd = new ParamFundRecordAdd();
        paramFundRecordAdd.setFkShpShopId(shopId);
        paramFundRecordAdd.setUserId(getUserId());
        paramFundRecordAdd.setMoney(orderUpload.getFinalPrice());
        //获取订单的总数量
        BigDecimal totalNum = new BigDecimal(orderUpload.getTotalNum());
        //获取订单的总成本
        BigDecimal allInitPrice = pro.getInitPrice().multiply(totalNum);
        paramFundRecordAdd.setInitPrice(allInitPrice.toString());
        paramFundRecordAdd.setState(paramFinShopRecordAdd.getInoutType());
        paramFundRecordAdd.setFundType("20");
        paramFundRecordAdd.setCount("1");
        paramFundRecordAdd.setFinClassifyName("新增订单");
        paramFundRecordAdd.setAttributeCode(pro.getFkProAttributeCode());
        fundRecordService.addOrderFundRecord(paramFundRecordAdd, order.getId().toString());

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
        //寄卖传送添加判断逻辑
        orderUpload.setShopId(getShopId());
        proConveyProductService.addOrderCheck(ordOrder,orderUpload,request);
        return LocalUtils.getBaseResult(orderNumber);
    }

    /**
     * 针对开单时,需要判断是否解锁锁单商品来进行开单
     *
     * @param request      HttpServletRequest
     * @param shopId
     * @param bizId
     * @param pro          从db里查询出来的ProProduct
     * @param openTotalNum 需要开单的数量
     */
    private void lockProConfirm(HttpServletRequest request, int shopId, String bizId, ProProduct pro, int openTotalNum) {
        VoProRedisNum proRedisNum = proProductService.getProRedisNum(shopId, bizId);
        if (openTotalNum > proRedisNum.getTotalNum()) {
            throw new MyException("总库存不足,开单失败;总库存为:" + proRedisNum.getTotalNum());
        }

        //解锁全部商品权限
        boolean unlockAllProduct = hasPermWithCurrentUser(ConstantPermission.MOD_UNLOCK_PRODUCT);
        //拥有锁单权限则开单时,优先解锁锁单商品;
        boolean uPermLock = hasPermWithCurrentUser(ConstantPermission.MOD_LOCK_PRODUCT);

        //是否有锁单数量,更改为支持批量开单;修改于2021-06-23 18:24:43
        if ((uPermLock || unlockAllProduct) && proRedisNum.getLockNum() > 0) {
            //锁单记录;判断是否为自己锁单的商品
            List<VoProLockRecord> lockList = proLockRecordService.listVoProLockRecordByBizId(getShopId(), bizId);
            if (!LocalUtils.isEmptyAndNull(lockList)) {
                //可最大解锁商品数量;(包含他人解锁)
                int maxLockNum = 0;
                //自己解锁数量
                int ownLockNum = 0;
                int ownLockId = 0;
                for (VoProLockRecord lockPro : lockList) {
                    int lockNum = lockPro.getLockNum();
                    //锁单人是自己,又或者有解锁全部的权限
                    if (lockPro.getLockUserId() == getUserId()) {
                        maxLockNum += lockNum;
                        ownLockNum += lockNum;
                        ownLockId = lockPro.getLockId();
                    }
                    if (unlockAllProduct) {
                        maxLockNum += lockNum;
                    }
                }
                //自己没有锁单的情况下,可用库存足够可以开单, 则直接开单, 不需要扣除他人的锁单数量;
                if (!(openTotalNum <= proRedisNum.getLeftNum() && ownLockId == 0)) {
                    int needLockNum = 0;
                    //还需要补的解锁数量;
                    int defaultNum = 0;
                    boolean enoughNum = true;
                    //如果没有解锁全部商品的权限;则开单数量只能是自己的(锁单商品的数量+可用库存);
                    if (!unlockAllProduct) {
                        int leftLockNum = (ownLockNum + proRedisNum.getLeftNum());
                        if (openTotalNum > leftLockNum) {
                            throw new MyException("x001;可开单库存不足,请解锁;可开单库存为:" + leftLockNum);
                        }
                        needLockNum = Math.min(openTotalNum, ownLockNum);
                    } else {
                        //如果有解锁全部的权限;分两种情况;一种是自己的(锁单商品的数量+可用库存)足够;第二种是不够; 优先解锁自己;
                        int leftLockNum = (ownLockNum + proRedisNum.getLeftNum());
                        //自己的够用;
                        defaultNum = openTotalNum - leftLockNum;
                        if (defaultNum <= 0) {
                            needLockNum = Math.min(openTotalNum, ownLockNum);
                        } else {
                            enoughNum = false;
                        }
                    }
                    if (enoughNum) {
                        int unLockNum1 = proRedisNum.getLockNum();
                        ProLockRecord lockRecord = proLockRecordService.getProLockRecordById(ownLockId);
                        proLockRecordService.unlockProductByUserId(lockRecord, getUserId(), needLockNum, request);
                        ProLockRecord lockRecord2 = proLockRecordService.getProLockRecordById(ownLockId);
                        int unLockNum2 = lockRecord2.getLockNum();
                        //添加商品记录
                        addProModifyRecord(pro.getId(), "解锁", "解锁前数量: " + unLockNum1, "解锁后数量: " + unLockNum2);
                    } else {
                        //自己的(锁单商品的数量+可用库存)不足以开单;需要解锁他人商品;
                        boolean isOk = false;
                        for (VoProLockRecord lockPro : lockList) {
                            if (lockPro.getLockUserId() == getUserId()) {
                                int unLockNum1 = proRedisNum.getLockNum();
                                ProLockRecord lockRecord = proLockRecordService.getProLockRecordById(lockPro.getLockId());
                                proLockRecordService.unlockProductByUserId(lockRecord, getUserId(), lockPro.getLockNum(), request);
                                ProLockRecord lockRecord2 = proLockRecordService.getProLockRecordById(ownLockId);
                                int unLockNum2 = lockRecord2.getLockNum();
                                isOk = true;
                                //添加商品记录
                                addProModifyRecord(pro.getId(), "解锁", "解锁前数量: " + unLockNum1, "解锁后数量: " + unLockNum2);
                            } else {
                                if (defaultNum > 0) {
                                    int unLockNum1 = proRedisNum.getLockNum();
                                    int needLockNum2 = Math.min(defaultNum, lockPro.getLockNum());
                                    ProLockRecord lockRecord = proLockRecordService.getProLockRecordById(lockPro.getLockId());
                                    proLockRecordService.unlockProductByUserId(lockRecord, getUserId(), needLockNum2, request);
                                    ProLockRecord lockRecord2 = proLockRecordService.getProLockRecordById(ownLockId);
                                    int unLockNum2 = lockRecord2.getLockNum();
                                    //添加商品记录
                                    addProModifyRecord(pro.getId(), "解锁", "解锁前数量: " + unLockNum1, "解锁后数量: " + unLockNum2);
                                    defaultNum -= lockPro.getLockNum();
                                }
                                if (defaultNum <= 0 && isOk) {
                                    break;
                                }
                            }
                        }
                    }

                }
            }
        } else {
            //针对没有锁单权限的用户,开单时,只能选择开单可用库存;不包括锁单库存;修改于2021-06-23 18:52:15
            if (openTotalNum > proRedisNum.getLeftNum()) {
                throw new MyException("x002;可开单库存不足,请解锁;可开单库存为:" + proRedisNum.getLeftNum());
            }
        }
    }


    /**
     * 快速开单
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "快速开单2.5.1;",
            notes = "快速开单;vid从initOrderParam接口获取",
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
        BasicParam basicParam = getBasicParam();
        String appVersion = basicParam.getAppVersion();
        try {
            //2.4.2(含)增加了寄卖结款的状态; 2.6.6之后编辑订单不传结款状态
            int compareVersion = VersionUtils.compareVersion(appVersion, "2.6.6");
            if (compareVersion < 0) {
                //增加2.4.2的需求判断;
                //判断是否为寄卖商品 判断结款状态是否为空
                if ("20".equals(pro.getFkProAttributeCode()) && (LocalUtils.isEmptyAndNull(orderUpload.getEntrustState()))) {
                    throw new MyException("寄卖商品结款状态不能为空");
                }
            } else {
                if ("20".equals(pro.getFkProAttributeCode()) && (LocalUtils.isEmptyAndNull(orderUpload.getEntrustState()))) {
                    orderUpload.setEntrustState("0");
                }
            }

        } catch (Exception e) {

            throw new MyException("寄卖商品的状态异常" + e);
        }
        paramProductUpload.setBizId(pro.getBizId());
        ProDetail proDetail = proProductService.pickProDetail(paramProductUpload, true);
        pro.setSaleNum(0);
        proProductService.saveProProductReturnId(pro, proDetail, null);
        //快速开单新增入库记录
        BigDecimal initPrice = pro.getInitPrice();
        if (initPrice == null) {
            initPrice = new BigDecimal(0);

        }

        ParamFundRecordAdd paramFundRecordAddPro = new ParamFundRecordAdd();
        paramFundRecordAddPro.setFkShpShopId(shopId);
        paramFundRecordAddPro.setUserId(getUserId());
        paramFundRecordAddPro.setMoney(initPrice.toString());
        paramFundRecordAddPro.setInitPrice("0");
        paramFundRecordAddPro.setState(EnumFinShopRecordInoutType.OUT.getCode());
        paramFundRecordAddPro.setFundType("10");
        paramFundRecordAddPro.setCount("1");
        paramFundRecordAddPro.setFinClassifyName("入库记录");
        paramFundRecordAddPro.setAttributeCode(pro.getFkProAttributeCode());
        fundRecordService.addProductFundRecord(paramFundRecordAddPro);

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
        //新增订单添加账单记录
        ParamFundRecordAdd paramFundRecordAdd = new ParamFundRecordAdd();
        paramFundRecordAdd.setFkShpShopId(shopId);
        paramFundRecordAdd.setUserId(getUserId());
        paramFundRecordAdd.setMoney(orderUpload.getFinalPrice());
        if (paramProductUploadQuick.getInitPrice() == null) {
            paramFundRecordAdd.setInitPrice("0");
        } else {
            paramFundRecordAdd.setInitPrice(paramProductUploadQuick.getInitPrice());
        }

        paramFundRecordAdd.setState(paramFinShopRecordAdd.getInoutType());
        paramFundRecordAdd.setFundType("20");
        paramFundRecordAdd.setCount("1");
        paramFundRecordAdd.setFinClassifyName("新增订单");
        paramFundRecordAdd.setAttributeCode(pro.getFkProAttributeCode());
        fundRecordService.addOrderFundRecord(paramFundRecordAdd, order.getId().toString());

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
        //寄卖传送添加判断逻辑
        orderUpload.setShopId(shopId);
        proConveyProductService.addOrderCheck(ordOrder,orderUpload,request);
        return LocalUtils.getBaseResult(orderNumber);
    }


    /**
     * 临时仓开单;
     *
     * @param orderUpload 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "临时仓开单2.5.2;",
            notes = "临时仓开单2.5.2;vid从initOrderParam接口获取",
            httpMethod = "POST")
    @ApiImplicitParams({

            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "tempId", value = "[tempId]参数非法"),
    })
    @RequestRequire
    @PostMapping("/confirmTempOrder")
    @RequiresPermissions("ord:confirmOrd")
    public BaseResult confirmTempOrder(@Valid ParamOrderUpload orderUpload, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        int shopId = getShopId();
        String bizId = orderUpload.getBizId();
        String vid = orderUpload.getVid();
        String key = ConstantRedisKey.getInitIssueOrderKey(shopId, getUserId());
        //锁单记录表id;v262(含)版本以后才有此字段,因此可以用判空来做版本控制
        String lockId = orderUpload.getLockId();
        //判断成交价格是否为空2.6.3
        uploadFinalPrice(orderUpload);
        //2020-10-22 sanjin145 销售日期不能大于当前日期
        Date saleTime = orderUpload.getSaleTime();
        Date now = new Date();
        if (now.before(saleTime)) {
            throw new MyException("销售时间不可大于当前时间");
        }
        servicesUtil.validVid(key, vid);
        ProProduct pro = proProductService.getProProductByShopIdBizId(shopId, bizId);

        BasicParam basicParam = getBasicParam();
        String appVersion = basicParam.getAppVersion();
        try {
            //2.4.2(含)增加了寄卖结款的状态; 2.6.6之后编辑订单不传结款状态
            int compareVersion = VersionUtils.compareVersion(appVersion, "2.6.6");
            if (compareVersion < 0) {
                //增加2.4.2的需求判断;
                //判断是否为寄卖商品 判断结款状态是否为空
                if ("20".equals(pro.getFkProAttributeCode()) && (LocalUtils.isEmptyAndNull(orderUpload.getEntrustState()))) {
                    throw new MyException("寄卖商品结款状态不能为空");
                }
            } else {
                if ("20".equals(pro.getFkProAttributeCode()) && (LocalUtils.isEmptyAndNull(orderUpload.getEntrustState()))) {
                    orderUpload.setEntrustState("0");
                }
            }

        } catch (Exception e) {
            throw new MyException("寄卖商品的状态异常" + e);
        }

        if (LocalUtils.isEmptyAndNull(orderUpload.getTempId())) {
            throw new MyException("临时仓id不为空");
        }

        String totalNumStr = orderUpload.getTotalNum();
        int openTotalNum = Integer.parseInt(totalNumStr);

        ProTempProduct tempProduct = tempProductService.getProTempProduct(shopId, Integer.parseInt(orderUpload.getTempId()), pro.getId());
        if (tempProduct == null) {
            throw new MyException("暂无此商品临时仓");
        }
        Integer num = tempProduct.getNum();
        if (num == null || openTotalNum > num) {
            throw new MyException("临时仓商品库存不足");
        }

        //锁单商品开单
        if (LocalUtils.isEmptyAndNull(lockId)) {
            try {
                //262版本之前没有预付定金,所以默认尾款就是总额;
                int compareVersion = VersionUtils.compareVersion(appVersion, "2.6.2");
                if (compareVersion < 0) {
                    //262版本之前没有预付定金,所以默认尾款就是总额;
                    orderUpload.setPreMoney("0");
                    orderUpload.setLastMoney(orderUpload.getFinalPrice());
                    lockProConfirm(request, shopId, bizId, pro, openTotalNum);
                }
            } catch (Exception e) {
                throw new MyException("开单版本错误: " + e);
            }
        } else {
            lockProConfirmV262(request, lockId, pro);
        }

        try {
            //判断版本控制是否在2.6.6或以上版本
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.6") >= 0) {
                if (pro.getTotalNum() - openTotalNum <= 0) {
                    proDynamicProductService.updateStateByProId(pro.getId(), EnumDynamicProductState.SOLD_OUT.getCode(), shopId);
                }
            }
        } catch (Exception e) {
            throw new MyException("商品修改动态错误" + e);
        }


        OrdOrder order = pickOrdOrder(orderUpload, pro);
        OrdAddress address = pickOrdAddress(orderUpload);
        order.setOpenType("temp");

        order.setFkProTempId(Integer.parseInt(orderUpload.getTempId()));
        String orderNumber = ordOrderService.confirmOrder(pro, order, address);
        redisUtil.delete(key);

        //减去临时仓的数量
        tempProduct.setNum(tempProduct.getNum() - openTotalNum);
        tempProductService.updateTempProductCount(tempProduct);


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
        //新增订单添加账单记录
        ParamFundRecordAdd paramFundRecordAdd = new ParamFundRecordAdd();
        paramFundRecordAdd.setFkShpShopId(shopId);
        paramFundRecordAdd.setUserId(getUserId());
        paramFundRecordAdd.setMoney(orderUpload.getFinalPrice());
        //获取订单的总数量
        BigDecimal totalNum = new BigDecimal(orderUpload.getTotalNum());
        //获取订单的总成本
        BigDecimal allInitPrice = pro.getInitPrice().multiply(totalNum);
        paramFundRecordAdd.setInitPrice(allInitPrice.toString());
        paramFundRecordAdd.setState(paramFinShopRecordAdd.getInoutType());
        paramFundRecordAdd.setFundType("20");
        paramFundRecordAdd.setCount("1");
        paramFundRecordAdd.setFinClassifyName("新增订单");
        paramFundRecordAdd.setAttributeCode(pro.getFkProAttributeCode());
        fundRecordService.addOrderFundRecord(paramFundRecordAdd, order.getId().toString());

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
        if (!LocalUtils.isEmptyAndNull(lockId)) {
            proDeliverService.updateSourceByLockId(Integer.parseInt(lockId), ordOrder);
        } else {
            ordOrderService.savProDeliver(ordOrder);
        }
        //寄卖传送添加判断逻辑
        orderUpload.setShopId(shopId);
        proConveyProductService.addOrderCheck(ordOrder,orderUpload,request);
        return LocalUtils.getBaseResult(orderNumber);
    }

    /**
     * 判断成交价格是否为空
     *
     * @param orderUpload
     * @return
     */
    public ParamOrderUpload uploadFinalPrice(ParamOrderUpload orderUpload) {
        BasicParam basicParam = getBasicParam();
        String appVersion = basicParam.getAppVersion();
        try {
            //2.6.3(含)开单价格需手动录入 ，开单时成交价格默认为空，不默认为0;
            int compareVersion = VersionUtils.compareVersion(appVersion, "2.6.3");
            if (compareVersion >= 0) {
                //增加2.6.3的需求判断 开单时，成交价格，必填项;
                if (LocalUtils.isEmptyAndNull(orderUpload.getFinalPrice())) {
                    throw new MyException("成交价格不能为空");
                }
            } else {
                //2020-09-02 sanjin145 成交价格：改成选填,不填则为0
                if (null == orderUpload.getFinalPrice()) {
                    orderUpload.setFinalPrice("0");
                }
            }

        } catch (Exception e) {
            throw new MyException("成交价格判断状态异常" + e);
        }
        return orderUpload;
    }

    /**
     * 取消开单，退货退款;
     *
     * @return Result
     */
    @ApiOperation(
            value = "取消开单，退货退款;",
            notes = "取消开单，退货退款;",
            httpMethod = "POST")
    @RequestRequire
    @PostMapping("/cancelOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
    })
    @RequiresPermissions("ord:updateOrder")
    public BaseResult cancelOrder(ParamOrderCancel paramOrderCancel, HttpServletRequest request) {
        String orderBizId = paramOrderCancel.getOrderBizId();
        if (null == orderBizId) {
            return BaseResult.defaultErrorWithMsg("取消订单失败,业务ID为空");
        }
        String cancelReason = paramOrderCancel.getCancelReason();
        int shopId = getShopId();
        Integer cancelPerson = getUserId();
        OrdOrder ordOrder = ordOrderService.getOrdOrderDetail(shopId, orderBizId);
        if (LocalUtils.isEmptyAndNull(ordOrder)) {
            throw new MyException("订单不存在!");
        }
        ProProduct pro = proProductService.getProProductById(ordOrder.getFkProProductId());
        BasicParam basicParam = getBasicParam();
        ordOrderService.cancelOrder(shopId, ordOrder, cancelReason, cancelPerson, basicParam);
        //后台线程刷新薪资
        //ordOrderService.refreshSalaryByOrderId(ordOrder.getId());
        //添加账单流水-【退货退款】
        ParamFinShopRecordAdd paramFinShopRecordAdd = new ParamFinShopRecordAdd();
        paramFinShopRecordAdd.setInoutType(EnumFinShopRecordInoutType.OUT.getCode());
        paramFinShopRecordAdd.setChangeAmount(ordOrder.getFinishPrice().divide(new BigDecimal(100.00)).toString());
        paramFinShopRecordAdd.setType("退货退款");
        paramFinShopRecordAdd.setHappenTime(DateUtil.format(new Date()));
        String note = "【" + pro.getName() + "】退货退款金额";
        paramFinShopRecordAdd.setNote(note);
        String imgUrl = pro.getSmallImg();
        finShopRecordService.addFinShopRecord(shopId, getUserId(), paramFinShopRecordAdd, EnumFinShopRecordType.SYSTEM, imgUrl, ordOrder.getNumber());
        //取消订单添加账单记录
        ParamFundRecordAdd paramFundRecordAdd = new ParamFundRecordAdd();
        paramFundRecordAdd.setFkShpShopId(shopId);
        paramFundRecordAdd.setUserId(getUserId());
        paramFundRecordAdd.setMoney(ordOrder.getFinishPrice().toString());
        //获取订单的总数量
        BigDecimal totalNum = new BigDecimal(ordOrder.getTotalNum());
        //获取订单的总成本

        try {
            Object obj = LocalUtils.isEmptyAndNull(pro.getInitPrice()) ? 0 : pro.getInitPrice().toString();
            String allInitPrice = LocalUtils.calcNumber(obj, "*", totalNum).toString();
            paramFundRecordAdd.setInitPrice(allInitPrice);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        paramFundRecordAdd.setState(paramFinShopRecordAdd.getInoutType());
        paramFundRecordAdd.setFundType("20");
        paramFundRecordAdd.setCount("1");
        paramFundRecordAdd.setFinClassifyName("取消订单");
        paramFundRecordAdd.setAttributeCode(pro.getFkProAttributeCode());
        fundRecordService.addOrderFundRecord(paramFundRecordAdd, ordOrder.getId().toString());

        try {
            //判断版本控制是否在2.6.6或以上版本
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.6") >= 0) {
                ProDynamicProduct proDynamicProduct = proDynamicProductService.getDynamicProductInfoByProId(pro.getId(), shopId);
                if (proDynamicProduct != null && proDynamicProduct.getState().equals(EnumDynamicProductState.SOLD_OUT.getCode())) {
                    proDynamicProductService.updateStateByProId(proDynamicProduct.getFkProProductId(), EnumDynamicProductState.NORMAL.getCode(), shopId);
                }
            }
        } catch (Exception e) {
            throw new MyException("商品修改动态错误" + e);
        }


        //添加商品操作记录
        addProModifyRecord(pro.getId(), "退货", pro.getName(), pro.getName());

        //推送开单消息
        opPushService.pushCancelOrderMsg(shopId, ordOrder, pro);
        //删除发货信息
        proDeliverService.deleteDeliverInfoByOrderInfo(ordOrder.getId());
        //退单时关联锁单跟着解锁
        proConveyProductService.cancelOrderForConvey(ordOrder,pro,request);
        return BaseResult.okResult();
    }

    /**
     * 修改订单信息;
     *
     * @return Result
     */
    @ApiOperation(
            value = "修改订单信息2.5.5;",
            notes = "修改订单信息",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @PostMapping("/updateOrder")
    @RequiresPermissions("ord:updateOrder")
    public BaseResult updateOrder(@Valid ParamOrderUpload orderUpload, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        String orderBizId = orderUpload.getBizId();
        System.out.println("orderBizId=" + orderBizId);
        if (null == orderBizId) {
            return BaseResult.defaultErrorWithMsg("bizId参数为空");
        }

        //2020-10-22 sanjin145 销售日期不能大于当前日期
        Date saleTime = orderUpload.getSaleTime();
        Date now = new Date();
        if (now.before(saleTime)) {
            throw new MyException("销售时间不可大于当前时间");
        }

        int shopId = getShopId();
        int userId = getUserId();
        VoOrderLoad voOrderLoad = ordOrderService.getOrderDetailByNumber(shopId, orderBizId);
        if (LocalUtils.isEmptyAndNull(voOrderLoad)) {
            throw new MyException("订单不存在");
        }
        //比较新老【交易价】大小，0表示相等，1表示修改后的价格大，-1表示修改后的价格小
        BigDecimal finishPriceOld = new BigDecimal(voOrderLoad.getSalePrice());
        BigDecimal finishPriceNew = new BigDecimal(orderUpload.getFinalPrice());
        BigDecimal changePrice = finishPriceNew.subtract(finishPriceOld);
        int compareResult = finishPriceNew.compareTo(finishPriceOld);
        OrdOrder ordOrderUpdate = new OrdOrder();
        ordOrderUpdate.setId(voOrderLoad.getOrderId());
        ordOrderUpdate.setUpdateAdmin(userId);
        ordOrderUpdate.setUpdateTime(new Date());
        //BeanUtils.copyProperties(orderUpload,ordOrderUpdate);
        convertOrderUpload2OrdOrder(orderUpload, ordOrderUpdate);
        //用户ID
        ordOrderUpdate.setFkShpUserId(Integer.parseInt(orderUpload.getUserId()));
        ordOrderUpdate.setFkShpShopId(shopId);
        ordOrderUpdate.setEntrustState(orderUpload.getEntrustState());
        if (orderUpload.getEntrustState() != null && "0".equals(orderUpload.getEntrustState())) {
            ordOrderUpdate.setEntrustImg("");
            ordOrderUpdate.setEntrustRemark("");
        } else {
            ordOrderUpdate.setEntrustImg(orderUpload.getEntrustImg());
            ordOrderUpdate.setEntrustRemark(orderUpload.getEntrustRemark());
        }

        Integer updateResult = ordOrderService.updateOrder(ordOrderUpdate, orderUpload.getReceiveAddress(), voOrderLoad);
        ProProduct pro = proProductService.getProProductByShopIdBizId(shopId, voOrderLoad.getBizId());
        try {
            //订单添加回收人员错误判断版本控制是否在2.5.5或以上版本
            BasicParam basicParam = getBasicParam();
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.5.5") >= 0) {
                if (!LocalUtils.isEmptyAndNull(orderUpload.getRecycleAdmin())) {
                    pro.setRecycleAdmin(orderUpload.getRecycleAdmin());
                    proProductService.updateProProduct(pro);
                }
            }
        } catch (Exception e) {
            throw new MyException("订单添加回收人员错误" + e);
        }
        //后台线程刷新薪资
        //ordOrderService.refreshSalaryByOrderId(ordOrderUpdate.getId());
        //添加账单流水-【价差调整】-【修改订单】
        //等于0表示价格没有修改，不生成记账流水
        if (compareResult != 0) {
            ParamFinShopRecordAdd paramFinShopRecordAdd = new ParamFinShopRecordAdd();
            //修改后的价格大，说明收入增加了
            if (compareResult == 1) {
                paramFinShopRecordAdd.setInoutType(EnumFinShopRecordInoutType.IN.getCode());
            }
            //修改后的价格小
            else if (compareResult == -1) {
                paramFinShopRecordAdd.setInoutType(EnumFinShopRecordInoutType.OUT.getCode());
            }

            paramFinShopRecordAdd.setChangeAmount(changePrice.divide(new BigDecimal(100.00)).toString());
            paramFinShopRecordAdd.setType("价差调整");
            paramFinShopRecordAdd.setHappenTime(DateUtil.format(new Date()));
            String note = "【" + pro.getName() + "】订单成交价修改";
            paramFinShopRecordAdd.setNote(note);
            String imgUrl = pro.getSmallImg();
            finShopRecordService.addFinShopRecord(shopId, getUserId(), paramFinShopRecordAdd, EnumFinShopRecordType.SYSTEM, imgUrl, ordOrderUpdate.getNumber());
            //修改订单添加账单记录
            String initPrice = LocalUtils.isEmptyAndNull(pro.getInitPrice()) ? "0" : pro.getInitPrice() + "";
            ParamFundRecordAdd paramFundRecordAdd = new ParamFundRecordAdd();
            paramFundRecordAdd.setFkShpShopId(shopId);
            paramFundRecordAdd.setUserId(getUserId());
            paramFundRecordAdd.setMoney(changePrice.toString());
            paramFundRecordAdd.setInitPrice("0");
            paramFundRecordAdd.setState(paramFinShopRecordAdd.getInoutType());
            paramFundRecordAdd.setFundType("20");
            paramFundRecordAdd.setCount("1");
            paramFundRecordAdd.setFinClassifyName("修改订单");
            paramFundRecordAdd.setAttributeCode(pro.getFkProAttributeCode());
            fundRecordService.addOrderFundRecord(paramFundRecordAdd, ordOrderUpdate.getId().toString());

        }

        //推送开单消息
        opPushService.pushUpdateOrderMsg(shopId, ordOrderUpdate, pro, getUserId());

        //添加【店铺操作日志】-【修改订单】
        if (null != pro) {
            ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
            paramAddShpOperateLog.setShopId(shopId);
            paramAddShpOperateLog.setOperateUserId(getUserId());
            paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.ORDER.getName());
            paramAddShpOperateLog.setOperateName("修改订单");
            paramAddShpOperateLog.setOperateContent(pro.getName());
            paramAddShpOperateLog.setProdId(pro.getId());
            paramAddShpOperateLog.setOrderId(voOrderLoad.getOrderId());
            paramAddShpOperateLog.setRequest(request);

            shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        }

        return LocalUtils.getBaseResult(updateResult);
    }

    /**
     * 修改结款状态 添加备注;
     *
     * @return Result
     */
    @ApiOperation(
            value = "修改订单改结款状态2.5.1;",
            notes = "修改订单改结款状态",
            httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestRequire
    @PostMapping("/updateOrderEntrust")
    @RequiresPermissions("ord:updateOrder")
    public BaseResult updateOrderEntrust(@Valid ParamOrderUploadEntrust orderUpload, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        orderUpload.setUserId(getUserId());
        String orderBizId = orderUpload.getOrderBizId();
        if (null == orderBizId) {
            return BaseResult.defaultErrorWithMsg("bizId参数为空");
        }
        Integer shopId = getShopId();
        VoOrderLoad voOrderLoad = ordOrderService.getOrderDetailByNumber(shopId, orderUpload.getOrderBizId());
        if (LocalUtils.isEmptyAndNull(voOrderLoad)) {
            throw new MyException("订单不存在");
        }

        if (!"20".equals(voOrderLoad.getState())) {
            throw new MyException("订单暂未售出");
        }
        orderUpload.setOrderBizId(voOrderLoad.getOrderId().toString());
        Integer updateResult = ordOrderService.updateOrderEntrust(orderUpload);

        return LocalUtils.getBaseResult(updateResult);
    }


    private void convertOrderUpload2OrdOrder(ParamOrderUpload orderUpload, OrdOrder ordOrderUpdate) {
        //独立编码
        ordOrderUpdate.setUniqueCode(orderUpload.getUniqueCode());
        //订单类型
        ordOrderUpdate.setType(orderUpload.getOrderType());
        //成交价格
        ordOrderUpdate.setFinishPrice(new BigDecimal(orderUpload.getFinalPrice()));
        //销售人员
        Integer saleUserId = Integer.parseInt(orderUpload.getUserId());
        String nickName = shpUserShopRefService.getNameFromShop(getShopId(), saleUserId);
        ordOrderUpdate.setSaleNickname(nickName);

        //销售途径
        ordOrderUpdate.setSaleChannel(orderUpload.getSaleChannel());
        //销售时间
        ordOrderUpdate.setFinishTime(orderUpload.getSaleTime());
        //售后保障
        ordOrderUpdate.setAfterSaleGuarantee(orderUpload.getAfterSaleGuarantee());
        //订单备注
        ordOrderUpdate.setRemark(orderUpload.getNote());

        //收货信息
        ordOrderUpdate.setToAddress(orderUpload.getAddress());


        //订单商品编号`
        ordOrderUpdate.setNumber(orderUpload.getBizId());

        //扣款凭证图片地址
        ordOrderUpdate.setDeductVoucherImgUrl(orderUpload.getDeductVoucherImgUrl());
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
                    name = "orderBizId", value = "订单编号")
    })
    @RequestRequire
    @RequestMapping("/listOrderModifyRecordById")
    public BaseResult<List<VoOrderModifyRecord>> listOrderModifyRecordById(@RequestParam String orderBizId) {
        PageHelper.startPage(getPageNum(), PAGE_SIZE_10);
        Integer shopId = getShopId();
        //是否有查看成交价的权限
        boolean hasSeeSalePricePerm = hasPermWithCurrentUser(ConstantPermission.CHK_PRICE_FINISH);
        List<VoOrderModifyRecord> voOrderModifyRecordList = ordOrderService.listOrderModifyRecordById(shopId, orderBizId, hasSeeSalePricePerm);

        return BaseResult.okResult(voOrderModifyRecordList);
    }

    /**
     * 获取订单详情
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "获取订单详情2.5.5;",
            notes = "获取订单详情2.5.5",
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
        //订单详情判断商品删除状态
        VoProductLoad productLoad = proProductService.getProductDetailByShopIdBizId(getShopId(), orderLoad.getBizId());
        if (productLoad != null && "-90".equals(productLoad.getStateUs())) {
            orderLoad.setProductDeleteState("1");
        } else {
            orderLoad.setProductDeleteState("0");
        }
        if ("-90".equals(orderLoad.getState())) {
            //已删除订单删除权限
            String uPermDeleteHistory = ConstantPermission.PROORORD_DELETE_DELETEHISTORY;
            orderLoad.setUPermHistoryDelete(hasPermToPageWithCurrentUser(uPermDeleteHistory));
        }
        if (LocalUtils.isEmptyAndNull( orderLoad.getLockId())){
            return LocalUtils.getBaseResult(orderLoad);
        }
        VoProLockRecord lockRecord = proLockRecordService.getLockRecordByIdAndShopId(getShopId(), orderLoad.getLockId());
        if (LocalUtils.isEmptyAndNull(lockRecord)) {
            return LocalUtils.getBaseResult(orderLoad);
        }
        orderLoad.setLockReason(lockRecord.getLockReason());
        orderLoad.setPreFinishMoney(lockRecord.getPreFinishMoney());
        orderLoad.setLockTime(DateUtil.format(lockRecord.getInsertTime()));
        orderLoad.setLockUser(lockRecord.getLockUser());
        return LocalUtils.getBaseResult(orderLoad);
    }


    /**
     * 删除订单
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "删除订单;",
            notes = "删除订单",
            httpMethod = "POST")
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
    @PostMapping("/deleteOrder")
    @RequiresPermissions("ord:deleteOrder")
    public BaseResult deleteOrder(@RequestParam Map<String, String> params, HttpServletRequest request, @Valid ParamOrderDelete delete, BindingResult result) {

        servicesUtil.validControllerParam(result);
        delete.setShopId(getShopId());
        delete.setUserId(getUserId());
        ordOrderService.deleteOrderForFalse(delete, request);
        return BaseResult.okResult();
    }

    /**
     * 批量删除订单
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "批量删除订单;",
            notes = "批量删除订单",
            httpMethod = "POST")
    @ApiImplicitParams({

            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "orderBizId", value = "订单流水号,多个用英文逗号(,)隔开"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequiresPermissions("ord:deleteOrder")
    @PostMapping("/deleteBatchOrder")
    public BaseResult deleteBatchOrder(
            @RequestParam Map<String, String> params, @Valid ParamOrderDelete delete,
            BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        int shopId = getShopId();
        int userId = getUserId();
        delete.setShopId(shopId);
        delete.setUserId(userId);
        ordOrderService.deleteBatchOrderForFalse(delete, request);
        //添加【店铺操作日志】-【删除订单】
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.ORDER.getName());
        paramAddShpOperateLog.setOperateName("删除订单");
        paramAddShpOperateLog.setOperateContent("批量删除订单");
        paramAddShpOperateLog.setProdId(null);
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(request);
        shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        return BaseResult.okResult();
    }


}
