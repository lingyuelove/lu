package com.luxuryadmin.mapper.ord;

import com.luxuryadmin.entity.ord.OrdModifyRecord;
import com.luxuryadmin.vo.ord.VoOrderModifyRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrdModifyRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrdModifyRecord record);

    int insertSelective(OrdModifyRecord record);

    OrdModifyRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrdModifyRecord record);

    int updateByPrimaryKey(OrdModifyRecord record);

    /**
     * 根据订单ID,店铺ID查询记录列表
     * @param orderId
     * @param shopId
     * @return
     */
    List<VoOrderModifyRecord> selectRecordListByOrderIdShopId(@Param("orderId") Integer orderId, @Param("shopId") Integer shopId);
}