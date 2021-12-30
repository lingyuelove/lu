package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.shp.ShpDetail;
import com.luxuryadmin.vo.shp.VoShopValidInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author monkey king
 */
@Mapper
public interface ShpDetailMapper extends BaseMapper {

    /**
     * 根据shopId更新实体;
     *
     * @param shpDetail
     * @return
     */
    int updateShpDetailByShopId(ShpDetail shpDetail);

    /**
     * 根据店铺id查询店铺详情
     * @param id
     * @return
     */
    ShpDetail getObjectByShopId(Integer id);

    /**
     * 获取店铺认证信息
     *
     * @param shopId
     * @return
     */
    VoShopValidInfo getShopValidInfo(String shopId);
}