package com.luxuryadmin.mapper.ord;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.param.ord.ParamShareReceiptQuery;
import com.luxuryadmin.param.pro.ParamShareProductQuery;
import com.luxuryadmin.vo.ord.VoShareReceipt;
import com.luxuryadmin.vo.pro.VoShareProduct;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分享商品
 *
 * @author monkey king
 * @date 2020-05-27 15:26:29
 */
@Mapper
public interface OrdShareReceiptMapper extends BaseMapper {


    /**
     * 根据店铺编号,用户编号和分享批号来获取分享的产品id
     *
     * @param shareReceiptQuery
     * @return
     */
    VoShareReceipt getOrderNoByShareBatch(ParamShareReceiptQuery shareReceiptQuery);

    /**
     * 根据订单编号删除分享凭证记录用in查询;物理删除
     *
     * @param orderNumber
     * @return
     */
    int deleteShareReceipt(String orderNumber);

    /**
     * 删除订单分享记录
     *
     * @param shopId
     * @return
     */
    int deleteShareReceiptByShopId(int shopId);
}
