package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProDynamicProduct;
import com.luxuryadmin.param.pro.ParamDynamicProductQuery;
import com.luxuryadmin.vo.pro.VoCountDynamic;
import com.luxuryadmin.vo.pro.VoDynamicProductList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 动态列表商品信息 dao
 *
 * @author zhangsai
 * @Date 2021-11-05 11:45:33
 */
@Mapper
public interface ProDynamicProductMapper extends BaseMapper<ProDynamicProduct> {


    /**
     * 获取每个商品总数量
     *
     * @param dynamicIds
     * @return
     */
    List<VoCountDynamic> listCountByDynamicIds(@Param("dynamicIds") List<Integer> dynamicIds);

    /**
     * 根据动态ids删除商品位置信息
     *
     * @param ids
     */
    void deleteDynamicProductByDynamicIds(@Param("ids") List<Integer> ids);

    /**
     * 获取商品位置列表
     *
     * @param param
     * @return
     */
    List<VoDynamicProductList> listDynamicProduct(ParamDynamicProductQuery param);


    /**
     * 根据商品ids获取动态商品信息
     *
     * @param proIds
     * @param shopId
     * @return
     */
    List<ProDynamicProduct> getDynamicProductInfoByProductId(@Param("proIds") List<Integer> proIds, @Param("shopId") Integer shopId);

    /**
     * 根据ids 修改动态
     *
     * @param ids
     * @param dynamicId
     */
    void updateByIds(@Param("ids") List<Integer> ids, @Param("dynamicId") String dynamicId);

    /**
     * 2.6.6 根据ids删除商品位置
     *
     * @param ids
     */
    void deleteDynamicProduct(@Param("ids") List<Integer> ids);

    /**
     * 根据商品id删除动态信息
     *
     * @param proId
     * @param shopId
     */
    void deleteDynamicProductByProId(@Param("proId") Integer proId, @Param("shopId") Integer shopId);

    /**
     * 根据proid修改动态状态
     *
     * @param proId
     * @param state
     */
    void updateStateByProId(@Param("proId") Integer proId, @Param("state") Integer state, @Param("shopId") Integer shopId);

    /**
     * 根据商品id删除动态信息
     *
     * @param proIds
     * @param shopId
     */
    void deleteDynamicProductByProIds(@Param("proIds") List<Integer> proIds, @Param("shopId") Integer shopId);
}
