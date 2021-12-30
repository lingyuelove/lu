package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProStandard;
import org.apache.ibatis.annotations.Mapper;


/**
 *商品规格表 dao
 *@author zhangsai
 *@Date 2021-09-16 17:48:52
 */
@Mapper
public interface ProStandardMapper extends BaseMapper<ProStandard> {

    /**
     * 根据商品id获取商品规格详情
     * @param productId
     * @return
     */
    ProStandard getByProductId(Integer productId);
}
