package com.luxuryadmin.service.fin.impl;

import com.alibaba.fastjson.JSONObject;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.entity.fin.FinSalary;
import com.luxuryadmin.entity.fin.FinSalaryDetail;
import com.luxuryadmin.enums.shp.EnumShpOperateLogModule;
import com.luxuryadmin.mapper.fin.FinSalaryDetailMapper;
import com.luxuryadmin.mapper.fin.FinSalaryMapper;
import com.luxuryadmin.param.fin.ParamCreateSalaryQuery;
import com.luxuryadmin.param.fin.ParamFundRecordAdd;
import com.luxuryadmin.param.fin.ParamSalaryDetailQuery;
import com.luxuryadmin.param.shp.ParamAddShpOperateLog;
import com.luxuryadmin.service.fin.FinFundRecordService;
import com.luxuryadmin.service.fin.FinSalaryDetailService;
import com.luxuryadmin.service.fin.FinSalaryService;
import com.luxuryadmin.service.ord.OrdOrderService;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.service.shp.ShpOperateLogService;
import com.luxuryadmin.service.shp.ShpServiceService;
import com.luxuryadmin.service.shp.ShpUserService;
import com.luxuryadmin.service.shp.ShpUserShopRefService;
import com.luxuryadmin.vo.fin.VoCreateSalaryDetail;
import com.luxuryadmin.vo.fin.VoSalaryOrdType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.validation.BindingResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * @author monkey king
 * @date 2020-11-18 20:02:41
 */
@Slf4j
@Service
public class FinSalaryDetailServiceImpl implements FinSalaryDetailService {

    @Resource
    private FinSalaryMapper finSalaryMapper;

    @Resource
    private FinSalaryDetailMapper finSalaryDetailMapper;

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    @Autowired
    private ShpServiceService shpServiceService;

    @Autowired
    private FinSalaryService finSalaryService;

    @Autowired
    private OrdOrderService ordOrderService;

    @Autowired
    private ProProductService proProductService;

    @Autowired
    private ServicesUtil servicesUtil;

    @Autowired
    private ShpUserService shpUserService;

    @Autowired
    private ShpOperateLogService shpOperateLogService;
    @Autowired
    private FinFundRecordService fundRecordService;
    @Override
    public VoCreateSalaryDetail getCreateSalaryDetail(int shopId, int userId, String startDate) {
        String st = startDate + " 00:00:00";
        return finSalaryDetailMapper.getVoCreateSalaryDetail(shopId, userId, st);
    }

    @Override
    public FinSalaryDetail getFinSalaryDetail(int shopId, int userId, String startDate) {
        String st = startDate + " 00:00:00";
        return finSalaryDetailMapper.getFinSalaryDetail(shopId, userId, st);
    }

    @Override
    public VoCreateSalaryDetail getOwnLastCreateSalaryDetail(int shopId, int userId) {
        return finSalaryDetailMapper.getVoCreateSalaryDetail(shopId, userId, null);
    }

    @Override
    public VoCreateSalaryDetail getShopLastCreateSalaryDetail(int shopId) {
        return finSalaryDetailMapper.getVoCreateSalaryDetail(shopId, null, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSalaryDetail(int shopId, int userId, String startDate,HttpServletRequest request) {
        try {
            FinSalaryDetail fsd = finSalaryDetailMapper.getFinSalaryDetail(shopId, userId, startDate);
            if (null != fsd) {
                finSalaryDetailMapper.deleteObject(fsd.getId());
                finSalaryMapper.deleteObject(fsd.getFkFinSalaryId());

                //??????????????????????????????-??????????????????
                String username = shpUserService.getUsernameByUserId(userId);
                String salMonth = DateUtil.format(fsd.getSalaryStTime(),"MM");
                ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
                paramAddShpOperateLog.setShopId(shopId);
                paramAddShpOperateLog.setOperateUserId(userId);
                paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.SALARY.getName());
                paramAddShpOperateLog.setOperateName("????????????");
                paramAddShpOperateLog.setOperateContent(username+"-"+salMonth+"?????????");
                paramAddShpOperateLog.setProdId(null);
                paramAddShpOperateLog.setOrderId(null);
                paramAddShpOperateLog.setRequest(request);

                shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
            }
        } catch (Exception e) {
            log.info("=====??????????????????????????????: shopId:{};userId{}", shopId, userId);
            log.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Override
    public BigDecimal createSalary(ParamCreateSalaryQuery param, BindingResult result, HttpServletRequest request) throws Exception {
        int shopId = param.getShopId();
        int userId = Integer.parseInt(param.getUserId());
        String st = param.getStartDate();
        String et = param.getEndDate();
        //??????????????????????????????
        Date startDate = DateUtil.parseShort(st);
        Calendar stCalendar = Calendar.getInstance();
        stCalendar.setTime((startDate));
        stCalendar.set(Calendar.DAY_OF_MONTH, 1);
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.set(Calendar.DAY_OF_MONTH, 1);
        if (stCalendar.after(now)) {
            throw new MyException("??????????????????????????????????????????!");
        }

        if (DateUtil.parseShort(et).before(DateUtil.parseShort(st))) {
            throw new MyException("????????????????????????????????????");
        }

        boolean existsEmployee = shpUserShopRefService.existsShpUserShopRef(shopId, userId);
        if (!existsEmployee) {
            log.info("???????????????????????????!shopId: {}, userId: {}", shopId, userId);
            throw new MyException("???????????????????????????!");
        }
        FinSalaryDetail fd = getFinSalaryDetail(shopId, userId, st);
        //??????????????????
        boolean isSave = ConstantCommon.ZERO.equals(param.getUpdateOrSave());
        if (isSave) {
            //????????????,??????????????????????????????,?????????
            if (!LocalUtils.isEmptyAndNull(fd)) {
                throw new MyException("????????????????????????");
            }
        } else if (LocalUtils.isEmptyAndNull(fd)) {
            //?????????????????????????????????????????????,?????????
            throw new MyException("???????????????????????????");
        }

        Map<String, Object> map = calcTotalSalaryMoney(param, result);
        BigDecimal finalMoney = new BigDecimal(map.get("finalMoney").toString());

        //????????????
        FinSalary finSalary = pickFinSalary(param, finalMoney.longValue());
        FinSalaryDetail fsd = pickFinSalaryDetail(userId, param);

        //??????????????????????????????
        if (ConstantCommon.ONE.equals(param.getSalaryState())) {
            //??????????????????????????????
            Object saleResultJson = map.get("saleResultJson");
            fsd.setSaleResultJson(saleResultJson.toString());
        }

        //??????????????????????????????-??????????????????/????????????????????????
        String username = shpUserService.getUsernameByUserId(userId);
        Integer salMonth = stCalendar.get(Calendar.MONTH) + 1;
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.SALARY.getName());
        paramAddShpOperateLog.setOperateContent(username+"-"+salMonth+"?????????");
        paramAddShpOperateLog.setProdId(null);
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(request);
        if (isSave) {
            finSalaryService.createSalary(finSalary, fsd);
            paramAddShpOperateLog.setOperateName("????????????");
        } else {
            finSalary.setInsertTime(fd.getInsertTime());
            fsd.setInsertTime(fd.getInsertTime());
            fsd.setFkFinSalaryId(fd.getFkFinSalaryId());
            fsd.setId(fd.getId());
            finSalaryService.updateSalary(finSalary, fsd);

            //???????????????????????????
            if (ConstantCommon.ONE.equals(param.getSalaryState())) {
                paramAddShpOperateLog.setOperateName("????????????");
            }else{
                paramAddShpOperateLog.setOperateName("??????????????????");
            }
        }
        if (ConstantCommon.ONE.equals(param.getSalaryState())) {
            //????????????????????????
            ParamFundRecordAdd paramFundRecordAdd = new ParamFundRecordAdd();
            paramFundRecordAdd.setFkShpShopId(shopId);
            paramFundRecordAdd.setUserId(userId);
            paramFundRecordAdd.setMoney( finalMoney.toString());
            paramFundRecordAdd.setInitPrice("0");
            paramFundRecordAdd.setState("out");
            paramFundRecordAdd.setFundType("40");
            paramFundRecordAdd.setCount("1");
            paramFundRecordAdd.setFinClassifyName("????????????");
            fundRecordService.addOtherFundRecord(paramFundRecordAdd);

        }
        if (null != request) {
            shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        }
        return finalMoney;
    }

    @Override
    public Map<String, Object> calcTotalSalaryMoney(ParamCreateSalaryQuery param, BindingResult result) throws Exception {
        int shopId = param.getShopId();
        int userId = Integer.parseInt(param.getUserId());
        String st = param.getStartDate();
        String et = param.getEndDate();


        String proAttr = param.getProAttr();
        JSONObject salaryDetail = getSalaryDetail(param, result);

        //????????????
        Map<String, Object> kindOfProfit = getKindOfProfit(shopId, userId, proAttr, st, et);

        //TA?????????????????????(???)
        long recycleInitPrice = parseLong(kindOfProfit.get("recycleInitPrice").toString());
        //TA??????????????????
        long recycleNum = parseLong(kindOfProfit.get("recycleNum").toString());
        //TA?????????????????????(???)
        long recycleProfit = parseLong(kindOfProfit.get("recycleProfit").toString());
        //??????????????????(???)
        long serviceProfit = parseLong(kindOfProfit.get("serviceProfit").toString());

        //?????????????????? ??????: ????????????-??????,????????????:???,????????????2:???2;?????????????????????
        String[] schemeArray = param.getSchemeType().split(";");
        //????????????
        Map<String, Map<String, Double>> salaryPlan = getSalaryPlan(salaryDetail);
        //??????: ????????????-??????,????????????:???,????????????2:???2;
        double planResult = 0;
        //??????????????????3?????????;
        for (String scheme : schemeArray) {
            String[] sh = scheme.split(",");
            String schemeName = sh[0];
            Map<String, Double> planObj = salaryPlan.get(schemeName);
            if (LocalUtils.isEmptyAndNull(planObj)) {
                continue;
            }
            planResult += calcResult(sh, planObj);
        }

        //?????????????????????(%)
        String recycleInitPricePercent = param.getRecycleInitPricePercent();
        BigDecimal rcyInitPricePerStr = LocalUtils.calcNumber(recycleInitPricePercent, "*", 0.01);
        BigDecimal recyclePercentMoney = LocalUtils.calcNumber(recycleInitPrice, "*", rcyInitPricePerStr);
        //??????????????????
        String recycleUnitPrice = param.getRecycleUnitPrice();
        //??????????????????
        BigDecimal recycleUnitPriceStr = LocalUtils.calcNumber(recycleUnitPrice, "*", 100);
        BigDecimal recycleUnitPriceTotalMoney = LocalUtils.calcNumber(recycleNum, "*", recycleUnitPriceStr);

        //TA?????????????????????(???) ????????????????????????
        String recyclePercentStr = param.getRecycleProfitPercent();
        BigDecimal recyclePercent = LocalUtils.calcNumber(recyclePercentStr, "*", 0.01);
        double recycleResult = LocalUtils.calcNumber(recyclePercent, "*", recycleProfit).doubleValue();

        //??????????????????(???) ????????????????????????
        String servicePercentStr = param.getServiceProfitPercent();
        BigDecimal servicePercent = LocalUtils.calcNumber(servicePercentStr, "*", 0.01);
        double serviceResult = LocalUtils.calcNumber(servicePercent, "*", serviceProfit).doubleValue();


        //??????????????????
        //??????
        String basicMoney = param.getBasicMoney();
        //????????????
        String elseMoney = param.getElseMoney();

        //???????????????+????????????
        BigDecimal basicElseMoney = LocalUtils.calcNumber(basicMoney, "+", elseMoney);
        //????????????????????????
        BigDecimal basicElsePlanMoney = LocalUtils.calcNumber(basicElseMoney, "+", planResult);
        //?????????????????????????????????
        BigDecimal recyclePercentTotalMoney = LocalUtils.calcNumber(recyclePercentMoney, "+", basicElsePlanMoney);
        //?????????????????????????????????
        BigDecimal recycleUnitTotalMoney = LocalUtils.calcNumber(recycleUnitPriceTotalMoney, "+", recyclePercentTotalMoney);
        //??????TA?????????????????????(???)??????
        BigDecimal money4 = LocalUtils.calcNumber(recycleResult, "+", recycleUnitTotalMoney);
        //????????????????????????,?????? ??????????????????

        BigDecimal finalMoney = LocalUtils.calcNumber(serviceResult, "+", money4);
        Map<String, Object> map = new HashMap<>(16);
        map.put("finalMoney", finalMoney);
        map.put("saleResultJson", salaryDetail.toJSONString());
        return map;
    }

    /**
     * ??????????????????FinSalary
     *
     * @param param
     * @param salaryMoney
     * @return
     * @throws Exception
     */
    private FinSalary pickFinSalary(ParamCreateSalaryQuery param, long salaryMoney) throws Exception {
        Date month = DateUtil.parseShort(param.getStartDate());
        String salaryName = DateUtil.format(month, "yyyy???MM???");
        FinSalary finSalary = new FinSalary();
        finSalary.setFkShpShopId(param.getShopId());
        finSalary.setFkShpUserId(Integer.parseInt(param.getUserId()));
        finSalary.setSalaryState(param.getSalaryState());
        finSalary.setSalaryMoney(salaryMoney);
        finSalary.setSalaryName(salaryName);
        finSalary.setSalaryStTime(DateUtil.parseDefaultSt(param.getStartDate()));
        finSalary.setSalaryEtTime(DateUtil.parseDefaultEt(param.getEndDate()));
        finSalary.setInsertTime(new Date());
        finSalary.setUpdateTime(finSalary.getInsertTime());
        finSalary.setInsertAdmin(param.getLocalUserId());
        finSalary.setUpdateAdmin(param.getLocalUserId());
        finSalary.setSalaryState(param.getSalaryState());
        return finSalary;
    }


    /**
     * ??????????????????FinSalaryDetail
     *
     * @param userId
     * @param param
     * @return
     */
    private FinSalaryDetail pickFinSalaryDetail(int userId, ParamCreateSalaryQuery param) throws ParseException {
        FinSalaryDetail fsd = new FinSalaryDetail();
        fsd.setFkShpShopId(param.getShopId());
        fsd.setFkShpUserId(userId);
        fsd.setProductAttr(param.getProAttr());
        fsd.setSaleMoney(0L);
        fsd.setGrossProfitMoney(0L);
        fsd.setRecycleCostMoney(0L);
        fsd.setRecycleProfitMoney(0L);
        fsd.setServiceProfitMoney(0L);
        fsd.setSchemeType(param.getSchemeType());
        fsd.setBasicMoney(parseLong(param.getBasicMoney()));
        fsd.setElseMoney(parseLong(param.getElseMoney()));
        fsd.setSalePushMoney(0L);
        fsd.setRecyclePushMoney(0L);
        fsd.setServicePushMoney(0L);
        fsd.setSalaryTotalMoney(0L);
        fsd.setRecycleInitPricePercent(new BigDecimal(param.getRecycleInitPricePercent()));
        fsd.setRecycleUnitPrice(new BigDecimal(param.getRecycleUnitPrice()));
        fsd.setRecycleProfitPercent(new BigDecimal(param.getRecycleProfitPercent()));
        fsd.setServiceProfitPercent(new BigDecimal(param.getServiceProfitPercent()));
        fsd.setSalaryStTime(DateUtil.parseDefaultSt(param.getStartDate()));
        fsd.setSalaryEtTime(DateUtil.parseDefaultEt(param.getEndDate()));
        fsd.setInsertTime(new Date());
        fsd.setUpdateTime(fsd.getInsertTime());
        fsd.setInsertAdmin(param.getLocalUserId());
        fsd.setUpdateAdmin(param.getLocalUserId());
        fsd.setRemark("");
        return fsd;
    }

    private double calcResult(String[] sh, Map<String, Double> planObj) {
        double v = 0;
        try {
            for (int i = 1; i < sh.length; i++) {
                //????????????:???; ??????: ????????????:5
                String orderTypeStr = sh[i];
                String[] orderAndValue = orderTypeStr.split(":");
                String paramOrderType = orderAndValue[0];
                String paramOrderValueStr = orderAndValue[1];
                double paramOrderValue = Double.parseDouble(paramOrderValueStr);
                double value = planObj.get(paramOrderType) == null ? 0 : planObj.get(paramOrderType);
                v += LocalUtils.calcNumber(paramOrderValue, "*", value).doubleValue();
            }
            //????????????????????????
            if ("2-1,2-2".contains(sh[0])) {
                v = LocalUtils.calcNumber(v, "*", 0.01).doubleValue();
            }
            //??????????????????
            if ("2-3".contains(sh[0])) {
                v = LocalUtils.calcNumber(v, "*", 100).doubleValue();
            }
            System.err.println(sh[0] + "???????????????: " + v);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param salaryDetail
     * @return
     */
    private Map<String, Map<String, Double>> getSalaryPlan(Map<String, Object> salaryDetail) {
        String key = "allPro";
        Map<String, Map<String, Double>> hashMap = new HashMap<>(16);
        //????????????????????????????????????????????????
        List<Map<String, Object>> jsonArray = getProAttrJsonArray(salaryDetail, key);
        //????????????
        Map<String, Object> saleTotalMoneyObj = jsonArray.get(0);
        List<Map<String, Double>> salePlanOrderTypeMap = getPlanOrderTypeMap(saleTotalMoneyObj);
        Map<String, Double> saleOrderMoneyMap = salePlanOrderTypeMap.get(0);
        Map<String, Double> saleOrderNumMap = salePlanOrderTypeMap.get(1);
        //2-1???????????????
        hashMap.put("2-1", saleOrderMoneyMap);
        //2-3??????????????????
        hashMap.put("2-3", saleOrderNumMap);

        //???????????????
        Map<String, Object> grossProfitTotalMoneyObj = jsonArray.get(1);
        List<Map<String, Double>> grossProfitPlanOrderTypeMap = getPlanOrderTypeMap(grossProfitTotalMoneyObj);
        Map<String, Double> grossProfitOrderMoneyMap = grossProfitPlanOrderTypeMap.get(0);
        //2-2???????????????
        hashMap.put("2-2", grossProfitOrderMoneyMap);

        ////??????????????????
        //Map<String, Object> recycleTotalMoneyObj = jsonArray.get(2);
        //List<Map<String, Double>> recyclePlanOrderTypeMap = getPlanOrderTypeMap(recycleTotalMoneyObj);
        //Map<String, Double> recycleOrderMoneyMap = recyclePlanOrderTypeMap.get(0);
        //Map<String, Double> recycleOrderNumMap = recyclePlanOrderTypeMap.get(1);
        ////3-1??????????????????
        //hashMap.put("3-1", recycleOrderMoneyMap);
        ////3-2??????????????????
        //hashMap.put("3-2", recycleOrderNumMap);
        return hashMap;
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param totalMoneyObj
     * @return
     */
    private List<Map<String, Double>> getPlanOrderTypeMap(Map<String, Object> totalMoneyObj) {
        List<Map<String, Double>> list = new ArrayList<>(16);
        Map<String, Double> orderMoneyMap = new HashMap<>(16);
        Map<String, Double> orderNumMap = new HashMap<>(16);

        ArrayList<VoSalaryOrdType> totalMoneyList = (ArrayList<VoSalaryOrdType>) totalMoneyObj.get("list");
        if (!LocalUtils.isEmptyAndNull(totalMoneyList)) {
            for (VoSalaryOrdType orderType : totalMoneyList) {
                String orderTypeKey = orderType.getOrderType();
                orderMoneyMap.put(orderTypeKey, Double.parseDouble(orderType.getMoney().toString()));
                orderNumMap.put(orderTypeKey, Double.parseDouble(orderType.getNum().toString()));
            }
        }
        list.add(orderMoneyMap);
        list.add(orderNumMap);
        return list;
    }

    private List<Map<String, Object>> getProAttrJsonArray(Map<String, Object> salaryDetail, String key) {
        List<Map<String, Object>> proAttrObj = (List<Map<String, Object>>) salaryDetail.get(key);
        return proAttrObj;
    }

    private Long parseLong(String number) {
        return new BigDecimal(number).longValue();
    }

    /**
     * TA??????????????????????????????,TA?????????????????????,??????????????????<br/>
     *
     * @param shopId
     * @param userId
     * @param proAttr
     * @param st
     * @param et
     * @return
     */
    private Map<String, Object> getKindOfProfit(int shopId, int userId, String proAttr, String st, String et) {
        st = st + " 00:00:00";
        et = et + " 23:59:59";
        //TA????????????????????????????????????
        Map<String, Object> recyclePriceAndNum = proProductService.countRecycleInitPriceAndNum(shopId, userId, proAttr, st, et);
        Object recycleInitPrice = recyclePriceAndNum.get("initPrice");
        Object recycleNum = recyclePriceAndNum.get("num");
        //TA?????????????????????
        Object recycleProfit = ordOrderService.countRecycleProfit(shopId, userId, proAttr, st, et);
        //??????????????????
        BigDecimal serviceProfit = shpServiceService.getTotalFinishServiceProfit(shopId, userId, st, et);
        Map<String, Object> hashMap = new HashMap<>(16);
        hashMap.put("recycleInitPrice", recycleInitPrice);
        hashMap.put("recycleNum", recycleNum);
        hashMap.put("recycleProfit", recycleProfit);
        hashMap.put("serviceProfit", serviceProfit);
        return hashMap;
    }

    /**
     * ???????????????????????????????????????????????????(???????????????????????????),<br/>
     * ?????????, ??????????????????????????????????????????
     *
     * @param param
     * @param result
     * @return
     */
    @Override
    public JSONObject getSalaryDetail(ParamSalaryDetailQuery param, BindingResult result) throws Exception {
        servicesUtil.validControllerParam(result);
        int shopId = param.getShopId();
        int userId = Integer.parseInt(param.getUserId());
        String proAttr = param.getProAttr();
        String st = param.getStartDate();
        String et = param.getEndDate();
        param.setShopId(shopId);
        String username = shpUserService.getUsernameByUserId(userId);
        if (LocalUtils.isEmptyAndNull(username)) {
            throw new MyException("???????????????");
        }
        //???????????????????????????????????????????????????
        //??????????????????????????????????????????????????????????????????????????????
        List<List<VoSalaryOrdType>> saleGrossList = ordOrderService.countSaleAndGrossProfitMoney(param);
        List<Map<String, Object>> list = formatMap(saleGrossList);
        //????????????
        Map<String, Object> kindOfProfit = getKindOfProfit(shopId, userId, proAttr, st, et);
        //TA????????????????????????????????????
        Object recycleInitPrice = kindOfProfit.get("recycleInitPrice");
        Object recycleNum = kindOfProfit.get("recycleNum");
        Map<String, Object> recycleCostNum = packMoneyAndNum("TA?????????????????????", recycleInitPrice, recycleNum);
        //TA?????????????????????
        Object recycleProfit = kindOfProfit.get("recycleProfit");
        Map<String, Object> recycleProfitMap = packMoneyAndNum("TA?????????????????????", recycleProfit, 0);
        //??????????????????
        Object serviceProfit = kindOfProfit.get("serviceProfit");
        Map<String, Object> fixProfitMap = packMoneyAndNum("??????????????????", serviceProfit, 0);
        list.add(recycleCostNum);
        list.add(recycleProfitMap);
        list.add(fixProfitMap);
        Map<String, Object> map = new HashMap<>(16);
        map.put("allPro", list);
        return new JSONObject(map);
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param title
     * @param money
     * @param num
     * @return
     */
    private Map<String, Object> packMoneyAndNum(String title, Object money, Object num) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("title", title);
        map.put("list", new ArrayList<>());
        map.put("totalMoney", money);
        map.put("totalNum", num);
        return map;
    }


    /**
     * ?????????????????????
     *
     * @param mainList
     * @return
     * @throws Exception
     */
    private List<Map<String, Object>> formatMap(List<List<VoSalaryOrdType>> mainList) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        if (LocalUtils.isEmptyAndNull(mainList)) {
            return list;
        }
        int i = 0;
        for (List<VoSalaryOrdType> typeList : mainList) {
            List<VoSalaryOrdType> allOrdTypeList = new ArrayList<>();
            Map<String, Object> allMap = new HashMap<>(16);
            int allNum = 0;
            long allTotalMoney = 0;
            Map<String, VoSalaryOrdType> allOrdTypeMap = new HashMap<>(16);
            String title = "??????";
            switch (i) {
                case 0:
                    title = "????????????";
                    break;
                case 1:
                    title = "???????????????";
                    break;
                default:
                    break;
            }
            for (VoSalaryOrdType ordType : typeList) {
                BigDecimal money = ordType.getMoney();
                Integer num = ordType.getNum();
                allNum = LocalUtils.calcNumber(allNum, "+", num).intValue();
                allTotalMoney = LocalUtils.calcNumber(allTotalMoney, "+", money).longValue();
                VoSalaryOrdType ot = allOrdTypeMap.get(ordType.getOrderType());
                if (null != ot) {
                    ot.setOrderType(ordType.getOrderType());
                    ot.setNum(ot.getNum() + ordType.getNum());
                    BigDecimal otMoney = LocalUtils.calcNumber(ot.getMoney(), "+", ordType.getMoney());
                    ot.setMoney(otMoney);
                } else {
                    ot = ordType;
                }
                allOrdTypeMap.put(ordType.getOrderType(), ot);
            }
            if (!LocalUtils.isEmptyAndNull(allOrdTypeMap)) {
                allOrdTypeMap.forEach((s, voSalaryOrdType) -> allOrdTypeList.add(voSalaryOrdType));
            }

            allMap.put("totalMoney", allTotalMoney);
            allMap.put("totalNum", allNum);
            allMap.put("list", allOrdTypeList);
            allMap.put("title", title);
            list.add(allMap);
            i++;
        }
        return list;
    }
}
