package com.luxuryadmin.entity.pro;

import lombok.Data;

import java.util.Date;

/**
 * 商品过期提醒表
 *
 * @author ZhangSai
 * @date   2021/05/11 10:45:48
 */
@Data
public class ProExpiredNotice {
    /**
     * 主键Id
     */
    private Integer id;

    /**
     * shp_shop店铺id
     */
    private Integer fkShpShopId;

    /**
     * 产品属性名称
     */
    private String attributeName;

    /**
     * 分类名称
     */
    private String classifyName;

    /**
     * 产品属性表的code集合
     */
    private String fkProAttributeCodes;

    /**
     * 商品分类名称; 和分类列表对应; 默认0:无分类;
     */
    private String fkProClassifyCodes;

    /**
     * 设置过期天数
     */
    private Integer expiredDay;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建的用户
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
