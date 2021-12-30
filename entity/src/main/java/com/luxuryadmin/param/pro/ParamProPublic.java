package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamBasic;
import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 公价查询的参数
 *
 * @author monkey king
 * @date 2021-06-22 23:36:24
 */
@ApiModel(description = "公价查询的参数--前端接收参数模型")
@Data
public class ParamProPublic extends ParamBasic {


    /**
     * 产品分类表的code;默认':无分类;
     */
    @ApiModelProperty(name = "classifyCode", value = "产品分类表的code;")
    private String classifyCode;

    /**
     * 品牌名称
     */
    @ApiModelProperty(name = "name", value = "品牌名称;")
    private String name;

    /**
     * 型号
     */
    @ApiModelProperty(name = "typeNo", value = "型号;")
    private String typeNo;

    /**
     * 系列
     */
    @ApiModelProperty(name = "serialNo", value = "系列")
    private String serialNo;

    /**
     * 机芯类型
     */
    @ApiModelProperty(name = "watchCoreType", value = "机芯类型;")
    private String watchCoreType;

    /**
     * 表壳材质
     */
    @ApiModelProperty(name = "watchcase", value = "表壳材质;")
    private String watchcase;

    /**
     * 表盘直径
     */
    @ApiModelProperty(name = "watchcaseSize", value = "表盘直径;")
    private String watchcaseSize;

    /**
     * 材质
     */
    @ApiModelProperty(name = "material", value = "材质;")
    private String material;

    /**
     * 尺寸;一般有长宽高
     */
    @ApiModelProperty(name = "objectSize", value = "尺寸;一般有长宽高;")
    private String objectSize;

    /**
     * 尺码;一般指衣服和鞋的大小
     */
    @ApiModelProperty(name = "clothesSize", value = "尺码;一般指衣服和鞋的大小;")
    private String clothesSize;

    /**
     * 查询关键字
     */
    @ApiModelProperty(name = "queryKey", value = "查询关键字")
    private String queryKey;

}
