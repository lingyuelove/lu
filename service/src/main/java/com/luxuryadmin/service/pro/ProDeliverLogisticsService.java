package com.luxuryadmin.service.pro;


import com.luxuryadmin.param.pro.ParamDeliverLogisticsDetail;
import com.luxuryadmin.vo.pro.VoDeliver;

import java.io.UnsupportedEncodingException;

/**
 *发货物流表 service
 *@author zhangsai
 *@Date 2021-10-14 17:20:49
 */
public interface ProDeliverLogisticsService {


    void addOrUpdateList(Integer shopId)  throws UnsupportedEncodingException;

    /**
     * 根据物流单号查询物流详情
     * @param logisticsNumber
     * @return
     */
    VoDeliver getDeliverByLogisticsNumber(String logisticsNumber);


    void addDeliverLogistics(String logisticsNumber,Integer shopId,String phone)throws UnsupportedEncodingException ;

    /**
     * 根据物流单号和手机号查询物流详情
     * @param params
     * @return
     */
    VoDeliver getDeliverByLogisticsNumAndPhone(ParamDeliverLogisticsDetail params)throws UnsupportedEncodingException;

    /**
     * 根据订单编号查询物流
     * @param shopId
     * @param orderBizId
     * @return
     * @throws UnsupportedEncodingException
     */
    VoDeliver getDeliverByOrderNumber(int shopId, String orderBizId);
}
