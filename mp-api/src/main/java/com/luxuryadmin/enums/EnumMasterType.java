package com.luxuryadmin.enums;

/**
 * 主体类型枚举
 */
public enum EnumMasterType {


    BY(59800, "鲍杨小程序");

    EnumMasterType(Integer money, String msg) {
        this.money = money;
        this.msg = msg;
    }

    private Integer money;

    private String msg;

    public Integer getMoney() {
        return money;
    }

    public String getMsg() {
        return msg;
    }
}