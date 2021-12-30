package com.luxuryadmin.service;


import com.luxuryadmin.entity.MpUser;
import com.luxuryadmin.param.common.ParamBasic;
import com.luxuryadmin.param.user.ParamUserInfoSave;
import com.luxuryadmin.vo.user.VOAddVipUserList;
import com.luxuryadmin.vo.user.VOUserInfo;

import java.util.Date;
import java.util.List;

/**
 * 小程序用户表 service
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:01
 */
public interface MpUserService {

    /**
     * 同步用户信息
     *
     * @param param
     */
    void saveUserInfo(ParamUserInfoSave param);

    /**
     * 获取当前登录用户信息
     */
    VOUserInfo getUserInfo();

    /**
     * 修改体验期会员
     *
     * @return
     */
    void updatePastUserInfo(Date date);


    /**
     * 修改正式会员信息
     *
     * @param date
     * @return
     */
    void updateVipUserInfo(Date date);

    /**
     * 用户退出登录
     */
    void exitLogin();

    List<String> getUserName(Date date);

    List<MpUser> getVipUserName();

    /**
     * 将小程序会员转为奢当家会员
     *
     * @param yesSdjVipPhones
     */
    void updateSdjVipByMpVipUsername(List<String> yesSdjVipPhones);

    /**
     * 将奢当家会员过期
     *
     * @param noSdjVipPhones
     */
    void updateSdjVipPast(List<String> noSdjVipPhones);

    /**
     * 获取添加用户信息
     *
     * @param param
     * @return
     */
    List<VOAddVipUserList> listAddVipUser(ParamBasic param);
}
