package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.vo.pro.VoProAccessory;
import com.luxuryadmin.vo.pro.VoProSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品附件表--Mapper
 *
 * @author monkey king
 * @date 2020-05-27 15:26:29
 */
@Mapper
public interface ProAccessoryMapper extends BaseMapper {

    /**
     * 获取店铺自己的附件表
     *
     * @param shopId
     * @return
     */
    List<VoProAccessory> listProAccessoryByShopId(int shopId);

    /**
     * 根据店铺id 初始化商品来源表;从模板中初始化
     *
     * @param shopId
     * @param userId
     * @return
     */
    int initProAccessoryByShopIdAndUserId(@Param("shopId") int shopId, @Param("userId") int userId);

    /**
     * 删除商品附件表
     *
     * @param shopId
     * @return
     */
    int deleteProAccessoryByShopId(int shopId);
}
