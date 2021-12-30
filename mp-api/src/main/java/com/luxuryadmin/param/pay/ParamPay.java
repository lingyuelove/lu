package com.luxuryadmin.param.pay;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class ParamPay {

    @ApiModelProperty(value = "订单编号", required = true, name = "orderNo")
    @NotNull(message = "订单编号不能为空")
    private String orderNo;
}
