package com.luxuryadmin.param.common;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 公用参数id
 * Date: 2021-08-30 20:26:05
 *
 * @author Administrator
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ParamIds extends ParamToken {


    @ApiModelProperty(value = "多个id,用英文逗号拼接;", name = "ids", required = true)
    @NotBlank(message = "[ids]参数不允许为空")
    private String ids;

}
