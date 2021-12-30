package com.luxuryadmin.service.pay;

import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.entity.pay.PayOrder;
import com.luxuryadmin.entity.shp.ShpShop;
import com.luxuryadmin.param.mem.ParamMemShopOrder;
import com.luxuryadmin.param.pay.ParamAddPayOrderForAdmin;
import com.luxuryadmin.vo.mem.VoMemShopOrder;
import com.luxuryadmin.vo.pay.VoPayOrderShopForAdmin;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * @author monkey king
 * @date 2021-03-21 18:14:53
 */
public interface PayOrderService {


    /**
     * 创建未支付订单
     *
     * @param shopId
     * @param userId
     * @param goodsName
     * @param orderType
     * @param totalMoney
     * @param discountMoney
     * @param realMoney
     * @param payChannel
     * @param payPlatform
     * @param tradeType
     * @param createType
     * @return
     */
    PayOrder createNoPayOrder(int shopId, int userId, String goodsName,
                              String orderType, Long totalMoney, Long discountMoney, Long realMoney,
                              String payChannel, String payPlatform, String tradeType, String createType);

    /**
     * 新增或更新实体类;支付订单
     *
     * @param payOrder
     * @return
     */
    Integer saveOrUpdatePayOrder(PayOrder payOrder);

    /**
     * 根据平台订单号获取支付记录
     *
     * @param userId
     * @param orderNo
     * @return
     */
    PayOrder getPayOrderByOrderNo(int userId, String orderNo);

    /**
     * 根据支付平台订单号获取支付记录;<br/>
     * 如: 微信支付返回的订单号, 支付宝返回的订单号
     *
     * @param userId
     * @param transactionId
     * @return
     */
    PayOrder getPayOrderByTransactionId(int userId, String transactionId);

    /**
     * 后台集合显示会员店铺订单
     * @param paramMemShopOrder
     * @return
     */
    List<VoMemShopOrder> pageVoMemShopOrder(ParamMemShopOrder paramMemShopOrder);

    /**
     * 后台新增账单
     * @param paramAddPayOrderForAdmin
     */
    BaseResult addPayOrderForAdmin(ParamAddPayOrderForAdmin paramAddPayOrderForAdmin);

    /**
     * 店铺详情会员获取记录
     * @param fkShpShopId
     * @return
     */
    List<VoPayOrderShopForAdmin> getVoPayOrderShopForAdmin(Integer fkShpShopId);
}
