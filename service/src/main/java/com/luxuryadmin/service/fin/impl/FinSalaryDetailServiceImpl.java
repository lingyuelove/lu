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

                //添加【店铺操作日志】-【删除薪资】
                String username = shpUserService.getUsernameByUserId(userId);
                String salMonth = DateUtil.format(fsd.getSalaryStTime(),"MM");
                ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
                paramAddShpOperateLog.setShopId(shopId);
                paramAddShpOperateLog.setOperateUserId(userId);
                paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.SALARY.getName());
                paramAddShpOperateLog.setOperateName("删除薪资");
                paramAddShpOperateLog.setOperateContent(username+"-"+salMonth+"月工资");
                paramAddShpOperateLog.setProdId(null);
                paramAddShpOperateLog.setOrderId(null);
                paramAddShpOperateLog.setRequest(request);

                shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
            }
        } catch (Exception e) {
            log.info("=====删除薪资明细失败回滚: shopId:{};userId{}", shopId, userId);
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
        //判断当前月份是否超前
        Date startDate = DateUtil.parseShort(st);
        Calendar stCalendar = Calendar.getInstance();
        stCalendar.setTime((startDate));
        stCalendar.set(Calendar.DAY_OF_MONTH, 1);
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.set(Calendar.DAY_OF_MONTH, 1);
        if (stCalendar.after(now)) {
            throw new MyException("创建薪资月份不能超过当前月份!");
        }

        if (DateUtil.parseShort(et).before(DateUtil.parseShort(st))) {
            throw new MyException("开始时间不能大于结束时间");
        }

        boolean existsEmployee = shpUserShopRefService.existsShpUserShopRef(shopId, userId);
        if (!existsEmployee) {
            log.info("该用户非本店铺员工!shopId: {}, userId: {}", shopId, userId);
            throw new MyException("该用户非本店铺员工!");
        }
        FinSalaryDetail fd = getFinSalaryDetail(shopId, userId, st);
        //是否新增薪资
        boolean isSave = ConstantCommon.ZERO.equals(param.getUpdateOrSave());
        if (isSave) {
            //新增薪资,但又找到此月份的薪资,则报错
            if (!LocalUtils.isEmptyAndNull(fd)) {
                throw new MyException("该月份已创建薪资");
            }
        } else if (LocalUtils.isEmptyAndNull(fd)) {
            //已创建薪资但又找不到此月份薪资,则报错
            throw new MyException("没找到该月份的薪资");
        }

        Map<String, Object> map = calcTotalSalaryMoney(param, result);
        BigDecimal finalMoney = new BigDecimal(map.get("finalMoney").toString());

        //打包数据
        FinSalary finSalary = pickFinSalary(param, finalMoney.longValue());
        FinSalaryDetail fsd = pickFinSalaryDetail(userId, param);

        //发放薪资之后不可更改
        if (ConstantCommon.ONE.equals(param.getSalaryState())) {
            //此用户销售数据结果集
            Object saleResultJson = map.get("saleResultJson");
            fsd.setSaleResultJson(saleResultJson.toString());
        }

        //添加【店铺操作日志】-【发放薪资】/【修改薪资方案】
        String username = shpUserService.getUsernameByUserId(userId);
        Integer salMonth = stCalendar.get(Calendar.MONTH) + 1;
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.SALARY.getName());
        paramAddShpOperateLog.setOperateContent(username+"-"+salMonth+"月工资");
        paramAddShpOperateLog.setProdId(null);
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(request);
        if (isSave) {
            finSalaryService.createSalary(finSalary, fsd);
            paramAddShpOperateLog.setOperateName("创建薪资");
        } else {
            finSalary.setInsertTime(fd.getInsertTime());
            fsd.setInsertTime(fd.getInsertTime());
            fsd.setFkFinSalaryId(fd.getFkFinSalaryId());
            fsd.setId(fd.getId());
            finSalaryService.updateSalary(finSalary, fsd);

            //判断是否是发放薪资
            if (ConstantCommon.ONE.equals(param.getSalaryState())) {
                paramAddShpOperateLog.setOperateName("发放薪资");
            }else{
                paramAddShpOperateLog.setOperateName("修改薪资方案");
            }
        }
        if (ConstantCommon.ONE.equals(param.getSalaryState())) {
            //新增薪资账单记录
            ParamFundRecordAdd paramFundRecordAdd = new ParamFundRecordAdd();
            paramFundRecordAdd.setFkShpShopId(shopId);
            paramFundRecordAdd.setUserId(userId);
            paramFundRecordAdd.setMoney( finalMoney.toString());
            paramFundRecordAdd.setInitPrice("0");
            paramFundRecordAdd.setState("out");
            paramFundRecordAdd.setFundType("40");
            paramFundRecordAdd.setCount("1");
            paramFundRecordAdd.setFinClassifyName("薪资支出");
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

        //各种利润
        Map<String, Object> kindOfProfit = getKindOfProfit(shopId, userId, proAttr, st, et);

        //TA回收的成本总额(分)
        long recycleInitPrice = parseLong(kindOfProfit.get("recycleInitPrice").toString());
        //TA回收的总件数
        long recycleNum = parseLong(kindOfProfit.get("recycleNum").toString());
        //TA回收产生的利润(分)
        long recycleProfit = parseLong(kindOfProfit.get("recycleProfit").toString());
        //服务利润总额(分)
        long serviceProfit = parseLong(kindOfProfit.get("serviceProfit").toString());

        //提成方案明细 格式: 提成模块-方案,订单类型:值,订单类型2:值2;多个用分号隔开
        String[] schemeArray = param.getSchemeType().split(";");
        //提成计划
        Map<String, Map<String, Double>> salaryPlan = getSalaryPlan(salaryDetail);
        //格式: 提成模块-方案,订单类型:值,订单类型2:值2;
        double planResult = 0;
        //目前最多包含3个方案;
        for (String scheme : schemeArray) {
            String[] sh = scheme.split(",");
            String schemeName = sh[0];
            Map<String, Double> planObj = salaryPlan.get(schemeName);
            if (LocalUtils.isEmptyAndNull(planObj)) {
                continue;
            }
            planResult += calcResult(sh, planObj);
        }

        //回收成本百分比(%)
        String recycleInitPricePercent = param.getRecycleInitPricePercent();
        BigDecimal rcyInitPricePerStr = LocalUtils.calcNumber(recycleInitPricePercent, "*", 0.01);
        BigDecimal recyclePercentMoney = LocalUtils.calcNumber(recycleInitPrice, "*", rcyInitPricePerStr);
        //回收件数单价
        String recycleUnitPrice = param.getRecycleUnitPrice();
        //把元转换为分
        BigDecimal recycleUnitPriceStr = LocalUtils.calcNumber(recycleUnitPrice, "*", 100);
        BigDecimal recycleUnitPriceTotalMoney = LocalUtils.calcNumber(recycleNum, "*", recycleUnitPriceStr);

        //TA回收产生的利润(分) 百分比转换成小数
        String recyclePercentStr = param.getRecycleProfitPercent();
        BigDecimal recyclePercent = LocalUtils.calcNumber(recyclePercentStr, "*", 0.01);
        double recycleResult = LocalUtils.calcNumber(recyclePercent, "*", recycleProfit).doubleValue();

        //服务利润总额(分) 百分比转换成小数
        String servicePercentStr = param.getServiceProfitPercent();
        BigDecimal servicePercent = LocalUtils.calcNumber(servicePercentStr, "*", 0.01);
        double serviceResult = LocalUtils.calcNumber(servicePercent, "*", serviceProfit).doubleValue();


        //计算各项总和
        //底薪
        String basicMoney = param.getBasicMoney();
        //其他款项
        String elseMoney = param.getElseMoney();

        //统计了底薪+其他款项
        BigDecimal basicElseMoney = LocalUtils.calcNumber(basicMoney, "+", elseMoney);
        //追加方案提成总额
        BigDecimal basicElsePlanMoney = LocalUtils.calcNumber(basicElseMoney, "+", planResult);
        //追加按回收成本提点总额
        BigDecimal recyclePercentTotalMoney = LocalUtils.calcNumber(recyclePercentMoney, "+", basicElsePlanMoney);
        //追加按回收件数提点总额
        BigDecimal recycleUnitTotalMoney = LocalUtils.calcNumber(recycleUnitPriceTotalMoney, "+", recyclePercentTotalMoney);
        //追加TA回收产生的利润(分)提成
        BigDecimal money4 = LocalUtils.calcNumber(recycleResult, "+", recycleUnitTotalMoney);
        //追加服务提成总额,得出 所有利润总额

        BigDecimal finalMoney = LocalUtils.calcNumber(serviceResult, "+", money4);
        Map<String, Object> map = new HashMap<>(16);
        map.put("finalMoney", finalMoney);
        map.put("saleResultJson", salaryDetail.toJSONString());
        return map;
    }

    /**
     * 封装未入库的FinSalary
     *
     * @param param
     * @param salaryMoney
     * @return
     * @throws Exception
     */
    private FinSalary pickFinSalary(ParamCreateSalaryQuery param, long salaryMoney) throws Exception {
        Date month = DateUtil.parseShort(param.getStartDate());
        String salaryName = DateUtil.format(month, "yyyy年MM月");
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
     * 封装未入库的FinSalaryDetail
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
                //订单类型:值; 例如: 友商订单:5
                String orderTypeStr = sh[i];
                String[] orderAndValue = orderTypeStr.split(":");
                String paramOrderType = orderAndValue[0];
                String paramOrderValueStr = orderAndValue[1];
                double paramOrderValue = Double.parseDouble(paramOrderValueStr);
                double value = planObj.get(paramOrderType) == null ? 0 : planObj.get(paramOrderType);
                v += LocalUtils.calcNumber(paramOrderValue, "*", value).doubleValue();
            }
            //百分比转换成小数
            if ("2-1,2-2".contains(sh[0])) {
                v = LocalUtils.calcNumber(v, "*", 0.01).doubleValue();
            }
            //把元转化为分
            if ("2-3".contains(sh[0])) {
                v = LocalUtils.calcNumber(v, "*", 100).doubleValue();
            }
            System.err.println(sh[0] + "方案子结果: " + v);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    /**
     * 获取某个方案的具体销售额和件数
     *
     * @param salaryDetail
     * @return
     */
    private Map<String, Map<String, Double>> getSalaryPlan(Map<String, Object> salaryDetail) {
        String key = "allPro";
        Map<String, Map<String, Double>> hashMap = new HashMap<>(16);
        //得到了具体某个属性的所有总额情况
        List<Map<String, Object>> jsonArray = getProAttrJsonArray(salaryDetail, key);
        //销售总额
        Map<String, Object> saleTotalMoneyObj = jsonArray.get(0);
        List<Map<String, Double>> salePlanOrderTypeMap = getPlanOrderTypeMap(saleTotalMoneyObj);
        Map<String, Double> saleOrderMoneyMap = salePlanOrderTypeMap.get(0);
        Map<String, Double> saleOrderNumMap = salePlanOrderTypeMap.get(1);
        //2-1销售额提点
        hashMap.put("2-1", saleOrderMoneyMap);
        //2-3销售件数提点
        hashMap.put("2-3", saleOrderNumMap);

        //毛利润总额
        Map<String, Object> grossProfitTotalMoneyObj = jsonArray.get(1);
        List<Map<String, Double>> grossProfitPlanOrderTypeMap = getPlanOrderTypeMap(grossProfitTotalMoneyObj);
        Map<String, Double> grossProfitOrderMoneyMap = grossProfitPlanOrderTypeMap.get(0);
        //2-2毛利润提点
        hashMap.put("2-2", grossProfitOrderMoneyMap);

        ////回收成本总额
        //Map<String, Object> recycleTotalMoneyObj = jsonArray.get(2);
        //List<Map<String, Double>> recyclePlanOrderTypeMap = getPlanOrderTypeMap(recycleTotalMoneyObj);
        //Map<String, Double> recycleOrderMoneyMap = recyclePlanOrderTypeMap.get(0);
        //Map<String, Double> recycleOrderNumMap = recyclePlanOrderTypeMap.get(1);
        ////3-1回收成本提点
        //hashMap.put("3-1", recycleOrderMoneyMap);
        ////3-2回收件数提点
        //hashMap.put("3-2", recycleOrderNumMap);
        return hashMap;
    }

    /**
     * 获取具体某个方案的类型和对应的值
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
     * TA回收的成本总额和件数,TA回收产生的利润,维修保养利润<br/>
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
        //TA回收的成本总额和回收件数
        Map<String, Object> recyclePriceAndNum = proProductService.countRecycleInitPriceAndNum(shopId, userId, proAttr, st, et);
        Object recycleInitPrice = recyclePriceAndNum.get("initPrice");
        Object recycleNum = recyclePriceAndNum.get("num");
        //TA回收产生的利润
        Object recycleProfit = ordOrderService.countRecycleProfit(shopId, userId, proAttr, st, et);
        //维修保养利润
        BigDecimal serviceProfit = shpServiceService.getTotalFinishServiceProfit(shopId, userId, st, et);
        Map<String, Object> hashMap = new HashMap<>(16);
        hashMap.put("recycleInitPrice", recycleInitPrice);
        hashMap.put("recycleNum", recycleNum);
        hashMap.put("recycleProfit", recycleProfit);
        hashMap.put("serviceProfit", serviceProfit);
        return hashMap;
    }

    /**
     * 获取员工的薪资明细表的各种提成统计(页面的上半部分内容),<br/>
     * 该接口, 薪资明细和创建薪资都需要调用
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
            throw new MyException("用户不存在");
        }
        //自由和寄卖商品的销售情况和回收情况
        //获取用户的各订单类型的销售总额、销售件数、毛利润总额
        List<List<VoSalaryOrdType>> saleGrossList = ordOrderService.countSaleAndGrossProfitMoney(param);
        List<Map<String, Object>> list = formatMap(saleGrossList);
        //各种利润
        Map<String, Object> kindOfProfit = getKindOfProfit(shopId, userId, proAttr, st, et);
        //TA回收的成本总额和回收件数
        Object recycleInitPrice = kindOfProfit.get("recycleInitPrice");
        Object recycleNum = kindOfProfit.get("recycleNum");
        Map<String, Object> recycleCostNum = packMoneyAndNum("TA回收的成本总额", recycleInitPrice, recycleNum);
        //TA回收产生的利润
        Object recycleProfit = kindOfProfit.get("recycleProfit");
        Map<String, Object> recycleProfitMap = packMoneyAndNum("TA回收产生的利润", recycleProfit, 0);
        //维修保养利润
        Object serviceProfit = kindOfProfit.get("serviceProfit");
        Map<String, Object> fixProfitMap = packMoneyAndNum("维修保养利润", serviceProfit, 0);
        list.add(recycleCostNum);
        list.add(recycleProfitMap);
        list.add(fixProfitMap);
        Map<String, Object> map = new HashMap<>(16);
        map.put("allPro", list);
        return new JSONObject(map);
    }

    /**
     * 添加其他种类的成本和利润和件数
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
     * 对统计进行分组
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
            String title = "未知";
            switch (i) {
                case 0:
                    title = "销售总额";
                    break;
                case 1:
                    title = "毛利润总额";
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
