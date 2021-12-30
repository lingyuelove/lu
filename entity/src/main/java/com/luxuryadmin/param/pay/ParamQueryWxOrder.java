package com.luxuryadmin.param.pay;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author monkey king
 * @date 2021-03-21 20:04:35
 */
/**
 * token--前端接收参数模型
 *
 * @author monkey king
 * @Date 2020-07-29 17:21:03
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "微信支付-查询订单")
@Data
public class ParamQueryWxOrder extends ParamToken {

    /**
     * 订单编号
     */
    @ApiModelProperty(name = "orderNo", required = true, value = "订单编号")
    private String orderNo;



}
