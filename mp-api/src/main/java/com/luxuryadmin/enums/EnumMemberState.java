package com.luxuryadmin.enums;


public enum EnumMemberState {

    /**
     * 0:非会员;
     */
    mistake_vip(0, "非会员"),

    /**
     * 1:体验会员;
     */
    experience_vip(1, "体验会员"),

    /**
     * 2:正式会员;
     */
    official_vip(2, "正式会员"),
    /**
     * 3:靓号会员
     */
    good_number_vip(3, "靓号会员");

    EnumMemberState(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;

    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
