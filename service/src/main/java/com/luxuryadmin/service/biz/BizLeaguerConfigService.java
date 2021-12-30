package com.luxuryadmin.service.biz;

import com.luxuryadmin.entity.biz.BizLeaguerConfig;
import com.luxuryadmin.param.biz.ParamCanSeeLeaguerPriceInfo;
import com.luxuryadmin.vo.biz.VoBizLeaguerShop;
import com.luxuryadmin.vo.biz.VoCanSeeLeaguerPriceInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * 友商配置Service
 */
public interface BizLeaguerConfigService {

    /**
     * 根据店铺ID查询友商是否有查看价格的权限
     * @param shopId
     * @return
     */
    VoCanSeeLeaguerPriceInfo getCanSeeLeaguerPriceInfo(Integer shopId, Integer userId) ;

    /**
     * 修改友商是否可以查看权限
     * @param shopId
     * @param userId
     * @param paramCanSeeLeaguerPriceInfo
     */
    void updateCanSeeLeaguerPriceInfo(Integer shopId, Integer userId, ParamCanSeeLeaguerPriceInfo paramCanSeeLeaguerPriceInfo, HttpServletRequest request);

    /**
     * 根据ID查询友商配置
     * @param shopId
     * @return
     * @throws Exception
     */
    BizLeaguerConfig selectBizLeaguerConfigByShopId(Integer shopId, Integer userId) throws Exception ;

    /**
     * 创建友商配置
     * @param shopId
     * @param userId
     * @return
     * @throws Exception
     */
    BizLeaguerConfig buildBizLeaguerConfig(Integer shopId, Integer userId);

    /**
     * 友商相册个人店铺信息
     * @param shopId
     * @return
     */
    VoBizLeaguerShop getLeaguerShop(Integer shopId);

    /**
     * 删除友商店铺信息
     * @param shopId
     */
    void deleteLeaguerConfigByShop(Integer shopId);
}
