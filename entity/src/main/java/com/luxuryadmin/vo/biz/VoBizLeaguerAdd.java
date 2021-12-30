package com.luxuryadmin.vo.biz;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author monkey king
 * @date 2020-01-12 20:37:15
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel
public class VoBizLeaguerAdd {

    /**
     * 添加友商记录id
     */
    @ApiModelProperty(value = "添加友商记录id")
    private Integer leaguerAddId;

    /**
     * 友商店铺名称
     */
    @ApiModelProperty(value = "友商店铺名称")
    private String leaguerShopName;

    /**
     * 友商店铺编号
     */
    @ApiModelProperty(value = "友商店铺编号")
    private String leaguerShopNumber;

    /**
     * 本店店铺id
     */
    @ApiModelProperty(value = "本店店铺id")
    private Integer shopId;

    /**
     * 友商店铺id
     */
    @ApiModelProperty(value = "友商店铺id")
    private Integer leaguerShopId;

    /**
     * 好友状态: -10:已删除该条信息; 10:已发请求(待确认); 11:已发请求(已忽略); 12:已发请求(已过期); 20:已成为好友
     */
    @ApiModelProperty(value = "好友状态: -10:已删除; 10:已发请求(待确认); 11:已发请求(已忽略); 12:已发请求(已过期); 20:已成为好友")
    private String state;

    /**
     * 友商状态: -90: 拉黑; -10:已删除; 10:已发请求(待确认); 20:已成为好友
     */
    @ApiModelProperty(value = "友商状态: -90: 拉黑; -10:已删除; 10:已发请求(待确认); 20:已成为好友")
    private String leaguerState;

    /**
     * 发起添加好友时间
     */
    @ApiModelProperty(value = "发起添加好友时间")
    private Date insertTime;

    /**
     * 添加请求信息
     */
    @ApiModelProperty(value = "添加请求信息")
    private String requestMsg;

    /**
     * 友商店铺头像
     */
    @ApiModelProperty(value = "友商店铺头像")
    private String headImgUrl;

    /**
     * 店铺地址
     */
    @ApiModelProperty(value = "店铺地址")
    private String address;

    @ApiModelProperty(value = "友商来源", name = "source", required = false)
    private String source;

    public Integer getLeaguerAddId() {
        return leaguerAddId;
    }

    public void setLeaguerAddId(Integer leaguerAddId) {
        this.leaguerAddId = leaguerAddId;
    }

    public String getLeaguerShopName() {
        return leaguerShopName;
    }

    public void setLeaguerShopName(String leaguerShopName) {
        this.leaguerShopName = leaguerShopName;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getLeaguerShopId() {
        return leaguerShopId;
    }

    public void setLeaguerShopId(Integer leaguerShopId) {
        this.leaguerShopId = leaguerShopId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public String getRequestMsg() {
        return requestMsg;
    }

    public void setRequestMsg(String requestMsg) {
        this.requestMsg = requestMsg;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getLeaguerShopNumber() {
        return leaguerShopNumber;
    }

    public void setLeaguerShopNumber(String leaguerShopNumber) {
        this.leaguerShopNumber = leaguerShopNumber;
    }

    public String getLeaguerState() {
        return leaguerState;
    }

    public void setLeaguerState(String leaguerState) {
        this.leaguerState = leaguerState;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
