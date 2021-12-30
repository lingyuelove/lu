package com.luxuryadmin.vo.sys;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author monkey king
 * @date 2020-09-07 22:23:36
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoSysEnum {

    private String code;

    private String name;

    private String description;

    private Integer sort;

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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
