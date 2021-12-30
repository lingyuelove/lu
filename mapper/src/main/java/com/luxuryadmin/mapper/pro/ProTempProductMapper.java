package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProTempProduct;
import com.luxuryadmin.param.pro.ParamProTempProductQuery;
import com.luxuryadmin.param.pro.ParamProTempUpdate;
import com.luxuryadmin.param.pro.ParamTempProductOrgPageBySearch;
import com.luxuryadmin.vo.pro.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 */
@Mapper
public interface ProTempProductMapper extends BaseMapper {

    /**
     * 根据id,获取临时仓商品的记录
     *
     * @param shopId
     * @param id
     * @return
     */
    ProTempProduct getProTempProductById(@Param("shopId") int shopId, @Param("id") int id);

    /**
     * 获取这个临时仓的所有商品的id
     *
     * @param shopId
     * @param tempId
     * @return
     */
    List<Integer> listProTempIdByShopId(
            @Param("shopId") int shopId, @Param("tempId") int tempId);

    /**
     * 批量添加临时仓的商品
     *
     * @param shopId
     * @param userId
     * @param tempId
     * @param list
     * @return
     */
    int saveBatchProTempProduct(
            @Param("shopId") int shopId, @Param("userId") int userId,
            @Param("tempId") int tempId, @Param("list") List<String> list);

    /**
     * 删除临时仓的商品
     *
     * @param shopId
     * @param tempId
     * @param proIds
     * @return
     */
    int deleteProTempProductByTempIdAndProId(
            @Param("shopId") int shopId, @Param("tempId") int tempId,
            @Param("proIds") String proIds);

    /**
     * 根据临时仓id删除所有临时仓的商品
     *
     * @param shopId
     * @param tempId
     * @return
     */
    int deleteAllProTempProductByTempId(@Param("shopId") int shopId, @Param("tempId") int tempId);

    /**
     * 判断该商品数量是否大于临时仓数量
     * @param proId
     * @param totalNum
     * @return
     */
    List<VoTempForPro> selectProductNum(@Param("proId")Integer proId,@Param("totalNum") Integer totalNum);
    /**
     * 获取临时仓商品的详情填充到VoProductLoad实体
     *
     * @param shopId
     * @param tempId
     * @param proId
     * @return
     */
    VoProductLoad getVoProductFromTempProduct(
            @Param("shopId") int shopId, @Param("tempId") int tempId,
            @Param("proId") int proId);


    /**
     * 获取临时仓商品的详情
     *
     * @param shopId
     * @param tempId
     * @param proId
     * @return
     */
    ProTempProduct getProTempProduct(
            @Param("shopId") int shopId, @Param("tempId") int tempId,
            @Param("proId") int proId);

    /**
     * 更新临时仓商品详情的值
     *
     * @param update
     * @return
     */
    int updateProTempProductById(ParamProTempUpdate update);

    /**
     * 获取临时仓的所有商品id
     *
     * @param shopId
     * @param tempId
     * @return
     */
    List<Integer> listProIdFromTempProduct(
            @Param("shopId") int shopId, @Param("tempId") int tempId);

    /**
     * 获取临时仓的所有商品
     * @param tempProductOrgPageBySearch
     * @return
     */
    List<VoTempProductOrgPageByApp> getTempProductOrgByApp(@Param("tempProductOrgPageBySearch")ParamTempProductOrgPageBySearch tempProductOrgPageBySearch);

    /**
     * 临时仓商品详情
     * @param tempProductId
     * @return
     */
    VoTempProductOrgDetailByApp getTempProductOrgDetail(@Param("tempProductId")Integer tempProductId);

    /**
     * 小程序端商品列表
     * @param tempProductOrgPageBySearch
     * @return
     */
    List<VoTempProductOrgPageByApplets>  getTempProductOrgByApplets(@Param("tempProductOrgPageBySearch")ParamTempProductOrgPageBySearch tempProductOrgPageBySearch);

    /**
     * 获取临时仓的店铺详情
     * @param tempId
     * @return
     */
    VoTempProductOrgByApplets getByTempId(@Param("tempId")Integer tempId);

    VoProTempProduct getNewProTempProduct(@Param("shopId") int shopId, @Param("tempId") int tempId,
                                          @Param("proId") int proId);

    void updateTempPriceTypeById(@Param("priceType") String priceType,@Param("proTempId") String proTempId);
}