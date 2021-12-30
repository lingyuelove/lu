package com.luxuryadmin.service;


import com.luxuryadmin.param.weixin.ParamGetPhone;
import com.luxuryadmin.param.weixin.ParamLogin;
import com.luxuryadmin.vo.wx.VOLogin;
import com.luxuryadmin.vo.wx.VOWeixinPhoneDecryptInfo;

import javax.servlet.http.HttpServletRequest;

public interface WeiXinLoginService {

    /**
     * 微信登录
     *
     * @param paramLogin
     */
    VOLogin wxLogin(ParamLogin paramLogin);

    /**
     * 微信第三方获取手机号
     *
     * @param param
     * @return
     */
    VOWeixinPhoneDecryptInfo getPhoneInfo(ParamGetPhone param, HttpServletRequest request);
}
