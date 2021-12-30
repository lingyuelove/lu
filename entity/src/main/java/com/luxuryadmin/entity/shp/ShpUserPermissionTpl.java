package com.luxuryadmin.entity.shp;

import java.util.Date;

/**
 * 店铺--用户与权限对应关系--模板
 *
 * @author monkey king
 * @date   2020-08-14 17:41:11
 */
public class ShpUserPermissionTpl {
    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 模板名称
     */
    private Integer fkShpUserTypeId;

    /**
     * 店铺--权限管理ID
     */
    private Integer fkShpPermissionId;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

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

    public Integer getFkShpUserTypeId() {
        return fkShpUserTypeId;
    }

    public void setFkShpUserTypeId(Integer fkShpUserTypeId) {
        this.fkShpUserTypeId = fkShpUserTypeId;
    }

    public Integer getFkShpPermissionId() {
        return fkShpPermissionId;
    }

    public void setFkShpPermissionId(Integer fkShpPermissionId) {
        this.fkShpPermissionId = fkShpPermissionId;
    }

    public Integer getFkShpShopId() {
        return fkShpShopId;
    }

    public void setFkShpShopId(Integer fkShpShopId) {
        this.fkShpShopId = fkShpShopId;
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