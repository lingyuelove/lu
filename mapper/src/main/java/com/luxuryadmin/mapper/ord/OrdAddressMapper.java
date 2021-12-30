package com.luxuryadmin.mapper.ord;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.ord.OrdAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrdAddressMapper  extends BaseMapper {
    /**
     * 根据订单ID查询订单收货地址
     * @param orderId
     * @return
     */
    OrdAddress selectByOrderId(@Param("orderId") Integer orderId);
}