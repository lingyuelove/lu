package com.luxuryadmin.service.pro;


import com.luxuryadmin.entity.pro.ProDynamicProduct;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.param.pro.ParamDynamicProductAdd;
import com.luxuryadmin.param.pro.ParamDynamicProductDelete;
import com.luxuryadmin.param.pro.ParamDynamicProductQuery;
import com.luxuryadmin.param.pro.ParamProductLock;
import com.luxuryadmin.vo.pro.VoDynamicProductList;

import java.util.List;

/**
 * 动态列表商品信息 service
 *
 * @author zhangsai
 * @Date 2021-11-05 11:45:33
 */
public interface ProDynamicProductService {

    /**
     * 获取商品位置列表
     *
     * @param param
     * @return
     */
    List<VoDynamicProductList> listDynamicProduct(ParamDynamicProductQuery param);

    /**
     * 2.6.6添加商品位置
     *
     * @param dynamicProduct
     */
    void saveDynamicProduct(ParamDynamicProductAdd dynamicProduct);

    /**
     * 2.6.6删除商品位置
     *
     * @param param
     */
    void deleteDynamicProduct(ParamDynamicProductDelete param);

    /**
     * 根据商品id删除动态信息
     *
     * @param proId
     * @param shopId
     */
    void deleteDynamicProductByProId(Integer proId, Integer shopId);

    /**
     * 根据proid修改动态状态
     *
     * @param proId
     * @param state
     */
    void updateStateByProId(Integer proId, Integer state, Integer shopId);

    /**
     * 更新商品id集合
     * @param lockParam
     */
    void updateListStateByProId( ParamProductLock lockParam, ProProduct proProduct);

    /**
     * 根据商品id查询动态信息
     *
     * @param proId
     * @param shopId
     * @return
     */
    ProDynamicProduct getDynamicProductInfoByProId(Integer proId, Integer shopId);

    /**
     * 正常修改
     *
     * @param proDynamicProduct
     */
    void update(ProDynamicProduct proDynamicProduct);
}
