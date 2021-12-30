package com.luxuryadmin.service.pro;

import com.luxuryadmin.vo.pro.VoProSaleChannel;

import java.util.List;

/**
 * 商品销售渠道
 *
 * @author monkey king
 * @date 2020-06-03 22:36:55
 */
public interface ProSaleChannelService {

    /**
     * 获取店铺自己的销售途径
     *
     * @param shopId
     * @return
     */
    List<VoProSaleChannel> listProSaleChannel(int shopId);

    /**
     * 根据店铺id 初始化销售渠道;从模板中初始化
     *
     * @param shopId
     * @param userId
     * @return
     */
    int initProSaleChannelByShopIdAndUserId(int shopId, int userId);

    /**
     * 删除店铺销售渠道
     *
     * @param shopId
     */
    void deleteProSaleChannelByShopId(int shopId);

}
