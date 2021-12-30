package com.luxuryadmin.service.fin.impl;

import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.fin.FinBill;
import com.luxuryadmin.entity.fin.FinFundRecord;
import com.luxuryadmin.mapper.fin.FinBillDayMapper;
import com.luxuryadmin.mapper.fin.FinBillMapper;
import com.luxuryadmin.mapper.fin.FinFundRecordMapper;
import com.luxuryadmin.param.fin.ParamFundRecordAdd;
import com.luxuryadmin.service.fin.FinFundRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 帐单流水表
 *
 * @author zhangSai
 * @date 2021/04/23 14:19:52
 */
@Slf4j
@Service
public class FinFundRecordServiceImpl implements FinFundRecordService {

    @Resource
    private FinBillDayMapper billDayMapper;
    @Resource
    private FinBillMapper billMapper;
    @Resource
    private FinFundRecordMapper fundRecordMapper;

    @Override
    public void addFinFundRecord(ParamFundRecordAdd fundRecordAdd) {

    }

    @Override
    public void addOtherFundRecord(ParamFundRecordAdd paramFundRecordAdd) {
        List<FinBill> billList = billDayMapper.getBillList(paramFundRecordAdd.getFkShpShopId());
        if (billList != null && billList.size() >0) {
            billList.forEach(bill -> {
                FinFundRecord finFundRecord = new FinFundRecord();
                finFundRecord.setFkFinBillId(bill.getId());
                finFundRecord.setFkShpShopId(paramFundRecordAdd.getFkShpShopId());
                finFundRecord.setFundType(paramFundRecordAdd.getFundType());
                finFundRecord.setWay("30");
                finFundRecord.setInsertTime(new Date());
                finFundRecord.setInsertAdmin(paramFundRecordAdd.getUserId());
                finFundRecord.setFinClassifyName(paramFundRecordAdd.getFinClassifyName());
                finFundRecord.setInsertTime(new Date());
                BigDecimal surplusMoney = new BigDecimal(bill.getMoney());
                BigDecimal moneyNew = new BigDecimal(paramFundRecordAdd.getMoney());
                if (new BigDecimal(paramFundRecordAdd.getMoney()).compareTo(BigDecimal.ZERO) == -1) {
                    moneyNew = new BigDecimal(0).subtract(moneyNew);

                }
                if ("out".equals(paramFundRecordAdd.getState())) {
                    finFundRecord.setState("1");
                    surplusMoney = surplusMoney.subtract(moneyNew);
                }
                if ("in".equals(paramFundRecordAdd.getState())) {
                    finFundRecord.setState("0");
                    surplusMoney = surplusMoney.add(moneyNew);
                }
                bill.setMoney(surplusMoney.longValue());
                //维修服务
                if ("30".equals(paramFundRecordAdd.getFundType())) {
                    finFundRecord.setMoney(moneyNew);

                    fundRecordMapper.saveObject(finFundRecord);
                    billMapper.updateObject(bill);
                }
                //薪资
                if ("40".equals(paramFundRecordAdd.getFundType())) {
                    finFundRecord.setMoney(moneyNew);
                    fundRecordMapper.saveObject(finFundRecord);
                    billMapper.updateObject(bill);
                }
                //其他收支
                if ("50".equals(paramFundRecordAdd.getFundType())) {
                    finFundRecord.setMoney(moneyNew);
                    fundRecordMapper.saveObject(finFundRecord);
                    billMapper.updateObject(bill);
                }
            });
        }
    }

    @Override
    public void addFundRecordForUpdateProduct(ParamFundRecordAdd paramFundRecordAdd, String nowCount) {

        Integer nowNum = Integer.parseInt(nowCount);
        Integer oldNum = Integer.parseInt(paramFundRecordAdd.getCount());
        BigDecimal oldPrice = new BigDecimal(paramFundRecordAdd.getMoney()).multiply(new BigDecimal(oldNum));
        BigDecimal nowMoney = new BigDecimal(paramFundRecordAdd.getInitPrice()).multiply(new BigDecimal(nowNum));
        BigDecimal payMoney = new BigDecimal(0);
        if (oldPrice.compareTo(nowMoney) == 0) {
            return;
        }
        //原价大于现价
        if (oldPrice.compareTo(nowMoney) == 1) {
            payMoney = oldPrice.subtract(nowMoney);
            paramFundRecordAdd.setState("in");
        }
        //现价大于原价
        if (oldPrice.compareTo(nowMoney) == -1) {
            payMoney = nowMoney.subtract(oldPrice);
            paramFundRecordAdd.setState("out");
        }
        String billState = paramFundRecordAdd.getState();
        BigDecimal pay = payMoney;
        List<FinBill> billList = billDayMapper.getBillList(paramFundRecordAdd.getFkShpShopId());
        if (billList != null) {
            billList.forEach(bill -> {
                FinFundRecord finFundRecord = new FinFundRecord();
                finFundRecord.setFkFinBillId(bill.getId());
                finFundRecord.setFkShpShopId(paramFundRecordAdd.getFkShpShopId());
                finFundRecord.setFundType(paramFundRecordAdd.getFundType());
                finFundRecord.setWay("30");
                finFundRecord.setInsertTime(new Date());
                finFundRecord.setInsertAdmin(paramFundRecordAdd.getUserId());
                finFundRecord.setFinClassifyName("修改商品价格/库存");
                finFundRecord.setInsertTime(new Date());
                BigDecimal stayMoney = new BigDecimal(bill.getMoney());
                if (!bill.getTypes().contains(paramFundRecordAdd.getAttributeCode())) {
                    return;
                }
                if ("out".equals(billState)) {
                    finFundRecord.setState("1");
                    //剩余金额 原有金额-进货金额
                    stayMoney = new BigDecimal(bill.getMoney()).subtract(pay);
                }
                if ("in".equals(billState)) {
                    finFundRecord.setState("0");
                    //剩余金额 原有金额+进货修改的金额补差价
                    stayMoney = new BigDecimal(bill.getMoney()).add(pay);
                }
                //入库商品
//                if ("10".equals(fundType)){

                finFundRecord.setMoney(pay);
                bill.setMoney(stayMoney.longValue());
                fundRecordMapper.saveObject(finFundRecord);
                billMapper.updateObject(bill);
//                }

            });
        }
    }

    @Override
    public void addOrderFundRecord(ParamFundRecordAdd paramFundRecordAdd, String fkOrdOrderId) {
        List<FinBill> billList = billDayMapper.getBillList(paramFundRecordAdd.getFkShpShopId());
        if (billList != null && billList.size() >0) {
            billList.forEach(bill -> {

                FinFundRecord finFundRecord = new FinFundRecord();
                finFundRecord.setFkFinBillId(bill.getId());
                finFundRecord.setFkShpShopId(paramFundRecordAdd.getFkShpShopId());
                finFundRecord.setFundType(paramFundRecordAdd.getFundType());
                finFundRecord.setWay("30");
                finFundRecord.setInsertTime(new Date());
                finFundRecord.setInsertAdmin(paramFundRecordAdd.getUserId());
                finFundRecord.setFinClassifyName(paramFundRecordAdd.getFinClassifyName());
                BigDecimal surplusMoney = new BigDecimal(bill.getMoney());
                if (!bill.getTypes().contains(paramFundRecordAdd.getAttributeCode())) {
                    return;
                }
                if ("out".equals(paramFundRecordAdd.getState())) {
                    finFundRecord.setState("1");
                    BigDecimal moneyNew = new BigDecimal(paramFundRecordAdd.getMoney());
                    if (new BigDecimal(paramFundRecordAdd.getMoney()).compareTo(BigDecimal.ZERO) == -1) {
                        moneyNew = new BigDecimal(0).subtract(new BigDecimal(paramFundRecordAdd.getMoney()));
//                        money = moneyNew.toString();
                    }
                    surplusMoney = surplusMoney.subtract(moneyNew);
                }
                if ("in".equals(paramFundRecordAdd.getState())) {
                    finFundRecord.setState("0");
                    surplusMoney = surplusMoney.add(new BigDecimal(paramFundRecordAdd.getMoney()));
                }
                bill.setMoney(surplusMoney.longValue());
                //商品销售
                if ("20".equals(paramFundRecordAdd.getFundType())) {
                    if (new BigDecimal(paramFundRecordAdd.getMoney()).compareTo(BigDecimal.ZERO) == -1) {
                        BigDecimal moneyNew = new BigDecimal(0).subtract(new BigDecimal(paramFundRecordAdd.getMoney()));
//                        money = moneyNew.toString();
                        log.info("价格为" + moneyNew);
                        finFundRecord.setMoney(moneyNew);
                    } else {
                        finFundRecord.setMoney(new BigDecimal(paramFundRecordAdd.getMoney()));
                    }
                    if (!LocalUtils.isEmptyAndNull(paramFundRecordAdd.getFinClassifyName()) && !"修改订单".equals(paramFundRecordAdd.getFinClassifyName())) {
                        finFundRecord.setInitPrice(new BigDecimal(paramFundRecordAdd.getInitPrice()));
                    } else {
                        finFundRecord.setInitPrice(new BigDecimal(0));
                    }
                    finFundRecord.setFkOrdOrderId(Integer.parseInt(fkOrdOrderId));
                    fundRecordMapper.saveObject(finFundRecord);
                    billMapper.updateObject(bill);
                }

            });
        }
    }

    @Override
    public void addProductFundRecord(ParamFundRecordAdd paramFundRecordAdd) {
        List<FinBill> billList = billDayMapper.getBillList(paramFundRecordAdd.getFkShpShopId());
        if (billList != null && billList.size() >0) {
            billList.forEach(bill -> {
                FinFundRecord finFundRecord = new FinFundRecord();
                finFundRecord.setFkFinBillId(bill.getId());
                finFundRecord.setFkShpShopId(paramFundRecordAdd.getFkShpShopId());
                finFundRecord.setFundType(paramFundRecordAdd.getFundType());
                finFundRecord.setWay("30");
                finFundRecord.setInsertTime(new Date());
                finFundRecord.setInsertAdmin(paramFundRecordAdd.getUserId());
                finFundRecord.setFinClassifyName(paramFundRecordAdd.getFinClassifyName());
                if ("out".equals(paramFundRecordAdd.getState())) {
                    finFundRecord.setState("1");
                }
                if ("in".equals(paramFundRecordAdd.getState())) {
                    finFundRecord.setState("0");
                }

                if (!bill.getTypes().contains(paramFundRecordAdd.getAttributeCode()) && !"remove".equals(paramFundRecordAdd.getAttributeCode())) {
                    return;
                }

                //入库商品
                if ("10".equals(paramFundRecordAdd.getFundType())) {
                    BigDecimal allMoney = new BigDecimal(paramFundRecordAdd.getMoney()).multiply(new BigDecimal(paramFundRecordAdd.getCount()));
                    finFundRecord.setMoney(allMoney);
                    //剩余金额 原有金额-进货金额
                    BigDecimal stayMoney = new BigDecimal(0);
                    if ("1".equals(finFundRecord.getState())) {
                        stayMoney = new BigDecimal(bill.getMoney()).subtract(allMoney);
                    }
                    if ("0".equals(finFundRecord.getState())) {
                        stayMoney = new BigDecimal(bill.getMoney()).add(allMoney);
                    }
                    bill.setMoney(stayMoney.longValue());
                    fundRecordMapper.saveObject(finFundRecord);
                    billMapper.updateObject(bill);
                }

                //质押商品
                if ("60".equals(paramFundRecordAdd.getFundType())) {
                    BigDecimal moneyNow = new BigDecimal(paramFundRecordAdd.getMoney()).multiply(new BigDecimal(100));
                    finFundRecord.setMoney(moneyNow);
                    finFundRecord.setState("0");
                    BigDecimal stayMoney = new BigDecimal(bill.getMoney()).add(moneyNow);
                    bill.setMoney(stayMoney.longValue());
                    finFundRecord.setInitPrice(new BigDecimal(paramFundRecordAdd.getInitPrice()));
                    fundRecordMapper.saveObject(finFundRecord);
                    billMapper.updateObject(bill);
                }
            });
        }
    }


}
