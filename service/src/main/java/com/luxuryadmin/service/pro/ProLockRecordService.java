package com.luxuryadmin.service.pro;

import com.luxuryadmin.entity.pro.ProLockRecord;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.param.pro.ParamLockProductQuery;
import com.luxuryadmin.param.pro.ParamProPageDeliver;
import com.luxuryadmin.param.pro.ParamProductLock;
import com.luxuryadmin.param.pro.ParamProductLockRemark;
import com.luxuryadmin.vo.pro.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 商品锁单记录表
 *
 * @author monkey king
 * @date 2020-05-29 19:27:07
 */
public interface ProLockRecordService {

    /**
     * 添加实体
     *
     * @param proLockRecord
     * @return
     */
    int saveProLockRecord(ProLockRecord proLockRecord);

    /**
     * 更新实体
     *
     * @param proLockRecord
     * @return
     */
    int updateProLockRecord(ProLockRecord proLockRecord);

    /**
     * 根据商品id获取锁单记录的锁单数量
     *
     * @param proId 商品id
     * @return
     */
    int getProductLockNumByProId(int proId);

    /**
     * 锁定商品之前的状态必须在[20,39]区间<br/>
     * 根据店铺id和商品业务id-【锁定】商品;<br/>
     * 生成一条锁单记录
     *
     * @param pro       需要锁单的商品
     * @param userId
     * @param myLockNum 锁单数量
     * @return
     */
    void lockProductByUserId(ProProduct pro, int userId, int myLockNum, ParamProductLock lockParam, HttpServletRequest request);


    /**
     * 解锁商品之前的状态必须在[20,39]区间<br/>
     * 根据店铺id和商品业务id-【解锁】商品;<br/>
     * 修改锁单记录
     *
     * @param lockRecord   锁单记录表
     * @param unlockUserId 解锁用户id
     * @param myLockNum    锁单数量
     * @param request
     */
    void unlockProductByUserId(ProLockRecord lockRecord, int unlockUserId, int myLockNum, HttpServletRequest request);

    /**
     * 获取锁单中的商品
     *
     * @param shopId
     * @param proName
     * @return
     */
    List<VoProductLoad> listLockProductByShopId(int shopId, String proName, String uniqueCode);

    /**
     * 索取锁单详情的商品详情
     *
     * @param shopId
     * @param bizId
     * @return
     */
    VoProductLoad getProductDetail(int shopId, String bizId);


    /**
     * 获取锁单商品的锁单记录
     *
     * @param shopId
     * @param bizId
     * @return
     */
    List<VoProLockRecord> listVoProLockRecordByBizId(int shopId, String bizId);


    /**
     * 获取锁单商品的锁单记录(原实体)
     *
     * @param shopId
     * @param bizId
     * @return
     */
    List<ProLockRecord> lisProLockRecordByBizId(int shopId, String bizId);

    /**
     * 获得商品的锁单记录id;LockId
     *
     * @param shopId
     * @param bizId
     * @return
     */
    Integer getLockIdByBizId(int shopId, String bizId);

    /**
     * 清除锁单记录;<br/>
     * 发生在更新商品库存时;
     *
     * @param shopId
     * @param bizId
     * @return
     */
    int deleteProLockRecord(int shopId, String bizId);

    /**
     * 根据锁单id获取实体
     *
     * @param lockId
     * @return
     */
    ProLockRecord getProLockRecordById(int lockId);


    /**
     * 根据锁单id获取实体
     *
     * @param shopId
     * @param lockId
     * @return
     */
    VoProLockRecord getProLockRecordByIdAndShopId(int shopId, int lockId);

    /**
     * 根据锁单id获取实体无论包含解锁的锁单
     *
     * @param shopId
     * @param lockId
     * @return
     */
    VoProLockRecord getLockRecordByIdAndShopId(int shopId, int lockId);
    /**
     * 删除店铺的商品锁单记录
     *
     * @param shopId
     */
    void deleteProLockRecordByShopId(int shopId);

    /**
     * 修改锁单备注
     *
     * @param productLockRemark
     */
    void updateProLockRecordForRemark(ParamProductLockRemark productLockRemark);


    /**
     * 获取正在锁单中的人员;
     *
     * @param shopId
     * @return
     */
    List<VoProLockRecord> listLockUser(int shopId);

    /**
     * 统计锁单商品中的信息v2.6.2(含)以上版本使用
     *
     * @param params
     * @return
     */
    VoProLockTotal countProLockTotal(ParamLockProductQuery params);

    /**
     * 统计我的锁单数量(正在锁单中)v2.6.2(含)以上版本使用
     *
     * @param shopId
     * @param userId
     * @return
     */
    int countMyLockNum(int shopId, int userId);

    /**
     * 统计寄卖传送某件商品的锁单数量
     * @param shopId
     * @param proId
     * @param type
     * @return
     */
    int countConveyProLockNum(int shopId, int proId, String type);
    /**
     * 获取锁单中的商品v2.6.2(含)以上版本使用
     *
     * @param params
     * @return
     */
    List<VoProductLoad> listLockProductByParam(ParamLockProductQuery params);

    /**
     * v2.6.2<br/>
     * 锁定商品之前的状态必须在[20,39]区间<br/>
     * 根据店铺id和商品业务id-【锁定】商品;<br/>
     * 生成一条锁单记录
     *
     * @param lockParam
     * @param request
     * @return
     */
    int lockProductReturnId(ParamProductLock lockParam, HttpServletRequest request);

    /**
     * 获取发货信息列表
     *
     * @param param
     * @return
     */
    List<VoProDeliverByPage> getProDeliverInfo(ParamProPageDeliver param);

    /**
     * 根据店铺id获取锁单人员简信息
     *
     * @param shopId
     * @return
     */
    List<VoOrderUserInfo> getFiltrateinfoByShopId(Integer shopId);

    /**
     * 2.6.6发货列表
     *
     * @param ids
     * @param state
     * @return
     */
    List<VoProDeliverByPage> listProDeliverInfo(List<Integer> ids, String state);

    /**
     * 根据商品id获取锁单数量
     *
     * @param ids
     * @param shopId
     * @return
     */
    List<VoLockNumByProId> getLockByProId(List<Integer> ids, Integer shopId);

    /**
     * 查询锁单数量
     *
     * @param ids
     * @return
     */
    Integer getLockSum(List<Integer> ids);
}
