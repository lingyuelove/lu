package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamClassifyTypeShop
 * @Author: ZhangSai
 * Date: 2021/8/5 15:05
 */
@ApiModel(description = "单个店铺补充信息不适用关联表")
@Data
public class ParamClassifyTypeShopAdd {
    @ApiModelProperty(name = "shopId", hidden = true, value = "商铺主键id 后端自己获取")
    private Integer shopId;

    @ApiModelProperty(name = "userId", hidden = true, value = "用户id 后端自己获取")
    private Integer userId;

//    @ApiModelProperty(name = "state", required = true,value = "默认 0 不启用 1 启用")
//    private Integer state;

    @ApiModelProperty(name = "classifyTypeId", required = false, value = "产品补充信息启用id,多个分号分开")
//    @Pattern(regexp = "^[0-9]+$", message = "classifyTypeId--参数错误")
//    @NotBlank(message = "请选择补充信息!")
    private String classifyTypeId;

    @ApiModelProperty(name = "classifyTypeIdForNot", required = false, value = "产品补充信息禁用id,多个分号分开")
    private String classifyTypeIdForNot;
}
