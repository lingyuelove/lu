package com.luxuryadmin.param.pro;


import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author taoqimin
 * @date 2021-11-05
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "商品位置列表")
@Data
public class ParamDynamicProductQuery extends ParamToken {


    @ApiModelProperty(name = "dynamicId", required = true, value = "动态id")
    @Pattern(regexp = "^\\d+$", message = "[pageNum]格式错误")
    @NotBlank(message = "动态id不能为空")
    private String dynamicId;

}
