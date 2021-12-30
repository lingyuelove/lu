package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Classname VoShpOrderDailyCount
 * @Description TODO
 * @Date 2020/9/15 15:01
 * @Created by sanjin145
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoShpOrderDailyCount {



    /**
     * 订单数量
     */
    private Integer orderNum;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 订单收益
     */
    private BigDecimal orderNetProfit;

    public VoShpOrderDailyCount() {
        this.orderNum = 0;
        this.orderAmount = new BigDecimal(0.00);
        this.orderNetProfit = new BigDecimal(0.00);
    }

}
