package com.luxuryadmin.entity.biz;

import java.util.Date;

/**
 * 商务模块--添加友商好友;发起添加好友的消息记录(类似微信)
 *
 * @author monkey king
 * @date   2019/12/01 04:54:26
 */
public class BizLeaguerAdd {
    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 邀请者(店主)shp_shop的id字段,主键id
     */
    private Integer fkInviterShopId;

    /**
     * 被邀请者(友商)shp_shop的id字段,主键id
     */
    private Integer fkBeInviterShopId;

    /**
     * 好友状态: -10:已删除该条信息; 10:已发请求(待确认); 11:已发请求(已忽略); 12:已发请求(已过期); 20:已成为好友
     */
    private String state;

    /**
     * 友商来源 默认友商搜索
     */
    private String source;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 确认成为好友时间
     */
    private Date becomeFriendTime;

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

    public Integer getFkInviterShopId() {
        return fkInviterShopId;
    }

    public void setFkInviterShopId(Integer fkInviterShopId) {
        this.fkInviterShopId = fkInviterShopId;
    }

    public Integer getFkBeInviterShopId() {
        return fkBeInviterShopId;
    }

    public void setFkBeInviterShopId(Integer fkBeInviterShopId) {
        this.fkBeInviterShopId = fkBeInviterShopId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Date getBecomeFriendTime() {
        return becomeFriendTime;
    }

    public void setBecomeFriendTime(Date becomeFriendTime) {
        this.becomeFriendTime = becomeFriendTime;
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