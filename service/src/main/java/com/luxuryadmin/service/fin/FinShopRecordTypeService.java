package com.luxuryadmin.service.fin;

import com.luxuryadmin.entity.fin.FinShopRecordType;
import com.luxuryadmin.vo.fin.VoFinShopRecordType;

import java.util.List;

/**
 * 订单类型业务逻辑层
 *
 * @author sanjin145
 * @date 2020-10-20 16:09
 */
public interface FinShopRecordTypeService {
    /**
     * 根据店铺id 初始化订单类型表;从模板中初始化
     *
     * @param shopId
     * @param userId
     * @return
     */
    int initFinShopRecordTypeByShopIdAndUserId(int shopId, int userId);

    /**
     * 获取订单类型列表
     *
     * @param shopId
     * @return
     */
    List<VoFinShopRecordType> listFinShopRecordTypeByShopId(int shopId, String inoutType);

    /**
     * 根据店铺ID,用户ID，订单类型名称添加【订单类型记录】
     *
     * @param shopId
     * @param userId
     * @param FinShopRecordTypeName
     * @return
     */
    FinShopRecordType addFinShopRecordType(Integer shopId, Integer userId, String FinShopRecordTypeName, String inoutType);

    /**
     * 根据店铺ID,用户ID，订单类型名称添加【订单类型记录】
     *
     * @param shopId
     * @param userId
     * @param FinShopRecordTypeId
     * @return
     */
    Integer deleteFinShopRecordType(Integer shopId, Integer userId, Integer FinShopRecordTypeId);

    /**
     * 初始化所有老店铺的店铺账单流水
     *
     * @return
     */
    Integer initAllShpFinShopRecordType();

    /**
     * 获取订单类型列表,不区分支出收入类型，去重
     *
     * @param shopId
     * @return
     */
    List<String> listAllRecordTypeWithoutInoutType(Integer shopId);

    /**
     * 删除店铺财务流水类型
     *
     * @param shopId
     */
    void deleteFinShopRecordTypeByShopId(int shopId);
}
