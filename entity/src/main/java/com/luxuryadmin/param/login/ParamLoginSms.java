package com.luxuryadmin.param.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 验证码登录--前端接收参数模型
 *
 * @author monkey king
 * @Date 2019-12-26 02:15:20
 */
@ApiModel(description = "验证码登录")
public class ParamLoginSms extends ParamUsername {

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码", name = "smsCode", required = true)
    @Pattern(regexp = "^\\d{6}$", message = "验证码格式错误")
    @NotBlank(message = "验证码不允许为空")
    private String smsCode;

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
