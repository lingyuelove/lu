package com.luxuryadmin.vo.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoProductAndRecycleData
 * @Author: ZhangSai
 * Date: 2021/6/28 13:41
 */
@Data
@ApiModel(value="店铺月报统计类", description="店铺月报统计类")
public class VoShpOrderDailyCountForMonth {


    /**
     * 统计日期
     */
    @ApiModelProperty(name = "countDate", value = "统计数据-统计日期")
    private Date countDate;

    /**
     * 统计月份
     */
    @ApiModelProperty(name = "countMonth", value = "统计数据-统计月份")
    private Integer countMonth;

    /**
     * 店铺ID
     */
    @ApiModelProperty(name = "shopId", value = "统计数据-店铺ID")
    private Integer shopId;

    /**
     * 今日销售总量
     */
    @ApiModelProperty(name = "ordNum", value = "统计数据-销售总量")
    private Integer ordNum;

    /**
     * 今日销售总额
     */
    @ApiModelProperty(name = "ordAmount", value = "统计数据-销售总额")
    private BigDecimal ordAmount;

    /**
     * 今日净利润
     */
    @ApiModelProperty(name = "ordNetProfit", value = "统计数据-净利润")
    private BigDecimal ordNetProfit;

    /**
     * 今日退货总量
     */
    @ApiModelProperty(name = "ordCancelNum", value = "统计数据-退货总量")
    private Integer ordCancelNum;

    /**
     * 今日退货总额
     */
    @ApiModelProperty(name = "ordCancelAmount", value = "统计数据-退货总额")
    private BigDecimal ordCancelAmount;

    /**
     * 今日【自有商品】入库总量
     */
    @ApiModelProperty(name = "inRepoNumSelf", value = "统计数据-自有商品入库总量")
    private Integer inRepoNumSelf;

    /**
     * 今日【寄卖商品】入库总量
     */
    @ApiModelProperty(name = "inRepoNumConsignment", value = "统计数据-寄卖商品入库总量")
    private Integer inRepoNumConsignment;

    /**
     * 今日【质押商品】入库总量
     */
    @ApiModelProperty(name = "inRepoNumPledge", value = "统计数据-质押商品 入库总量")
    private Integer inRepoNumPledge;

    /**
     * 今日【其它商品】入库总量
     */
    @ApiModelProperty(name = "inRepoNumOther", value = "统计数据-其它商品入库总量")
    private Integer inRepoNumOther;

    /**
     * 今日【自有商品】入库总额
     */
    @ApiModelProperty(name = "inRepoAmountSelf", value = "统计数据-自有商品 入库总额")
    private BigDecimal inRepoAmountSelf;

    /**
     * 今日【寄卖商品】入库总额
     */
    @ApiModelProperty(name = "inRepoAmountConsignment", value = "统计数据-寄卖商品 入库总额")
    private BigDecimal inRepoAmountConsignment;

    /**
     * 今日【质押商品】入库总额
     */
    @ApiModelProperty(name = "inRepoAmountPledge", value = "统计数据-质押商品 入库总额")
    private BigDecimal inRepoAmountPledge;

    /**
     * 今日【其它商品】入库总额
     */
    @ApiModelProperty(name = "inRepoAmountOther", value = "统计数据-其它商品 入库总额")
    private BigDecimal inRepoAmountOther;

    /**
     * 今日上架总量
     */
    @ApiModelProperty(name = "inRepoNumTotal", value = "统计数据-上架总量")
    private Integer inRepoNumTotal;

    /**
     * 今日上架总额
     */
    @ApiModelProperty(name = "inRepoAmountTotal", value = "统计数据-上架总额")
    private BigDecimal inRepoAmountTotal;

    /**
     * 维修保养结算总量
     */
    @ApiModelProperty(name = "serviceSettleNum", value = "统计数据-维修保养结算总量")
    private Integer serviceSettleNum;

    /**
     *维修保养结算销售总额
     */
    @ApiModelProperty(name = "serviceSettleSellAmount", value = "统计数据-维修保养结算销售总额")
    private BigDecimal serviceSettleSellAmount;

    /**
     * 维修保养结算成本总额
     */
    @ApiModelProperty(name = "serviceSettleCostAmount", value = "统计数据-维修保养结算成本总额")
    private BigDecimal serviceSettleCostAmount;

    /**
     * 维修保养结算收益
     */
    @ApiModelProperty(name = "serviceSettleProfitAmount", value = "统计数据-维修保养结算收益")
    private BigDecimal serviceSettleProfitAmount;


    /**
     * 上架商品数量
     */
    @ApiModelProperty(name = "onshelvesProdNum", value = "统计数据-上架商品数量")
    private Integer onshelvesProdNum;
}
