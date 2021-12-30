package com.luxuryadmin.vo.fin;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author monkey king
 * @date 2020-09-23 23:59:09
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoFinSalaryDetail {

    /**
     * 10:自有商品; 20:寄卖商品
     */
    private String proAttr;

    /**
     * 金额(分)
     */
    private BigDecimal money;

    /**
     * 销售总额(分)
     */
    private BigDecimal saleMoney;

    /**
     * 毛利润总额(分)
     */
    private BigDecimal grossProfitMoney;

    /**
     * 回收成本总额(分)
     */
    private BigDecimal recycleCostMoney;

    /**
     * TA回收产生的利润
     */
    private BigDecimal recycleProfitMoney;

    /**
     * 件数
     */
    private Integer num;

    /**
     * 订单类型
     */
    private String orderType;


}
