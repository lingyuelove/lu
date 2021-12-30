package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamProClassifySubSunAdd
 * @Author: ZhangSai
 * Date: 2021/8/4 14:12
 */
@ApiModel(description = "品牌系列分类添加类")
@Data
public class ParamProClassifySubSunAdd  extends ParamToken {

    @ApiModelProperty(name = "name", required = true, value = "品牌/系列分类名称")
    @NotBlank(message = "分类名称不能为空!")
    private String name;

    /**
     * 商品品牌分类pro_classify_sub表品牌名称
     */
    @ApiModelProperty(name = "classifySubName", required = true,value = "商品品牌分类pro_classify_sub表品牌名称")
    private String classifySubName;
    @ApiModelProperty(name = "sort", hidden = false,value = "序号排序")
    private Integer sort;
    @ApiModelProperty(name = "shopId", hidden = true, value = "商铺主键id 后端自己获取")
    private Integer shopId;

    @ApiModelProperty(name = "userId", hidden = true, value = "用户id 后端自己获取")
    private Integer userId;
    @ApiModelProperty(name = "serviceModelName", hidden = true,value = "型号名称;逗号分隔")
    private String serviceModelName;
}
