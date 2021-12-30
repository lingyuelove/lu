package com.luxuryadmin.entity.sys;

import java.util.Date;

/**
 * 系统配置信息表
 *
 * @author monkey king
 * @date   2019/12/10 16:19:07
 */
public class SysConfig {
    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 参数aes加密传输: 0:不加密; 1:加密
     */
    private String paramAesSwitch;

    /**
     * 参数签名: 0不签名; 1:签名
     */
    private String paramSignSwitch;

    /**
     * 针对IP;参24小时内已经获取验证码达到N次,需要进行图形验证码验证; 次数
     */
    private Integer ipSmsLimit;

    /**
     * 针对手机号;参24小时内已经获取验证码达到N次,需要进行图形验证码验证; 次数
     */
    private Integer smsLimit;

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

    public String getParamAesSwitch() {
        return paramAesSwitch;
    }

    public void setParamAesSwitch(String paramAesSwitch) {
        this.paramAesSwitch = paramAesSwitch;
    }

    public String getParamSignSwitch() {
        return paramSignSwitch;
    }

    public void setParamSignSwitch(String paramSignSwitch) {
        this.paramSignSwitch = paramSignSwitch;
    }

    public Integer getIpSmsLimit() {
        return ipSmsLimit;
    }

    public void setIpSmsLimit(Integer ipSmsLimit) {
        this.ipSmsLimit = ipSmsLimit;
    }

    public Integer getSmsLimit() {
        return smsLimit;
    }

    public void setSmsLimit(Integer smsLimit) {
        this.smsLimit = smsLimit;
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