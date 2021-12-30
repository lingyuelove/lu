package com.luxuryadmin.service.op;

import com.luxuryadmin.enums.op.EnumOpMessageJumpType;
import com.luxuryadmin.param.op.ParamOpMessageQuery;
import com.luxuryadmin.param.op.ParamOpMessageSave;
import com.luxuryadmin.vo.op.VoMessageSubTypeList;
import com.luxuryadmin.vo.op.VoOpMessage;
import com.luxuryadmin.vo.op.VoOpMessageDetail;
import com.luxuryadmin.vo.op.VoOpMessageUnreadCount;

import java.util.List;
import java.util.Map;

/**
 * 用户管理--消息中心
 *
 * @author sanjin
 * @date 2020-07-13 16:04:06
 */
public interface OpMessageService {

    /**
     * 新增消息方法
     * @param paramOpMessageSave 消息主体内容参数
     * @return
     * @throws Exception
     */
    Long addOpMessage(ParamOpMessageSave paramOpMessageSave, Integer uid, Integer shopId, List<Integer> userIdList, Map<String,String> extraParam)
            throws Exception;

    /**
     * 新增消息【店铺-店员】关联记录
     * @param fkShpShopId 店铺ID，【消息类型】为【系统消息】时【店铺ID】可为空，其它条件下必填。
     * @param userIdList 用户ID
     * @param messageId 消息ID
     * @param enumOpMessageType 消息类型
     * @return
     */
    Boolean addOpMessageShopUserRef(Integer fkShpShopId, List<Integer> userIdList, Long messageId,
                                    String enumOpMessageType,Map<String,String> extraParam) throws Exception;


    /**
     * 修改消息状态为已读
     * @param id
     * @return
     * @throws Exception
     */
    Boolean updateOpMessageReadStateByRefId(Long id) throws Exception;

    /**
     * 根据用户ID获取消息列表
     *
     * @param fkShpShopId
     * @return
     * @throws Exception
     */
    List<VoOpMessageDetail> loadOpMessageOtherListByShopIdAndUserId(Integer fkShpShopId, Integer fkUserId) throws Exception;

    /**
     * 根据用户ID获取【消息】未读数量
     * @param fkShpShopId 店铺ID
     * @param fkShpUserId 用户ID
     * @return
     * @throws Exception
     */
    VoOpMessageUnreadCount loadOpMessageUnReadCountByShopId(Integer fkShpShopId,Integer fkShpUserId) throws Exception;

    /**
     * 根据【用户ID】逻辑删除【消息】
     * @param id
     * @return
     */
    Boolean delOpMessageRefById(Long id) throws Exception;

    /**
     * 全部已读所有【消息】,不包含type在【友商消息】里的消息，但包含【店铺消息|系统消息|其他消息】
     * 1.如果【店铺ID】为空，说明用户当前没有店铺，只更新【系统消息】
     * 2.如果【店铺ID】不为空，则【系统消息】【除系统消息外的其它消息】都进行更新。
     * @param shopId 店铺ID
     * @param userId 用户ID
     * @return
     */
    Boolean updateAllUnReadOpMessageByShopIdAndUserId(Integer shopId,Integer userId) throws Exception;

    /**
     * 根据类型查询消息列表，仅限【店铺消息】【系统消息】
     * @param shopId
     * @param userId
     * @param type
     * @return
     */
    List<VoOpMessageDetail> loadOpMessageOtherListByType(Integer shopId, Integer userId, String type,String subType) throws Exception;

    /**
     * CMS根据查询条件查询消息列表
     * @param paramOpMessageQuery
     * @return
     */
    List<VoOpMessage> listOpMessageForCms(ParamOpMessageQuery paramOpMessageQuery);

    /**
     * 逻辑删除用户消息
     * @param msgId
     * @param uid
     * @return
     */
    int delOpMessage(Long msgId, Integer uid);

    /**
     * 根据时间范围删除用户消息
     * @param shopId
     * @param startTime
     * @param endTime
     * @return
     */
    int delOpMessageByDateRange(Integer shopId,String startTime,String endTime);

    /**
     * 更新用户消息
     * @param paramOpMessageSave
     * @return
     */
    int updateOpMessage(ParamOpMessageSave paramOpMessageSave,Integer uid) throws Exception;

    /**
     * 根据消息ID查询
     * @param msgId
     * @return
     */
    VoOpMessage loadOpMessageByIdForCms(Long msgId);

    /**
     * 设置【跳转APP原生页面】消息默认参数
     * @param paramOpMessageSave
     */
    void setCommonMsgParamNative(ParamOpMessageSave paramOpMessageSave,EnumOpMessageJumpType enumOpMessageJumpType);

    /**
     * 根据消息ID立即发送消息
     * @param msgId
     * @return
     */
    int rightSend(Long msgId, Integer uid,List<Integer> userIdList,Integer shopId, Map<String,String> extraParam) throws Exception;

    /**
     * 自动定时发送
     */
    void autoTimerPush();

    /**
     * 根据子类型获取中文名称列表
     * @return
     */
    VoMessageSubTypeList loadSubTypeCnNameList();
}
