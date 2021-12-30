package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProClassify;
import com.luxuryadmin.vo.pro.VoProClassify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProClassifyMapper extends BaseMapper {


    /**
     * 根据状态来获取分类;
     *
     * @param shopId
     * @param state  null:查询所有(不包含已删除状态)
     * @return
     */
    List<VoProClassify> listProClassifyByState(@Param("shopId") int shopId, @Param("state") String state);

    /**
     * 根据类型来获取分类
     * @param shopId
     * @param state
     * @param code
     * @return
     */
    VoProClassify getProClassifyByType(@Param("shopId") Integer shopId, @Param("state") String state, @Param("code") String code);

    /**
     * 根据店铺id 初始化分类表;
     *
     * @param shopId
     * @param userId
     * @return
     */
    int initProClassifyByShopIdAndUserId(@Param("shopId") int shopId, @Param("userId") int userId);


    /**
     * 加载友商店铺分类
     *
     * @param leaguerShopId
     * @return
     */
    List<VoProClassify> listLeaguerProClassify(int leaguerShopId);


    /**
     * 修改店铺分类
     *
     * @param list
     * @param shopId
     * @return
     */
    int updateProClassify(@Param("list") List<ProClassify> list, @Param("shopId") int shopId);

    /**
     * 删除店铺分类(注销时调用)
     *
     * @param shopId
     * @return
     */
    int deleteProClassifyByShopId(@Param("shopId") int shopId);

}