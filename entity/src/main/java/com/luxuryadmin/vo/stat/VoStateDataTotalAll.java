package com.luxuryadmin.vo.stat;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoStateDataTotalAll {

    /**
     * 总注册人数
     */
    @ApiModelProperty(name = "regPersonNumTotal", required = false, value = "总注册人数")
    private String regPersonNumTotal;

    /**
     * 总注册店铺数
     */
    @ApiModelProperty(name = "regShopNumTotal", required = false, value = "总注册店铺数")
    private String regShopNumTotal;

    /**
     * 总订单量
     */
    @ApiModelProperty(name = "orderNumTotal", required = false, value = "总订单量")
    private String orderNumTotal;

    /**
     * 总订单销售额
     */
    @ApiModelProperty(name = "orderSellAmountTotal", required = false, value = "总订单销售额")
    private String orderSellAmountTotal;

}
