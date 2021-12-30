package com.luxuryadmin.service.ord;

import com.luxuryadmin.entity.ord.OrdType;
import com.luxuryadmin.param.ord.ParamOrdTypeUpFopApp;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 订单类型业务逻辑层
 *
 * @author monkey king
 * @date 2019-12-26 22:56:25
 */
public interface OrdTypeService {
    /**
     * 根据店铺id 初始化订单类型表;从模板中初始化
     *
     * @param shopId
     * @param userId
     * @return
     */
    int initOrdTypeByShopIdAndUserId(int shopId, int userId);

    /**
     * 获取订单类型列表
     *
     * @param shopId
     * @return
     */
    List<OrdType> listOrdTypeByShopId(int shopId);

    /**
     * 根据店铺ID,用户ID，订单类型名称添加【订单类型记录】
     *
     * @param shopId
     * @param userId
     * @param ordTypeName
     * @return
     */
    OrdType addOrdType(Integer shopId, Integer userId, String ordTypeName, HttpServletRequest request);

    /**
     * 编辑订单类型
     * @param ordTypeUpFopApp
     */
    void updateOrderType(ParamOrdTypeUpFopApp ordTypeUpFopApp);
    /**
     * 根据店铺ID,用户ID，订单类型名称添加【订单类型记录】
     *
     * @param shopId
     * @param userId
     * @param ordTypeId
     * @return
     */
    Integer deleteOrdType(Integer shopId, Integer userId, Integer ordTypeId,HttpServletRequest request);

    /**
     * 删除店铺订单类型
     *
     * @param shopId
     */
    void deleteOrdTypeByShopId(int shopId);
}
