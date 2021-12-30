package com.luxuryadmin.param.pro;


import com.luxuryadmin.common.aop.check.DateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * 商品页面查询参数实体--前端接收参数模型
 *
 * @author monkey king
 * @date 2019-12-23 13:56:43
 */
@ApiModel(description = "商品页面查询参数实体")
public class ParamShareProductQuery extends ParamProductQuery {


    /**
     * 店铺编号
     */
    @ApiModelProperty(name = "shopNumber", required = true, value = "店铺编号")
    @Length(max = 9, message = "shopNumber--参数错误")
    private String shopNumber;

    /**
     * 用户编号
     */
    @ApiModelProperty(name = "userNumber", required = true, value = "用户编号")
    @Length(max = 9, message = "userNumber--参数错误")
    private String userNumber;

    /**
     * 分享批号
     */
    @ApiModelProperty(name = "shareBatch", required = true, value = "分享批号")
    @Length(max = 50, message = "shareBatch--参数错误")
    private String shareBatch;

    /**
     * 当前页
     */
    @ApiModelProperty(name = "pageNum", required = false, value = "当前页;默认为1")
    @Min(value = 1, message = "当前页不能小于1")
    @Max(value = 999, message = "当前页最大为999")
    private String pageNum;

    /**
     * 排序字段
     * normal(默认排序);price(价格排序);time(入库时间排序);notDown(最久未一键下载)
     */
    @ApiModelProperty(name = "sortKey", required = false,
            value = "排序字段;normal(默认排序);price(价格排序);")
    private String sortKey;

    /**
     * 排序顺序;desc(降序) | asc(升序)
     */
    @ApiModelProperty(name = "sortValue", required = false, value = "排序顺序;desc(降序) | asc(升序)")
    @Pattern(regexp = "^(desc)|(asc)$", message = "排序顺序--参数错误")
    private String sortValue;


    /**
     * 适用人群
     */
    @ApiModelProperty(name = "targetUser", required = false, value = "适用人群;(通用;男;女)可多选,分号隔开")
    @Pattern(regexp = "^[通用男女;]{1,7}$", message = "适用人群--参数错误")
    private String targetUser;


    /**
     * 分类编码;all:全部
     */
    @ApiModelProperty(name = "classifyCode", required = false, value = "分类编码(传code值到服务器);")
    @Length(min = 2, max = 20, message = "分类编码长度在2~20个字符")
    private String classifyCode;

    /**
     * 商品名称
     */
    @ApiModelProperty(name = "proName", required = false, value = "商品名称")
    @Length(max = 50, message = "商品名称必须≤50个字符")
    private String proName;


    /**
     * 商品成色;
     */
    @ApiModelProperty(name = "sortValue", required = false,
            value = "商品成色;(N;S;A;B;C;D)可多选,分号隔开")
    @Pattern(regexp = "^[NSABCD;]{1,12}$", message = "适用人群--参数错误")
    private String quality;


    /**
     * 最低价格
     */
    @ApiModelProperty(name = "priceMin", required = false, value = "最低价格;")
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "最低价格--参数错误")
    private String priceMin;

    /**
     * 最高价格
     */
    @ApiModelProperty(name = "priceMax", required = false, value = "最高价格;")
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "最高价格--参数错误")
    private String priceMax;


    /**
     * shopId;此值在控制层赋值(获取登录者的shopId)
     */
    @ApiModelProperty(hidden = true)
    private Integer shopId;

    /**
     * 商品id,逗号拼接而成;用作与in查询
     */
    @ApiModelProperty(hidden = true)
    private String proIds;

    @ApiModelProperty(hidden = true)
    private String token;

    @ApiModelProperty(hidden = true)
    private String attributeCode;

    @ApiModelProperty(hidden = true)
    private String stateCode;

    @ApiModelProperty(hidden = true)
    private String repairCard;

    @ApiModelProperty(hidden = true)
    private String uploadStDateTime;

    @ApiModelProperty(hidden = true)
    private String uploadEtDateTime;

    @ApiModelProperty(hidden = true)
    private String priceType;

    @ApiModelProperty(hidden = true)
    private String uploadUserId;
    @ApiModelProperty(name = "classifyCodes", hidden = true, value = "分类编码(传code值到服务器)多个分号分隔;")
    @Length(min = 2, max = 20, message = "分类编码长度在2~20个字符")
    private String classifyCodes;

    public String getShopNumber() {
        return shopNumber;
    }

    public void setShopNumber(String shopNumber) {
        this.shopNumber = shopNumber;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getShareBatch() {
        return shareBatch;
    }

    public void setShareBatch(String shareBatch) {
        this.shareBatch = shareBatch;
    }

    @Override
    public String getPageNum() {
        return pageNum;
    }

    @Override
    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public String getSortKey() {
        return sortKey;
    }

    @Override
    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    @Override
    public String getSortValue() {
        return sortValue;
    }

    @Override
    public void setSortValue(String sortValue) {
        this.sortValue = sortValue;
    }

    @Override
    public String getTargetUser() {
        return targetUser;
    }

    @Override
    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }

    @Override
    public String getClassifyCode() {
        return classifyCode;
    }

    @Override
    public void setClassifyCode(String classifyCode) {
        this.classifyCode = classifyCode;
    }

    @Override
    public String getProName() {
        return proName;
    }

    @Override
    public void setProName(String proName) {
        this.proName = proName;
    }

    @Override
    public String getQuality() {
        return quality;
    }

    @Override
    public void setQuality(String quality) {
        this.quality = quality;
    }

    @Override
    public String getPriceMin() {
        return priceMin;
    }

    @Override
    public void setPriceMin(String priceMin) {
        this.priceMin = priceMin;
    }

    @Override
    public String getPriceMax() {
        return priceMax;
    }

    @Override
    public void setPriceMax(String priceMax) {
        this.priceMax = priceMax;
    }

    @Override
    public Integer getShopId() {
        return shopId;
    }

    @Override
    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    public String getProIds() {
        return proIds;
    }

    public void setProIds(String proIds) {
        this.proIds = proIds;
    }

    @Override
    public String getAttributeCode() {
        return attributeCode;
    }

    @Override
    public void setAttributeCode(String attributeCode) {
        this.attributeCode = attributeCode;
    }

    @Override
    public String getStateCode() {
        return stateCode;
    }

    @Override
    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    @Override
    public String getRepairCard() {
        return repairCard;
    }

    @Override
    public void setRepairCard(String repairCard) {
        this.repairCard = repairCard;
    }

    @Override
    public String getUploadStDateTime() {
        return uploadStDateTime;
    }

    @Override
    public void setUploadStDateTime(String uploadStDateTime) {
        this.uploadStDateTime = uploadStDateTime;
    }

    @Override
    public String getUploadEtDateTime() {
        return uploadEtDateTime;
    }

    @Override
    public void setUploadEtDateTime(String uploadEtDateTime) {
        this.uploadEtDateTime = uploadEtDateTime;
    }

    @Override
    public String getPriceType() {
        return priceType;
    }

    @Override
    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    @Override
    public String getUploadUserId() {
        return uploadUserId;
    }

    @Override
    public void setUploadUserId(String uploadUserId) {
        this.uploadUserId = uploadUserId;
    }

    public String getClassifyCodes() {
        return classifyCodes;
    }

    public void setClassifyCodes(String classifyCodes) {
        this.classifyCodes = classifyCodes;
    }
}
