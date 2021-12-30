package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProConveyProduct;
import com.luxuryadmin.param.pro.ParamConveyProductAdd;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 *pro_convey_product dao
 *@author zhangsai
 *@Date 2021-11-22 15:05:52
 */
@Mapper
public interface ProConveyProductMapper  extends BaseMapper<ProConveyProduct> {

    /**
     *新增多条寄卖传送商品
     * @param conveyProductAdd
     */
    void addConveyProductList(ParamConveyProductAdd conveyProductAdd);

    /**
     * 编辑仓库商品寄卖传送状态
     * @param conveyProductAdd
     */
    void updateProductConveyState(ParamConveyProductAdd conveyProductAdd);

    /**
     * 编辑寄卖传送商品结算价
     * @param conveyId
     */
    void updateConveyPrice(Integer conveyId);

    /**
     * 移除寄卖传送无库存（开单无库存+锁单无库存）商品
     * @param conveyId
     */
    void removeProductForNoNum(Integer conveyId);
    /**
     * 移除寄卖传送未提取商品
     * @param conveyId
     */
    void removeProductForConveyId(Integer conveyId);

    /**
     * 寄卖传送商品改为仓库商品
     * @param conveyId
     */
    void updateProductForConveyId(Integer conveyId);
    /**
     * 编辑寄卖传送商品列表的价格 数量
     * @param conveyId
     * @param defaultPrice
     */
    void updateConveyList(@Param("conveyId") Integer conveyId ,@Param("defaultPrice") String defaultPrice);

    /**
     *
     * @param conveyId
     * @return
     */
    List<ProConveyProduct> listConveyProduct(Integer conveyId);

    /**
     * 根据商品id获取该商品所属的寄卖传送
     * @param productId
     * @param type send  receive
     * @return
     */
    ProConveyProduct getByProductId(@Param("productId") Integer productId,@Param("type")String type);

    ProConveyProduct getByProductIdAndType(@Param("productId") Integer productId,@Param("type")String type,@Param("defaultPrice")String defaultPrice);
}
