package com.luxuryadmin.service.pro;


import com.luxuryadmin.entity.pro.ProShareSeeUserAgency;
import com.luxuryadmin.param.pro.ParamShareSeeUserAdd;
import com.luxuryadmin.vo.pro.VoProShareSeeUserAgency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 小程序访客记录表--代理专用 service
 *
 * @author zhangsai
 * @Date 2021-07-06 14:11:33
 */
public interface ProShareSeeUserAgencyService {

    /**
     * luxurySir的访客记录
     *
     * @param proShareSeeUserAgency
     */
    void saveObject(ProShareSeeUserAgency proShareSeeUserAgency);

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
    boolean existsWxUserByPhone(String type, String phone);

    /**
     * 获取访问小程序用户
     *
     * @param userId
     * @return
     */
    List<VoProShareSeeUserAgency> listVoProShareSeeUserAgency(String userId);

    /**
     * 根据手机号获取实体
     *
     * @param phone
     * @param type
     * @return
     */
    ProShareSeeUserAgency getObjectByPhone(String type, String phone);

    /**
     * 更新实体
     *
     * @param proShareSeeUserAgency
     */
    void updateObject(ProShareSeeUserAgency proShareSeeUserAgency);
}
