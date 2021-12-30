package com.luxuryadmin.entity.shp;

import lombok.Data;

import java.util.Date;

/**
 * 店铺编号池
 *
 * @author monkey king
 * @date   2019/12/19 16:22:21
 */
@Data
public class ShpShopNumber {
    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer shopId;

    /**
     * 编号
     */
    private String number;

    /**
     * 编号状态：0:未使用；1:已使用; 2:已弃用;
     */
    private String state;

    /**
     * 靓号状态：10:普通号; 20:豹子号(所有都一样aaaaa);21:连号(abcde);22:多带1(aaaaab);23多带2(aaabb);50:特殊数字靓号(例如5201314)
     */
    private String nice;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 版本号;用于更新时对比操作;
     */
    private Integer versions;

    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
     */
    private String del;

    /**
     * 备注
     */
    private String remark;

}