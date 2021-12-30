package com.luxuryadmin.param.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 密码登录--前端接收参数模型
 *
 * @author monkey king
 * @Date 2019-12-26 02:15:20
 */
@ApiModel(description = "密码登录")
public class ParamLoginPwd extends ParamUsername {

    /**
     * md5格式的密码
     */
    @ApiModelProperty(value = "md5格式的密码", name = "password", required = true)
    //@Pattern(regexp = "^[a-fA-F0-9]{32}$", message = "密码格式错误")
    @NotBlank(message = "密码不允许为空")
    private String password;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
