package com.luxuryadmin.service.shp;

import com.luxuryadmin.entity.shp.ShpDetail;
import com.luxuryadmin.vo.shp.VoShopValidInfo;

/**
 * 店铺详情表--业务逻辑层
 *
 * @author monkey king
 * @date 2019-12-25 01:42:18
 */
public interface ShpDetailService {

    /**
     * 新增或者更新店铺详情表记录;
     *
     * @param shpDetail
     * @return
     */
    int saveOrUpdateShpDetail(ShpDetail shpDetail);

    /**
     * 根据shopId更新实体;
     *
     * @param shpDetail
     * @return
     */
    int updateShpDetailByShopId(ShpDetail shpDetail);

    /**
     * 根据店铺id查询店铺详情
     *
     * @param id
     * @return
     */
    ShpDetail selectByShopId(Integer id);


    /**
     * 获取店铺认证信息
     *
     * @param shopId
     * @return
     */
    VoShopValidInfo getShopValidInfo(String shopId);
}
