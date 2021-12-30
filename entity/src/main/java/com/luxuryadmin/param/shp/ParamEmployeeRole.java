package com.luxuryadmin.param.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 员工角色--前端参数模型
 *
 * @author monkey king
 * @date 2020-06-12 20:49:44
 */
@ApiModel(description = "员工角色--前端参数模型")
public class ParamEmployeeRole {

    /**
     * 登录标识符
     */
    @ApiModelProperty(name = "token", required = true, value = "登录标识符")
    private String token;

    /**
     * 用户id
     */
    @ApiModelProperty(name = "userId", required = true, value = "用户id")
    @Pattern(regexp = "^\\d{5,}$", message = "userId--参数错误")
    private String userId;

    /**
     * 用户类型
     */
    @ApiModelProperty(name = "refId", required = false, value = "用户类型")
    @Pattern(regexp = "^(-9)|(-2)|(-1)|(0)|(1)|(2)|$", message = "userType--参数错误")
    private String userType;

    /**
     * 角色id
     */
    @ApiModelProperty(name = "roleIds", required = false, value = "角色id")
    @Pattern(regexp = "^[0-9;]{5,}$", message = "roleIds--参数错误")
    private String roleIds;

    /**
     * 对于店铺所显示的姓名
     */
    @ApiModelProperty(name = "name", required = true, value = "对于店铺所显示的姓名")
    @Length(max = 8, message = "姓名不得超过8个字符")
    @NotBlank(message = "姓名不允许为空")
    private String name;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
