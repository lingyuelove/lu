package com.luxuryadmin.entity.sys;

import java.util.Date;

/**
 * 后台管理员详情表
 *
 * @author monkey king
 * @date   2019/12/01 05:24:38
 */
public class SysUserDetail {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * 后台系统用户ID
     */
    private Integer fkSysUserId;

    /**
     * 性别; 男,女,未知
     */
    private String sex;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String qq;

    /**
     * 身份证人脸面图片地址
     */
    private String idcardUrlFace;

    /**
     * 身份证国徽面图片地址
     */
    private String idcardUrlNation;

    /**
     * 用户头像图片地址
     */
    private String iconUrl;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

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

    public Integer getFkSysUserId() {
        return fkSysUserId;
    }

    public void setFkSysUserId(Integer fkSysUserId) {
        this.fkSysUserId = fkSysUserId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getIdcardUrlFace() {
        return idcardUrlFace;
    }

    public void setIdcardUrlFace(String idcardUrlFace) {
        this.idcardUrlFace = idcardUrlFace;
    }

    public String getIdcardUrlNation() {
        return idcardUrlNation;
    }

    public void setIdcardUrlNation(String idcardUrlNation) {
        this.idcardUrlNation = idcardUrlNation;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
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