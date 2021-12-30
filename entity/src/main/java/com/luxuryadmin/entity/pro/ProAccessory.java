package com.luxuryadmin.entity.pro;

import java.util.Date;

/**
 * 商品附件表
 *
 * @author monkey king
 * @date   2020/05/28 10:51:28
 */
public class ProAccessory {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * 附件名称
     */
    private String name;

    /**
     * 序号排序
     */
    private Integer sort;

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
     * 备注
     */
    private String remark;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}