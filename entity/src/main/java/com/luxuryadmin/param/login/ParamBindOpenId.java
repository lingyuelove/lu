package com.luxuryadmin.param.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * APP微信登录--前端接收参数模型
 *
 * @author monkey king
 * @Date 2019-12-26 02:15:20
 */
@ApiModel(description = "APP微信登录")
public class ParamBindOpenId extends ParamImageCode {

    /**
     * 第三方的唯一标识符
     */
    @ApiModelProperty(value = "第三方的唯一标识符", name = "openId", required = true)
    @Length(max = 50, message = "openId长度超限")
    @NotBlank(message = "openId不允许为空")
    private String openId;

    @ApiModelProperty(hidden = true)
    private String nickname;

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
