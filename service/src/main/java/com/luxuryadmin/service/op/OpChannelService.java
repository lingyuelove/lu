package com.luxuryadmin.service.op;

import com.luxuryadmin.entity.op.OpChannel;

/**
 * 渠道业务逻辑
 *
 * @author monkey king
 * @date 2021-04-01 23:12:32
 */
public interface OpChannelService {


    /**
     * 根据outsideCode来获取渠道
     *
     * @param outsideCode
     * @return
     */
    OpChannel getOpChannelByOutsideCode(String outsideCode);
}
