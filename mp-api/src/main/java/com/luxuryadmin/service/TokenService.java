package com.luxuryadmin.service;


public interface TokenService {


    /**
     * 获取token
     *
     * @param
     */
    String getToken(String username, Integer userId);

    /**
     * 获取系统token
     *
     * @param uuid
     * @return
     */
    String getSystemUserIdKey(String uuid);

    /**
     * 清除缓存token
     *
     * @param username
     */
    void clearToken(String username);


    /**
     * 微信token
     *
     */
    String getWXToken();
}
