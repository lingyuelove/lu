package com.luxuryadmin.param.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 添加员工(授权角色)--前端参数模型
 *
 * @author monkey king
 * @date 2020-06-12 20:49:44
 */
@ApiModel(description = "员工角色--前端参数模型")
public class ParamEmployeeRoleAdd extends ParamEmployeeRole {

    /**
     * 帐号(手机号码)
     */
    @ApiModelProperty(value = "帐号(手机号码)", name = "username", required = true)
    @Pattern(regexp = "^[1][3456789][0-9]{9}$", message = "帐号--参数错误")
    @NotBlank(message = "帐号不能为空")
    private String username;

    /**
     * 用户类型(已弃用)
     */
    @ApiModelProperty(name = "refId", required = false, value = "用户类型")
    @Pattern(regexp = "^(-2)|(-1)|(0)|(1)|(2)|$", message = "userType--参数错误")
    private String userType;

    /**
     * 添加类型
     */
    @ApiModelProperty(name = "addType", required = false, value = "添加类型")
    @Pattern(regexp = "^1$", message = "addType--参数错误")
    private String addType;

    /**
     * 用户id
     */
    @ApiModelProperty(hidden = true)
    private String userId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getUserType() {
        return userType;
    }

    @Override
    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAddType() {
        return addType;
    }

    public void setAddType(String addType) {
        this.addType = addType;
    }

}
