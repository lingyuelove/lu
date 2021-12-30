package com.luxuryadmin.entity.pro;

import java.util.Date;

/**
 * 小程序访客记录表---代理专用;
一人一条记录
 *
 * @author monkey king
 * @date   2021/08/28 17:22:54
 */
public class ProShareSeeUserAgency {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 店铺id
     */
    private Integer fkShpShopId;

    /**
     * 分享id
     */
    private Integer fkProShareId;

    /**
     * 分享人userId
     */
    private Integer fkShpUserId;

    /**
     * 分享批次
     */
    private String fkProShareBatch;

    /**
     * 佣金发放: -1:不发放 | 0:未发放; | 1:已发放
     */
    private String state;

    /**
     * 0:【LuxurySir】小程序访客;1:【奢当家】小程序访客
     */
    private String type;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 性别  0：未知、1：男、2：女
     */
    private String gender;

    /**
     * 微信的openId
     */
    private String openId;

    /**
     * 微信的unionId
     */
    private String unionId;

    /**
     * 已发放佣金(分);
     */
    private Integer alreadySendMoney;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 后台修改人员;
     */
    private Integer updateAdmin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFkShpShopId() {
        return fkShpShopId;
    }

    public void setFkShpShopId(Integer fkShpShopId) {
        this.fkShpShopId = fkShpShopId;
    }

    public Integer getFkProShareId() {
        return fkProShareId;
    }

    public void setFkProShareId(Integer fkProShareId) {
        this.fkProShareId = fkProShareId;
    }

    public Integer getFkShpUserId() {
        return fkShpUserId;
    }

    public void setFkShpUserId(Integer fkShpUserId) {
        this.fkShpUserId = fkShpUserId;
    }

    public String getFkProShareBatch() {
        return fkProShareBatch;
    }

    public void setFkProShareBatch(String fkProShareBatch) {
        this.fkProShareBatch = fkProShareBatch;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public Integer getAlreadySendMoney() {
        return alreadySendMoney;
    }

    public void setAlreadySendMoney(Integer alreadySendMoney) {
        this.alreadySendMoney = alreadySendMoney;
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
}