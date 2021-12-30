package com.luxuryadmin.param.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 添加店铺角色--前端参数模型
 *
 * @author monkey king
 * @Date 2020-05-31 23:28:04
 */
@ApiModel(description = "添加店铺角色--前端参数模型")
public class ParamShpRole {

    /**
     * 角色id
     */
    @ApiModelProperty(hidden = true)
    private String roleId;

    /**
     * 登录标识符
     */
    @ApiModelProperty(name = "token", required = true, value = "登录标识符")
    private String token;

    /**
     * 角色名称
     */
    @ApiModelProperty(name = "roleName", required = true, value = "角色名称;2~20个字符长度")
    @NotBlank(message = "角色名称不允许为空")
    @Length(max = 50, message = "角色名称长度在50个字符以内")
    private String roleName;

    /**
     * 权限id,多个用分号隔开;只需要传最后一层菜单的id
     */
    @ApiModelProperty(name = "permIds", required = true, value = "权限id,多个用分号隔开;只需要传最后一层菜单的id")
    @NotBlank(message = "请为角色赋予权限!")
    private String permIds;

    /**
     * 描述
     */
    @ApiModelProperty(name = "remark", required = false, value = "描述;250个字符长度")
    @Length(max = 250, message = "城市长度在250个字符以内")
    private String remark;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getPermIds() {
        return permIds;
    }

    public void setPermIds(String permIds) {
        this.permIds = permIds;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
