package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luxuryadmin.enums.pro.EnumProState;

import java.util.Date;

/**
 * 商品VO模型-友商分享产品-详情显示
 *
 * @author monkey king
 * @date 2020-01-13 02:55:15
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoProductLeaguer {


    /**
     * 商品唯一标识符,业务id,此id对外开放
     */
    private String bizId;

    /**
     * 名称
     */
    private String name;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 标签;多个用分号隔开;
     */
    private String tag;


    /**
     * 商品分类;英文;
     */
    private String classifyUs;

    /**
     * 商品分类;中文
     */
    private String classifyCn;

    /**
     * 商品提示;
     */
    private String tips;


    /**
     * 友商价(分)
     */
    private Integer tradePrice;


    /**
     * 缩略图地址
     */
    private String smallImg;


    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 商品上架时间
     */
    private Date releaseTime;

    /**
     * 保卡;0:没有; 1:有;
     */
    private String repairCard;
    /**
     * 保卡有效时间
     */
    private String repairCardTime;
    /**
     * 保卡图片
     */
    private String cardCodeImg;

    /**
     * 商品图片
     */
    private String productImg;

    /**
     * 保卡图片集合
     */
    private String[] cardCodeImgList;

    /**
     * 商品图片集合
     */
    private String[] productImgList;

    /**
     * 标签集合
     */
    private String[] tags;

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
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

    public String getClassifyUs() {
        return classifyUs;
    }

    public void setClassifyUs(String classifyUs) {
        this.classifyUs = classifyUs;
    }

    public String getClassifyCn() {
        return classifyCn;
    }

    public void setClassifyCn(String classifyCn) {
        this.classifyCn = classifyCn;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public Integer getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(Integer tradePrice) {
        this.tradePrice = tradePrice;
    }

    public String getSmallImg() {
        return smallImg;
    }

    public void setSmallImg(String smallImg) {
        this.smallImg = smallImg;
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

    public String getRepairCard() {
        return repairCard;
    }

    public void setRepairCard(String repairCard) {
        this.repairCard = repairCard;
    }

    public String getRepairCardTime() {
        return repairCardTime;
    }

    public void setRepairCardTime(String repairCardTime) {
        this.repairCardTime = repairCardTime;
    }

    public String getCardCodeImg() {
        return cardCodeImg;
    }

    public void setCardCodeImg(String cardCodeImg) {
        this.cardCodeImg = cardCodeImg;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String[] getCardCodeImgList() {
        return cardCodeImgList;
    }

    public void setCardCodeImgList(String[] cardCodeImgList) {
        this.cardCodeImgList = cardCodeImgList;
    }

    public String[] getProductImgList() {
        return productImgList;
    }

    public void setProductImgList(String[] productImgList) {
        this.productImgList = productImgList;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
