package com.luxuryadmin.service.pro;

import com.luxuryadmin.entity.pro.ProCheckProduct;
import com.luxuryadmin.param.pro.ParamCheckProductListForApiBySearch;
import com.luxuryadmin.param.pro.ParamCheckProductUpdateStateForApi;
import com.luxuryadmin.vo.pro.VoCheckProductDetailByApi;
import com.luxuryadmin.vo.pro.VoCheckProductListForApi;

import java.util.List;
import java.util.Map;

public interface ProCheckProductService {

    /**
     * 集合显示所有盘点商品
     * @param checkProductListForApiBySearch
     * @return
     */
    Map<String,Object> getCheckProductListForApi(ParamCheckProductListForApiBySearch checkProductListForApiBySearch);

    /**
     * 盘点商品详情
     * @param id
     * @return
     */
    VoCheckProductDetailByApi getCheckProductDetailByApi(Integer id);

    /**
     * 修改盘点商品的状态
     * @param checkProductUpdateStateForApi
     */
    void updateCheckProduct(ParamCheckProductUpdateStateForApi checkProductUpdateStateForApi);

    ProCheckProduct getById(Integer id);

    /**
     * 根据盘点id和商品id获取详情
     * @param bizId
     * @param fkProCheckId
     * @return
     */
    VoCheckProductListForApi getCheckProductForApi(String bizId,Integer fkProCheckId);

    /**
     * 盘点商品集合显示已盘点未盘点商品
     * @param fkProCheckId
     * @return
     */
    Map<String,Integer> getCheckProductCount(Integer fkProCheckId);
}
