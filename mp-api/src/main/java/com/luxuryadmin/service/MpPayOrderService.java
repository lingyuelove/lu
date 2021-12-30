package com.luxuryadmin.service;


import com.luxuryadmin.entity.MpPayOrder;

/**
 * 支付订单 service
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:01
 */
public interface MpPayOrderService {

    /**
     * 创建鲍杨小程序购买会员订单
     *
     * @return
     */
    String createOrder();

    /**
     * 根据订单编号获取用户订单信息
     *
     * @param out_trade_no
     * @return
     */
    MpPayOrder getOrderInfoByOrderNo(String out_trade_no);

    /**
     * 修改订单信息
     *
     * @param mpPayOrder
     */
    void updateOrderInfo(MpPayOrder mpPayOrder);
}
