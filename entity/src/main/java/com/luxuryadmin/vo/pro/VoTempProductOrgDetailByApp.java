package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 临时仓商品在机构集合显示app端
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="临时仓商品在机构集合显示", description="临时仓商品在机构集合显示")
public class VoTempProductOrgDetailByApp {

    /**
     * 主键Id,逻辑id,软件内部关联
     */
    @ApiModelProperty(value = "主键Id", name = "id")
    private Integer tempProductId;

    @ApiModelProperty(value = "临时仓id", name = "tempId")
    private Integer tempId;

    @ApiModelProperty(value = "商品id", name = "productId")
    private Integer productId;

    /**
     * 友商价(分)结算价
     */
    @ApiModelProperty(value = "结算价即友商价(分)", name = "tradePrice")
    private BigDecimal tradePrice;

    /**
     * 名称
     */
    @ApiModelProperty(value = "商品名称", name = "name")
    private String name;

    /**
     * 商品描述
     */
    @ApiModelProperty(value = "商品描述", name = "description")
    private String description;

    /**
     * 适用人群
     */
    @ApiModelProperty(value = "适用人群", name = "targetUser")
    private String targetUser;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片", name = "productImg")
    private String productImg;

    /**
     * 商品图片集合
     */
    @ApiModelProperty(value = "商品图片集合", name = "productImgList")
    private String[] productImgList;

    /**
     * 视频地址
     */
    @ApiModelProperty(value = "视频地址", name = "videoUrl")
    private String videoUrl;

    @ApiModelProperty(value = "店铺名称", name = "shopName")
    private String shopName;

    /**
     * 店铺固话
     */
    @ApiModelProperty(value = "店铺固话", name = "shopContact")
    private String shopContact;
}
