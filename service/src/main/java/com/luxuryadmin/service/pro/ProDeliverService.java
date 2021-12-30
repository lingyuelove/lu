package com.luxuryadmin.service.pro;

import com.luxuryadmin.entity.ord.OrdOrder;
import com.luxuryadmin.entity.pro.ProDeliver;
import com.luxuryadmin.entity.pro.ProLockRecord;
import com.luxuryadmin.mapper.pro.ProDeliverMapper;
import com.luxuryadmin.param.op.ParamIdQuery;
import com.luxuryadmin.param.pro.ParamProPageDeliver;
import com.luxuryadmin.param.pro.ParamSendProDeliver;
import com.luxuryadmin.vo.pro.VoFiltrateInfo;
import com.luxuryadmin.vo.pro.VoProDeliverDetail;

import java.io.UnsupportedEncodingException;
import java.util.List;


/**
 * 发货表 service
 *
 * @author taoqimin
 * @Date 2021-09-24 16:28:40
 */
public interface ProDeliverService {

    /**
     * 添加代发货信息
     *
     * @param proDeliver
     */
    void saveProDeliver(ProDeliver proDeliver);

    /**
     * 根据锁单id删除发货信息
     *
     * @param lockId
     */
    void deleteDeliverInfoByLockId(Integer lockId);

    /**
     * 根据锁单id变化订单来源
     *
     * @param lockId
     * @param ordOrder
     */
    void updateSourceByLockId(Integer lockId, OrdOrder ordOrder);

    /**
     * 根据订单信息删除发货信息
     *
     * @param orderId
     */
    void deleteDeliverInfoByOrderInfo(Integer orderId);

    /**
     * 发货
     *
     * @param param
     */
    void sendProDeliver(ParamSendProDeliver param) throws UnsupportedEncodingException;

    /**
     * 获取发货详情
     *
     * @param params
     * @return
     */
    VoProDeliverDetail getProDeliverDetail(ParamIdQuery params) throws UnsupportedEncodingException;

    /**
     * 多个删除
     *
     * @param ids
     */
    void deleteList(String ids);

    /**
     * 筛选条件列表
     *
     * @param shopId
     * @return
     */
    VoFiltrateInfo getFiltrateinfo(Integer shopId);

    /**
     * 修改锁单数量
     *
     * @param lockRecord
     */
    void updateNum(ProLockRecord lockRecord);

    /**
     * 2.6.6发货列表
     *
     * @param param
     * @return
     */
    List<ProDeliver> listDeliver(ParamProPageDeliver param);

    /**
     * 2.6.6新增代发货统计
     *
     * @param startTime
     * @param endTime
     * @param shpUserId
     * @param deliverStartTime
     * @param deliverEndTime
     * @param proName
     * @return
     */
    List<ProDeliver> listDeliverAll(Integer shopId, String startTime, String endTime, String shpUserId, String deliverStartTime, String deliverEndTime, String proName);
}
