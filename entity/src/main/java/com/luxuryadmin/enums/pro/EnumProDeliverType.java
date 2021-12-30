package com.luxuryadmin.enums.pro;


/**
 * 发货表中发货方式
 *
 * @author taoqimin
 * @date 2020-09-25
 */
public enum EnumProDeliverType {

    ME_CLAIM("ME_CLAIM","自取"),
    FLASH_SEND("FLASH_SEND","闪送"),

    SF_EXPRESS("SF_EXPRESS", "顺丰"),
    OTHER_PEOPLE_TAKE("OTHER_PEOPLE_TAKE", "他人代取"),

    OTHER("OTHER", "其他"),
    ;

    EnumProDeliverType(String code, String msg) {
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
