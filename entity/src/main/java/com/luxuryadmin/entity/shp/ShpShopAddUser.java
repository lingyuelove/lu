package com.luxuryadmin.entity.shp;

import java.util.Date;

/**
 * 店铺--添加员工记录表
 *
 * @author monkey king
 * @date   2019/12/01 04:55:22
 */
public class ShpShopAddUser {
    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 邀请者(店主)shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * 店铺成员ID
     */
    private Integer fkShpUserId;

    /**
     * 被邀请者(员工)shp_shop的id字段,主键id
     */
    private Integer fkBeShpUserId;

    /**
     * 添加状态: -10:已删除该条信息; 10:已发请求(待确认); 11:已发请求(已忽略); 12:已发请求(已过期); 20:同意
     */
    private String state;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 操作完成时间
     */
    private Date finishTime;

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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getFkShpShopId() {
        return fkShpShopId;
    }

    public void setFkShpShopId(Integer fkShpShopId) {
        this.fkShpShopId = fkShpShopId;
    }

    public Integer getFkShpUserId() {
        return fkShpUserId;
    }

    public void setFkShpUserId(Integer fkShpUserId) {
        this.fkShpUserId = fkShpUserId;
    }

    public Integer getFkBeShpUserId() {
        return fkBeShpUserId;
    }

    public void setFkBeShpUserId(Integer fkBeShpUserId) {
        this.fkBeShpUserId = fkBeShpUserId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
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