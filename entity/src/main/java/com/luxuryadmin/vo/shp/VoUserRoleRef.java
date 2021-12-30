package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

/**
 * 用户角色关系--Vo层
 *
 * @author monkey king
 * @date 2020-06-12 22:31:10
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoUserRoleRef {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 角色名称
     */
    private String roleName;

    private Date insertTime;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
}
