package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.entity.shp.ShpServiceRecord;
import com.luxuryadmin.param.pro.ParamProductQuery;
import com.luxuryadmin.param.shp.ParamShpServiceQuery;
import com.luxuryadmin.vo.shp.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface ShpServiceRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShpServiceRecord record);

    int insertSelective(ShpServiceRecord record);

    ShpServiceRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShpServiceRecord record);

    int updateByPrimaryKey(ShpServiceRecord record);

    /**
     * 根据查询条件查询店铺服务列表
     * @param paramShpServiceQuery
     * @return
     */
    List<VoShpServiceRecord> selectShpServiceRecord(ParamShpServiceQuery paramShpServiceQuery);

    /**
     * 根据店铺ID和服务记录ID查询服务记录信息
     * @param shopId
     * @param shpServiceId
     * @return
     */
    VoShpServiceRecordDetail selectShpServiceById(@Param("shopId") Integer shopId, @Param("shpServiceId") Integer shpServiceId);

    /**
     * 根据【店铺ID】【用户ID】查询累计完成服务的收益<br>
     *     按结束时间为准[finishTime]
     * @param shopId
     * @param userId
     * @param insertTimeStart
     * @param insertTimeEnd
     * @return
     */
    BigDecimal selectTotalFinishServiceProfit(@Param("shopId") Integer shopId, @Param("userId") Integer userId,
                                              @Param("insertTimeStart") String insertTimeStart, @Param("insertTimeEnd") String insertTimeEnd);

    /**
     * 根据店铺ID和类型名称查询服务记录数量
     * @param shopId
     * @param typeName
     * @return
     */
    Integer selectShpServiceRecordByShopIdTypeName(@Param("shopId") Integer shopId, @Param("typeName") String typeName);

    /**
     * 根据【店铺ID】和【时间范围】查询店铺服务记录
     * @param shopId
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    List<String> listServiceNumber(@Param("shopId") int shopId,
                                   @Param("startDateTime") String startDateTime,
                                   @Param("endDateTime") String endDateTime);

    /**
     * 根据【店铺ID】删除所有【店铺服务记录】
     *
     * @param shopId
     * @return
     */
    int deleteShpServiceRecordByShopId(int shopId);

    /**
     * 删除【店铺服务记录】
     * @param numberStr
     */
    int deleteShpService(@Param("shopId") Integer shopId, @Param("numberStr") String numberStr);

    /**
     * 根据条件查询指定日期的维修保养结算数据
     * @param queryParam
     * @return
     */
    VoShpServiceSettleCount countTodaySettle(ParamProductQuery queryParam);

    /**
     * 获取利润
     * @param paramShpServiceQuery
     * @return
     */
    VoShpServiceRecordPrice getProfitPrice( ParamShpServiceQuery paramShpServiceQuery);


    /**
     * 2.6.5获取利润
     * @param paramShpServiceQuery
     * @return
     */
    VoShpServiceRecordPrice getServiceRecordPrice( ParamShpServiceQuery paramShpServiceQuery);

    /**
     * 服务人员列表
     * @param shopId
     * @return
     */
    List<VoEmployee> listServiceUser(Integer shopId);

    /**
     *
     * @param shopId
     * @return
     */
    List<VoEmployee> listReceiveUser(Integer shopId);
}