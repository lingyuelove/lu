package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Classname VoProduct
 * @Description TODO
 * @Date 2020/6/28 14:46
 * @Created by Administrator
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoProduct {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * 商品唯一标识符,业务id,此id对外开放
     */
    private String bizId;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 商品状态表的code
     */
    private Integer fkProStateCode;

    /**
     * 状态名称
     */
    private String stateName;

    /**
     * 商品属性表的code
     */
    private String fkProAttributeCode;

    /**
     * 属性名称
     */
    private String attributeName;

    /**
     * 商品分类id; 和分类列表对应; 默认0:无分类;
     */
    private String fkProClassifyCode;

    /**
     * 名称
     */
    private String name;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品成色
     */
    private String quality;

    /**
     * 适用人群
     */
    private String targetUser;

    /**
     * 标签;多个用分号隔开;
     */
    private String tag;

    /**
     * 该商品总库存;
     */
    private int totalNum;


    /**
     * 卖出库存
     */
    private Integer saleNum;

    /**
     * 锁住库存数量
     */
    private Integer lockNum;

    /**
     * 是否推荐商品；0：不推荐；1：推荐; 目前预留字段
     */
    private String hot;

    /**
     * 是否推荐商品；10：不分享；20：分享给友商; 21:分享给代理; 22:分享给所有人(任何一级分享都可以分享给用户看,除非不分享)
     */
    private String share;

    /**
     * 成本价(分)
     */
    private BigDecimal initPrice;

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
     * 最终成交价(分)
     */
    private BigDecimal finishPrice;

    /**
     * 缩略图地址
     */
    private String smallImg;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 商品上架时间
     */
    private Date releaseTime;

    /**
     * 锁定时间;(时间结束之前,该商品不能被其他人卖掉)
     */
    private Date lockTime;

    /**
     * 锁单用户id
     */
    private Integer lockUserId;

    /**
     * 商品卖出时间
     */
    private Date finishTime;

    /**
     * 商品质押结束时间
     */
    private Date saveEndTime;

    /**
     * 添加用户_管理员id
     */
    private String insertAdmin;

    /**
     * 修改用户_管理员id
     */
    private String updateAdmin;

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

    /**
     *   商品来源
     */
    private String source;

    /**
     * 产品独立(唯一)编码,用户自填;
     */
    private String uniqueCode;

    /**
     * 产品图片地址,多个用英文分号隔开
     */
    private String productImg;

    /**
     * 保卡图片地址或者独立编码图片地址
     */
    private String cardCodeImg;

    /**
     * 委托人，赎回人
     */
    private String customerName;

    /**
     * 商铺编号
     */
    private String shopNumber;

    public String getShopNumber() {
        return shopNumber;
    }

    public void setShopNumber(String shopNumber) {
        this.shopNumber = shopNumber;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getCardCodeImg() {
        return cardCodeImg;
    }

    public void setCardCodeImg(String cardCodeImg) {
        this.cardCodeImg = cardCodeImg;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public Integer getFkShpShopId() {
        return fkShpShopId;
    }

    public void setFkShpShopId(Integer fkShpShopId) {
        this.fkShpShopId = fkShpShopId;
    }

    public Integer getFkProStateCode() {
        return fkProStateCode;
    }

    public void setFkProStateCode(Integer fkProStateCode) {
        this.fkProStateCode = fkProStateCode;
    }

    public String getFkProAttributeCode() {
        return fkProAttributeCode;
    }

    public void setFkProAttributeCode(String fkProAttributeCode) {
        this.fkProAttributeCode = fkProAttributeCode;
    }

    public String getFkProClassifyCode() {
        return fkProClassifyCode;
    }

    public void setFkProClassifyCode(String fkProClassifyCode) {
        this.fkProClassifyCode = fkProClassifyCode;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public BigDecimal getInitPrice() {
        return initPrice;
    }

    public void setInitPrice(BigDecimal initPrice) {
        this.initPrice = initPrice;
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

    public BigDecimal getFinishPrice() {
        return finishPrice;
    }

    public void setFinishPrice(BigDecimal finishPrice) {
        this.finishPrice = finishPrice;
    }

    public String getSmallImg() {
        return smallImg;
    }

    public void setSmallImg(String smallImg) {
        this.smallImg = smallImg;
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

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    public Integer getLockUserId() {
        return lockUserId;
    }

    public void setLockUserId(Integer lockUserId) {
        this.lockUserId = lockUserId;
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

    public String getInsertAdmin() {
        return insertAdmin;
    }

    public void setInsertAdmin(String insertAdmin) {
        this.insertAdmin = insertAdmin;
    }

    public String getUpdateAdmin() {
        return updateAdmin;
    }

    public void setUpdateAdmin(String updateAdmin) {
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

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Integer getLockNum() {
        return lockNum;
    }

    public void setLockNum(Integer lockNum) {
        this.lockNum = lockNum;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
