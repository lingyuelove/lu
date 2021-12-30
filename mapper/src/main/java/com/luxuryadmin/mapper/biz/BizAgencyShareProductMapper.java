package com.luxuryadmin.mapper.biz;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.biz.BizAgencyShareProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Administrator
 */
@Mapper
public interface BizAgencyShareProductMapper extends BaseMapper {

    /**
     * 根据相关条件获取分享产品
     *
     * @param shopId   店铺id
     * @param proBizId 商品的业务逻辑id
     * @param userId   代理用户id
     * @return
     */
    BizAgencyShareProduct getBizAgencyShareProduct(
            @Param("shopId") int shopId, @Param("proBizId") String proBizId, @Param("userId") int userId);

    /**
     * 更新BizAgencyShareProduct By shopId,proBizId,userId
     *
     * @param shareProduct
     * @return
     */
    int updateBizAgencyShareProduct(BizAgencyShareProduct shareProduct);


}