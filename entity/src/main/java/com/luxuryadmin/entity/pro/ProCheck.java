package com.luxuryadmin.entity.pro;

import lombok.Data;

import java.util.Date;

/**
 * 盘点表
 *
 * @author monkey king
 * @date   2021/04/09 10:45:48
 */
@Data
public class ProCheck {
    /**
     * 主键Id
     */
    private Integer id;

    /**
     * shp_shop店铺id
     */
    private Integer fkShpShopId;
    /**
     * pro_temp的id字段,主键id
     */
    private Integer fkProTempId;
    /**
     * 盘点类型 temp:临时仓； warehouse:仓库
     */
    private String type;
    /**
     * 盘点名称
     */
    private String checkName;

    /**
     * 盘点状态 10:进行中 | 20:取消 | 30:完成
     */
    private String checkState;

    /**
     * 盘点开始金额
     */
    private Long salePriceStart;

    /**
     * 盘点结束金额
     */
    private Long salePriceEnd;

    /**
     * 产品属性表的code集合
     */
    private String fkProAttributeCodes;

    /**
     * 商品分类名称; 和分类列表对应; 默认0:无分类;
     */
    private String fkProClassifyCodes;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 盘点时间
     */
    private Date updateTime;

    /**
     * 盘点开始时间
     */
    private Date startTime;

    /**
     * 盘点结束时间
     */
    private Date endTime;

    /**
     * 创建盘点的用户
     */
    private Integer insertAdmin;

    /**
     * 更新用户id
     */
    private Integer updateAdmin;

    /**
     * 备注
     */
    private String remark;



}