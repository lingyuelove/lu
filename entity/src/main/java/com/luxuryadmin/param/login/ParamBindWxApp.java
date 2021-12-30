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
public class ParamBindWxApp extends ParamLoginSms {

    /**
     * 第三方的唯一标识符
     */
    @ApiModelProperty(value = "第三方的唯一标识符", name = "openId", required = true)
    @Length(max = 50, message = "openId长度超限")
    @NotBlank(message = "openId不允许为空")
    private String openId;

    /**
     * 邀请码
     */
    @ApiModelProperty(value = "邀请码", name = "inviteCode", required = false)
    @Pattern(regexp = "^\\d{3,8}$", message = "邀请码格式错误")
    private String inviteCode;

    @ApiModelProperty(hidden = true)
    private String nickname;

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
