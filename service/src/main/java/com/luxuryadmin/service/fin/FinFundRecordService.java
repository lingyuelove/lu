package com.luxuryadmin.service.fin;

import com.luxuryadmin.param.fin.ParamFundRecordAdd;

import java.math.BigDecimal;

/**
 * 帐单流水表
 *
 * @author zhangSai
 * @date 2021/04/23 14:19:52
 */
public interface FinFundRecordService {

    /**
     * 新增账单流水
     *
     * @param fundRecordAdd
     */
    void addFinFundRecord(ParamFundRecordAdd fundRecordAdd);

    /**
     * /记一笔财务流水/维修收支/薪资支出
     * @param paramFundRecordAdd
     */
    void addOtherFundRecord(ParamFundRecordAdd paramFundRecordAdd);

    /**
     * 修改商品价格新增记录
//     * @param fkShpShopId
//     * @param userId
//     * @param oldMoney
//     * @param nowPrice
//     * @param state
//     * @param fundType
//     * @param oldCount
     * @param nowCount
     */
    void addFundRecordForUpdateProduct(ParamFundRecordAdd paramFundRecordAdd,String nowCount);

    /**
     * 新增订单/退单/修改订单
     * @param paramFundRecordAdd
     * @param fkOrdOrderId
     */
    void addOrderFundRecord( ParamFundRecordAdd paramFundRecordAdd,String fkOrdOrderId);



    /**
     * 入库商品/质押赎回商品/删除商品
//     * @param fkShpShopId
//     * @param userId
//     * @param money
//     * @param initPrice
//     * @param state
//     * @param fundType
//     * @param count
//     * @param finClassifyName
//     * @param attributeCode 商品属性;10:自有商品; 20:寄卖商品; 30:质押(典当)商品; 40:其他商品
     */
    void addProductFundRecord(ParamFundRecordAdd paramFundRecordAdd);

}
