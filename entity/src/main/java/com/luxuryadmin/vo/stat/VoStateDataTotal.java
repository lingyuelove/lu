package com.luxuryadmin.vo.stat;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoStateDataTotal {

    /**
     * 最后更新时间
     */
    @ApiModelProperty(name = "newestUpdateTime", required = false, value = "最后更新时间")
    private String newestUpdateTime;

    /**************************************  1.注册人数  **************************************/
    /**
     * 今日注册人数
     */
    @ApiModelProperty(name = "regPersonNumToday", required = false, value = "今日注册人数")
    private String regPersonNumToday;

    /**
     * 总注册人数
     */
    @ApiModelProperty(name = "regPersonNumTotal", required = false, value = "总注册人数")
    private String regPersonNumTotal;

    /**
     * 注册人数-日环比
     */
    @ApiModelProperty(name = "regPersonNumChangeRatio", required = false, value = "注册人数-日环比")
    private String regPersonNumChangeRatio;

    /**
     * 注册人数-日环比-符号
     */
    @ApiModelProperty(name = "regPersonNumSign", required = false, value = "注册人数-日环比-符号,【+】表示上涨，【-】表示下降")
    private String regPersonNumSign;

    /**************************************  2.注册店铺数  **************************************/
    /**
     * 今日注册店铺数
     */
    @ApiModelProperty(name = "regShopNumToday", required = false, value = "今日注册店铺数")
    private String regShopNumToday;

    /**
     * 总注册店铺数
     */
    @ApiModelProperty(name = "regShopNumTotal", required = false, value = "总注册店铺数")
    private String regShopNumTotal;

    /**
     * 注册店铺数-日环比
     */
    @ApiModelProperty(name = "regShopNumChangeRatio", required = false, value = "注册店铺数-日环比")
    private String regShopNumChangeRatio;

    /**
     * 注册店铺数-日环比-符号
     */
    @ApiModelProperty(name = "regShopNumSign", required = false, value = "注册店铺数-日环比-符号,【+】表示上涨，【-】表示下降")
    private String regShopNumSign;

    /**************************************  3.订单量  **************************************/
    /**
     * 今日订单量
     */
    @ApiModelProperty(name = "orderNumToday", required = false, value = "今日订单量")
    private String orderNumToday;

    /**
     * 总订单量
     */
    @ApiModelProperty(name = "orderNumTotal", required = false, value = "总订单量")
    private String orderNumTotal;

    /**
     * 订单量-日环比
     */
    @ApiModelProperty(name = "orderNumChangeRatio", required = false, value = "订单量-日环比")
    private String orderNumChangeRatio;

    /**
     * 订单量-日环比-符号
     */
    @ApiModelProperty(name = "orderNumSign", required = false, value = "订单量-日环比-符号,【+】表示上涨，【-】表示下降")
    private String orderNumSign;

    /**************************************  4.订单销售额  **************************************/
    /**
     * 今日订单销售额
     */
    @ApiModelProperty(name = "orderSellAmountToday", required = false, value = "今日订单销售额")
    private String orderSellAmountToday;

    /**
     * 总订单销售额
     */
    @ApiModelProperty(name = "orderSellAmountTotal", required = false, value = "总订单销售额")
    private String orderSellAmountTotal;

    /**
     * 订单销售额-日环比
     */
    @ApiModelProperty(name = "orderSellAmountChangeRatio", required = false, value = "订单销售额-日环比")
    private String orderSellAmountChangeRatio;

    /**
     * 订单销售额-日环比-符号
     */
    @ApiModelProperty(name = "orderSellAmountSign", required = false, value = "订单销售额-日环比-符号,【+】表示上涨，【-】表示下降")
    private String orderSellAmountSign;

}
