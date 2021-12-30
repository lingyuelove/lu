package com.luxuryadmin.vo.biz;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luxuryadmin.vo.op.VoOpBannerForUnion;
import com.luxuryadmin.vo.pro.VoProClassify;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.biz
 * @ClassName: ShopUnionByAppShow
 * @Author: ZhangSai
 * Date: 2021/7/19 16:08
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel
@Data
public class VoShopUnionByAppShow {


    @ApiModelProperty(value = "店铺联盟商品总价值", name = "productTotalPrice")
    private BigDecimal productTotalPrice;

    @ApiModelProperty(value = "店铺联盟商品总数", name = "productTotalNum")
    private Integer productTotalNum;

    @ApiModelProperty(value = "商品分类集合显示", name = "classifyList")
    private List<VoProClassify> classifyList;

    @ApiModelProperty(value = "banner集合显示", name = "classifyList")
    private List<VoOpBannerForUnion> bannerList;

    @ApiModelProperty(value = "商品总数中文显示", name = "productTotalName")
    private String productTotalName;

    @ApiModelProperty(value = "商品总价值中文显示", name = "productTotalPriceName")
    private String productTotalPriceName;
    @ApiModelProperty(value = "联盟商品价格价值显示权限", name = "uPermShowUnionShop")
    private String uPermShowUnionShop;


    /**
     * 是否有商家联盟的分享权限
     */
    @ApiModelProperty(value = "是否有商家联盟的分享权限", name = "uPermShare")
    private String uPermShare;

    /**
     * 封面地址
     */
    @ApiModelProperty(value = "小程序封面;公用(长地址)", name = "mpImgUrl")
    private String mpImgUrl;

    /**
     * 封面地址
     */
    @ApiModelProperty(value = "小程序封面;公用(短地址)", name = "mpImgUrlShort")
    private String mpImgUrlShort;

    /**
     * 当前用户id
     */
    @ApiModelProperty(value = "当前用户id", name = "currentUserId")
    private String currentUserId;


    /**
     * 商家联盟分享关联用户
     */
    @ApiModelProperty(value = "商家联盟分享关联用户", name = "mpUnionAgencyList")
    private List<VoMpUnionAgencyUser> mpUnionAgencyList;


}

