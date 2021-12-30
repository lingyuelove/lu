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
@ApiModel(description = "删除商品位置")
@Data
public class ParamDynamicProductDelete extends ParamToken {


    @ApiModelProperty(name = "dynamicId", required = true, value = "动态id 1,2,3,4,5,6,7")
    @NotBlank(message = "商品位置id不能为空")
    private String dynamicProductIds;


}
