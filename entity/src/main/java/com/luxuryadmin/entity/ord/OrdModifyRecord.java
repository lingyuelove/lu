package com.luxuryadmin.entity.ord;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单修改记录表
 *
 * @author monkey king
 * @date   2020/09/22 17:21:57
 */
@Data
public class OrdModifyRecord {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 订单ID
     */
    private Integer fkOrdOrderId;

    /**
     * 店铺ID
     */
    private Integer fkShpShopId;

    /**
     * 商品ID
     */
    private Integer fkProProductId;

    /**
     * 创建订单用户ID
     */
    private Integer createUserId;

    /**
     * 修改订单用户ID
     */
    private Integer updateUserId;

    /**
     * 独立编码（修改前）
     */
    private String uniqueCodeBefore;

    /**
     * 独立编码（修改后）
     */
    private String uniqueCodeAfter;

    /**
     * 订单类型（修改前）
     */
    private String typeBefore;

    /**
     * 订单类型（修改后）
     */
    private String typeAfter;

    /**
     * 成交价格（修改前）
     */
    private BigDecimal finishPriceBefore;

    /**
     * 成交价格（修改后）
     */
    private BigDecimal finishPriceAfter;

    /**
     * 销售人员名称(修改前)
     */
    private Integer saleUserIdBefore;

    /**
     * 销售人员名称(修改后)
     */
    private Integer saleUserIdAfter;

    /**
     * 售后保障（修改前）
     */
    private String afterSaleGuaranteeBefore;

    /**
     * 售后保障（修改后）
     */
    private String afterSaleGuaranteeAfter;

    /**
     * 订单备注（修改前）
     */
    private String remarkBefore;

    /**
     * 订单备注（修改后）
     */
    private String remarkAfter;

    /**
     * 收货信息（修改前）
     */
    private String addressBefore;

    /**
     * 收货信息（修改后）
     */
    private String addressAfter;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;
}