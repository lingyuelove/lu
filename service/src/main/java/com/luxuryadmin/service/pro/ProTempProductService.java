package com.luxuryadmin.service.pro;

import com.luxuryadmin.entity.pro.ProTempProduct;
import com.luxuryadmin.param.pro.ParamProTempProductQuery;
import com.luxuryadmin.param.pro.ParamProTempUpdate;
import com.luxuryadmin.param.pro.ParamTempProductOrgPageBySearch;
import com.luxuryadmin.vo.pro.*;

import java.util.List;
import java.util.Map;

/**
 * 临时仓
 *
 * @author monkey king
 * @date 2021-01-17 14:04:11
 */
public interface ProTempProductService {

    /**
     * 根据id,获取临时仓商品的记录
     *
     * @param shopId
     * @param id
     * @return
     */
    ProTempProduct getProTempProductById(int shopId, int id);

    /**
     * 获取临时仓的商品
     *
     * @param queryParam
     * @return
     */
    List<VoProductLoad> listVoProductLoad(ParamProTempProductQuery queryParam);

    /**
     * 临时仓添加临时商品
     *
     * @param shopId
     * @param userId
     * @param tempId
     * @param proIds
     */
    void addProTempProduct(int shopId, int userId, int tempId, String proIds);


    /**
     * 删除临时仓的商品
     *
     * @param shopId
     * @param proTempId
     * @param proIds
     */
    void deleteProTempProduct(int shopId, int proTempId, String proIds);

    /**
     * 根据临时仓id删除所有临时仓的商品
     *
     * @param shopId
     * @param proTempId
     */
    void deleteAllProTempProductByTempId(int shopId, int proTempId);

    /**
     * 判断该商品数量是否大于临时仓数量
     * @param proId
     * @param totalNum
     */
    void selectProductNum( Integer proId,Integer totalNum);
    /**
     * 获取临时仓商品的详情
     *
     * @param shopId
     * @param tempId
     * @param proId
     * @return
     */
    VoProductLoad getVoProductFromTempProduct(int shopId, int tempId, int proId);


    /**
     * 获取临时仓商品的详情
     *
     * @param shopId
     * @param tempId
     * @param proId
     * @return
     */
    ProTempProduct getProTempProduct(int shopId, int tempId, int proId);


    /**
     * 更新临时仓商品详情
     *
     * @param paramUpdate
     */
    void updateProTempProduct(ParamProTempUpdate paramUpdate);

    /**
     * 获取临时仓的所有商品id
     *
     * @param shopId
     * @param tempId
     * @return
     */
    List<Integer> listProIdFromTempProduct(int shopId, int tempId);

    /**
     * 获取临时仓的所有商品
     * @param tempProductOrgPageBySearch
     * @return
     */
    VoTempProductOrgByApp getTempProductOrgByApp(ParamTempProductOrgPageBySearch tempProductOrgPageBySearch);

    /**
     * 某个店铺的商品
     * @param tempProductOrgPageBySearch
     * @return
     */
    VoTempProductOrgByApp getTempProductOrgForShopByApp(ParamTempProductOrgPageBySearch tempProductOrgPageBySearch);

    /**
     * 临时仓商品详情
     * @param tempProductId
     * @return
     */
    VoTempProductOrgDetailByApp getTempProductOrgDetail(Integer tempProductId);

    /**
     * 小程序端集合显示
     * @param paramTempProductOrgPageBySearch
     * @return
     */
    VoTempProductOrgByApplets getTempProductOrgByApplets(ParamTempProductOrgPageBySearch paramTempProductOrgPageBySearch);

    List<VoProductLoad> listVoProductLoadByPrice(ParamProTempProductQuery queryParam);
    /**
     * 根据价格类型查询临时仓商品价格汇总
     * @param queryParam
     * @return
     */
    Map<String,Object> getProductLoadByPriceByPrice(ParamProTempProductQuery queryParam);
    /**
     * 根据临时仓id修改商品销售价格为空
     * @param proTempId
     */
    void updateSalePriceByTempId(String proTempId);

    Integer getShopNumById(String bizId);

    /**
     * 更新临时仓商品数量
     * @param tempProduct 从db查询出来的实体
     */
    void updateTempProductCount(ProTempProduct tempProduct);

    /**
     * 新查找临时仓商品编辑页面----2.5.2--mong
     * @param shopId
     * @param tempId
     * @param proId
     * @return
     */
    VoProTempProduct getNewProTempProduct(int shopId, int tempId, int proId);

    /**
     * 修改临时仓默认价格类型
     * @param priceType
     * @param proTempId
     */
    void updateTempPriceTypeById(String priceType, String proTempId);

    /**
     * 获取临时仓商品状态
     * @param shopId
     * @param tempId
     * @param proId
     * @return
     */
    String getTempProState(int shopId, int tempId, int proId);
}
