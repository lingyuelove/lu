package com.luxuryadmin.param.api;

import com.luxuryadmin.param.common.ParamBasic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author monkey king
 * @date 2021-04-26 21:16:43
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "微信的code")
@Data
public class ParamWxJsCode extends ParamBasic {

    /**
     * jsCode
     */
    @ApiModelProperty(value = "jsCode", name = "jsCode", required = true)
    @NotBlank(message = "jsCode不允许为空")
    private String jsCode;
}