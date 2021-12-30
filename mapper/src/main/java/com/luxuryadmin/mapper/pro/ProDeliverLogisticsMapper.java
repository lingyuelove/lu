package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProDeliverLogistics;
import com.luxuryadmin.vo.pro.VoDeliver;
import org.apache.catalina.LifecycleState;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.UnsupportedEncodingException;
import java.util.List;


/**
 *发货物流表 dao
 *@author zhangsai
 *@Date 2021-10-14 17:20:49
 */
@Mapper
public interface ProDeliverLogisticsMapper extends BaseMapper<ProDeliverLogistics> {

    /**
     * 根据物流单号查询物流信息
     * @param logisticsNumber
     * @return
     */
    ProDeliverLogistics getDeliverLogistByLogisticsNumber(String logisticsNumber);

    List<ProDeliverLogistics> listDeliverLogisticsByShopId(Integer shopId);

    /**
     * 根据订单编号查询物流
     * @param shopId
     * @param orderBizId
     * @return
     * @throws UnsupportedEncodingException
     */
    VoDeliver getDeliverByOrderNumber(@Param("shopId") int shopId,@Param("number")  String number);
}
