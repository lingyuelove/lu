package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoProPublicForAdmin
 * @Author: ZhangSai
 * Date: 2021/8/18 15:20
 */
@Data
@ApiModel(value="公价商品库后台接口", description="公价商品库后台接口")
public class VoProPublicForAdmin {

    /**
     * 主键ID
     */
    @ApiModelProperty(name = "id", value = "主键ID")
    private Integer id;


    /**
     * -1:删除 | 0:停用 | 1:启用; 默认为 1
     */
    @ApiModelProperty(name = "state", value = " -1:删除 | 0:停用 | 1:启用; 默认为 1")
    private String state;

    /**
     * 产品分类表的code;默认':无分类;
     */
    @ApiModelProperty(name = "classifyCode", value = "产品分类表的code;默认':无分类;")
    private String classifyCode;
    @ApiModelProperty(name = "classifyCodeName", value = "产品分类表的code名称;默认':无分类;")
    private String classifyCodeName;
    /**
     * 品牌名称
     */
    @ApiModelProperty(name = "name", value = "品牌名称")
    private String name;

    /**
     * 图标地址
     */
    @ApiModelProperty(value = "品牌图标地址", name = "iconUrl")
    private String iconUrl;
    /**
     * 品牌名称
     */
    @ApiModelProperty(name = "description", value = "品牌名称")
    private String description;
    /**
     * 型号
     */
    @ApiModelProperty(name = "typeNo", value = "型号")
    private String typeNo;

    /**
     * 系列
     */
    @ApiModelProperty(name = "serialNo", value = "系列")
    private String serialNo;


    /**
     * 公价图地址
     */
    @ApiModelProperty(name = "smallImg", value = "公价图缩略地址")
    private String smallImg;

    /**
     * 国内公价(元)
     */
    @ApiModelProperty(name = "publicPrice", value = "国内公价(元)")
    private String publicPrice;
}
