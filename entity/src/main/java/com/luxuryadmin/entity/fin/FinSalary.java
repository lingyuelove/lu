package com.luxuryadmin.entity.fin;

import java.util.Date;

/**
 * 工资条记录表
 *
 * @author monkey king
 * @date   2020/09/23 22:40:38
 */
public class FinSalary {
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
     * 0:未发放; 1:已发放;
     */
    private String salaryState;

    /**
     * 薪资总额
     */
    private Long salaryMoney;

    /**
     * 工资条名称
     */
    private String salaryName;

    /**
     * 工资条统计的开始时间
     */
    private Date salaryStTime;

    /**
     * 工资条统计的结束时间
     */
    private Date salaryEtTime;

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

    public String getSalaryState() {
        return salaryState;
    }

    public void setSalaryState(String salaryState) {
        this.salaryState = salaryState;
    }

    public Long getSalaryMoney() {
        return salaryMoney;
    }

    public void setSalaryMoney(Long salaryMoney) {
        this.salaryMoney = salaryMoney;
    }

    public String getSalaryName() {
        return salaryName;
    }

    public void setSalaryName(String salaryName) {
        this.salaryName = salaryName;
    }

    public Date getSalaryStTime() {
        return salaryStTime;
    }

    public void setSalaryStTime(Date salaryStTime) {
        this.salaryStTime = salaryStTime;
    }

    public Date getSalaryEtTime() {
        return salaryEtTime;
    }

    public void setSalaryEtTime(Date salaryEtTime) {
        this.salaryEtTime = salaryEtTime;
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