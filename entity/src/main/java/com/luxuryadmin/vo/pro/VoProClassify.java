package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author monkey king
 * @date 2019-12-18 20:20:30
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoProClassify {

    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * 模板id+店铺编号+本店增加id个数(字符串拼接);eg:WB100001
     */
    private String code;

    /**
     * 分类名称;限长10个汉字
     */
    private String name;

    /**
     * 状态;-1:已删除; 0:未使用; 1:使用中
     */
    private Integer state;

    /**
     * 序号排序
     */
    private Integer sort;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
