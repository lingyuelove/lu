package com.luxuryadmin.vo.op;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

/**
 * 运营模块--商家版意见反馈表
 *
 * @author monkey king
 * @date   2019/12/01 05:22:30
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoOpBizComment {
    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 平台类型pc，android，ios，wap;
     */
    private String platform;

    /**
     * 商家版用户ID
     */
    private Integer fkShpUserId;

    /**
     * 反馈内容
     */
    private String content;

    /**
     * 反馈IP
     */
    private String ip;

    /**
     * 反馈人姓名
     */
    private String name;

    /**
     * 反馈人联系方式
     */
    private String contact;

    /**
     * 回复状态:0:未回复；1：已回复
     */
    private String replyStatus;

    /**
     * 回复内容
     */
    private String replyContent;

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

    /**
     * 商家用户手机号
     */
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Integer getFkShpUserId() {
        return fkShpUserId;
    }

    public void setFkShpUserId(Integer fkShpUserId) {
        this.fkShpUserId = fkShpUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getReplyStatus() {
        return replyStatus;
    }

    public void setReplyStatus(String replyStatus) {
        this.replyStatus = replyStatus;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
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