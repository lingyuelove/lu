package com.luxuryadmin.mapper.ord;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.ord.OrdType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author monkey king
 * @date 2019-12-26 21:29:10
 */
@Mapper
public interface OrdTypeMapper extends BaseMapper {

    /**
     * 获取订单类型列表
     *
     * @param shopId
     * @return
     */
    List<OrdType> listOrdTypeByShopId(int shopId);

    /**
     * 根据店铺id 初始化订单类型表;从模板中初始化
     *
     * @param shopId
     * @param userId
     * @return
     */
    int initOrdTypeByShopIdAndUserId(@Param("shopId") int shopId, @Param("userId") int userId);

    /**
     * 根据店铺ID，查询有效的订单类型数量
     *
     * @param shopId
     * @return
     */
    Integer selectCountByShopId(Integer shopId);

    /**
     * 根据ID查询订单类型
     *
     * @param shopId
     * @param id
     * @return
     */
    OrdType selectOrdTypeById(@Param("shopId") Integer shopId, @Param("id") Integer id);

    /**
     * 删除店铺订单类型
     *
     * @param shopId
     * @return
     */
    int deleteOrdTypeByShopId(int shopId);
}
