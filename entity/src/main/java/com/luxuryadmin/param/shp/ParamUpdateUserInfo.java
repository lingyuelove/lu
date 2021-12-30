package com.luxuryadmin.param.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 更新用户信息--前端参数模型
 *
 * @author monkey king
 * @Date 2020-05-31 23:28:04
 */
@ApiModel(description = "更新用户信息--前端参数模型")
public class ParamUpdateUserInfo {

    /**
     * 用户昵称
     */
    @ApiModelProperty(name = "nickname", required = false,
            value = "用户昵称;(长度不超过50个字符)")
    @Length(max = 50, message = "昵称长度不得超过50个字符")
    private String nickname;

    /**
     * 用户头像地址
     */
    @ApiModelProperty(name = "userHeadImgUrl", required = false, value = "用户头像地址;长度不得超过250个字符")
    @Length(max = 250, message = "用户头像地址长度不得超过250个字符")
    private String userHeadImgUrl;

    @ApiModelProperty(name = "token", required = true, value = "登录标识符")
    @NotBlank(message = "token不允许为空")
    private String token;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserHeadImgUrl() {
        return userHeadImgUrl;
    }

    public void setUserHeadImgUrl(String userHeadImgUrl) {
        this.userHeadImgUrl = userHeadImgUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
