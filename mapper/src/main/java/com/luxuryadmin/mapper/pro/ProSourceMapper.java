package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.vo.pro.VoProSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author monkey king
 * @date 2020-05-27 15:26:29
 */
@Mapper
public interface ProSourceMapper extends BaseMapper {

    /**
     * 查找店铺的商品来源
     *
     * @param shopId
     * @return
     */
    List<VoProSource> listProSourceByShopId(int shopId);

    /**
     * 根据店铺id 初始化商品来源表;从模板中初始化
     *
     * @param shopId
     * @param userId
     * @return
     */
    int initProSourceByShopIdAndUserId(@Param("shopId") int shopId, @Param("userId") int userId);

    /**
     * 删除店铺商品来源
     *
     * @param shopId
     * @return
     */
    int deleteProSourceByShopId(int shopId);

}
