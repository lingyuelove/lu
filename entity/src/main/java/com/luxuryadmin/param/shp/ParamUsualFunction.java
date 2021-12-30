package com.luxuryadmin.param.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 设置常用服务--前端参数模型
 *
 * @author monkey king
 * @Date 2020-06-09 17:20:17
 */
@ApiModel(description = "设置常用服务--前端参数模型")
public class ParamUsualFunction {

    /**
     * 服务id
     */
    @ApiModelProperty(name = "permId", required = true, value = "权限id;(多少按顺序排序用逗号隔开)")
    @Pattern(regexp = "^[0-9,]{5,}$", message = "permId--参数错误")
    private String permId;

    /**
     * 登录标识符
     */
    @ApiModelProperty(name = "token", required = true, value = "登录标识符")
    @NotBlank(message = "token不允许为空")
    private String token;

    public String getPermId() {
        return permId;
    }

    public void setPermId(String permId) {
        this.permId = permId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
