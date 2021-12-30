package com.luxuryadmin.vo.fin;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 销售排行榜
 *
 * @author monkey king
 * @date 2020-01-15 17:05:08
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoSaleTop {

    /**
     * db结果集的序号;从1开始
     */
    private Integer serialNo;

    /**
     * 头像
     */
    private String headImageUrl;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 职位
     */
    private String role;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 会员编号
     */
    private String userNumber;

    /**
     * 销售数量
     */
    private String saleOrderCount;

    /**
     * 销售总额
     */
    private String salePrice;

    /**
     * 毛利润
     */
    private String grossProfit;


    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getSaleOrderCount() {
        return saleOrderCount;
    }

    public void setSaleOrderCount(String saleOrderCount) {
        this.saleOrderCount = saleOrderCount;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(String grossProfit) {
        this.grossProfit = grossProfit;
    }
}
