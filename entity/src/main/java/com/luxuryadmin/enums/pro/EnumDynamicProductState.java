package com.luxuryadmin.enums.pro;


/**
 * 动态列表商品状态
 *
 * @author taoqimin
 * @date 2020-09-25
 */
public enum EnumDynamicProductState {

    NORMAL(10, "正常"),
    SOLD_OUT(20, "商品已售罄"),
    ALREADY_LOCK(30, "商品已锁单"),
    ALREADY_DELETE(40, "商品已删除"),
    ;

    EnumDynamicProductState(Integer code, String msg) {
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
