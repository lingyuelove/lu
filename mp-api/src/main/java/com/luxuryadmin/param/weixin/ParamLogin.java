package com.luxuryadmin.param.weixin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class ParamLogin {

    @ApiModelProperty(value = "登录时获取的code", required = true, name = "jsCode")
    @NotBlank(message = "jsCode不能为空")
    private String jsCode;
}
