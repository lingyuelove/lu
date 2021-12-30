package com.luxuryadmin.entity.fin;

import java.util.Date;

/**
 * 工资的订单利润表
 *
 * @author monkey king
 * @date   2020/09/23 22:40:38
 */
public class FinSalaryOrderProfit {
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
     * 工资方案表id
     */
    private Integer fkFinSalarySchemeId;

    /**
     * 统计的商品属性类型;10:自有商品; 20:寄卖商品; 30:其它商品
     */
    private String productAttr;

    /**
     * 订单类型;
     */
    private String orderType;

    /**
     * 总成本
     */
    private Long totalCostMoney;

    /**
     * 销售总额
     */
    private Long totalSaleMoney;

    /**
     * 毛利润总额
     */
    private Long totalProfitMoney;

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

    public Integer getFkFinSalarySchemeId() {
        return fkFinSalarySchemeId;
    }

    public void setFkFinSalarySchemeId(Integer fkFinSalarySchemeId) {
        this.fkFinSalarySchemeId = fkFinSalarySchemeId;
    }

    public String getProductAttr() {
        return productAttr;
    }

    public void setProductAttr(String productAttr) {
        this.productAttr = productAttr;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Long getTotalCostMoney() {
        return totalCostMoney;
    }

    public void setTotalCostMoney(Long totalCostMoney) {
        this.totalCostMoney = totalCostMoney;
    }

    public Long getTotalSaleMoney() {
        return totalSaleMoney;
    }

    public void setTotalSaleMoney(Long totalSaleMoney) {
        this.totalSaleMoney = totalSaleMoney;
    }

    public Long getTotalProfitMoney() {
        return totalProfitMoney;
    }

    public void setTotalProfitMoney(Long totalProfitMoney) {
        this.totalProfitMoney = totalProfitMoney;
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