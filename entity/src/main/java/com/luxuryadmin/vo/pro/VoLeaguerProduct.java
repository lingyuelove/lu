package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luxuryadmin.enums.pro.EnumProState;
import com.luxuryadmin.vo.shp.VoShpWechat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 商品(友商)VO模型-卡片形式显示-详情显示
 *
 * @author monkey king
 * @date 2020-08-03 22:39:28
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoLeaguerProduct {

    private Integer proId;

    /**
     * 店铺id,属于哪个店铺的商品
     */
    private Integer shopId;

    /**
     * 商品唯一标识符,业务id,此id对外开放
     */
    private String bizId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品描述
     */
    private String description;


    /**
     * 友商价(分)
     */
    private String tradePrice;

    /**
     * 销售价
     */
    private String salePrice;

    /**
     * 缩略图地址
     */
    private String smallImg;

    /**
     * 商品图片
     */
    private String productImg;

    /**
     * 视频地址
     */
    private String videoUrl;


    /**
     * 适用人群
     */
    private String targetUser;


    /**
     * 附件
     */
    private String accessory;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 地址
     */
    private String address;

    /**
     * 店铺电话
     */
    private String phone;


    /**
     * 商品图片集合
     */
    private String[] productImgList;

    /**
     * 店铺微信列表
     */
    @ApiModelProperty(value = "店铺微信列表")
    private List<VoShpWechat> voShpWechatList;

    /**
     * 商品上架时间
     */
    private String releaseTime;
    /**
     * 商品数量
     */
    private Integer totalNum;

    /**
     * biz_shop_union.id
     */
    private String bsuId;

    /**
     * 商品上架时间
     */
    @ApiModelProperty(value = "商品上架时间前端显示使用")
    private String showTime;

    private String shopNumber;

    @ApiModelProperty(value="友商好友状态 0:不是好友; 1:是好友 2:是本人")
    private String leaguerFriendState;

    @ApiModelProperty(value = "补充信息分类集合显示")
    private List<VoClassifyTypeSonList> classifyTypeList;

    /**
     * 公价
     */
    @ApiModelProperty(value = "官方指导价")
    private String publicPrice;

    @ApiModelProperty(value = "商家联盟查看权限")
    private String  unionPerm;
}
