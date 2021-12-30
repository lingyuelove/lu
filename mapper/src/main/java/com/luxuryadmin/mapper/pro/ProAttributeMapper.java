package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.vo.pro.VoProAttribute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author monkey king
 */
@Mapper
public interface ProAttributeMapper extends BaseMapper {

    /**
     * 获取商品属性列表
     *
     * @param shopId
     * @return
     */
    List<VoProAttribute> listProAttributeByShopId(int shopId);

    /**
     * 根据店铺id 初始化属性表;从模板中初始化
     *
     * @param shopId
     * @param userId
     * @return
     */
    int initProAttributeByShopIdAndUserId(@Param("shopId") int shopId, @Param("userId") int userId);

    /**
     * 删除店铺属性(注销时调用)
     *
     * @param shopId
     * @return
     */
    int deleteProAttributeByShopId(int shopId);
}