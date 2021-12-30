package com.luxuryadmin.entity.biz;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商务模块--代理人员订单记录表
 *
 * @author monkey king
 * @date   2019/12/01 04:54:26
 */
public class BizAgencyOrder {
    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 代理人员表的id字段,主键id
     */
    private Integer fkBizAgencyId;

    /**
     * 卖家shp_shop的id字段,主键id
     */
    private Integer fkSellerShopId;

    /**
     * pro_product的主键Id,逻辑id,软件内部关联
     */
    private Integer fkProProductId;

    /**
     * ord_order的主键Id,逻辑id,软件内部关联
     */
    private Integer fkOrdOrderId;

    /**
     * 运营模块--渠道的主键id
     */
    private Integer fkOpChannelId;

    /**
     * 最终成交价(分)
     */
    private BigDecimal finishPrice;

    /**
     * ag开头;订单编号(针对店铺的唯一订单编码)
     */
    private String number;

    /**
     * 状态  -90 已删除;-10:取消订单(买家);-11取消订单(卖家);10:已下单(待确认);11:确认订单(卖家);12:已发货(卖家);20:已收货(买家)
     */
    private String state;

    /**
     * 下单时间
     */
    private Date bookingTime;

    /**
     * 取消订单时间
     */
    private Date cancelTime;

    /**
     * 卖家确认发货时间
     */
    private Date sellerConfirmTime;

    /**
     * 买家确认收货时间
     */
    private Date buyerConfirmTime;

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

    public Integer getFkBizAgencyId() {
        return fkBizAgencyId;
    }

    public void setFkBizAgencyId(Integer fkBizAgencyId) {
        this.fkBizAgencyId = fkBizAgencyId;
    }

    public Integer getFkSellerShopId() {
        return fkSellerShopId;
    }

    public void setFkSellerShopId(Integer fkSellerShopId) {
        this.fkSellerShopId = fkSellerShopId;
    }

    public Integer getFkProProductId() {
        return fkProProductId;
    }

    public void setFkProProductId(Integer fkProProductId) {
        this.fkProProductId = fkProProductId;
    }

    public Integer getFkOrdOrderId() {
        return fkOrdOrderId;
    }

    public void setFkOrdOrderId(Integer fkOrdOrderId) {
        this.fkOrdOrderId = fkOrdOrderId;
    }

    public Integer getFkOpChannelId() {
        return fkOpChannelId;
    }

    public void setFkOpChannelId(Integer fkOpChannelId) {
        this.fkOpChannelId = fkOpChannelId;
    }

    public BigDecimal getFinishPrice() {
        return finishPrice;
    }

    public void setFinishPrice(BigDecimal finishPrice) {
        this.finishPrice = finishPrice;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(Date bookingTime) {
        this.bookingTime = bookingTime;
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    public Date getSellerConfirmTime() {
        return sellerConfirmTime;
    }

    public void setSellerConfirmTime(Date sellerConfirmTime) {
        this.sellerConfirmTime = sellerConfirmTime;
    }

    public Date getBuyerConfirmTime() {
        return buyerConfirmTime;
    }

    public void setBuyerConfirmTime(Date buyerConfirmTime) {
        this.buyerConfirmTime = buyerConfirmTime;
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