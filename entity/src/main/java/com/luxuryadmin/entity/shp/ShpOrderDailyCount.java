package com.luxuryadmin.entity.shp;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 店铺订单每日统计
 *
 * @author monkey king
 * @date   2020/09/14 16:47:35
 */
@Data
public class ShpOrderDailyCount {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 统计日期
     */
    private Date countDate;

    /**
     * 统计月份
     */
    private Integer countMonth;

    /**
     * 统计年份
     */
    private Integer countYear;

    /**
     * 店铺ID
     */
    private Integer fkShpShopId;

    /**
     * 今日销售总量
     */
    private Integer ordNum;

    /**
     * 今日销售总额
     */
    private BigDecimal ordAmount;

    /**
     * 今日净利润
     */
    private BigDecimal ordNetProfit;

    /**
     * 今日退货总量
     */
    private Integer ordCancelNum;

    /**
     * 今日退货总额
     */
    private BigDecimal ordCancelAmount;

    /**
     * 今日【自有商品】入库总量
     */
    private Integer inRepoNumSelf;

    /**
     * 今日【寄卖商品】入库总量
     */
    private Integer inRepoNumConsignment;

    /**
     * 今日【质押商品】入库总量
     */
    private Integer inRepoNumPledge;

    /**
     * 今日【其它商品】入库总量
     */
    private Integer inRepoNumOther;

    /**
     * 今日【自有商品】入库总额
     */
    private BigDecimal inRepoAmountSelf;

    /**
     * 今日【寄卖商品】入库总额
     */
    private BigDecimal inRepoAmountConsignment;

    /**
     * 今日【质押商品】入库总额
     */
    private BigDecimal inRepoAmountPledge;

    /**
     * 今日【其它商品】入库总额
     */
    private BigDecimal inRepoAmountOther;

    /**
     * 今日上架总量
     */
    private Integer inRepoNumTotal;

    /**
     * 今日上架总额
     */
    private BigDecimal inRepoAmountTotal;

    /**
     * 维修保养结算总量
     */
    private Integer serviceSettleNum;

    /**
     *
     */
    private BigDecimal serviceSettleSellAmount;

    /**
     * 维修保养结算成本总额
     */
    private BigDecimal serviceSettleCostAmount;

    /**
     * 维修保养结算收益
     */
    private BigDecimal serviceSettleProfitAmount;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 上架商品数量
     */
    private Integer onshelvesProdNum;
}