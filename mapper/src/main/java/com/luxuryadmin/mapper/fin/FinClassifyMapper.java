package com.luxuryadmin.mapper.fin;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.fin.FinClassify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 */
@Mapper
public interface FinClassifyMapper extends BaseMapper {

    /**
     * 获取资金分类列表
     *
     * @param shopId
     * @return
     */
    List<FinClassify> listFinClassifyByShopId(int shopId);

    /**
     * 根据店铺id 初始化资金分类表;从模板中初始化
     *
     * @param shopId
     * @param userId
     * @return
     */
    int initFinClassifyByShopIdAndUserId(@Param("shopId") int shopId, @Param("userId") int userId);
}