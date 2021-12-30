package com.luxuryadmin.common.constant;

/**
 * @author monkey king
 * @date 2021-04-14 17:52:53
 */
public class RedisKeyAdminLogin {


    /**
     * shp_user的token-key值;<br/>
     * 拼接用户名使用<br/>
     * eg: SHP_TOKEN_+"15112304365";
     */
    private static final String SHP_TOKEN_ = "shp:token:";

    private static final String ADMIN = "admin";


    //--------------------------------后台登录的key-------//

    /**
     * shp_user的token-key值;<br/>
     * eg: shp:token:15112304365:admin<br/>
     *
     * @param username
     * @return
     */
    public static String getShpAdminTokenKey(String username) {
        return SHP_TOKEN_ + username + "_" + ADMIN;
    }

    public static String getShopIdRedisKeyByToken(String token) {
        return getShpTokenByToken(token, ":shop_id");
    }

    public static String getUserNumberRedisKeyByToken(String token) {
        return getShpTokenByToken(token, ":user_number");
    }

    public static String getShpUserIdRedisKeyByToken(String token) {
        return getShpTokenByToken(token, ":user_id");
    }

    public static String getShpUsernameRedisKeyByToken(String token) {
        return getShpTokenByToken(token, ":username");
    }

    private static String getShpTokenByToken(String token, String key) {
        return SHP_TOKEN_ + ADMIN + ":" + token + key;
    }
}
