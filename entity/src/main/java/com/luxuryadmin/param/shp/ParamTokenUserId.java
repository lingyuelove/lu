package com.luxuryadmin.param.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * token和userId的参数模型
 *
 * @author monkey king
 * @date 2020-06-12 22:53:40
 */
@ApiModel(description = "修改店铺角色--前端参数模型")
public class ParamTokenUserId {

    /**
     * 登录标识符
     */
    @ApiModelProperty(name = "token", required = true, value = "登录标识符")
    @NotBlank(message = "token不允许为空")
    private String token;

    /**
     * 用户id
     */
    @ApiModelProperty(name = "userId", required = true, value = "用户id")
    @Pattern(regexp = "^\\d{5,}$", message = "userId--参数错误")
    @NotBlank(message = "userId--参数错误")
    private String userId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
