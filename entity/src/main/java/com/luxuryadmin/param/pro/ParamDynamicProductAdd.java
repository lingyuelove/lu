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
@ApiModel(description = "添加商品位置")
@Data
public class ParamDynamicProductAdd extends ParamToken {


    @ApiModelProperty(name = "dynamicId", required = true, value = "动态id")
    private String dynamicId;

    @ApiModelProperty(name = "proId", required = false, value = "商品id,多商品已逗号分割， 1,2,3,4")
    private String proId;


    @ApiModelProperty(hidden = true)
    private Integer shopId;

    @ApiModelProperty(hidden = true)
    private Integer userId;
}
