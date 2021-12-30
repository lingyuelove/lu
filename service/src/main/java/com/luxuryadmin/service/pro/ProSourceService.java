package com.luxuryadmin.service.pro;

import com.luxuryadmin.entity.pro.ProSource;
import com.luxuryadmin.vo.pro.VoProSource;

import java.util.List;

/**
 * 商品来源
 *
 * @author monkey king
 * @date 2020-05-27 15:46:29
 */
public interface ProSourceService {

    /**
     * 获取店铺各自的商品来源
     *
     * @param shopId
     * @return
     */
    List<VoProSource> listProSource(int shopId);

    /**
     * 根据店铺id 初始化商品来源表;从模板中初始化
     *
     * @param shopId
     * @param userId
     * @return
     */
    int initProSourceByShopIdAndUserId(int shopId, int userId);

    /**
     * 删除店铺商品来源
     *
     * @param shopId
     */
    void deleteProSourceByShopId(int shopId);
}
