package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamClassifySubAddForApi
 * @Author: ZhangSai
 * Date: 2021/6/8 16:26
 */
@ApiModel(description = "二级分类添加其他类中的分类")
@Data
public class ParamClassifySubAddForApi {

    @ApiModelProperty(name = "shopId", required = false, value = "商铺主键id 后端自己获取")
//    @Pattern(regexp = "^[0-9]+$", message = "id--参数错误")
    private Integer shopId;

    @ApiModelProperty(name = "userId", required = false, value = "用户id 后端自己获取")
//    @Pattern(regexp = "^[0-9]+$", message = "id--参数错误")
    private Integer userId;

    @ApiModelProperty(name = "name", required = true, value = "二级分类名称")
    @NotBlank(message = "分类名称不能为空!")
    private String name;
    @ApiModelProperty(name = "classifyCode", required = true, value = "一级分类名称")
    @NotBlank(message = "一级分类名称不能为空!")
    private String classifyCode;

}
