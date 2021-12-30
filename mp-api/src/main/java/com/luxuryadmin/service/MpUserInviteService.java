package com.luxuryadmin.service;


import com.luxuryadmin.param.invite.ParamUserInviteList;
import com.luxuryadmin.vo.invite.VOUserInviteList;

import java.util.List;

/**
 * 邀请记录 service
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:01
 */
public interface MpUserInviteService {

    /**
     * 获取邀请用户列表
     *
     * @param param
     * @return
     */
    List<VOUserInviteList> listUserInvite(ParamUserInviteList param);

    /**
     * 支付成功后修改邀请记录和奖励信息
     *
     * @param userId
     */
    void updateUserInviteAndAddDayInfo(Integer userId);

    /**
     * 处理奖励信息
     * @param userId
     */
    void setAwardInfo(Integer userId);
}
