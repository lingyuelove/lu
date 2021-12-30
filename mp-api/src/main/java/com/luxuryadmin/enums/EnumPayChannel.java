package com.luxuryadmin.enums;


public enum EnumPayChannel {


    /**
     * 微信
     */
    WEIXIN("weixin", "微信"),
    /**
     * 阿里
     */
    ALIPAY("alipay", "阿里"),
    /**
     * 其他
     */
    OTHER("other", "其他");

    EnumPayChannel(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}