package com.luxuryadmin.entity.shp;

import java.util.Date;

/**
 * 
 *
 * @author monkey king
 * @date   2021/01/25 19:46:08
 */
public class ShpBindCount {
    /**
     * id
     */
    private Integer id;

    /**
     * 
     */
    private Integer fkShpUserId;

    /**
     * 
     */
    private String username;

    /**
     * wx:微信
     */
    private String type;

    /**
     * 状态: -1: 解绑; 0:未绑定; 1:已绑定
     */
    private String state;

    /**
     * 微信的openId
     */
    private String openId;

    /**
     * 
     */
    private Date insertTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 
     */
    private String refreshToken;

    /**
     * 
     */
    private String accessToken;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
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

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}