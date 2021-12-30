package com.luxuryadmin.entity.fin;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 工资方案表
 *
 * @author monkey king
 * @date   2020/09/23 22:40:38
 */
public class FinSalaryScheme {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 店铺ID
     */
    private Integer fkShpShopId;

    /**
     * 用户id
     */
    private Integer fkShpUserId;

    /**
     * 工资条详情表id
     */
    private Integer fkFinSalaryDetailId;

    /**
     * 方案类型;2-1; 2-2; 2-3; 3-1
     */
    private String schemeType;

    /**
     * 百分比
     */
    private BigDecimal schemePercent;

    /**
     * 件数
     */
    private Long schemeNum;

    /**
     * 合计(分)
     */
    private Long schemeMoney;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 创建人用户id
     */
    private Integer insertAdmin;

    /**
     * 修改人用户id
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

    public Integer getFkShpUserId() {
        return fkShpUserId;
    }

    public void setFkShpUserId(Integer fkShpUserId) {
        this.fkShpUserId = fkShpUserId;
    }

    public Integer getFkFinSalaryDetailId() {
        return fkFinSalaryDetailId;
    }

    public void setFkFinSalaryDetailId(Integer fkFinSalaryDetailId) {
        this.fkFinSalaryDetailId = fkFinSalaryDetailId;
    }

    public String getSchemeType() {
        return schemeType;
    }

    public void setSchemeType(String schemeType) {
        this.schemeType = schemeType;
    }

    public BigDecimal getSchemePercent() {
        return schemePercent;
    }

    public void setSchemePercent(BigDecimal schemePercent) {
        this.schemePercent = schemePercent;
    }

    public Long getSchemeNum() {
        return schemeNum;
    }

    public void setSchemeNum(Long schemeNum) {
        this.schemeNum = schemeNum;
    }

    public Long getSchemeMoney() {
        return schemeMoney;
    }

    public void setSchemeMoney(Long schemeMoney) {
        this.schemeMoney = schemeMoney;
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