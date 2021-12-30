package com.luxuryadmin.enums.pro;

public enum EnumDynamicInitialize {
    YES_INITIALIZE(0, "是"),
    NO_INITIALIZE(1, "否"),
            ;

    EnumDynamicInitialize(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
