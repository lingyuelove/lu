package com.luxuryadmin.entity.shp;

import java.util.Date;

/**
 * 常用功能
 *
 * @author monkey king
 * @date   2020/06/15 17:32:12
 */
public class ShpUsualFunction {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * code值;用作于app跳转页面;
     */
    private String code;

    /**
     * 功能名称
     */
    private String name;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * shp_user的id字段,主键id
     */
    private Integer fkShpUserId;

    /**
     * 权限id
     */
    private Integer fkShpPermissionId;

    /**
     * 权限图标地址
     */
    private String iconUrl;

    /**
     * 父节点名称
     */
    private String parentName;

    /**
     * 父节点编码
     */
    private String parentCode;

    /**
     * 父节点ID
     */
    private Integer parentId;

    /**
     * 序号排序
     */
    private Integer sort;

    /**
     * 插入时间
     */
    private Date insertTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFkShpShopId() {
        return fkShpShopId;
    }

    public void setFkShpShopId(Integer fkShpShopId) {
        this.fkShpShopId = fkShpShopId;
    }

    public Integer getFkShpUserId() {
        return fkShpUserId;
    }

    public void setFkShpUserId(Integer fkShpUserId) {
        this.fkShpUserId = fkShpUserId;
    }

    public Integer getFkShpPermissionId() {
        return fkShpPermissionId;
    }

    public void setFkShpPermissionId(Integer fkShpPermissionId) {
        this.fkShpPermissionId = fkShpPermissionId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}