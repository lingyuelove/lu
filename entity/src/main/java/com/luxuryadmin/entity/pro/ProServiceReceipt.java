package com.luxuryadmin.entity.pro;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品服务单据(维修,保养,寄卖,质押)
 *
 * @author monkey king
 * @date   2020/05/28 15:55:45
 */
public class ProServiceReceipt {
    /**
     * 主键Id,逻辑id,软件内部关联;不要把此id暴露在前端;
     */
    private Integer id;

    /**
     * pro_product的主键Id,当类型为寄卖或质押时,此字段有值;
     */
    private Integer fkProProductId;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * 订单编号(针对店铺的唯一订单编码)
     */
    private String number;

    /**
     * 单据类型;维修,保养,寄卖,质押
     */
    private String type;

    /**
     * 商品名称
     */
    private String proName;

    /**
     * 单据状态;-90:已取消;10:进行中;20:已完成;
     */
    private Integer state;

    /**
     * 费用类型:0:不含手续费; 1:含手续费
     */
    private String priceType;

    /**
     * 单据价格(分)
     */
    private BigDecimal price;

    /**
     * 服务费(分)
     */
    private BigDecimal servicePrice;

    /**
     * 缩略图地址
     */
    private String smallImg;

    /**
     * 产品图片地址,多个用英文分号隔开
     */
    private String productImg;

    /**
     * 收件人姓名
     */
    private String customerName;

    /**
     * 手机号码
     */
    private String customerPhone;

    /**
     * 地址
     */
    private String customerAddress;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 单据完成时间(如果是提前赎回也要更新此时间)
     */
    private Date finishTime;

    /**
     * 质押预期结束时间
     */
    private Date saveEndTime;

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

    public Integer getFkProProductId() {
        return fkProProductId;
    }

    public void setFkProProductId(Integer fkProProductId) {
        this.fkProProductId = fkProProductId;
    }

    public Integer getFkShpShopId() {
        return fkShpShopId;
    }

    public void setFkShpShopId(Integer fkShpShopId) {
        this.fkShpShopId = fkShpShopId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(BigDecimal servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getSmallImg() {
        return smallImg;
    }

    public void setSmallImg(String smallImg) {
        this.smallImg = smallImg;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
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

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Date getSaveEndTime() {
        return saveEndTime;
    }

    public void setSaveEndTime(Date saveEndTime) {
        this.saveEndTime = saveEndTime;
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