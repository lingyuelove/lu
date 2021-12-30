package com.luxuryadmin.service.pro;


import com.luxuryadmin.param.pro.ParamShareSeeUserAdd;

/**
 * 小程序访客记录表 service
 *
 * @author zhangsai
 * @Date 2021-07-06 14:11:33
 */
public interface ProShareSeeUserService {

    /**
     * luxurySir的访客记录
     *
     * @param shareSeeUserAdd
     */
    void addShareSeeUser(ParamShareSeeUserAdd shareSeeUserAdd);

    /**
     * 商家联盟的访客记录
     *
     * @param shareSeeUserAdd
     * @return
     */
    void addShareSeeUserForUnion(ParamShareSeeUserAdd shareSeeUserAdd);

    /**
     * 该微信用户是否已存在
     *
     * @param type   0:【LuxurySir】小程序访客;1:【奢当家】小程序访客
     * @param openId 微信的开放id
     * @return
     */
    boolean existsWxUserByOpenId(String type, String openId);


    /**
     * 该微信用户是否已存在
     *
     * @param type  0:【LuxurySir】小程序访客;1:【奢当家】小程序访客
     * @param phone 手机号
     * @return
     */
    Integer existsWxUserReturnUserId(String type, String phone);
}
