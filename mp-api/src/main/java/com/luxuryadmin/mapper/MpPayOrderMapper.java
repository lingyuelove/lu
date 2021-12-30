package com.luxuryadmin.mapper;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.MpPayOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 支付订单 dao
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:01
 */
@Mapper
public interface MpPayOrderMapper extends BaseMapper<MpPayOrder> {


    /**
     * 根据订单编号获取订单信息
     *
     * @param orderNo
     * @return
     */
    MpPayOrder getOrderInfoByOrderNo(@Param("orderNo") String orderNo, @Param("userId") Integer userId);
}
