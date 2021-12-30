package com.luxuryadmin.service.fin.impl;

import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.entity.fin.FinBill;
import com.luxuryadmin.entity.fin.FinBillDay;
import com.luxuryadmin.mapper.fin.FinBillDayMapper;
import com.luxuryadmin.mapper.fin.FinBillMapper;
import com.luxuryadmin.service.fin.FinBillDayService;
import com.luxuryadmin.service.fin.FinBillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 帐单表
 *
 * @author zhangSai
 * @date   2021/04/23 14:19:52
 */
@Slf4j
@Service
public class FinBillDayServiceImpl implements FinBillDayService {

    @Resource
    private FinBillMapper billMapper;

    @Resource
    private FinBillDayMapper billDayMapper;

    @Autowired
    protected FinBillService billService;
    @Override
    public void addFinBillDay() {
        List<FinBill> bills = this.getBillList();
        if (bills == null){
            return;
        }
        bills.forEach(bill ->{
            FinBillDay billDay = new FinBillDay();
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            String endTime  = DateUtil.format(calendar.getTime());
            int day1 = calendar.get(Calendar.DATE);
            calendar.set(Calendar.DATE, day1 - 1);
            String startTime = DateUtil.format(calendar.getTime());
            log.info("开始时间"+startTime);
            //总金额
            //商品初始总金额 = 商品利润+服务利润
            BigDecimal totalMoney = new BigDecimal(bill.getTotalMoney());
            //商品利润
            BigDecimal productMoney = billMapper.getMakeMoney(bill.getFkShpShopId(),startTime,endTime,"0","20",bill.getId());
            if (productMoney == null){
                productMoney = new BigDecimal(0);
            }
            BigDecimal productPayMoney = billMapper.getMakeMoney(bill.getFkShpShopId(), startTime, endTime, "1", "20", bill.getId());
            if (productPayMoney == null) {
                productPayMoney = new BigDecimal(0);
            }
            BigDecimal productZhiYatMoney = billMapper.getMakeMoney(bill.getFkShpShopId(), startTime, endTime, "0", "60", bill.getId());
            if (productZhiYatMoney == null) {
                productZhiYatMoney = new BigDecimal(0);
            }
            productMoney = productMoney.subtract(productPayMoney).add(productZhiYatMoney);
            //服务利润
//            BigDecimal serviceMoney = billMapper.getMakeMoney(bill.getFkShpShopId(),startTime,endTime,"0","40",bill.getId());
//            if (serviceMoney == null){
//                serviceMoney = new BigDecimal(0);
//            }
            //收入
            BigDecimal serviceIncomeMoney = billMapper.getMoneyForType(bill.getFkShpShopId(), startTime, endTime, "0", "30", bill.getId());
            if (serviceIncomeMoney == null) {
                serviceIncomeMoney = new BigDecimal(0);
            }
            //支出
            BigDecimal servicePayMoney = billMapper.getMoneyForType(bill.getFkShpShopId(), startTime, endTime, "1", "30", bill.getId());
            if (servicePayMoney == null) {
                servicePayMoney = new BigDecimal(0);
            }
            BigDecimal serviceMoney = serviceIncomeMoney.subtract(servicePayMoney);

            //记一笔(收入)其它收支
//        BigDecimal otherMoney = billMapper.getMoneyForType(billDaySearch.getShopId(),startTime,endTime,null,"40");
            //收入
            BigDecimal incomeOtherMoney = billMapper.getMoneyForType(bill.getFkShpShopId(), startTime,endTime,"0","50",bill.getId());
            if (incomeOtherMoney == null){
                incomeOtherMoney = new BigDecimal(0);
            }
            //支出
            BigDecimal payOtherMoney = billMapper.getMoneyForType(bill.getFkShpShopId(), startTime,endTime,"1","50",bill.getId());
            if (payOtherMoney == null){
                payOtherMoney = new BigDecimal(0);
            }
            BigDecimal otherMoney = incomeOtherMoney.subtract(payOtherMoney);
//            if (otherMoney.compareTo(BigDecimal.ZERO) <0){
//                otherMoney = new BigDecimal(0);
//            }
            //总金额
            BigDecimal totalAllMoney = totalMoney.add(productMoney).add(serviceMoney);
            //现有现金
            BigDecimal cashMoney = new BigDecimal(bill.getMoney());

            //商品成本
            //商品当前剩余成本
            BigDecimal productAllInitPrice = billService.getProductMoneyForTypeByApp(bill.getTypes(), bill.getFkShpShopId());
            if (productAllInitPrice == null){
                productAllInitPrice = new BigDecimal(0);
            }

            //销售利润
            BigDecimal profitMoney = productMoney;

            //薪资支出
            BigDecimal salaryMoney = billMapper.getMoneyForType(bill.getFkShpShopId(), startTime,endTime,"1","40",bill.getId());
            if (salaryMoney == null){
                salaryMoney = new BigDecimal(0);
            }
            //其他收入
            BigDecimal otherNowMoney = otherMoney;
            //维修成本
            BigDecimal serviceNowMoney = serviceMoney;

            billDay.setFkFinBillId(bill.getId());
            billDay.setFkShpShopId(bill.getFkShpShopId());
            billDay.setTotalMoney(totalAllMoney);
            billDay.setCashMoney(cashMoney);
            billDay.setProductMoney(productAllInitPrice);
            billDay.setProfitMoney(profitMoney);
            billDay.setSalaryMoney(salaryMoney);
            billDay.setOtherMoney(otherNowMoney);
            billDay.setServiceMoney(serviceNowMoney);
            billDay.setBizTime(calendar.getTime());
            billDay.setInsertTime(new Date());
            billDayMapper.saveObject(billDay);
        });
    }

    public List<FinBill> getBillList(){
        List<FinBill> bills = billDayMapper.getBillList(null);
        if (bills == null || bills.size()<=0){
            return null;
        }
        return bills;
    }
}
