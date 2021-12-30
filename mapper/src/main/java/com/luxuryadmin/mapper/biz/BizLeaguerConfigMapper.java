package com.luxuryadmin.mapper.biz;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.biz.BizLeaguerConfig;
import com.luxuryadmin.vo.biz.VoBizLeaguerShop;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BizLeaguerConfigMapper  extends BaseMapper {

    /**
     * 根据店铺ID查询友商配置信息
     * @param shopId
     * @return
     */
    BizLeaguerConfig selectBizLeaguerConfigByShopId(Integer shopId);
    /**
     * 根据店铺ID查询本店配置信息
     * @param shopId
     * @return
     */
    VoBizLeaguerShop getLeaguerShop(Integer shopId);

    /**
     * 获取友商商品数量
     * @param shopId
     * @return
     */
    Integer getRecommendShopProductCount(Integer shopId);

    /**
     * 获取友商数量
     * @param shopId
     * @return
     */
    Integer getRecommendShopCount(Integer shopId);

    /**
     * 删除友商店铺信息
     * @param shopId
     */
    void deleteLeaguerConfigByShop(Integer shopId);
}