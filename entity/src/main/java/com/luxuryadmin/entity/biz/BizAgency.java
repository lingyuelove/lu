package com.luxuryadmin.entity.biz;

import java.util.Date;

/**
 * 商务模块--代理人员
 *
 * @author monkey king
 * @date   2019/12/01 04:54:26
 */
public class BizAgency {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码;
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机号;对称加密存储
     */
    private String phone;

    /**
     * 预留字段;默认为0
     */
    private Integer type;

    /**
     * 状态  0：禁用   1：正常
     */
    private String state;

    /**
     * 员工编号
     */
    private Integer number;

    /**
     * 代理人员详情表id
     */
    private Integer fkBizAgencyDetailId;

    /**
     * 运营模块--渠道的主键id
     */
    private Integer fkOpChannelId;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getFkBizAgencyDetailId() {
        return fkBizAgencyDetailId;
    }

    public void setFkBizAgencyDetailId(Integer fkBizAgencyDetailId) {
        this.fkBizAgencyDetailId = fkBizAgencyDetailId;
    }

    public Integer getFkOpChannelId() {
        return fkOpChannelId;
    }

    public void setFkOpChannelId(Integer fkOpChannelId) {
        this.fkOpChannelId = fkOpChannelId;
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