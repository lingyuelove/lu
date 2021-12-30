package com.luxuryadmin.param.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Pattern;

/**
 * 注册账号--前端接收参数模型
 *
 * @author monkey king
 * @Date 2019-12-26 02:15:20
 */
@ApiModel(description = "注册账号")
public class ParamRegister extends ParamLoginSms {


    /**
     * 邀请码
     */
    @ApiModelProperty(value = "邀请码", name = "inviteCode", required = false)
    @Pattern(regexp = "^\\d{3,8}$", message = "邀请码格式错误")
    private String inviteCode;

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
