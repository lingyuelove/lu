package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luxuryadmin.vo.pro.VoProClassify;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 首页店铺用户价格权限-前端接受模型
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoShopUserPricePerm {

    //同行价（友商价）
    @ApiModelProperty(value = "是否有同行价权限", name = "isHaveFriendPricePerm", required = false)
    Boolean isHaveFriendPricePerm;

    //销售价
    @ApiModelProperty(value = "是否有销售价权限", name = "isHaveSalePricePerm", required = false)
    Boolean isHaveSalePricePerm;

    //代理价
    @ApiModelProperty(value = "是否有代理价权限", name = "isHaveAgencyPricePerm", required = false)
    Boolean isHaveAgencyPricePerm;

    //成本价
    @ApiModelProperty(value = "是否有成本价权限", name = "isHaveInitPricePerm", required = false)
    Boolean isHaveInitPricePerm;

    //是否有在售商品
    @ApiModelProperty(value = "是否有在售商品", name = "isHaveOnSaleProduct", required = false)
    Boolean isHaveOnSaleProduct;

    //在售商品列表中第一个商品的图片
    @ApiModelProperty(value = "在售商品列表中第一个商品的图片", name = "imageUrl", required = false)
    String imageUrl;
    @ApiModelProperty(value = "商品分类集合显示", name = "classifyList")
    private List<VoProClassify> classifyList;
}
