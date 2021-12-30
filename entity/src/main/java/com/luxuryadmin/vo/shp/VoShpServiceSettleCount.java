package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 店铺日报每日维修保养计算数据VO
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoShpServiceSettleCount {

    /**
     * 结算数量
     */
    private Integer serviceSettleNum;

    /**
     * 结算销售金额
     */
    private BigDecimal serviceSettleSellAmount;

    /**
     * 结算成本
     */
    private BigDecimal serviceSettleCostAmount;

    /**
     * 结算收益
     */
    private BigDecimal serviceSettleProfitAmount;

}
