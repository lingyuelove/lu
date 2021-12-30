package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProClassifyTypeShop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 *单个店铺补充信息不适用关联表 dao
 *@author zhangsai
 *@Date 2021-08-05 14:29:05
 */
@Mapper
public interface ProClassifyTypeShopMapper extends BaseMapper<ProClassifyTypeShop> {

    /**
     * 根据店铺id和补充信息id获取是否添加过该信息
     * @param shopId
     * @param classifyTypeId
     * @return
     */
    ProClassifyTypeShop getTypeShopByShopIdAndTypeId(@Param("shopId") Integer shopId,@Param("classifyTypeId") Integer classifyTypeId);
}
