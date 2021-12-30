package com.luxuryadmin.param.login;

import com.luxuryadmin.param.common.ParamBasic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 手机号码/用户名--前端接收参数模型
 *
 * @author monkey king
 * @Date 2019-12-26 02:15:20
 */
@ApiModel(description = "手机号码/用户名")
public class ParamUsername extends ParamBasic {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "用户名/手机号", name = "username", required = true)
    @Pattern(regexp = "^[1][3456789][0-9]{9}$", message = "用户名格式错误")
    @NotBlank(message = "用户名不允许为空")
    private String username;

    /**
     * ip;用服务器端获取赋值至此字段
     */
    @ApiModelProperty(hidden = true)
    private String ip;

    /**
     * 用户昵称
     */
    @ApiModelProperty(name = "nickname", required = false,
            value = "用户昵称;(长度不超过50个字符)")
    @Length(max = 50, message = "昵称长度不得超过50个字符")
    private String nickname;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
