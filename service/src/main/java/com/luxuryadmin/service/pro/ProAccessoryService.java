package com.luxuryadmin.service.pro;

import com.luxuryadmin.vo.pro.VoProAccessory;

import java.util.List;

/**
 * 商品附件表--业务逻辑层
 *
 * @author monkey king
 * @date 2020-05-28 11:01:06
 */
public interface ProAccessoryService {

    /**
     * 获取该店铺自己的附件表
     *
     * @param shopId
     * @return
     */
    List<VoProAccessory> listProAccessoryByShopId(int shopId);

    /**
     * 删除实体
     *
     * @param id
     * @return
     */
    int deleteProAccessoryById(int id);

    /**
     * 根据店铺id 初始化附件表;从模板中初始化
     *
     * @param shopId
     * @param userId
     * @return
     */
    int initProAccessoryByShopIdAndUserId(int shopId, int userId);

    /**
     * 删除商品附件表
     *
     * @param shopId
     */
    void deleteProAccessoryByShopId(int shopId);
}
