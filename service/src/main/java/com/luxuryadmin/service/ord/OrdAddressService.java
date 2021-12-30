package com.luxuryadmin.service.ord;

import com.luxuryadmin.entity.ord.OrdAddress;

/**
 * 订单地址业务逻辑
 *
 * @author monkey king
 * @date 2019-12-25 21:44:25
 */
public interface OrdAddressService {


    /**
     * 添加OrdAddress
     *
     * @param ordAddress
     * @return
     */
    int saveOrdAddress(OrdAddress ordAddress);

}
