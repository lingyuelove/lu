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
public class VoTempProductOrgPageByApp {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    @ApiModelProperty(value = "主键Id", name = "id")
    private Integer tempProductId;

    @ApiModelProperty(value = "商品id", name = "productId")
    private Integer productId;

    @ApiModelProperty(value = "机构临时仓详情id", name = "productId")
    private Integer  organizationTempId;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称", name = "productName")
    private String name;
    /**
     * 缩略图地址
     */
    @ApiModelProperty(value = "缩略图地址", name = "smallImg")
    private String smallImg;


    @ApiModelProperty(value = "商品数量", name = "productCount")
    private Integer productCount;

    /**
     * 友商价(分)结算价
     */
    @ApiModelProperty(value = "结算价即友商价(分)", name = "tradePrice")
    private BigDecimal tradePrice;

    @ApiModelProperty(value = "店铺名称", name = "shopName")
    private String shopName;

    @ApiModelProperty(value = "店铺地址", name = "shopAddress")
    private String address;

    @ApiModelProperty(value = "展会位置", name = "showSeat")
    private String showSeat;

    @ApiModelProperty(value = "店铺机构仓排序位置分组名称", name = "tempSeatName")
    private String tempSeatName;

    @ApiModelProperty(value = "商品状态", name = "stateUs")
    private  String stateUs;

    @ApiModelProperty(value = "临时仓id", name = "id")
    private Integer tempId;

}
