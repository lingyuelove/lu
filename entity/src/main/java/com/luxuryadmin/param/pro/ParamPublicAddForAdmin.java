package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamPublicAddForAdmin
 * @Author: ZhangSai
 * Date: 2021/8/18 16:14
 */
@Data
@ApiModel(value="公价商品新增后台", description="公价商品新增后台")
public class ParamPublicAddForAdmin {

    /**
     * 产品分类表的code;默认':无分类;
     */
    @ApiModelProperty(name = "classifyCode", value = "产品分类表的code;默认':无分类;")
    private String classifyCode;

    /**
     * 品牌名称
     */
    @ApiModelProperty(name = "name", value = "品牌名称")
    private String name;

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
    @ApiModelProperty(name = "smallImg", value = "公价图地址")
    private String smallImg;

    /**
     * 国内公价(元)
     */
    @ApiModelProperty(name = "publicPrice", value = "国内公价")
    private Integer publicPrice;
}
