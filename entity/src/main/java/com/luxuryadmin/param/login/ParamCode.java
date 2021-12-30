package com.luxuryadmin.param.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 第三方登录--前端接收参数模型
 *
 * @author monkey king
 * @Date 2019-12-26 02:15:20
 */
@ApiModel(description = "第三方登录")
public class ParamCode {

    /**
     * 第三方的唯一标识符
     */
    @ApiModelProperty(value = "第三方的唯一标识(如微信从用户授权得到的code，或者苹果授权返回的唯一标识符)",
            name = "code", required = true)
    @Length(max = 50, message = "code长度超限")
    @NotBlank(message = "code不允许为空")
    private String code;

    /**
     * 0:appleId登录; 1:微信授权登录
     */
    @ApiModelProperty(value = "0:苹果登录 | 1:微信登录", name = "bindType", required = true)
    @Pattern(regexp = "^[01]", message = "0:苹果登录 | 1:微信登录")
    @NotBlank(message = "bindType参数错误")
    private String bindType;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBindType() {
        return bindType;
    }

    public void setBindType(String bindType) {
        this.bindType = bindType;
    }
}
