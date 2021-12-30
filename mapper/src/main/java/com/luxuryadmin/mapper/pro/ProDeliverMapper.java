package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProDeliver;
import com.luxuryadmin.param.pro.ParamProPageDeliver;
import com.luxuryadmin.vo.pro.VoOrderUserInfo;
import com.luxuryadmin.vo.pro.VoProDeliverDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 发货表 dao
 *
 * @author taoqimin
 * @Date 2021-09-24 16:28:40
 */
@Mapper
public interface ProDeliverMapper extends BaseMapper<ProDeliver> {


    /**
     * 根据锁单id删除发货信息
     *
     * @param lockId
     */
    void deleteDeliverInfoByLockId(@Param("lockId") Integer lockId);

    /**
     * 根据锁单id查询发货信息
     *
     * @param lockId
     * @return
     */
    ProDeliver getDeliverByLockId(@Param("lockId") Integer lockId);

    /**
     * 根据订单id删除发货信息
     *
     * @param orderId
     */
    void deleteDeliverInfoByOrderInfo(@Param("orderId") Integer orderId);

    /**
     * 获取发货详情
     *
     * @param id
     * @return
     */
    VoProDeliverDetail getProDeliverOrderDetail(@Param("id") Integer id);

    /**
     * 根据店铺获取发货人员信息
     *
     * @param shopId
     * @return
     */
    List<VoOrderUserInfo> getFiltrateinfoByShopId(@Param("shopId") Integer shopId);

    /**
     * 或者锁单发货详情
     *
     * @param id
     * @return
     */
    VoProDeliverDetail getProDeliverLockDetail(@Param("id") Integer id);

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
    List<ProDeliver> listDeliverAll(@Param("shopId") Integer shopId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("shpUserId") String shpUserId,
                                    @Param("deliverStartTime") String deliverStartTime, @Param("deliverEndTime") String deliverEndTime, @Param("proName") String proName);
}
