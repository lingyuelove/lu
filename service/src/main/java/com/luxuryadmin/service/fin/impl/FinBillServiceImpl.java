package com.luxuryadmin.service.fin.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.entity.fin.FinBill;
import com.luxuryadmin.mapper.fin.FinBillDayMapper;
import com.luxuryadmin.mapper.fin.FinBillMapper;
import com.luxuryadmin.mapper.fin.FinFundRecordMapper;
import com.luxuryadmin.param.fin.*;
import com.luxuryadmin.param.fin.ParamBillSearch;
import com.luxuryadmin.service.fin.FinBillService;
import com.luxuryadmin.service.fin.FinFundRecordService;
import com.luxuryadmin.vo.fin.*;
import com.luxuryadmin.vo.org.VoOrganizationPageByApp;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * 帐单表
 *
 * @author zhangSai
 * @date 2021/04/23 14:19:52
 */
@Slf4j
@Service
public class FinBillServiceImpl implements FinBillService {

    @Autowired
    protected RedisUtil redisUtil;

    @Resource
    private FinBillMapper billMapper;

    @Resource
    private FinBillDayMapper billDayMapper;

    @Autowired
    private FinFundRecordService fundRecordService;

    @Autowired
    protected ServicesUtil servicesUtil;

    @Override
    public Integer addBill(ParamBillCreate billCreate) {
        //判断vid
        String finBillKey = ConstantRedisKey.getCreateFinBillKey(billCreate.getShopId(), billCreate.getUserId());
        servicesUtil.validVid(finBillKey, billCreate.getVid());
        getProductMoneyByApp(billCreate.getTypes(), billCreate.getShopId(), billCreate.getTotalMoney(), billCreate.getMoney());
        FinBill bill = new FinBill();
        Date date = new Date();
        String name = DateUtil.format(date, DateUtil.YYYY_MM_DD);
        bill.setName(name + " 对账单");
        BeanUtils.copyProperties(billCreate, bill);
        bill.setFkShpShopId(billCreate.getShopId());
        bill.setInsertAdmin(billCreate.getUserId());
        bill.setInsertTime(new Date());
        bill.setState("10");
        bill.setMoney(Long.parseLong(billCreate.getMoney()));
        bill.setTotalMoney(Long.parseLong(billCreate.getTotalMoney()));
        bill.setOldMoney(Long.parseLong(billCreate.getMoney()));
        billMapper.saveObject(bill);
        redisUtil.delete(finBillKey);
        return bill.getId();
    }

    @Override
    public Integer updateBill(ParamBillUpdate billUpdate) {
        FinBill bill = new FinBill();
        getProductMoneyByApp(billUpdate.getTypes(), billUpdate.getShopId(), billUpdate.getTotalMoney(), billUpdate.getMoney());
        BeanUtils.copyProperties(billUpdate, bill);
        bill.setUpdateTime(new Date());
        bill.setUpdateAdmin(billUpdate.getUserId());
        bill.setId(billUpdate.getBillId());
        bill.setMoney(Long.parseLong(billUpdate.getMoney()));
        bill.setTotalMoney(Long.parseLong(billUpdate.getTotalMoney()));
        return billMapper.updateObject(bill);
    }

    @Override
    public Integer deleteBill(ParamBillUpdate billUpdate) {
        FinBill bill = new FinBill();
        BeanUtils.copyProperties(billUpdate, bill);
        bill.setUpdateAdmin(billUpdate.getUserId());
        bill.setUpdateTime(new Date());
        bill.setId(billUpdate.getBillId());
        bill.setState("-99");
        return billMapper.updateObject(bill);
    }

    @Override
    public VoBillByApp getBillPageByApp(ParamBillSearch bilSearch) {
        VoBillByApp voBillByApp = new VoBillByApp();
        PageHelper.startPage(bilSearch.getPageNum(), bilSearch.getPageSize());
        List<VoBillPageByApp> billPageByApps = billMapper.getBillPageByApp(bilSearch.getShopId(), bilSearch.getState());
        PageInfo<VoBillPageByApp> pageInfo = new PageInfo(billPageByApps);
        voBillByApp.setPageNum(pageInfo.getPageNum());
        voBillByApp.setPageSize(pageInfo.getPageSize());
        if (billPageByApps == null || billPageByApps.size() <= 0) {
            voBillByApp.setHasNextPage(false);
            return voBillByApp;
        }
        billPageByApps.forEach(voBillPageByApp -> {
            List<String> typeList = Arrays.asList(voBillPageByApp.getTypes().split(","));
            List<String> attributeShortCnList = new ArrayList<>();
            if (typeList != null && typeList.size() > 0) {
                typeList.forEach(s -> {
                    attributeShortCnList.add(servicesUtil.getAttributeCn(s, true));
                });
                String tagNameList = StringUtils.join(attributeShortCnList, "/");
                voBillPageByApp.setAttributeShortCn(tagNameList);
            }

        });
        voBillByApp.setList(billPageByApps);
        if (pageInfo.getNextPage() > 0) {
            voBillByApp.setHasNextPage(true);
        } else {
            voBillByApp.setHasNextPage(false);
        }
        return voBillByApp;
    }

    @Override
    public VoBillDetailByApp getBillDetailByApp(ParamBillDaySearch billDaySearch) {

        VoBillDetailByApp billDetailByApp = new VoBillDetailByApp();
        FinBill bill = (FinBill) billMapper.getObjectById(billDaySearch.getBillId());
        if (bill == null) {
            throw new MyException("暂无此账单 ");
        }
        String endNewDate = null;
        String startDate = null;
        Date dateEnd =new Date();
        try {
             dateEnd = DateUtil.parseDefaultEt(billDaySearch.getEndDate());
            Date dateStart = DateUtil.parseDefaultEt(billDaySearch.getStartDate());
            endNewDate = DateUtil.format(dateEnd);
            startDate = DateUtil.format(dateStart);
        } catch (ParseException e) {
            //账单详情时间转换错误
            log.info("账单详情时间转换错误：" + e);
            throw new MyException("账单详情时间转换错误 " + e);
        }
        //判断此账单搜索日期和创建日期对比 是否搜索结束日期是否小于创建日期
        if (bill.getInsertTime().after(dateEnd)){
            billDetailByApp.setTotalAllMoney(new BigDecimal(0));
            billDetailByApp.setProductAllInitPrice(new BigDecimal(0));
            billDetailByApp.setSurplusAllMoney(new BigDecimal(0));
            return billDetailByApp;
        }
//        try {
//            dateStart = DateUtil.parse(billDaySearch.getStartDate());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        if (bill.getInsertTime().after(dateStart)){
//            return billDetailByApp;
//        }
        //商品初始总金额 = 商品利润+服务利润+投资金额+记一笔
        BigDecimal totalMoney = new BigDecimal(bill.getTotalMoney());
        //商品利润
        BigDecimal productMoney = billMapper.getMakeMoney(billDaySearch.getShopId(), startDate, endNewDate, "0", "20", billDaySearch.getBillId());
        if (productMoney == null) {
            productMoney = new BigDecimal(0);
        }
        BigDecimal productPayMoney = billMapper.getMakeMoney(billDaySearch.getShopId(), startDate, endNewDate, "1", "20", billDaySearch.getBillId());
        if (productPayMoney == null) {
            productPayMoney = new BigDecimal(0);
        }
        BigDecimal productZhiYatMoney = billMapper.getMakeMoney(billDaySearch.getShopId(), startDate, endNewDate, "0", "60", billDaySearch.getBillId());
        if (productZhiYatMoney == null) {
            productZhiYatMoney = new BigDecimal(0);
        }
        productMoney = productMoney.subtract(productPayMoney).add(productZhiYatMoney);
        //服务利润
        //服务利润:收入
        BigDecimal serviceIncomeOtherMoney = billMapper.getMoneyForType(bill.getFkShpShopId(), startDate, endNewDate, "0", "30", billDaySearch.getBillId());
        if (serviceIncomeOtherMoney == null) {
            serviceIncomeOtherMoney = new BigDecimal(0);
        }
        //服务利润:支出
        BigDecimal servicePayOtherMoney = billMapper.getMoneyForType(bill.getFkShpShopId(), startDate, endNewDate, "1", "30", billDaySearch.getBillId());
        if (servicePayOtherMoney == null) {
            servicePayOtherMoney = new BigDecimal(0);
        }
        BigDecimal serviceMoney = serviceIncomeOtherMoney.subtract(servicePayOtherMoney);
        //记一笔金额
        //记一笔收入
        BigDecimal incomeOtherMoney = billMapper.getMoneyForType(bill.getFkShpShopId(), startDate,endNewDate,"0","50",billDaySearch.getBillId());
        if (incomeOtherMoney == null){
            incomeOtherMoney = new BigDecimal(0);
        }
        //记一笔支出
        BigDecimal payOtherMoney = billMapper.getMoneyForType(bill.getFkShpShopId(), startDate,endNewDate,"1","50",billDaySearch.getBillId());
        if (payOtherMoney == null){
            payOtherMoney = new BigDecimal(0);
        }
        BigDecimal otherMoney = incomeOtherMoney.subtract(payOtherMoney);

        //总金额
        BigDecimal totalAllMoney = totalMoney.add(productMoney).add(serviceMoney).add(otherMoney);
        if (totalAllMoney == null) {
            totalAllMoney = new BigDecimal(0);
        }
        if (totalAllMoney.compareTo(BigDecimal.ZERO) < 0) {
            totalAllMoney = new BigDecimal(0);
        }
        billDetailByApp.setTotalAllMoney(totalAllMoney);

        //商品剩余成本
        BigDecimal productAllInitPrice = this.getProductMoneyForTypeByApp(bill.getTypes(), bill.getFkShpShopId());
        if (productAllInitPrice == null) {
            productAllInitPrice = new BigDecimal(0);
        }
        billDetailByApp.setProductAllInitPrice(productAllInitPrice);

        //应有资金的计算  创建时输入的现金-薪资支出+其它收支+服务利润+销售额-新入库的商品总成本+质押收支
        //初始资金
        BigDecimal initialMoney = new BigDecimal(bill.getMoney());
        if (initialMoney == null) {
            initialMoney = new BigDecimal(0);
        }

        //应有资金的计算
        BigDecimal surplusAllMoney = initialMoney;
        if (surplusAllMoney.compareTo(BigDecimal.ZERO) < 0) {
            surplusAllMoney = new BigDecimal(0);
        }
        billDetailByApp.setSurplusAllMoney(surplusAllMoney);

        this.getBillDayPageByApps(billDetailByApp, billDaySearch);
        billDetailByApp.setBillId(billDaySearch.getBillId());
        return billDetailByApp;
    }

    @Override
    public VoBillProductByApp getBillProductMoneyByApp(Integer shopId, Integer userId) {
        List<VoBillProductMoneyByApp> list = new ArrayList<>();
        //自有
        VoBillProductMoneyByApp billOwnProductMoneyByApp = new VoBillProductMoneyByApp();
        BigDecimal billOwnProductMoney = billMapper.getProductPrice(shopId, "10");
        if (billOwnProductMoney != null) {
            billOwnProductMoneyByApp.setProductMoney(billOwnProductMoney);
        } else {
            billOwnProductMoneyByApp.setProductMoney(new BigDecimal(0));
        }
        billOwnProductMoneyByApp.setAttributeCode("10");
        list.add(billOwnProductMoneyByApp);
        //其他
        VoBillProductMoneyByApp billOtherProductMoneyByApp = new VoBillProductMoneyByApp();
        BigDecimal billOtherProductMoney = billMapper.getProductPrice(shopId, "40");
        if (billOtherProductMoney != null) {
            billOtherProductMoneyByApp.setProductMoney(billOtherProductMoney);
        } else {
            billOtherProductMoneyByApp.setProductMoney(new BigDecimal(0));
        }

        billOtherProductMoneyByApp.setAttributeCode("40");
        list.add(billOtherProductMoneyByApp);

        //质押
        VoBillProductMoneyByApp billProductMoneyByApp = new VoBillProductMoneyByApp();
        BigDecimal billProductMoney = billMapper.getProductPrice(shopId, "30");
        if (billProductMoney != null) {
            billProductMoneyByApp.setProductMoney(billProductMoney);
        } else {
            billProductMoneyByApp.setProductMoney(new BigDecimal(0));
        }

        billProductMoneyByApp.setAttributeCode("30");
        list.add(billProductMoneyByApp);

        VoBillProductByApp voBillProductByApp = new VoBillProductByApp();
        String vid = LocalUtils.getUUID();
        String proTempKey = ConstantRedisKey.getCreateFinBillKey(shopId, userId);
        redisUtil.setExMINUTES(proTempKey, vid, 120);
        voBillProductByApp.setVid(vid);
        voBillProductByApp.setList(list);
        return voBillProductByApp;
    }

    public BigDecimal getProductMoneyByApp(String types, Integer shopId, String totalMoney, String money) {
        //计算价格
        List<String> typeList = Arrays.asList(types.split(","));
        if (typeList == null || typeList.size() <= 0) {
            throw new MyException("请选择对账类型!  ");
        }
        List<VoBillProductMoneyByApp> allMoney = new ArrayList<>();
        typeList.forEach(s -> {
            BigDecimal productMoney = billMapper.getProductPrice(shopId, s);
            VoBillProductMoneyByApp voBillProductMoneyByApp = new VoBillProductMoneyByApp();
            voBillProductMoneyByApp.setProductMoney(productMoney);
            voBillProductMoneyByApp.setAttributeCode(s);
            if (productMoney != null) {
                allMoney.add(voBillProductMoneyByApp);
            }

        });
        BigDecimal productMoney = new BigDecimal(0);
        if (allMoney != null && allMoney.size() > 0) {
            productMoney = allMoney.stream()
                    // 将CourseCost对象的closureMoney取出来map为Bigdecimal
                    .map(VoBillProductMoneyByApp::getProductMoney)
                    // 使用reduce()聚合函数,实现累加器
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        BigDecimal nowMoney = new BigDecimal(money);
        BigDecimal totalNowMoney = productMoney.add(nowMoney);
        if (totalNowMoney.compareTo(new BigDecimal(totalMoney)) != 0) {
            throw new MyException("对账价格不相符");
        }

        return totalNowMoney;
    }

    @Override
    public BigDecimal getProductMoneyForTypeByApp(String types, Integer shopId) {
        //计算价格
        List<String> typeList = Arrays.asList(types.split(","));
        if (typeList == null || typeList.size() <= 0) {
            return new BigDecimal(0);
        }
        List<VoBillProductMoneyByApp> allMoney = new ArrayList<>();
        typeList.forEach(s -> {
            BigDecimal productMoney = billMapper.getProductPrice(shopId, s);
            VoBillProductMoneyByApp voBillProductMoneyByApp = new VoBillProductMoneyByApp();
            voBillProductMoneyByApp.setProductMoney(productMoney);
            voBillProductMoneyByApp.setAttributeCode(s);
            if (productMoney != null) {
                allMoney.add(voBillProductMoneyByApp);
            }

        });
        BigDecimal productMoney = new BigDecimal(0);
        if (allMoney != null && allMoney.size() > 0) {
            productMoney = allMoney.stream()
                    // 将CourseCost对象的closureMoney取出来map为Bigdecimal
                    .map(VoBillProductMoneyByApp::getProductMoney)
                    // 使用reduce()聚合函数,实现累加器
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        return productMoney;
    }


    public VoBillDetailByApp getBillDayPageByApps(VoBillDetailByApp billDetailByApp, ParamBillDaySearch billDaySearch) {

        List<VoBillDayPageByApp> voBillDayPageByApps = new ArrayList<>();
        Date dateEnd = new Date();
        Date dateStart =new Date();
        try {
             dateEnd = DateUtil.parseDefaultEt(billDaySearch.getEndDate());
             dateStart = DateUtil.parseDefaultEt(billDaySearch.getStartDate());
        } catch (ParseException e) {
            //账单详情时间转换错误
            log.info("账单详情时间转换错误：" + e);
            throw new MyException("账单详情时间转换错误 " + e);
        }
        if (billDaySearch.getPageNum() == 1 && (dateStart).before(new Date()) && (dateEnd).after(new Date())) {
            VoBillDayPageByApp voBillDayPageByApp = new VoBillDayPageByApp();
            billDaySearch.setPageSize(billDaySearch.getPageSize() - 1);
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            String startDate = DateUtil.format(calendar.getTime());
            int day1 = calendar.get(Calendar.DATE);
            calendar.set(Calendar.DATE, day1 + 1);
            String endDate = DateUtil.format(calendar.getTime());
            //销售利润
            BigDecimal profitMoney = billMapper.getMakeMoney(billDaySearch.getShopId(), startDate, endDate, "0", "20", billDaySearch.getBillId());
            if (profitMoney == null) {
                profitMoney = new BigDecimal(0);
            }
            BigDecimal productPayMoney = billMapper.getMakeMoney(billDaySearch.getShopId(), startDate, endDate, "1", "20", billDaySearch.getBillId());
            if (productPayMoney == null) {
                productPayMoney = new BigDecimal(0);
            }
            BigDecimal productZhiYatMoney = billMapper.getMakeMoney(billDaySearch.getShopId(), startDate, endDate, "0", "60", billDaySearch.getBillId());
            if (productZhiYatMoney == null) {
                productZhiYatMoney = new BigDecimal(0);
            }
            profitMoney = profitMoney.subtract(productPayMoney).add(productZhiYatMoney);
//            if (profitMoney.compareTo(BigDecimal.ZERO) < 0) {
//                profitMoney = new BigDecimal(0);
//            }
            voBillDayPageByApp.setProfitMoney(profitMoney);
            //薪资支出
            BigDecimal salaryMoney = billMapper.getMoneyForType(billDaySearch.getShopId(), startDate, endDate, "1", "40", billDaySearch.getBillId());
            if (salaryMoney == null) {
                salaryMoney = new BigDecimal(0);
            }
            salaryMoney = new BigDecimal(0).subtract(salaryMoney);
            voBillDayPageByApp.setSalaryMoney(salaryMoney);
            //其他收入
            //收入
            BigDecimal incomeOtherMoney = billMapper.getMoneyForType(billDaySearch.getShopId(), startDate, endDate, "0", "50", billDaySearch.getBillId());
            if (incomeOtherMoney == null) {
                incomeOtherMoney = new BigDecimal(0);
            }
            //支出
            BigDecimal payOtherMoney = billMapper.getMoneyForType(billDaySearch.getShopId(), startDate, endDate, "1", "50", billDaySearch.getBillId());
            if (payOtherMoney == null) {
                payOtherMoney = new BigDecimal(0);
            }
            BigDecimal otherMoney = incomeOtherMoney.subtract(payOtherMoney);
//            if (otherMoney.compareTo(BigDecimal.ZERO) < 0) {
//                otherMoney = new BigDecimal(0);
//            }
            voBillDayPageByApp.setOtherMoney(otherMoney);
            //服务利润（维修收支）
//        BigDecimal serviceMoney = billMapper.getMoneyForType(shopId, DateUtil.format(calendar.getTime()),DateUtil.format(date),"0","30");
//        if (serviceMoney == null){
//            serviceMoney = new BigDecimal(0);
//        }
            //收入
            BigDecimal serviceIncomeMoney = billMapper.getMoneyForType(billDaySearch.getShopId(), startDate, endDate, "0", "30", billDaySearch.getBillId());
            if (serviceIncomeMoney == null) {
                serviceIncomeMoney = new BigDecimal(0);
            }
            //支出
            BigDecimal servicePayMoney = billMapper.getMoneyForType(billDaySearch.getShopId(), startDate, endDate, "1", "30", billDaySearch.getBillId());
            if (servicePayMoney == null) {
                servicePayMoney = new BigDecimal(0);
            }
            BigDecimal serviceMoney = serviceIncomeMoney.subtract(servicePayMoney);

            if (serviceMoney.compareTo(BigDecimal.ZERO) < 0) {
                serviceMoney = new BigDecimal(0);
            }
            voBillDayPageByApp.setServiceMoney(serviceMoney);
            voBillDayPageByApp.setBizTime("今天");
            voBillDayPageByApps.add(voBillDayPageByApp);
        }
        PageHelper.startPage(billDaySearch.getPageNum(), billDaySearch.getPageSize());
        List<VoBillDayPageByApp> billDayPageByAppsOld = billDayMapper.getBillDayPageByApps(billDaySearch.getShopId(),billDaySearch.getBillId(), billDaySearch.getStartDate(), billDaySearch.getEndDate());
        if (billDayPageByAppsOld != null && billDayPageByAppsOld.size()>0){
            billDayPageByAppsOld.forEach(billDayPageByApp ->{
                try {
                    Date bizTime =DateUtil.parseDefaultSt(billDayPageByApp.getBizTime());
                    String bizDate= DateUtil.formatMonthShort(bizTime);
                    billDayPageByApp.setBizTime(bizDate);
                } catch (ParseException e) {
                    throw new MyException("账单详情时间转换错误"+e);
                }

            });
        }
        PageInfo<VoBillPageByApp> pageInfo = new PageInfo(billDayPageByAppsOld);
        billDetailByApp.setPageNum(pageInfo.getPageNum());
        billDetailByApp.setPageSize(pageInfo.getPageSize());
        voBillDayPageByApps.addAll(billDayPageByAppsOld);
        billDetailByApp.setBillDayPageByApps(voBillDayPageByApps);
        return billDetailByApp;
    }
}


