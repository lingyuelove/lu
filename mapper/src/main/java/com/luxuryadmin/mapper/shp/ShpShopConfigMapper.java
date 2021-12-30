package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.shp.ShpShopConfig;
import org.apache.ibatis.annotations.Mapper;


/**
 *店铺配置表 dao
 *@author zhangsai
 *@Date 2021-07-01 11:16:02
 */
@Mapper
public interface ShpShopConfigMapper extends BaseMapper<ShpShopConfig> {

    /**
     * 根据店铺id获取详情
     * @param shopId
     * @return
     */
    ShpShopConfig getShopConfigByShopId(Integer shopId);
}
