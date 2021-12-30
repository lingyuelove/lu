package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author monkey king
 * @date 2019-12-18 20:20:30
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoProAttribute {

    /**
     * 商品属性代码;10;20;30
     */
    private String code;

    /**
     * 属性名称：10:自有商品; 20:寄卖商品; 30:质押(典当)商品
     */
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
