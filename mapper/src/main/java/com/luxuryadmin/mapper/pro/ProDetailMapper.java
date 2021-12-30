package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author monkey king
 */
@Mapper
public interface ProDetailMapper extends BaseMapper {

    /**
     * 根据商品业务id获取商品详情id
     *
     * @param shopId
     * @param bizId
     * @return
     */
    Integer getProDetailIdByShopIdAndBizId(@Param("shopId") int shopId, @Param("bizId") String bizId);

    /**
     * 根据商品业务id获取商品唯一编码;
     *
     * @param shopId
     * @param bizId
     * @return
     */
    String getUniqueCodeByShopIdBizId(@Param("shopId") int shopId, @Param("bizId") String bizId);

    /**
     * 更新商品独立编码
     *
     * @param proId
     * @param uniqueCode
     * @return
     */
    int updateUniqueCodeByProId(@Param("proId") int proId, @Param("uniqueCode") String uniqueCode);


    /**
     * 删除商品详情
     *
     * @param ids
     * @return
     */
    int deleteProDetailByProIds(String ids);

    /**
     * 根据商品id,获取详情
     *
     * @param proId
     * @return
     */
    ProDetail getProDetailByProId(int proId);
}