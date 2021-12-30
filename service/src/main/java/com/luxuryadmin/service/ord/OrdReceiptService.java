package com.luxuryadmin.service.ord;

import com.luxuryadmin.entity.ord.OrdReceipt;
import com.luxuryadmin.vo.ord.VoOrdReceipt;

/**
 * 订单收据
 *
 * @author monkey king
 * @date 2020-01-20 21:50:12
 */
public interface OrdReceiptService {

    /**
     * 新增或更新实体
     *
     * @param ordReceipt
     * @return
     */
    int saveOrUpdateOrdReceipt(OrdReceipt ordReceipt);

    /**
     * 根据订单编号或去订单凭证
     * @param shopId
     * @param orderBizId
     * @return
     */
    VoOrdReceipt getOrdReceiptByOrderNumber(int shopId, String orderBizId);
}
