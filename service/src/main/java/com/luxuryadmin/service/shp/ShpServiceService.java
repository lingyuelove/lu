package com.luxuryadmin.service.shp;

import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.entity.shp.ShpServiceRecord;
import com.luxuryadmin.entity.shp.ShpServiceType;
import com.luxuryadmin.param.pro.ParamProductQuery;
import com.luxuryadmin.param.shp.ParamShpServiceQuery;
import com.luxuryadmin.param.shp.ParamShpServiceRecordAdd;
import com.luxuryadmin.param.shp.ParamShpServiceRecordFinish;
import com.luxuryadmin.vo.shp.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Classname ShpServiceService
 * @Description 店铺服务Service
 * @Date 2020/9/18 15:46
 * @Created by sanjin145
 */
public interface ShpServiceService {
    /**
     * 获取新增店铺服务初始化数据
     *
     * @param shopId
     * @return
     */
    VoInitShopService getInitAddShpServiceData(Integer shopId, BasicParam basicParam);

    /**
     * 初始化所有店铺的【店铺维修类型】
     *
     * @return
     */
    Integer initAllShpServiceType();

    /**
     * 初始化一家店铺的【店铺维修类型】
     *
     * @param shopId
     * @param userId
     * @return
     */
    Integer initSingleShpServiceType(int shopId, int userId);

    /**
     * 添加一条【店铺维修类型】记录
     *
     * @param shopId
     * @param userId
     * @param serviceTypeName
     * @return
     */
    ShpServiceType addShpServiceType(Integer shopId, int userId, String serviceTypeName);

    /**
     * 新增店铺服务记录
     *
     * @param shopId
     * @param userId
     * @param serviceInfo
     * @return
     */
    ShpServiceRecord addShpServiceRecord(ParamShpServiceRecordAdd serviceInfo, HttpServletRequest request);

    /**
     * 根据查询条件查询店铺服务记录列表
     *
     * @param paramShpServiceQuery
     * @return
     */
    VoShpServiceRecordForPage listShpServiceRecord(ParamShpServiceQuery paramShpServiceQuery,BasicParam basicParam );

    /**
     * 根据店铺ID取消店铺服务记录
     *
     * @param shopId
     * @param userId
     * @param shpServiceId
     */
    Integer cancelShpService(Integer shopId, Integer userId, Integer shpServiceId,HttpServletRequest request);

    /**
     * 根据店铺ID和服务记录ID查询服务记录信息
     *
     * @param shopId
     * @param shpServiceId
     * @return
     */
    VoShpServiceRecordDetail getShpServiceById(Integer shopId, Integer shpServiceId);

    /**
     * 根据店铺ID完成店铺服务记录
     *
     * @param shopId
     * @param userId
     * @param recordFinish
     */
    Integer finishShpService(Integer shopId, Integer userId, ParamShpServiceRecordFinish recordFinish,HttpServletRequest request);

    /**
     * 更新店铺服务记录
     *
     * @param shopId
     * @param userId
     * @param serviceInfo
     * @return
     */
    Integer updateShpService( ParamShpServiceRecordAdd serviceInfo,HttpServletRequest request);

    /**
     * 逻辑删除店铺服务类型
     *
     * @param shopId
     * @param userId
     * @param shpServiceTypeId
     * @return
     */
    Integer deleteShpServiceType(Integer shopId, Integer userId, Integer shpServiceTypeId);

    /**
     * 根据【店铺ID】【用户ID】查询累计完成服务的收益
     *
     * @param shopId
     * @param userId
     * @param startTimeStr
     * @param endTimeStr
     * @return
     */
    BigDecimal getTotalFinishServiceProfit(Integer shopId, Integer userId, String startTimeStr, String endTimeStr);

    /**
     * 根据【店铺ID】删除指定时间范围内的店铺服务记录，包含分享凭证
     *
     * @param shopId
     * @param startTime
     * @param endTime
     * @return
     */
    Integer deleteShpServiceByDateRange(Integer shopId, Integer userId,String startTime, String endTime);

    /**
     * 删除店铺服务类型
     *
     * @param shopId
     */
    void deleteShpServiceTypeByShopId(int shopId);

    /**
     * 删除店铺服务记录
     *
     * @param shopId
     */
    void deleteShpServiceRecordByShopId(int shopId);

    /**
     * 删除店铺服务
     * @param shopId
     * @param shpServiceId
     * @return
     */
    Integer deleteShpService(Integer shopId,Integer userId, Integer shpServiceId,HttpServletRequest request);

    /**
     * 根据条件查询指定日期的维修保养结算数据
     * @param queryParam
     * @return
     */
    VoShpServiceSettleCount countTodaySettle(ParamProductQuery queryParam);


}
