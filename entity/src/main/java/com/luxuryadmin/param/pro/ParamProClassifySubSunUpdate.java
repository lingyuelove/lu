package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamProClassifySubSunUpdate
 * @Author: ZhangSai
 * Date: 2021/8/4 14:18
 */
@ApiModel(description = "品牌系列分类更新类")
@Data
public class ParamProClassifySubSunUpdate extends ParamProClassifySubSunAdd{
    @ApiModelProperty(name = "id;", required = true, value = "主键id不为空")
    @Pattern(regexp = "^[0-9]+$", message = "id--参数错误")
    private String id;
}
