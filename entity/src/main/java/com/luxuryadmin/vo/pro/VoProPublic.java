package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author monkey king
 * @date 2021-06-29 16:45:53
 */
@Data
public class VoProPublic {

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

    /**
     * 实际上是未处理过的name值
     */
    @ApiModelProperty(name = "classifyCodeSub", value = "实际上是未处理过的name值")
    private String classifyCodeSub;


    /**
     * 第三方分类;此分类是其它平台对其进行分类;
     */
    @ApiModelProperty(name = "thirdClassify", value = "第三方分类;此分类是其它平台对其进行分类;")
    private String thirdClassify;

    /**
     * 品牌名称
     */
    @ApiModelProperty(name = "name", value = "品牌名称")
    private String name;

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
     * 机芯类型
     */
    @ApiModelProperty(name = "watchCoreType", value = "机芯类型")
    private String watchCoreType;

    /**
     * 表壳材质
     */
    @ApiModelProperty(name = "watchcase", value = "表壳材质")
    private String watchcase;

    /**
     * 表盘直径
     */
    @ApiModelProperty(name = "watchcaseSize", value = "表盘直径")
    private String watchcaseSize;

    /**
     * 材质
     */
    @ApiModelProperty(name = "material", value = "材质")
    private String material;

    /**
     * 尺寸;一般有长宽高
     */
    @ApiModelProperty(name = "objectSize", value = "尺寸;一般有长宽高")
    private String objectSize;

    /**
     * 尺码;一般指衣服和鞋的大小
     */
    @ApiModelProperty(name = "clothesSize", value = "尺码;一般指衣服和鞋的大小")
    private String clothesSize;

    /**
     * 国内公价(元)
     */
    @ApiModelProperty(name = "publicPrice", value = "国内公价(元)")
    private String publicPrice;

    /**
     * 公价图地址
     */
    @ApiModelProperty(name = "smallImg", value = "公价图缩略地址")
    private String smallImg;

    /**
     * 原图
     */
    @ApiModelProperty(name = "bigImg", value = "原图")
    private String bigImg;

    @ApiModelProperty(name = "supplementInfo", value = "封装的补充信息")
    private List<String> supplementInfo;
}
