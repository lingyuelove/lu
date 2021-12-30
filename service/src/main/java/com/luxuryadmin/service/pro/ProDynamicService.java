package com.luxuryadmin.service.pro;


import com.luxuryadmin.entity.pro.ProDynamic;
import com.luxuryadmin.param.pro.ParamDynamicDelete;
import com.luxuryadmin.param.pro.ParamDynamicQuery;
import com.luxuryadmin.param.pro.ParamDynamicSave;
import com.luxuryadmin.vo.pro.VoDynamicAndProductInfoList;
import com.luxuryadmin.vo.pro.VoDynamicList;

import java.util.List;

/**
 * 商品位置列表 service
 *
 * @author zhangsai
 * @Date 2021-11-05 11:45:33
 */
public interface ProDynamicService {

    /**
     * 获取商品位置列表
     *
     * @param param
     * @return
     */
    List<VoDynamicList> listDynamic(ParamDynamicQuery param);

    /**
     * 新增商品位置
     *
     * @param param
     */
    void saveDynamic(ParamDynamicSave param);

    /**
     * 删除商品位置
     *
     * @param param
     */
    void deleteDynamic(ParamDynamicDelete param);

    /**
     * 根据商品id获取商品位置信息
     *
     * @param proId
     * @param shopId
     * @return
     */
    ProDynamic getDynamicProductInfoByProductId(Integer proId, Integer shopId);

    /**
     * 根据商品id或者动态名称和商品id
     *
     * @param proIds
     * @param shopId
     * @return
     */
    List<VoDynamicAndProductInfoList> listInfoByProId(List<Integer> proIds, Integer shopId);

    /**
     * 初始化店铺信息
     *
     * @param shopId
     */
    void saveInitializeDynamic(Integer shopId);
}
