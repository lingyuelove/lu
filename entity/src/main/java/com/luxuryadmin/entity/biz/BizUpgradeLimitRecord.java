package com.luxuryadmin.entity.biz;

import java.util.Date;

/**
 * 商务模块--升级友商好友数量限制
 *
 * @author monkey king
 * @date   2019/12/01 04:54:26
 */
public class BizUpgradeLimitRecord {
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
     * 原有友商数量上限
     */
    private Integer oldLimitLeaguerNum;

    /**
     * 现有友商数量上限
     */
    private Integer newLimitLeaguerNum;

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

    public Integer getOldLimitLeaguerNum() {
        return oldLimitLeaguerNum;
    }

    public void setOldLimitLeaguerNum(Integer oldLimitLeaguerNum) {
        this.oldLimitLeaguerNum = oldLimitLeaguerNum;
    }

    public Integer getNewLimitLeaguerNum() {
        return newLimitLeaguerNum;
    }

    public void setNewLimitLeaguerNum(Integer newLimitLeaguerNum) {
        this.newLimitLeaguerNum = newLimitLeaguerNum;
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