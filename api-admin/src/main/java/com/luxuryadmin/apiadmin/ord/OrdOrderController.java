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
@Api(tags = {"C002.??????????????????"}, description = "/shop/user/ord |??????????????????????????????")
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
     * ?????????????????????????????????
     *
     * @param paramToken ????????????
     * @return Result
     */
    @ApiOperation(
            value = "?????????????????????????????????;",
            notes = "?????????????????????????????????",
            httpMethod = "POST")
    @PostMapping("/initSaleAnalyse")
    public BaseResult initSaleAnalyse(@Valid ParamToken paramToken, BindingResult result) {
        servicesUtil.validControllerParam(result);
        return BaseResult.okResult(initSaleAnalyseQuery(getShopId()));
    }

    /**
     * ????????????????????????????????????
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
        //??????????????????
        for (VoProAttribute voProAttribute : voProAttributeList) {
            if (!(EnumProAttribute.PAWN.getCode() + "").equals(voProAttribute.getCode())) {
                voProAttributeListNew.add(voProAttribute);
            }
        }
        HashMap<String, Object> hashMap = LocalUtils.getHashMap("ordTypeList", ordTypeList);
        //????????????
        hashMap.put("attributeList", voProAttributeListNew);
        //????????????
        hashMap.put("classifyList", voProClassifyList);
        List<VoEmployee> salesmanOrderList;
        //????????????????????????????????????
        boolean hasPerm = hasPermWithCurrentUser(ConstantPermission.CHK_ALL_ORDER);
        String name = shpUserShopRefService.getNameFromShop(getShopId(), getUserId());
        if (hasPerm) {
            salesmanOrderList = new ArrayList<>(voEmployeeList);
        } else {
            //????????????????????????
            salesmanOrderList = new ArrayList<>();
            VoEmployee ve = new VoEmployee();
            ve.setUserId(getUserId());

            ve.setNickname(name);
            salesmanOrderList.add(ve);
        }
        //????????????????????????
        hashMap.put("salesmanOrderList", salesmanOrderList);
        return hashMap;
    }


    /**
     * ??????????????????????????????;
     *
     * @param params ????????????
     * @return Result
     */
    @ApiOperation(
            value = "??????????????????????????????;",
            notes = "??????????????????????????????;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "????????????;????????????")
    })
    @RequestRequire
    @RequestMapping("/listOrder")
    @RequiresPermissions("ord:listOrder")
    public BaseResult listOrder(@RequestParam Map<String, String> params,
                                @Valid ParamOrderQuery orderQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        //?????????????????????????????????
        orderQuery.setUniqueCode(orderQuery.getProName());
        //????????????????????????????????????
        boolean hasPerm = hasPermWithCurrentUser(ConstantPermission.CHK_ALL_ORDER);
        if (!hasPerm) {
            //?????????????????????????????????,????????????????????????
            orderQuery.setSaleUserId(getUserId() + "");
        }
        formatQueryParam(orderQuery);
        PageHelper.startPage(getPageNum(), getPageSize());
        List<VoOrderLoad> voOrderLoadList = ordOrderService.listOrderByCondition(orderQuery);
        formatVoOrderLoad(voOrderLoadList);
        //??????????????????, ??????????????????;
        orderQuery.setState("20");

        //??????????????????????????????????????????
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
                //????????????
                BigDecimal totalSaleCount = new BigDecimal(saleData.getTotalSaleCount());
                //????????????(???)
                String totalSaleAmount = saleData.getTotalSaleAmount();
                //????????????(???)
                String totalInitPrice = saleData.getTotalInitPrice();
                //?????????
                BigDecimal totalGrossProfit = LocalUtils.calcNumber(totalSaleAmount, "-", totalInitPrice);
                BigDecimal grossProfitRate = new BigDecimal(0);
                if (totalGrossProfit.longValue() != 0) {
                    //?????????
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

        //????????????
        String deleteOrderPerm = ConstantPermission.MOD_DELETE_ORDER;
        String hasDeleteOrderPerm = hasPermToPageWithCurrentUser(deleteOrderPerm);
        hashMap.put("uPermDeleteOrder", hasDeleteOrderPerm);

        return BaseResult.okResult(hashMap);
    }

    /**
     * ?????????????????????;-????????????;-??????????????????
     *
     * @param params ????????????
     * @return Result
     */
    @ApiOperation(
            value = "?????????????????????;",
            notes = "?????????????????????;-????????????;-??????????????????;<br/>" +
                    "vid????????????????????????,?????????",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "????????????;????????????"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "??????token"),
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
        //??????????????????
        for (VoProAttribute voProAttribute : voProAttributeList) {
            if (!(EnumProAttribute.PAWN.getCode() + "").equals(voProAttribute.getCode())) {
                voProAttributeListNew.add(voProAttribute);
            }
        }
        String vid = LocalUtils.getUUID();
        HashMap<String, Object> hashMap = LocalUtils.getHashMap("ordTypeList", ordTypeList);
        hashMap.put("salesmanList", voEmployeeList);
        //??????????????????
        hashMap.put("listRecycleUser", listRecycleUser);
        //????????????
        hashMap.put("attributeList", voProAttributeListNew);
        //????????????
        hashMap.put("classifyList", voProClassifyList);
        List<VoEmployee> salesmanOrderList;
        //????????????????????????????????????
        boolean hasPerm = hasPermWithCurrentUser(ConstantPermission.CHK_ALL_ORDER);
        String name = shpUserShopRefService.getNameFromShop(getShopId(), getUserId());
        if (hasPerm) {
            salesmanOrderList = new ArrayList<>(voEmployeeList);
        } else {
            //????????????????????????
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
     * ??????????????????
     *
     * @param params ????????????
     * @return Result
     */
    @ApiOperation(
            value = "??????????????????;",
            notes = "??????????????????",
            httpMethod = "GET")
    @ApiImplicitParams({

            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "orderBizId", value = "???????????????"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "????????????;????????????")
    })
    @RequestRequire
    @GetMapping("/getOrderDetail")
    public BaseResult getOrderDetail(@RequestParam Map<String, String> params) {
        String orderBizId = params.get("orderBizId");
        VoOrderLoad orderLoad = ordOrderService.getOrderDetailByNumber(getShopId(), orderBizId);
        if (LocalUtils.isEmptyAndNull(orderLoad)) {
            throw new MyException("???????????????");
        }
        formatVoOrderLoad(orderLoad);
        //????????????,????????????
        // Map<String, Object> numAndPrice = ordOrderService.getOrderNumAndPrice(orderQuery);
        //numAndPrice.put("objList", voOrderLoadList);
        return LocalUtils.getBaseResult(orderLoad);
    }


    /**
     * ????????????????????????????????????;
     *
     * @return Result
     */
    @ApiOperation(
            value = "????????????????????????????????????;",
            notes = "????????????????????????????????????;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "??????token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "orderNo", value = "????????????")
    })
    @RequestRequire
    @RequestMapping("/listOrderModifyRecordByOrderNo")
    public BaseResult<List<VoOrderModifyRecord>> listOrderModifyRecordByOrderNo(@RequestParam String orderNo) {
        PageHelper.startPage(getPageNum(), PAGE_SIZE_50);
        Integer shopId = getShopId();
        //?????????????????????????????????
        boolean hasSeeSalePricePerm = hasPermWithCurrentUser(ConstantPermission.CHK_PRICE_FINISH);
        List<VoOrderModifyRecord> voOrderModifyRecordList = ordOrderService.listOrderModifyRecordById(shopId, orderNo, hasSeeSalePricePerm);

        return BaseResult.okResult(voOrderModifyRecordList);
    }

    /**
     * ??????????????????
     *
     * @param params ????????????
     * @return Result
     */
    @ApiOperation(
            value = "??????????????????",
            notes = "??????????????????",
            httpMethod = "GET")
    @GetMapping("/getOrderReceipt")
    public BaseResult getOrderReceipt(@Valid ParamOrdReceipt params, BindingResult bindingResult) {
        servicesUtil.validControllerParam(bindingResult);
        String orderBizId = params.getOrderBizId();
        VoOrdReceipt ordReceipt = ordReceiptService.getOrdReceiptByOrderNumber(getShopId(), orderBizId);
        return LocalUtils.getBaseResult(ordReceipt);
    }

    /**
     * ????????????;
     *
     * @param orderUpload ????????????
     * @return Result
     */
    @ApiOperation(
            value = "????????????;--2.6.5???",
            notes = "????????????;vid???initOrderParam????????????--2.6.5???",
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
        //???????????????id;v262(???)???????????????????????????,???????????????????????????????????????
        String lockId = orderUpload.getLockId();
        //??????????????????????????????????????????2.6.3
        if (LocalUtils.isEmptyAndNull(orderUpload.getFinalPrice())) {
            throw new MyException("????????????????????????");
        }
//        //2020-09-02 sanjin145 ???????????????????????????,????????????0
//        if (null == orderUpload.getFinalPrice()) {
//            orderUpload.setFinalPrice("0");
//        }
        //2020-10-22 sanjin145 ????????????????????????????????????
        Date saleTime = orderUpload.getSaleTime();
        Date now = new Date();
        if (now.before(saleTime)) {
            throw new MyException("????????????????????????????????????");
        }
        servicesUtil.validVid(key, vid);
        ProProduct pro = proProductService.getProProductByShopIdBizId(shopId, bizId);

        if ("20".equals(pro.getFkProAttributeCode()) && (LocalUtils.isEmptyAndNull(orderUpload.getEntrustState()))) {
            throw new MyException("????????????????????????????????????");
        }
        String totalNumStr = orderUpload.getTotalNum();
        int openTotalNum = Integer.parseInt(totalNumStr);
        //??????????????????
        if (!LocalUtils.isEmptyAndNull(lockId)) {
            lockProConfirmV262(request, lockId, pro);
        }
        try {
            //???????????????????????????2.6.6???????????????
            BasicParam basicParam = getBasicParam();
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.6") >= 0) {
                if (pro.getTotalNum() - openTotalNum <= 0) {
                    proDynamicProductService.updateStateByProId(pro.getId(), EnumDynamicProductState.SOLD_OUT.getCode(), shopId);
                }
            }
        } catch (Exception e) {
            throw new MyException("????????????????????????" + e);
        }
        //ProProduct pro = proProductService.getProProductByShopIdBizId(shopId, bizId);
        OrdOrder order = pickOrdOrder(orderUpload, pro);
        OrdAddress address = pickOrdAddress(orderUpload);
        String orderNumber = ordOrderService.confirmOrder(pro, order, address);
        redisUtil.delete(key);

        //????????????????????????
        addProModifyRecord(pro.getId(), "??????", "", "???????????????: " + openTotalNum);


        //????????????????????????
        //ordOrderService.refreshSalaryByOrderId(order.getId());

        //?????????????????????????????????
        proProductService.subtractProRedisLeftNum(shopId, bizId, order.getTotalNum());
        //??????,??????new?????????redis??????
        String newRedisKey = ConstantRedisKey.getNotDownloadProductKey(getShopId(), bizId);
        redisUtil.delete(newRedisKey);

        //??????????????????-??????????????????
        ParamFinShopRecordAdd paramFinShopRecordAdd = new ParamFinShopRecordAdd();
        paramFinShopRecordAdd.setInoutType(EnumFinShopRecordInoutType.IN.getCode());
        paramFinShopRecordAdd.setChangeAmount(order.getFinishPrice().divide(new BigDecimal(100.00)).toString());
        paramFinShopRecordAdd.setType("????????????");
        paramFinShopRecordAdd.setHappenTime(DateUtil.format(new Date()));
        String note = "???" + pro.getName() + "??????????????????";
        paramFinShopRecordAdd.setNote(note);
        String imgUrl = pro.getSmallImg();
        finShopRecordService.addFinShopRecord(shopId, getUserId(), paramFinShopRecordAdd, EnumFinShopRecordType.SYSTEM, imgUrl, order.getNumber());
        //??????????????????
        opPushService.pushOrderMsg(shopId, order, pro);

        //??????????????????????????????-??????????????????
        if (null != pro) {
            ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
            paramAddShpOperateLog.setShopId(shopId);
            paramAddShpOperateLog.setOperateUserId(getUserId());
            paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.ORDER.getName());
            paramAddShpOperateLog.setOperateName("????????????");
            paramAddShpOperateLog.setOperateContent(pro.getName());
            paramAddShpOperateLog.setProdId(pro.getId());
            paramAddShpOperateLog.setOrderId(order.getId());
            paramAddShpOperateLog.setRequest(request);

            shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        }
        //?????????????????????
        OrdOrder ordOrder = ordOrderService.getOrderInfoByNumber(orderNumber);
        if (LocalUtils.isEmptyAndNull(lockId)) {
            ordOrderService.savProDeliver(ordOrder);
        } else {
            proDeliverService.updateSourceByLockId(Integer.parseInt(lockId), ordOrder);
        }
        return LocalUtils.getBaseResult(orderNumber);
    }

    /**
     * ????????????
     *
     * @param params ????????????
     * @return Result
     */
    @ApiOperation(
            value = "????????????--2.6.5???;",
            notes = "????????????;vid???initOrderParam????????????--2.6.5???",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "????????????;????????????")
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

        //2020-10-22 sanjin145 ????????????????????????????????????
        Date saleTime = orderUpload.getSaleTime();
        Date now = new Date();
        if (now.before(saleTime)) {
            throw new MyException("????????????????????????????????????");
        }

        //??????????????????????????????
        ParamProductUpload paramProductUpload = new ParamProductUpload();
        BeanUtils.copyProperties(paramProductUploadQuick, paramProductUpload);
        paramProductUpload.setAttribute(paramProductUploadQuick.getAttribute());
        paramProductUpload.setState(EnumProState.RELEASE_20.getCode() + "");
        paramProductUpload.setTotalNum(orderUpload.getTotalNum());
        paramProductUpload.setVid(orderUpload.getVid());
        ProProduct pro = proProductService.pickProProduct(paramProductUpload, true, shopId, userId);
        //??????2.4.2???????????????;
        //??????????????????????????? ??????????????????????????????
        if ("20".equals(pro.getFkProAttributeCode()) && (LocalUtils.isEmptyAndNull(orderUpload.getEntrustState()))) {
            throw new MyException("????????????????????????????????????");
        }
        paramProductUpload.setBizId(pro.getBizId());
        ProDetail proDetail = proProductService.pickProDetail(paramProductUpload, true);
        pro.setSaleNum(0);
        proProductService.saveProProductReturnId(pro, proDetail, null);

        //??????????????????????????????
        orderUpload.setBizId(pro.getBizId());
        OrdOrder order = pickOrdOrder(orderUpload, pro);
        OrdAddress address = pickOrdAddress(orderUpload);
        order.setOpenType("quick");
        String orderNumber = ordOrderService.confirmOrder(pro, order, address);
        redisUtil.delete(key);
        //?????????????????????,?????????????????????;
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
        //????????????????????????
        // ordOrderService.refreshSalaryByOrderId(order.getId());

        //??????,??????new?????????redis??????
        String newRedisKey = ConstantRedisKey.getNotDownloadProductKey(getShopId(), pro.getBizId());
        redisUtil.delete(newRedisKey);

        //??????????????????-??????????????????
        ParamFinShopRecordAdd paramFinShopRecordAdd = new ParamFinShopRecordAdd();
        paramFinShopRecordAdd.setInoutType(EnumFinShopRecordInoutType.IN.getCode());
        paramFinShopRecordAdd.setChangeAmount(order.getFinishPrice().divide(new BigDecimal(100.00)).toString());
        paramFinShopRecordAdd.setType("????????????");
        paramFinShopRecordAdd.setHappenTime(DateUtil.format(new Date()));
        String note = "???" + pro.getName() + "??????????????????";
        paramFinShopRecordAdd.setNote(note);
        String imgUrl = pro.getSmallImg();
        finShopRecordService.addFinShopRecord(shopId, getUserId(), paramFinShopRecordAdd, EnumFinShopRecordType.SYSTEM, imgUrl, order.getNumber());


//
//        //??????????????????
        //opPushService.pushOrderMsg(shopId, order, pro);

        //??????????????????????????????-???????????????(????????????)???
        if (null != pro) {
            ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
            paramAddShpOperateLog.setShopId(shopId);
            paramAddShpOperateLog.setOperateUserId(getUserId());
            paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.ORDER.getName());
            paramAddShpOperateLog.setOperateName("????????????");
            paramAddShpOperateLog.setOperateContent(pro.getName());
            paramAddShpOperateLog.setProdId(pro.getId());
            paramAddShpOperateLog.setOrderId(order.getId());
            paramAddShpOperateLog.setRequest(request);

            shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        }
        //?????????????????????
        OrdOrder ordOrder = ordOrderService.getOrderInfoByNumber(orderNumber);
        ordOrderService.savProDeliver(ordOrder);
        return LocalUtils.getBaseResult(orderNumber);
    }


    /**
     * ????????????
     *
     * @param delete ????????????
     * @return Result
     */
    @ApiOperation(
            value = "????????????;???",
            notes = "????????????",
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
