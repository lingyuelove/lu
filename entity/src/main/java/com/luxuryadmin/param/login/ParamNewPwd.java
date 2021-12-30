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
public class ParamNewPwd extends ParamUsername {

    /**
     * md5格式的密码
     */
    @ApiModelProperty(value = "md5格式的密码", name = "newPassword", required = true)
    @Pattern(regexp = "^[a-fA-F0-9]{32}$", message = "密码格式错误")
    @NotBlank(message = "密码不允许为空")
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


    /**
     * 当前登录者的用户id
     */
    @ApiModelProperty(hidden = true)
    private Integer tokenUserId;

    public Integer getTokenUserId() {
        return tokenUserId;
    }

    public void setTokenUserId(Integer tokenUserId) {
        this.tokenUserId = tokenUserId;
    }
}
