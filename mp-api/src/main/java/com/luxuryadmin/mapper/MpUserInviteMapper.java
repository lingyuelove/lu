package com.luxuryadmin.mapper;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.MpUserInvite;
import com.luxuryadmin.param.invite.ParamUserInviteList;
import com.luxuryadmin.vo.invite.VOUserInviteList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 邀请记录 dao
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:01
 */
@Mapper
public interface MpUserInviteMapper extends BaseMapper<MpUserInvite> {


    /**
     * 获取邀请用户列表
     *
     * @param param
     * @return
     */
    List<VOUserInviteList> listUserInvite(ParamUserInviteList param);

    /**
     * 根据被邀请用户id获取用户信息
     *
     * @param userId
     * @return
     */
    MpUserInvite getUserInviteByBeUserId(@Param("userId") Integer userId);
}
