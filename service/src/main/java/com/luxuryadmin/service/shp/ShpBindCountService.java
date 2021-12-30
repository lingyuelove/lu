package com.luxuryadmin.service.shp;

import com.luxuryadmin.entity.shp.ShpBindCount;
import com.luxuryadmin.enums.login.EnumOtherLoginType;
import com.luxuryadmin.vo.shp.VoWeChatUserinfo;

/**
 * @author monkey king
 * @date 2020-08-07 18:16:48
 */
public interface ShpBindCountService {

    /**
     * 保存绑定记录
     *
     * @param userId
     * @param username
     * @param openId
     * @param type
     */
    void saveBindCount(int userId, String username, String openId, EnumOtherLoginType type);

    /**
     * 保存绑定记录
     *
     * @param shpBindCount
     */
    void saveOrUpdateBindCount(ShpBindCount shpBindCount);

    /**
     * 是否存在已绑定的用户
     *
     * @param type
     * @param username
     * @return
     */
    boolean existsBindCount(EnumOtherLoginType type, String username);

    /**
     * 是否存在已绑定的用户
     *
     * @param type
     * @param openId
     * @return
     */
    ShpBindCount getBindCountByOpenId(EnumOtherLoginType type, String openId);


    /**
     * 获取实体
     *
     * @param type     类型; wx:微信
     * @param username 绑定的手机号
     * @return
     */
    ShpBindCount getBindCountByUsername(EnumOtherLoginType type, String username);

    /**
     * 微信登录
     *
     * @param appId
     * @param appSecret
     * @param code
     * @return ShpBindCount
     * @throws Exception
     */
    ShpBindCount bindWeChatLogin(String appId, String appSecret, String code) throws Exception;

    /**
     * 获取微信用户信息
     *
     * @param accessToken
     * @param openId
     * @return
     * @throws Exception
     */
    public VoWeChatUserinfo getWeChatUserinfo(String accessToken, String openId) throws Exception;

    /**
     * 授权之后更新用户信息
     *
     * @param openId
     * @param accessToken
     * @param refreshToken
     * @param sbc
     * @param type
     * @param userId
     * @throws Exception
     */
    public void updateUserinfoFromWeChat(
            String openId, String accessToken, String refreshToken,
            ShpBindCount sbc, EnumOtherLoginType type, int userId) throws Exception;
}
