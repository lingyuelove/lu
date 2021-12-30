package com.luxuryadmin.entity.shp;

import java.util.Date;

/**
 * 店铺用户邀请记录表
 *
 * @author monkey king
 * @date   2019/12/11 16:05:09
 */
public class ShpUserInvite {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * 邀请人id
     */
    private Integer fkInviteUserId;

    /**
     * 被邀请人id(唯一),一位用户只能被一个人邀请
     */
    private Integer fkBeInviteUserId;

    /**
     * 邀请状态：0:禁用；1:正常; 
     */
    private String state;

    /**
     * 奖励状态：10:无奖励；20:已发放全部奖励
     */
    private String rewardState;

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

    public Integer getFkInviteUserId() {
        return fkInviteUserId;
    }

    public void setFkInviteUserId(Integer fkInviteUserId) {
        this.fkInviteUserId = fkInviteUserId;
    }

    public Integer getFkBeInviteUserId() {
        return fkBeInviteUserId;
    }

    public void setFkBeInviteUserId(Integer fkBeInviteUserId) {
        this.fkBeInviteUserId = fkBeInviteUserId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRewardState() {
        return rewardState;
    }

    public void setRewardState(String rewardState) {
        this.rewardState = rewardState;
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