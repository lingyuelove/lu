package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author monkey king
 * @date 2019-12-30 16:19:05
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoShpRole {

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色Id
     */
    private Integer roleId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 类型;0:系统创建; 1:用户创建; 系统创建的不能删除
     */
    private String type;

    /**
     * 创建者:
     */
    private String createUser;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
