package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

/**
 * 商品销售渠道----实体接收参数模型
 *
 * @author monkey king
 * @date 2020-05-27 16:11:56
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoProSaleChannel {

    private Integer id;

    /**
     * 渠道名称;
     */
    private String name;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer shopId;

    /**
     * 状态;-1:已删除;0:未使用;1:使用中
     */
    private Integer state;

    /**
     * 序号排序
     */
    private Integer sort;

    /**
     * 插入时间
     */
    private Date insertTime;


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

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
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

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
}
