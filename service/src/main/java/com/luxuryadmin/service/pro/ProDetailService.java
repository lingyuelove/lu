package com.luxuryadmin.service.pro;

import com.luxuryadmin.entity.pro.ProDetail;

/**
 * 商品详情业务逻辑层
 *
 * @author monkey king
 * @date 2019-12-24 19:55:06
 */
public interface ProDetailService {

    /**
     * 新增ProDetail
     *
     * @param proDetail
     * @return
     */
    int saveProDetail(ProDetail proDetail);

    /**
     * 根据商品业务id获取商品详情id
     *
     * @param shopId
     * @param bizId
     * @return
     */
    Integer getProDetailIdByShopIdAndBizId(int shopId, String bizId);

    /**
     * 根据id更新实体
     *
     * @param proDetail
     * @return
     */
    int updateProDetail(ProDetail proDetail);

    /**
     * 根据商品业务id获取商品唯一编码;
     *
     * @param shopId
     * @param bizId
     * @return
     */
    String getUniqueCodeByShopIdBizId(int shopId, String bizId);

    /**
     * 更新商品独立编码
     *
     * @param proId
     * @param uniqueCode
     * @return
     */
    int updateUniqueCodeByProId(int proId, String uniqueCode);

    /**
     * 删除商品详情;
     *
     * @param ids
     */
    void deleteProDetailByProIds(String ids);

    /**
     * 根据商品id,获取详情
     *
     * @param proId
     * @return
     */
    ProDetail getProDetailByProId(int proId);

}
