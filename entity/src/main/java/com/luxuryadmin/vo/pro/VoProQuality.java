package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 商品成色表--VO模型
 *
 * @author monkey king
 * @date 2020-05-27 17:57:06
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoProQuality {
    /**
     * 状态码: N,S,A,B,C,D
     */
    private String code;

    /**
     * 名称:全新,99新
     */
    private String name;

    /**
     * 成色描述
     */
    private String description;


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
