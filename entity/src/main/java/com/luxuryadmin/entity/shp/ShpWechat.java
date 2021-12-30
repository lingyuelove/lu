package com.luxuryadmin.entity.shp;

import java.util.Date;

/**
 * 店铺微信表
 *
 * @author sanjin145
 * @date   2020/08/31 15:56:39
 */
public class ShpWechat {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 店铺ID
     */
    private Integer fkShopId;

    /**
     * 联系人名称
     */
    private String contactPersonName;

    /**
     * 联系人微信/手机号
     */
    private String contactPersonWechat;

    /**
     * 联系人类型 0:微信 1:手机号
     */
    private String type;

    /**
     * 负责内容
     */
    private String contactResponsible;


    /**
     * 创建者ID
     */
    private Integer insertAdmin;

    /**
     * 更新者ID
     */
    private Integer updateAdmin;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 排序字段
     */
    private Integer sortNum;

    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
     */
    private String del;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFkShopId() {
        return fkShopId;
    }

    public void setFkShopId(Integer fkShopId) {
        this.fkShopId = fkShopId;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonWechat() {
        return contactPersonWechat;
    }

    public void setContactPersonWechat(String contactPersonWechat) {
        this.contactPersonWechat = contactPersonWechat;
    }

    public Integer getInsertAdmin() {
        return insertAdmin;
    }

    public void setInsertAdmin(Integer insertAdmin) {
        this.insertAdmin = insertAdmin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContactResponsible() {
        return contactResponsible;
    }

    public void setContactResponsible(String contactResponsible) {
        this.contactResponsible = contactResponsible;
    }

    public Integer getUpdateAdmin() {
        return updateAdmin;
    }

    public void setUpdateAdmin(Integer updateAdmin) {
        this.updateAdmin = updateAdmin;
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

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
    }
}