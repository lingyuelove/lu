package com.luxuryadmin.service.shp;

import com.luxuryadmin.entity.shp.ShpAfterSaleGuarantee;
import com.luxuryadmin.param.shp.ParamAddShpAfterSaleGuarantee;
import com.luxuryadmin.vo.shp.VoShpAfterSaleGuarantee;

import java.util.List;

/**
 * @Description: 店铺售后保障Service
 * @author: sanjin
 * @date: 2020-08-20 15:02
 * @email: dingxin_hz@163.com
 * @version: V1.0
 */
public interface ShpAfterSaleGuaranteeService {

    /**
     * 添加店铺售后保障
     *
     * @param shopId
     * @param userId
     * @param guaranteeInfo
     */
    ShpAfterSaleGuarantee addAfterSaleGuarantee(Integer shopId, Integer userId, ParamAddShpAfterSaleGuarantee guaranteeInfo);

    /**
     * 删除店铺售后保障
     *
     * @param shopId
     * @param userId
     * @param guaranteeId
     * @return
     */
    Integer deleteAfterSaleGuarantee(Integer shopId, Integer userId, Integer guaranteeId) throws Exception;

    /**
     * 根据店铺ID查询【售后保障】列表
     *
     * @param shopId
     * @return
     */
    List<VoShpAfterSaleGuarantee> listAfterSaleGuarantee(Integer shopId);

    /**
     * 创建店铺时初始化售后保障ID
     *
     * @param shopId
     * @param userId
     */
    void initShpAfterSaleGuarantee(int shopId, int userId);

    /**
     * 初始化所有老店铺的售后保障
     *
     * @return
     */
    Integer initAllShpAfterSaleGuarantee();

    /**
     * 删除店铺标签
     *
     * @param shopId
     */
    void deleteShpAfterSaleGuaranteeByShopId(int shopId);

}
