package com.luxuryadmin.mapper.op;

import com.luxuryadmin.entity.op.OpMessageShopUserRef;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OpMessageShopUserRefMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OpMessageShopUserRef record);

    int insertSelective(OpMessageShopUserRef record);

    OpMessageShopUserRef selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OpMessageShopUserRef record);

    int updateByPrimaryKey(OpMessageShopUserRef record);

    int batchInsertMessageShopUserRef(List<OpMessageShopUserRef> list);

    /**
     * 获取未读消息数量
     * @param param
     * @return
     */
    Integer selectUnreadCountByShopIdAndType(Map<String, Object> param);

    /**
     * 根据【用户ID】更新所有未读的【系统消息】
     * @param userId
     * @return
     */
    Integer updateAllUnReadSystemOpMessageByUserId(Integer userId);

    /**
     * 根据【用户ID】和【店铺ID】更新所有未读的【非系统消息】
     * @param params
     * @return
     */
    Integer updateAllUnReadOtherOpMessageByUserIdAndShopId(Map<String, Object> params);

    /**
     * 删除指定时间范围内的店铺消息
     * @param shopId
     * @param startTime
     * @param endTime
     */
    void delOpMessageByDateRange(@Param("shopId") Integer shopId, @Param("startTime") String startTime, @Param("endTime") String endTime);
}