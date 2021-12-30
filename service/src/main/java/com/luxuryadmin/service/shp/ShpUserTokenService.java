package com.luxuryadmin.service.shp;

import com.luxuryadmin.entity.shp.ShpUserToken;
import com.luxuryadmin.vo.admin.shp.VoShpUserToken;

import java.util.List;

/**
 * 店铺用户token-防止缓存机制挂掉,解除用户无法登陆问题;
 *
 * @author monkey king
 * @date 2019-12-09 15:31:19
 */
public interface ShpUserTokenService {

    /**
     * 是否存在此token
     *
     * @param token 用户token
     * @return {@link ShpUserToken}
     */
    ShpUserToken existsToken(String token);

    /**
     * 添加ShpUserToken入库
     *
     * @param shpUserToken {@link ShpUserToken}
     */
    void saveShpUserToken(ShpUserToken shpUserToken);

    /**
     * 根据用户id 查询注册信息
     *
     * @param userId
     * @return
     */
    ShpUserToken getShpUserTokenByUserId(Integer userId);

    /**
     * 更新用户token的状态<br/>
     * 用户状态：0：禁用；1：正常
     *
     * @param userId
     */
    void updateShpUserTokenState(Integer userId);

    /**
     * 获取用户的登录记录
     *
     * @param userId
     * @return
     */
    List<VoShpUserToken> listShpUserToken(Integer userId);


}
