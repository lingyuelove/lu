package com.luxuryadmin.param.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 绑定微信帐号--前端接收参数模型
 *
 * @author monkey king
 * @Date 2019-12-26 02:15:20
 */
@ApiModel(description = "绑定微信帐号")
public class ParamBindCount extends ParamLoginSms {

    /**
     * 微信的code,获取code之后,在服务端再次请求微信服务器用code获取openId
     */
    @ApiModelProperty(value = "微信的code", name = "code", required = true)
    @Length(max = 50, message = "code长度超限")
    @NotBlank(message = "code不允许为空")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
