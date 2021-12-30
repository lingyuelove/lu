package com.luxuryadmin.service.shp;

import com.luxuryadmin.vo.shp.VoShpOrderDailyCountForMonth;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Classname ShpOrderDailyCountService
 * @Description TODO
 * @Date 2020/9/14 17:18
 * @Created by sanjin145
 */
public interface ShpOrderDailyCountService {

    /**
     * 查询统计日期生成的店铺订单统计数据数量
     * @param countDate
     * @return
     */
    Integer getCountByCountDate(Date countDate);

    Integer generateShopOrderDailyCount(Integer shopId,Date countDate);

    /**
     * 根据统计日期查询有统计数据的店铺ID列表
     * @param countDate
     * @return
     */
    List<Integer> queryShpShopIdListByCountDate(@Param("countDate") Date countDate);

    /**
     * 推送每日店铺订单商品统计
     * @param shopId
     * @param countDate
     */
    void pushDailyCountShopOrderMsg(Integer shopId, Date countDate);

    /**
     * 推送每日店铺订单商品统计
     * @param shopId
     * @param countDate
     */
    void pushDailyCountShopOrderMsgForMonth(Integer shopId, Date countDate);

    /**
     * 根据统计日期查询消息发送生成数量
     * @param countDate
     * @return
     */
    Integer getMsgCountByCountDate(String msgType,String msgSubType,Date countDate);


}
