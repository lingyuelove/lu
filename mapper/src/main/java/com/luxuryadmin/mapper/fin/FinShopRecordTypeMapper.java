package com.luxuryadmin.mapper.fin;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.fin.FinShopRecordType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FinShopRecordTypeMapper extends BaseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FinShopRecordType record);

    int insertSelective(FinShopRecordType record);

    FinShopRecordType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FinShopRecordType record);

    int updateByPrimaryKey(FinShopRecordType record);

    /**
     * 根据店铺ID 查询【财务店铺流水类型】列表
     *
     * @param shopId
     * @return
     */
    List<FinShopRecordType> listFinShopRecordTypeByShopId(@Param("shopId") int shopId, @Param("inoutType") String inoutType);

    /**
     * 根据店铺ID，查询有效的订单类型数量
     *
     * @param shopId
     * @return
     */
    Integer selectCountByShopId(Integer shopId);

    /**
     * 根据ID查询订单类型
     *
     * @param shopId
     * @param id
     * @return
     */
    FinShopRecordType selectFinShopRecordTypeById(@Param("shopId") Integer shopId, @Param("id") Integer id);

    /**
     * 获取所有需要初始化售后保障标签的店铺ID
     *
     * @return
     */
    List<Integer> selectAllNeedInitShopId();

    /**
     * 根据店铺ID查询所有的流水类型，不根据收入支出类型区分
     *
     * @param shopId
     * @return
     */
    List<String> listAllRecordTypeWithoutInoutType(Integer shopId);

    /**
     * 删除店铺财务流水类型
     *
     * @param shopId
     * @return
     */
    int deleteFinShopRecordTypeByShopId(int shopId);

    /**
     * 根据店铺ID和名称查询流水记录类型
     *
     * @param shopId
     * @param finShopRecordTypeName
     * @return
     */
    FinShopRecordType selectFinShopRecordTypeByName(
            @Param("shopId")Integer shopId,
            @Param("finShopRecordTypeName") String finShopRecordTypeName,
            @Param("inoutType") String inoutType);
}