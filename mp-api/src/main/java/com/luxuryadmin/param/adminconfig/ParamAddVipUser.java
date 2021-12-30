package com.luxuryadmin.param.adminconfig;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Data
public class ParamAddVipUser {

    @ApiModelProperty(value = "用户名（手机号）", required = true, name = "username")
    @NotBlank(message = "用户名（手机号）不能为空")
    @Pattern(regexp = "^[1][3456789][0-9]{9}$", message = "手机号格式错误!")
    private String username;

    @ApiModelProperty(value = "实际支付金额", required = true, name = "realMoney")
    @NotNull(message = "实际支付金额不能为空")
    private String realMoney;
}
