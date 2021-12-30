package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;

/**
 * @Classname VoShpUserInfo
 * @Description TODO
 * @Date 2020/6/23 15:39
 * @Created by Administrator
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoShpUserInfo {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * 商家版用户ID
     */
    private Integer fkShpUserId;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * token
     */
    private String token;

    /**
     * 上次登录时间
     */
    private Date lastLoginTime;

    /**
     * 登录设备IP
     */
    private String loginIp;

    /**
     * 用户设备ID
     */
    private String deviceId;

    /**
     * 用户状态：0：禁用；1：正常
     */
    private String state;

    /**
     * mac地址
     */
    private String mac;

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
     * 关联店铺
     */
    private List<VoShopBase> voShopBases;

    /**
     * 角色列表
     */
    private List<VoUserRoleRef> userRoleRefs;


    public List<VoUserRoleRef> getUserRoleRefs() {
        return userRoleRefs;
    }

    public void setUserRoleRefs(List<VoUserRoleRef> userRoleRefs) {
        this.userRoleRefs = userRoleRefs;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFkShpUserId() {
        return fkShpUserId;
    }

    public void setFkShpUserId(Integer fkShpUserId) {
        this.fkShpUserId = fkShpUserId;
    }

    public Integer getFkShpShopId() {
        return fkShpShopId;
    }

    public void setFkShpShopId(Integer fkShpShopId) {
        this.fkShpShopId = fkShpShopId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
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

    public List<VoShopBase> getVoShopBases() {
        return voShopBases;
    }

    public void setVoShopBases(List<VoShopBase> voShopBases) {
        this.voShopBases = voShopBases;
    }
}
