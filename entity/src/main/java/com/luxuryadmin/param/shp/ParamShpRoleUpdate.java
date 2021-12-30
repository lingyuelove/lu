package com.luxuryadmin.param.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 修改店铺角色--前端参数模型
 *
 * @author monkey king
 * @Date 2020-05-31 23:28:04
 */
@ApiModel(description = "修改店铺角色--前端参数模型")
public class ParamShpRoleUpdate extends ParamShpRole {

    /**
     * 角色id
     */
    @ApiModelProperty(name = "roleId", required = true, value = "角色id")
    @NotBlank(message = "roleId不允许为空")
    @Pattern(regexp = "^[0-9]+$", message = "roleId--参数错误")
    private String roleId;

    @Override
    public String getRoleId() {
        return roleId;
    }

    @Override
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
