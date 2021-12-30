package com.luxuryadmin.entity.fin;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 销售分析--销售排行榜,每个店铺成员每天一条记录(隔天统计前一天)
 *
 * @author monkey king
 * @date   2020/01/15 19:11:40
 */
public class FinSaleTop {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * 统计日期;
     */
    private Date bizDate;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * 销售人员id
     */
    private Integer fkShpUserId;

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
     * 毛利率%=毛利润/销售总额*100%; gross_profit/sale_price*100%
     */
    private Double grossProfitRate;

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

    public Date getBizDate() {
        return bizDate;
    }

    public void setBizDate(Date bizDate) {
        this.bizDate = bizDate;
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