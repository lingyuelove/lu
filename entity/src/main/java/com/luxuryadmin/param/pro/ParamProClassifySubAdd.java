package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @Author:     Mong
 * @Date:    2021/5/27 14:33
 * @Description: 二级分类添加类
 */
@ApiModel(description = "二级分类添加类")
@Data
public class ParamProClassifySubAdd {



    /**
     * 商品分类code; 和分类列表对应; 默认0:无分类;
     */
    @ApiModelProperty(name = "classifyCode", required = false,value = " 商品分类code; 和分类列表对应; 默认0:无分类;逗号分隔")
    private String classifyCode;

    @ApiModelProperty(name = "fkShpShopId", required = false, value = "商铺主键id")
    @Pattern(regexp = "^[0-9]+$", message = "id--参数错误")
    private String shopId;

    @ApiModelProperty(name = "name", required = true, value = "二级分类名称")
    @NotBlank(message = "分类名称不能为空!")
    private String name;
    /**
     * 图标地址
     */
    @ApiModelProperty(name = "name", required = true, value = "iconUrl")
    private String iconUrl;
    /**
     * 序号排序
     */
    @ApiModelProperty(value = "序号排序")
    private Integer sort;

}
