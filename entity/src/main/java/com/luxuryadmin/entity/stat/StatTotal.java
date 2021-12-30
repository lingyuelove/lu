package com.luxuryadmin.entity.stat;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 运营统计-大盘表
 *
 * @author sanjin145
 * @date   2020/12/15 16:18:43
 */
@Data
public class StatTotal {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 统计日期
     */
    private String countDate;

    /**
     * 统计月份
     */
    private String countMonth;

    /**
     * 统计年份
     */
    private String countYear;

    /**
     * 注册人数
     */
    private Integer regPersonNum;

    /**
     * 注册店铺数
     */
    private Integer regShopNum;

    /**
     * 订单量
     */
    private Integer orderNum;

    /**
     * 订单销售额
     */
    private BigDecimal orderSellAmount;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}