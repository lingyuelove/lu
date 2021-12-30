package com.luxuryadmin.service.pro;

import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.vo.pro.VoProClassifySub;
import com.luxuryadmin.vo.pro.VoProSubSeries;
import com.luxuryadmin.vo.pro.VoProSubSeriesByPageForAdmin;
import com.luxuryadmin.vo.pro.VoProSubSeriesForAdmin;

import java.util.List;

/**
 *商品分类系列表 service
 *@author zhangsai
 *@Date 2021-08-04 13:45:01
 */
public interface ProSubSeriesService {


    /**
     * 根据分类id查询系列分类
     *
     * @param classifySubSunQuery
     * @return
     */
    List<VoProSubSeries> listSubSeriesPage(ParamProClassifySubSunQuery classifySubSunQuery);

    /**
     * 添加分类
     *
     * @param classifySubSunAdd
     */
    void addSubSeries(ParamProClassifySubSunAdd classifySubSunAdd);


    /**
     * 添加二级分类api端
     *
     * @param classifySubAddForApi
     */
//    void addSubSeriesForApi(ParamClassifySubAddForApi classifySubAddForApi);

    /**
     * 修改系列分类
     *
     * @param classifySubSunUpdate
     */
    void updateSubSeries(ParamProClassifySubSunUpdate classifySubSunUpdate);

    /**
     * 修改系列分类
     *
     * @param classifySubSunUpdate
     */
//    void updateSubSeriesForApi(ParamProClassifySubSunUpdate classifySubSunUpdate);


    /**
     * 删除系列分类
     *
     * @param id
     */
    void deleteSubSeries(String id);


    /**
     * 根据id查询商品型号分类
     * @param id
     * @return
     */
    VoProSubSeries getSubSeriesById(String id);

    /**
     * 根据名称查询型号分类
     *
     * @param name
     * @param shopId
     * @return
     */
    VoProSubSeries getSubSeriesByName(String name, Integer shopId);


    /**
     * 添加系列型号
     * @param subSeriesForAdminAdd
     */
    void addSubSeriesForAdmin(ParamSubSeriesForAdminAdd subSeriesForAdminAdd);

    /**
     * 后台显示系列型号
     * @param paramProClassifySubSunQuery
     * @return
     */
    VoProSubSeriesByPageForAdmin getSubSeriesByPageForAdmin(ParamProClassifySubSunQuery paramProClassifySubSunQuery);

    /**
     * 后端修改
     * @param subSeriesForAdminUpdate
     */
    void updateSubSeriesForAdmin(ParamSubSeriesForAdminUpdate subSeriesForAdminUpdate);


    /**
     * 后台显示系列
     * @param paramProClassifySubSunQuery
     * @return
     */
    List<VoProSubSeriesForAdmin> listSubSeriesForAdmin(ParamProClassifySubSunQuery paramProClassifySubSunQuery);

    /**
     * 系列型号的定时新增
     */
    void addSubSeriesList();
}
