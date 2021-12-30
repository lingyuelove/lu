package com.luxuryadmin.service.pro;

import com.luxuryadmin.vo.pro.VoProAttribute;

import java.util.List;

/**
 * 商品分类表模板;<br/>
 * 系统默认模板;腕表、珠宝、鞋靴、佩饰、其它
 *
 * @author monkey king
 * @date 2019-12-09 15:31:19
 */
public interface ProAttributeService {

    /**
     * 获取商品属性列表
     *
     * @param shopId
     * @return
     */
    List<VoProAttribute> listProAttributeByShopId(int shopId);

    /**
     * 根据店铺id 初始化属性表;从模板中初始化
     *
     * @param shopId
     * @param userId
     * @return
     */
    int initProAttributeByShopIdAndUserId(int shopId, int userId);

    /**
     * 删除店铺属性(注销时调用)
     *
     * @param shopId
     */
    void deleteProAttributeByShopId(int shopId);
}
