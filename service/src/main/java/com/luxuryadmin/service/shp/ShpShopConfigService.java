package com.luxuryadmin.service.shp;


import com.luxuryadmin.entity.shp.ShpShopConfig;
import com.luxuryadmin.param.shp.ParamShopConfigUpdate;

/**
 *店铺配置表 service
 *@author zhangsai
 *@Date 2021-07-01 11:16:02
 */
public interface ShpShopConfigService{

    /**
     * 更新是否开启小程序访客功能
     * @param shopConfigUpdate
     */
    void updateShopConfig(ParamShopConfigUpdate shopConfigUpdate);

    /**
     * 根据店铺id获取店铺配置详情
     * @param shopId
     * @return
     */
    ShpShopConfig getShopConfigByShopId(Integer shopId);
}
