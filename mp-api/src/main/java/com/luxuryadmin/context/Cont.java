package com.luxuryadmin.context;

import java.util.UUID;

public class Cont {

    public static final String MP_USER = "mp:user:";

    public static final String USER_ID = ":user_id";

    public static final String END_TIME = ":end_time";

    public static final String SDJ_VIP = ":sdj_vip";

    public static final String USERNAME = ":username";

    public static final String PAY_END_TIME = ":pay_end_time";
    /**
     * 过期时间
     */
    public static final String MP_BAOYANG_CONFIG_EXPERIENCETIME = "mp:baoyang:config:experienceTime";

    /**
     * 奖励时间
     */
    public static final String MP_BAOYANG_CONFIG_AWARDTIME = "mp:baoyang:config:awardTime";

    /**
     * 白名单
     */
    public static final String MP_BAOYANG_CONFIG_WHITELIST = "mp:baoyang:config:whiteList";

    /**
     * 云仓编号
     */
    public static final String MP_BAOYANG_CONFIG_CLOUDWAREHOUSE = "mp:baoyang:config:cloudWarehouse";

    /**
     * 店铺编号
     */
    public static final String MP_BAOYANG_CONFIG_SHOPNUM = "mp:baoyang:config:shopNum";



    /**
     * 首页地址
     */
    public static final String MP_BAOYANG_CONFIG_HOMEURL = "mp:baoyang:config:homeUrl";
    /**
     * appid
     */
    public static final String MP_BAOYANG_CONFIG_APPID = "mp:baoyang:config:addId";

    /**
     * token过期时间
     */
    public static final Integer day = 30;

    public static final String IS_SDJ_VIP = "yes";

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
