package com.luxuryadmin.entity.op;

import java.util.Date;

/**
 * 短信发送记录表
 *
 * @author monkey king
 * @date   2019/12/11 19:24:47
 */
public class OpSmsRecord {

    public OpSmsRecord() {

    }

    public OpSmsRecord(String phone, String content,String smsType, String ip) {
        this.id = id;
        this.phone = phone;
        this.content = content;
        this.smsType = smsType;
        this.ip = ip;
        this.state = "10";
        this.insertTime = new Date();
        this.versions = 1;
        this.del = "0";
    }

    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 手机号码;多个用英文逗号隔开,最多一次发送200个号码
     */
    private String phone;

    /**
     * 发送内容
     */
    private String content;

    /**
     * 响应时间
     */
    private Date respTime;

    /**
     * 响应信息
     */
    private String resp;

    /**
     * 短信类型 99:通知；其他参考CaptchaTypeEnum枚举
     */
    private String smsType;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * ip兼容ipv6长度
     */
    private String ip;

    /**
     * 短信发送状态 10-已发送 20-发送失败
     */
    private String state;

    /**
     * 发送时间
     */
    private Date sendTime;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getRespTime() {
        return respTime;
    }

    public void setRespTime(Date respTime) {
        this.respTime = respTime;
    }

    public String getResp() {
        return resp;
    }

    public void setResp(String resp) {
        this.resp = resp;
    }

    public String getSmsType() {
        return smsType;
    }

    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
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