package com.luxuryadmin.mapper.ord;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.vo.ord.VoOrdReceipt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Administrator
 */
@Mapper
public interface OrdReceiptMapper extends BaseMapper {

    /**
     * 根据订单编号或去订单凭证
     *
     * @param shopId
     * @param orderBizId
     * @return
     */
    VoOrdReceipt getOrdReceiptByOrderNumber(@Param("shopId") int shopId, @Param("orderBizId") String orderBizId);

    /**
     * 根据订单编号删除订单凭证;订单编号用in查询
     *
     * @param orderNumber
     * @return
     */
    int deleteOrdReceipt(String orderNumber);

    /**
     * 删除店铺订单凭证
     *
     * @param shopId
     * @return
     */
    int deleteOrdReceiptByShopId(int shopId);
}