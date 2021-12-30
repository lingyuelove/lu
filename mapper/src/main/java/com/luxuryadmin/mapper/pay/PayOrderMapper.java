package com.luxuryadmin.mapper.pay;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pay.PayOrder;
import com.luxuryadmin.param.mem.ParamMemShopOrder;
import com.luxuryadmin.vo.mem.VoMemShopOrder;
import com.luxuryadmin.vo.pay.VoPayOrderShopForAdmin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 支付订单Mapper
 *
 * @author Administrator
 */
@Mapper
public interface PayOrderMapper extends BaseMapper {


    /**
     * 根据平台订单号获取支付记录
     *
     * @param userId
     * @param orderNo
     * @return
     */
    PayOrder getPayOrderByOrderNo(@Param("userId") int userId, @Param("orderNo") String orderNo);

    /**
     * 根据支付平台订单号获取支付记录;<br/>
     * 如: 微信支付返回的订单号, 支付宝返回的订单号
     *
     * @param userId
     * @param transactionId
     * @return
     */
    PayOrder getPayOrderByTransactionId(@Param("userId") int userId, @Param("transactionId") String transactionId);

    /**
     * 后台集合显示会员店铺订单
     * @param paramMemShopOrder
     * @return
     */
    List<VoMemShopOrder> pageVoMemShopOrder(ParamMemShopOrder paramMemShopOrder);

    /**
     * 店铺详情会员获取记录
     * @param fkShpShopId
     * @return
     */
    List<VoPayOrderShopForAdmin> getVoPayOrderShopForAdmin(@Param("fkShpShopId") Integer fkShpShopId);
}