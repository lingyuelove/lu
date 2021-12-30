package com.luxuryadmin.entity.pro;

import lombok.Data;

import java.util.Date;

/**
 * 盘点商品表
 *
 * @author monkey king
 * @date   2021/04/09 10:48:12
 */
@Data
public class ProCheckProduct {
    /**
     * 主键Id
     */
    private Integer id;

    /**
     * pro_product商品id
     */
    private Integer fkProProductId;

    /**
     * pro_check的主键Id
     */
    private Integer fkProCheckId;

    /**
     * 状态设置 已盘点yes 未盘点no
     */
    private String checkState;

    /**
     * 盘点类型 缺失:0  存在:1
     */
    private String checkType;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 盘点时间
     */
    private Date updateTime;

    /**
     * 盘点人
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