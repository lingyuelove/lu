package com.luxuryadmin.param.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 原密码修改--前端接收参数模型
 *
 * @author monkey king
 * @Date 2019-12-26 02:15:20
 */
@ApiModel(description = "原密码修改")
public class ParamModifyPwd {

    /**
     * 登录标识符
     */
    @ApiModelProperty(name = "token", value = "登录标识符", required = true)
    @NotBlank(message = "[token]--参数错误!")
    private String token;

    /**
     * md5格式的密码
     */
    @ApiModelProperty(value = "md5格式的密码", name = "oldPassword", required = true)
    @Pattern(regexp = "^[a-fA-F0-9]{32}$", message = "原密码格式错误")
    @NotBlank(message = "原密码不允许为空")
    private String oldPassword;

    /**
     * md5格式的密码
     */
    @ApiModelProperty(value = "md5格式的密码", name = "newPassword", required = true)
    @Pattern(regexp = "^[a-fA-F0-9]{32}$", message = "新密码格式错误")
    @NotBlank(message = "新密码不允许为空")
    private String newPassword;

    /**
     * 用户名
     */
    @ApiModelProperty(hidden = true)
    private String username;

    /**
     * 用户id
     */
    @ApiModelProperty(hidden = true)
    private Integer userId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
