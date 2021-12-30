package com.luxuryadmin.param.pay;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ZhangSai
 * @Classname 会员店铺查询类
 * @Description TODO
 * @Date 2021/3/30 15:57
 * @Created by ZhangSai
 */
@Data
public class ParamAddPayOrderForAdmin {

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID", name = "fkShpShopId", required = true)
    private Integer fkShpShopId;

    /**
     * 第三方支付订单号
     */
    @ApiModelProperty(value = "第三方支付订单号", name = "transactionId", required = false)
    private String transactionId;

    /**
     * 总金额(分)
     */
    @ApiModelProperty(value = "总金额(分)", name = "totalMoney", required = false)
    private Long totalMoney;

    /**
     * 实际支付金额(分)
     */
    @ApiModelProperty(value = "实际支付金额(分)", name = "realMoney", required = true)
    private Long realMoney;

    /**
     * 付费月数:  1:月付; 3:季付; 6:半年付; 12:年付; 36:三年付;
     */
    @ApiModelProperty(value = "付费月数:  1:月付; 3:季付; 6:半年付; 12:年付; 36:三年付;", name = "payMonth", required = true)
    private Integer payMonth;

    /**
     * 是否是会员 yes|是会员(付费会员) no|不是会员(或者体验会员)
     */
    @ApiModelProperty(value = "是否是会员 yes|是会员(付费会员) no|不是会员(或者体验会员)", name = "isMember", required = true)
    private String isMember;
}
