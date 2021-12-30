package com.luxuryadmin.service.op;

import com.luxuryadmin.entity.op.OpNotice;

import java.util.List;

/**
 * 消息中心--公告
 *
 * @author monkey king
 * @date 2020-01-16 20:48:33
 */
public interface OpNoticeService {


    /**
     * 加载系统消息;面对所有用户
     *
     * @return
     */
    List<OpNotice> listOpNotice();

    /**
     * 加载店铺消息
     *
     * @param shopId
     * @return
     */
    List<OpNotice> listOpNoticeByShopId(int shopId);

    /**
     * 根据id获取OpNotice
     * @param id
     * @return
     */
    OpNotice getNoticeById(String id);
}
