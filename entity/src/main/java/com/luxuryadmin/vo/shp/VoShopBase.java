package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luxuryadmin.enums.shp.EnumShpUserType;
import lombok.Data;

import java.util.Date;

/**
 * @author monkey king
 * @Date 2019/12/14 1:44
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoShopBase {

    /**
     * 店铺id
     */
    private Integer shopId;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 店铺编号
     */
    private String shopNumber;

    /**
     * -2:超级管理员(店长)；-1：管理员；0：普通人员；1：访客；
     */
    private String type;

    /**
     * 店铺头像地址
     */
    private String shopHeadImgUrl;

    /**
     * 注册时间
     */
    private Date insertTime;

    /**
     * 会员状态 0:非会员; 1:体验会员;2:正式会员;3:靓号会员
     */
    private String memberState;


    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getShopNumber() {
        return shopNumber;
    }

    public void setShopNumber(String shopNumber) {
        this.shopNumber = shopNumber;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = EnumShpUserType.getShpUserTypeName(type);
    }

    public String getShopHeadImgUrl() {
        return shopHeadImgUrl;
    }

    public void setShopHeadImgUrl(String shopHeadImgUrl) {
        this.shopHeadImgUrl = shopHeadImgUrl;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
}
