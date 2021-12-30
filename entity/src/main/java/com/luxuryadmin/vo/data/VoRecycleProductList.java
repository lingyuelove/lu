package com.luxuryadmin.vo.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luxuryadmin.vo.pro.VoProductLoad;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @PackgeName: com.luxuryadmin.vo.data
 * @ClassName: VoRecycleProductList
 * @Author: ZhangSai
 * Date: 2021/6/11 10:54
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="回收列表-- 回收的商品列表", description="回收列表-- 回收的商品列表")
public class VoRecycleProductList extends VoProductLoad {

    /**
     * 商品唯一标识符,业务id,此id对外开放
     */
    @ApiModelProperty(value = "产品bizId", name = "bizId")
    private String bizId;

    /**
     * 回收人员
     */
    @ApiModelProperty(value = "回收人员", name = "recycleAdmin")
    private Integer recycleAdmin;

    /**
     * 回收人员名称
     */
    @ApiModelProperty(value = "回收人员名称", name = "recycleAdminName")
    private String recycleAdminName;

//    /**
//     * 名称
//     */
//    @ApiModelProperty(value = "名称", name = "name")
//    private String name;
//
//    /**
//     * 缩略图地址
//     */
//    @ApiModelProperty(value = "缩略图地址", name = "smallImg")
//    private String smallImg;
//
//    /**
//     * 成本价(分)
//     */
//    @ApiModelProperty(value = "成本价(分)", name = "initPrice")
//    private String initPrice;
//
//    /**
//     * 友商价(分)
//     */
//    @ApiModelProperty(value = "友商价(分)", name = "tradePrice")
//    private String tradePrice;
//
//    /**
//     * 代理价(分)
//     */
//    @ApiModelProperty(value = "代理价(分)", name = "agencyPrice")
//    private String agencyPrice;
//
//    /**
//     * 销售价(分)
//     */
//    @ApiModelProperty(value = "销售价(分)", name = "salePrice")
//    private String salePrice;
//
//    /**
//     * 该商品总库存;
//     */
//    @ApiModelProperty(value = "该商品总库存", name = "totalNum")
//    private int totalNum;
//
//    /**
//     * 插入时间
//     */
//    @ApiModelProperty(value = "插入时间", name = "insertTime")
//    private String insertTime;
//
//    /**
//     * 动态名称
//     */
//    @ApiModelProperty(value = "动态名称", name = "dynamicName")
//    private String dynamicName;
//
//    /**
//     * 动态id
//     */
//    @ApiModelProperty(value = "动态id", name = "dynamicId")
//    private String dynamicId;
//
//    /**
//     * 商品属性;英文;
//     */
//    private String attributeUs;
//
//    /**
//     * 商品属性;中文
//     */
//    private String attributeCn;
//
//    /**
//     * 适用人群
//     */
//    @ApiModelProperty(value = "适用人群", name = "targetUser")
//    private String targetUser;
}
