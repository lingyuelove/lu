package com.luxuryadmin.enums;


public enum EnumVipType {

    /**
     * 小程序会员
     */
    MP_VIP("mpvip", "小程序会员"),
    /**
     * 奢当家会员
     */
    SDJ_VIP("sdjvip", "奢当家会员");

    EnumVipType(String code, String msg) {
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
