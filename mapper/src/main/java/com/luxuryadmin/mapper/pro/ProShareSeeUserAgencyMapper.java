package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProShareSeeUserAgency;
import com.luxuryadmin.vo.pro.VoProShareSeeUserAgency;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author monkey king
 * @date 2021-08-28 17:24:59
 */
@Mapper
public interface ProShareSeeUserAgencyMapper extends BaseMapper {


    /**
     * 该微信用户是否已存在
     *
     * @param type   0:【LuxurySir】小程序访客;1:【奢当家】小程序访客
     * @param openId 微信的开放id
     * @return 0:false; >0:true
     */
    int existsWxUserByOpenId(@Param("type") String type, @Param("openId") String openId);

    /**
     * 获取访问小程序用户
     *
     * @param userId
     * @return
     */
    List<VoProShareSeeUserAgency> listVoProShareSeeUserAgency(String userId);

    /**
     * 该微信用户是否已存在
     *
     * @param type  0:【LuxurySir】小程序访客;1:【奢当家】小程序访客
     * @param phone 手机号
     * @return 0:false; >0:true
     */
    int existsWxUserByPhone(@Param("type") String type, @Param("phone") String phone);

    /**
     * 根据手机号 获取 实体
     *
     * @param type  0:【LuxurySir】小程序访客;1:【奢当家】小程序访客
     * @param phone
     * @return
     */
    ProShareSeeUserAgency getObjectByPhone(@Param("type") String type, @Param("phone") String phone);

    /**
     * 当日新用户访问人数
     * @param userId
     * @return
     */
    Integer getNewVisitAppletCount(Integer userId);

}
