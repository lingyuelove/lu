package com.luxuryadmin.service.pro;


import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.vo.pro.VoProClassifySub;
import com.luxuryadmin.vo.pro.VoProSubSeries;

import java.util.List;

/**
 *商品分类型号表 service
 *@author zhangsai
 *@Date 2021-08-04 13:45:01
 */
public interface ProSeriesModelService {

    /**
     * 根据分类id查询型号分类
     *
     * @param classifySubSonQuery
     * @return
     */
    List<VoProSubSeries> listSeriesModelPage(ParamProClassifySubSonQuery classifySubSonQuery);

    /**
     * 添加型号分类
     *
     * @param classifySubSonAdd
     */
    void addSeriesModel(ParamProClassifySubSonAdd classifySubSonAdd);


    /**
     * 添加型号分类api端
     *
     * @param classifySubAddForApi
     */
//    void addSubSeriesForApi(ParamClassifySubAddForApi classifySubAddForApi);

    /**
     * 修改型号分类
     *
     * @param classifySubSunUpdate
     */
    void updateSeriesModel(ParamProClassifySubSunUpdate classifySubSunUpdate);

    /**
     * 修改型号分类
     *
     * @param classifySubSunUpdate
     */
//    void updateSubSeriesForApi(ParamProClassifySubSunUpdate classifySubSunUpdate);


    /**
     * 删除型号分类
     *
     * @param id
     */
    void deleteSeriesModel(String id);

    /**
     * 根据id查询商品型号分类
     * @param id
     * @return
     */
    VoProSubSeries getSeriesModelById(String id);

    /**
     * 根据名称查询型号分类
     *
     * @param name
     * @param shopId
     * @return
     */
    VoProSubSeries getSeriesModelByName(String name, Integer shopId);
}
