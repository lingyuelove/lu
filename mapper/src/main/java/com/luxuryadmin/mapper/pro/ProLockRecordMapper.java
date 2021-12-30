package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProLockRecord;
import com.luxuryadmin.param.pro.ParamLockProductQuery;
import com.luxuryadmin.param.pro.ParamProPageDeliver;
import com.luxuryadmin.vo.pro.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品锁单记录表
 *
 * @author monkey king
 * @date 2020-05-29 19:17:33
 */
@Mapper
public interface ProLockRecordMapper extends BaseMapper {

    /**
     * 根据商品id获取锁单记录的锁单数量
     *
     * @param proId 商品id
     * @return
     */
    int getProductLockNumByProId(int proId);

    /**
     * 根据商品bizId和userId查找锁单记录<br/>
     * 确保每个商品每个人只有一条锁单记录
     *
     * @param bizId
     * @param userId
     * @return
     */
    ProLockRecord getProLockRecordByBizIdAndUserId(@Param("bizId") String bizId,
                                                   @Param("userId") int userId);

    /**
     * 获取锁单中的商品
     *
     * @param shopId
     * @param proName
     * @param uniqueCode
     * @return
     */
    List<VoProductLoad> listLockProductByShopId(@Param("shopId") int shopId,
                                                @Param("proName") String proName,
                                                @Param("uniqueCode") String uniqueCode);

    /**
     * 索取锁单详情的商品详情
     *
     * @param shopId
     * @param bizId
     * @return
     */
    VoProductLoad getProductDetail(@Param("shopId") int shopId,
                                   @Param("bizId") String bizId);


    /**
     * 获取锁单商品的锁单记录
     *
     * @param shopId
     * @param bizId
     * @return
     */
    List<VoProLockRecord> listVoProLockRecordByBizId(@Param("shopId") int shopId,
                                                     @Param("bizId") String bizId);

    /**
     * 获得商品的锁单记录id;LockId
     *
     * @param shopId
     * @param bizId
     * @return
     */
    Integer getLockIdByBizId(@Param("shopId") int shopId, @Param("bizId") String bizId);

    /**
     * 根据proId删除记录
     *
     * @param shopId
     * @param bizId
     * @return
     */
    int deleteProLockRecordByProId(@Param("shopId") int shopId, @Param("bizId") String bizId);

    /**
     * 删除店铺锁单记录
     *
     * @param shopId
     * @return
     */
    int deleteProLockRecordByShopId(int shopId);

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
     * 统计我的锁单数量(正在锁单中)
     *
     * @param shopId
     * @param userId
     * @return
     */
    int countMyLockNum(@Param("shopId") int shopId, @Param("userId") Integer userId);

    /**
     * 统计寄卖传送我的锁单数量
     * @param shopId
     * @param proId
     * @param type
     * @return
     */
    int countConveyProLockNum(@Param("shopId")int shopId, @Param("proId")int proId, @Param("type")String type);
    /**
     * 获取锁单中的商品v2.6.2(含)以上版本使用
     *
     * @param params
     * @return
     */
    List<VoProductLoad> listLockProductByParam(ParamLockProductQuery params);

    /**
     * 查找某个店铺里具体的单个锁单记录
     *
     * @param shopId
     * @param lockId
     * @return
     */
    VoProLockRecord getProLockRecordByIdAndShopId(
            @Param("shopId") int shopId, @Param("lockId") int lockId);

    /**
     * 查找某个店铺里具体的单个锁单记录 无论是否锁单
     *
     * @param shopId
     * @param lockId
     * @return
     */
    VoProLockRecord getLockRecordByIdAndShopId(
            @Param("shopId") int shopId, @Param("lockId") int lockId);

    /**
     * 获取锁单商品的锁单记录
     *
     * @param shopId
     * @param bizId
     * @return
     */
    List<ProLockRecord> lisProLockRecordByBizId(@Param("shopId") int shopId, @Param("bizId") String bizId);

    /**
     * 获取发货信息列表
     *
     * @param param
     * @return
     */
    List<VoProDeliverByPage> getProDeliverInfo(ParamProPageDeliver param);

    /**
     * 根据proId查询记录
     *
     * @param shopId
     * @param bizId
     * @return
     */
    List<ProLockRecord> getProLockRecordByProId(@Param("shopId") Integer shopId, @Param("bizId") String bizId);

    /**
     * 根据店铺id获取锁单人员简信息
     *
     * @param shopId
     * @return
     */
    List<VoOrderUserInfo> getFiltrateinfoByShopId(@Param("shopId") Integer shopId);

    /**
     * 2.6.6发货列表
     *
     * @param ids
     * @param state
     * @return
     */
    List<VoProDeliverByPage> listProDeliverInfo(@Param("ids") List<Integer> ids, @Param("state") String state);

    /**
     * 2.6.6 获取锁单数量
     *
     * @param proIds
     * @param shopId
     * @return
     */
    List<VoLockNumByProId> getLockByProId(@Param("proIds") List<Integer> proIds,@Param("shopId") Integer shopId);

    /**
     * 查询锁单数量
     *
     * @param ids
     * @return
     */
    Integer getLockSum(@Param("ids") List<Integer> ids);

    /**
     * 根据关联id查询锁单是否存在
     * @param sonRecordId
     * @param conveyLockType
     * @return
     */
    ProLockRecord getProLockRecordBySonRecordId(@Param("sonRecordId") Integer sonRecordId,
                                                   @Param("conveyLockType") String conveyLockType);
}
