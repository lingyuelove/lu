package com.luxuryadmin.enums;


public enum EnumUserState {

    /**
     * 0：禁用
     */
    FORBIDDEN("0", "禁用"),
    /**
     * 正常
     */
    NORMAL("1", "正常");

    EnumUserState(String code, String msg) {
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
