package com.luxuryadmin.enums;


public enum EnumOrderType {

    /**
     * 会员费
     */
    VIP("vip", "会员费"),
    /**
     * 资源包
     */
    SOURCE("source", "资源包");

    EnumOrderType(String code, String msg) {
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
