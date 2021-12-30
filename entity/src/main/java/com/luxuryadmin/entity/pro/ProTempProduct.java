package com.luxuryadmin.entity.pro;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 临时仓产品表;
 *
 * @author monkey king
 * @date   2021/01/16 18:39:31
 */
public class ProTempProduct {
    /**
     * 主键Id,逻辑id,软件内部关联;不要把此id暴露在前端;
     */
    private Integer id;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * pro_temp的id字段,主键id
     */
    private Integer fkProTempId;

    /**
     * pro_product的id字段,主键id
     */
    private Integer fkProProductId;

    /**
     * 名称
     */
    private String name;

    /**
     * 临时仓数量
     */
    private Integer num;

    /**
     * 产品描述
     */
    private String description;

    /**
     * 友商价(分)
     */
    private BigDecimal tradePrice;

    /**
     * 代理价(分)
     */
    private BigDecimal agencyPrice;

    /**
     * 零售价(分)
     */
    private BigDecimal salePrice;

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
     * 备注
     */
    private String remark;


    /**
     * 成本价
     */
    private BigDecimal initPrice;

    public BigDecimal getInitPrice() {
        return initPrice;
    }

    public void setInitPrice(BigDecimal initPrice) {
        this.initPrice = initPrice;
    }

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

    public Integer getFkProTempId() {
        return fkProTempId;
    }

    public void setFkProTempId(Integer fkProTempId) {
        this.fkProTempId = fkProTempId;
    }

    public Integer getFkProProductId() {
        return fkProProductId;
    }

    public void setFkProProductId(Integer fkProProductId) {
        this.fkProProductId = fkProProductId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(BigDecimal tradePrice) {
        this.tradePrice = tradePrice;
    }

    public BigDecimal getAgencyPrice() {
        return agencyPrice;
    }

    public void setAgencyPrice(BigDecimal agencyPrice) {
        this.agencyPrice = agencyPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}