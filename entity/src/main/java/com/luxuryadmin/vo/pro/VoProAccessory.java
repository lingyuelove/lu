package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 商品附件表--vo模型
 *
 * @author monkey king
 * @date 2020-05-28 10:54:40
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoProAccessory {

    private Integer id;

    /**
     * 附件名称
     */
    private String name;


    /**
     * 附件排序
     */
    private Integer sort;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
