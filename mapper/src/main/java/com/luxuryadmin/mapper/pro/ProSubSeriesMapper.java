package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProSubSeries;
import com.luxuryadmin.param.pro.ParamProClassifySubSonQuery;
import com.luxuryadmin.param.pro.ParamProClassifySubSunQuery;
import com.luxuryadmin.vo.pro.VoProSubSeries;
import com.luxuryadmin.vo.pro.VoProSubSeriesForAdmin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 *商品分类系列表 dao
 *@author zhangsai
 *@Date 2021-08-04 13:45:01
 */
@Mapper
public interface ProSubSeriesMapper  extends BaseMapper<ProSubSeries> {

    /**
     * 根据id查询商品型号分类
     * @param id
     * @return
     */
    VoProSubSeries getSubSeriesById(Integer id);

    /**
     * 根据名称查询型号分类
     *
     * @param name
     * @param shopId
     * @return
     */
    VoProSubSeries getSubSeriesByName(@Param("name") String name, @Param("shopId")Integer shopId);

    /**
     * 根据分类id查询系列分类
     *
     * @param classifySubSunQuery
     * @return
     */
    List<VoProSubSeries> listSubSeriesPage(ParamProClassifySubSunQuery classifySubSunQuery);

    /**
     * 后端系列集合显示
     * @param classifySubSunQuery
     * @return
     */
    List<VoProSubSeriesForAdmin> listSubSeriesForAdminPage(ParamProClassifySubSunQuery classifySubSunQuery);

    /**
     * 删除该品牌下的系列
     * @param classifySubName
     */
    void deleteByClassifySub( String classifySubName);

    /**
     * 根据系列名和品牌名查询唯一
     * @param name
     * @param classifySubName
     * @return
     */
    ProSubSeries getSubSeriesByNameAndSub(@Param("name") String name,@Param("classifySubName")String classifySubName);
}
