package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProDynamic;
import com.luxuryadmin.param.pro.ParamDynamicQuery;
import com.luxuryadmin.param.pro.ParamDynamicSave;
import com.luxuryadmin.vo.pro.VoDynamicAndProductInfoList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 商品位置列表 dao
 *
 * @author zhangsai
 * @Date 2021-11-05 11:45:33
 */
@Mapper
public interface ProDynamicMapper extends BaseMapper<ProDynamic> {

    /**
     * 获取商品位置列表
     *
     * @param param
     * @return
     */
    List<ProDynamic> listDynamic(ParamDynamicQuery param);

    /**
     * 根据名称获取动态信息
     *
     * @param param
     * @return
     */
    List<ProDynamic> getDynamicInfoByName(ParamDynamicSave param);

    /**
     * 根据id删除动态列表
     *
     * @param ids
     */
    void deleteDynamicByIds(@Param("ids") List<Integer> ids);

    /**
     * 根据商品id获取商品位置信息
     *
     * @param proId
     * @param shopId
     * @return
     */
    ProDynamic getDynamicInfoByProductId(@Param("proId") Integer proId, @Param("shopId") Integer shopId);

    /**
     * 根据商品id或者动态名称和商品id
     *
     * @param proIds
     * @param shopId
     * @return
     */
    List<VoDynamicAndProductInfoList> listInfoByProId(@Param("proIds") List<Integer> proIds, @Param("shopId") Integer shopId);

    /**
     * 获取已有动态列表数量
     *
     * @param shopId
     * @return
     */
    Integer getCount(@Param("shopId") Integer shopId);
}
