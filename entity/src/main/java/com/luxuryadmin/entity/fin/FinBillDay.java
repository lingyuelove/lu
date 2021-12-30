package com.luxuryadmin.entity.fin;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 帐单日统计表
 *
 * @author zhangSai
 * @date   2021/04/23 14:19:52
 */
@Data
public class FinBillDay {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 店铺id
     */
    private Integer fkShpShopId;

    /**
     * 账单id
     */
    private Integer fkFinBillId;

    /**
     * 总成本
     */
    private BigDecimal totalMoney;

    /**
     * 现金
     */
    private BigDecimal cashMoney;

    /**
     * 商品成本
     */
    private BigDecimal productMoney;

    /**
     * 销售利润
     */
    private BigDecimal profitMoney;

    /**
     * 薪资支出
     */
    private BigDecimal salaryMoney;

    /**
     * 其他收支
     */
    private BigDecimal otherMoney;

    /**
     * 维修收支
     */
    private BigDecimal serviceMoney;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 统计时间(业务,统计前一天)
     */
    private Date bizTime;

    /**
     * 备注
     */
    private String remark;

}
