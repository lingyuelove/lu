package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProShareSeeUser;
import com.luxuryadmin.param.pro.ParamShareUser;
import com.luxuryadmin.vo.pro.VoShareUserList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 小程序访客记录表 dao
 *
 * @author zhangsai
 * @Date 2021-07-06 14:11:33
 */
@Mapper
public interface ProShareSeeUserMapper extends BaseMapper {


    /**
     * 根据用户openId和分享id获取详情
     *
     * @param openId
     * @param shareId
     * @return
     */
    ProShareSeeUser getShareSeeUserByOpenIdAndShareId(@Param("openId") String openId, @Param("shareId") Integer shareId);

    /**
     * 获取分享用户信息列表
     *
     * @param paramShareUser
     * @return
     */
    List<VoShareUserList> getShareUserList(ParamShareUser paramShareUser);


    /**
     * 该微信用户是否已存在
     *
     * @param type   0:【LuxurySir】小程序访客;1:【奢当家】小程序访客
     * @param openId 微信的开放id
     * @return 0:false; >0:true
     */
    int existsWxUserByOpenId(@Param("type") String type, @Param("openId") String openId);

    /**
     * 该微信用户是否存在--根据手机号
     *
     * @param type
     * @param phone
     * @return
     */
    Integer existsWxUserReturnUserId(@Param("type") String type, @Param("phone") String phone);

    /**
     * 当日小程序访问人数
     * @param userId
     * @return
     */
    Integer getVisitShopAppletCount(Integer userId);
}
