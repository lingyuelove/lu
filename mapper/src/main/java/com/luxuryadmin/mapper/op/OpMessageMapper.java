package com.luxuryadmin.mapper.op;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.op.OpMessage;
import com.luxuryadmin.param.op.ParamOpMessageQuery;
import com.luxuryadmin.vo.op.VoOpMessage;
import com.luxuryadmin.vo.op.VoOpMessageDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface OpMessageMapper extends BaseMapper {

    int deleteByPrimaryKey(Long id);

    int insert(OpMessage record);

    int insertSelective(OpMessage record);

    OpMessage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OpMessage record);

    int updateByPrimaryKey(OpMessage record);

    /**
     * 根据用户ID获取消息列表
     * @param params 查询参数Map
     * @return
     */
    List<VoOpMessageDetail> selectOpMessageOtherListByShopIdAndUserId(Map<String,Object> params);


    /**
     * 根据类型查询【消息列表】,只能为【shop|system】
     * @param params
     * @return
     */
    List<VoOpMessageDetail> selectOpMessageListByType(Map<String, Object> params);

    /**
     * 根据用户ID获取消息列表数量
     * @param param
     * @return
     */
    Integer selectOpMessageOtherListUnReadCountByShopIdAndUserId(Map<String, Object> param);

    /**
     * CMS根据查询条件查询消息列表
     * @param paramOpMessageQuery
     * @return
     */
    List<VoOpMessage> selectOpVoMessageForCms(ParamOpMessageQuery paramOpMessageQuery);

    /**
     * 根据ID查询消息
     * @param id
     * @return
     */
    VoOpMessage selectOpMessageByIdForCms(Long id);

    /**
     * 查询所有的未推送消息
     * @return
     */
    List<Long> selectAllUnPushPreSendMsgId();

    /**
     * 根据消息类型，子类型，消息日期查询消息数量
     * @param msgType
     * @param msgSubType
     * @param countDate
     * @return
     */
    Integer selectMsgCountByCountDate(@Param("msgType") String msgType, @Param("msgSubType") String msgSubType, @Param("countDate") Date countDate);
}