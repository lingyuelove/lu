package com.luxuryadmin.entity.fin;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 销售分析;只分析店铺销售数据;每天统计;每天每店铺一条数据(隔天统计前一天)
 *
 * @author monkey king
 * @date   2020/01/15 11:59:55
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class FinSaleAnalysis {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * 统计日期;
     */
    private Date bizDate;

    /**
     * 库存成本(分);
     */
    private BigDecimal storeCost;

    /**
     * 库存数量;
     */
    private Integer storeCount;

    /**
     * 销售成本(分);
     */
    private BigDecimal saleCost;

    /**
     * 销售总额(分);
     */
    private BigDecimal salePrice;

    /**
     * 销售订单数量;
     */
    private Integer saleOrderCount;

    /**
     * 毛利润(分);(销售总额-销售成本)
     */
    private BigDecimal grossProfit;

    /**
     * 毛利率%=毛利润/销售总额*100%
     */
    private Double grossProfitRate;

    /**
     * 新增商品数量(非质押商品);
     */
    private Integer addProCount;

    /**
     * 新增商品成本(非质押商品)(分);
     */
    private BigDecimal addProCost;

    /**
     * 质押商品数量
     */
    private Integer addPrivateProCount;

    /**
     * 质押商品成本(分)
     */
    private BigDecimal addPrivateProCost;

    /**
     * 插入时间
     */
    private Date insertTime;

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

    public Integer getFkShpShopId() {
        return fkShpShopId;
    }

    public void setFkShpShopId(Integer fkShpShopId) {
        this.fkShpShopId = fkShpShopId;
    }

    public Date getBizDate() {
        return bizDate;
    }

    public void setBizDate(Date bizDate) {
        this.bizDate = bizDate;
    }

    public BigDecimal getStoreCost() {
        return storeCost;
    }

    public void setStoreCost(BigDecimal storeCost) {
        this.storeCost = storeCost;
    }

    public Integer getStoreCount() {
        return storeCount;
    }

    public void setStoreCount(Integer storeCount) {
        this.storeCount = storeCount;
    }

    public BigDecimal getSaleCost() {
        return saleCost;
    }

    public void setSaleCost(BigDecimal saleCost) {
        this.saleCost = saleCost;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getSaleOrderCount() {
        return saleOrderCount;
    }

    public void setSaleOrderCount(Integer saleOrderCount) {
        this.saleOrderCount = saleOrderCount;
    }

    public BigDecimal getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(BigDecimal grossProfit) {
        this.grossProfit = grossProfit;
    }

    public Double getGrossProfitRate() {
        return grossProfitRate;
    }

    public void setGrossProfitRate(Double grossProfitRate) {
        this.grossProfitRate = grossProfitRate;
    }

    public Integer getAddProCount() {
        return addProCount;
    }

    public void setAddProCount(Integer addProCount) {
        this.addProCount = addProCount;
    }

    public BigDecimal getAddProCost() {
        return addProCost;
    }

    public void setAddProCost(BigDecimal addProCost) {
        this.addProCost = addProCost;
    }

    public Integer getAddPrivateProCount() {
        return addPrivateProCount;
    }

    public void setAddPrivateProCount(Integer addPrivateProCount) {
        this.addPrivateProCount = addPrivateProCount;
    }

    public BigDecimal getAddPrivateProCost() {
        return addPrivateProCost;
    }

    public void setAddPrivateProCost(BigDecimal addPrivateProCost) {
        this.addPrivateProCost = addPrivateProCost;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
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