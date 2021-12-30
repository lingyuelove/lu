package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProSeriesModel;
import com.luxuryadmin.param.pro.ParamProClassifySubSonQuery;
import com.luxuryadmin.param.pro.ParamProClassifySubSunQuery;
import com.luxuryadmin.vo.pro.VoProSubSeries;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 *商品分类型号表 dao
 *@author zhangsai
 *@Date 2021-08-04 13:45:01
 */
@Mapper
public interface ProSeriesModelMapper  extends BaseMapper<ProSeriesModel> {

    /**
     * 根据分类id查询型号分类
     *
     * @param classifySubSonQuery
     * @return
     */
    List<VoProSubSeries> listSeriesModelPage(ParamProClassifySubSonQuery classifySubSonQuery);

    /**
     * 根据id查询商品型号分类
     * @param id
     * @return
     */
    VoProSubSeries getSeriesModelById(Integer id);

    /**
     * 根据名称查询型号分类
     *
     * @param name
     * @param shopId
     * @return
     */
    VoProSubSeries getSeriesModelByName(@Param("name") String name,@Param("shopId") Integer shopId);

    /**
     * 根据品牌名删除系列
     * @param classifySubName
     * @param subSeriesName
     */
    void deleteSeriesModelClassifySub(@Param("classifySubName")  String classifySubName,@Param("subSeriesName")  String subSeriesName);

    /**
     * 根据品牌系列型号名称查询唯一
     * @param name
     * @param classifySubName
     * @param subSeriesName
     * @return
     */
    ProSeriesModel getSeriesModel(@Param("name") String name,@Param("classifySubName")String classifySubName,@Param("subSeriesName")String subSeriesName);
}
