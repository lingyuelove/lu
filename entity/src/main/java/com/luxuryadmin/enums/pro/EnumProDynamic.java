package com.luxuryadmin.enums.pro;


/**
 * 初始化商品位置信息
 *
 * @author taoqimin
 * @date 2020-09-25
 */
public enum EnumProDynamic {

    ON_PASSAGE(1, "在途", "/default/pro/dynamic/intransit.png"),
    IN_SHOP(2, "在店", "/default/pro/dynamic/shop.png"),
    MAINTENANCE(3, "维修养护", "/default/pro/dynamic/service.png");

    EnumProDynamic(Integer code, String msg, String url) {
        this.code = code;
        this.msg = msg;
        this.url = url;
    }

    private Integer code;

    private String msg;

    private String url;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getUrl() {
        return url;
    }
}

