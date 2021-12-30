package com.luxuryadmin.entity.shp;

import java.util.Date;

/**
 * 用户编号池
 *
 * @author monkey king
 * @date   2019/12/19 16:22:21
 */
public class ShpUserNumber {
    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 编号
     */
    private Integer number;

    /**
     * 编号状态：0:未使用；1:已使用; 2:已弃用;
     */
    private String state;

    /**
     * 靓号状态：10:普通号; 20:豹子号(所有都一样aaaaa);21:连号(abcde);22:多带1(aaaaab);23多带2(aaabb);50:特殊数字靓号(例如5201314)
     */
    private String nice;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNice() {
        return nice;
    }

    public void setNice(String nice) {
        this.nice = nice;
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