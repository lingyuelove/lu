package com.luxuryadmin.enums;


public enum EnumIsMember {

    /**
     * 是会员
     */
    YES("yes", "是会员"),
    /**
     * 不是会员
     */
    NO("no", "不是会员");

    EnumIsMember(String code, String msg) {
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
