package com.luxuryadmin.entity.sys;

import java.util.Date;

/**
 * 后台--角色与权限对应关系
 *
 * @author monkey king
 * @date   2019/12/01 05:24:38
 */
public class SysRolePermissionRef {
    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 后台--角色表ID
     */
    private Integer fkSysRoleId;

    /**
     * 后台--权限管理ID
     */
    private Integer fkSysPermissionId;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 添加用户_管理员id
     */
    private Integer insertAdmin;

    /**
     * 修改用户_管理员id
     */
    private Integer updateAdmin;

    /**
     * 版本号;用于更新时对比操作;
     */
    private Integer versions;

    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
     */
    private String del;

    /**
     * 备注
     */
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFkSysRoleId() {
        return fkSysRoleId;
    }

    public void setFkSysRoleId(Integer fkSysRoleId) {
        this.fkSysRoleId = fkSysRoleId;
    }

    public Integer getFkSysPermissionId() {
        return fkSysPermissionId;
    }

    public void setFkSysPermissionId(Integer fkSysPermissionId) {
        this.fkSysPermissionId = fkSysPermissionId;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getInsertAdmin() {
        return insertAdmin;
    }

    public void setInsertAdmin(Integer insertAdmin) {
        this.insertAdmin = insertAdmin;
    }

    public Integer getUpdateAdmin() {
        return updateAdmin;
    }

    public void setUpdateAdmin(Integer updateAdmin) {
        this.updateAdmin = updateAdmin;
    }

    public Integer getVersions() {
        return versions;
    }

    public void setVersions(Integer versions) {
        this.versions = versions;
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}