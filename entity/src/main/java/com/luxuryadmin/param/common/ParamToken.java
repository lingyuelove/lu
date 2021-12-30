package com.luxuryadmin.param.common;

/**
 * @author monkey king
 * @date 2020-07-29 17:20:40
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * token--前端接收参数模型
 *
 * @author monkey king
 * @Date 2020-07-29 17:21:03
 */
@ApiModel(description = "token/登录标识符")
@Data
public class ParamToken extends ParamBasic {

    /**
     * token
     */
    @ApiModelProperty(value = "token", name = "token", required = true)
    @Length(min = 30, max = 50, message = "token格式错误")
    @NotBlank(message = "token不允许为空")
    private String token;

}
