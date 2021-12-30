package com.luxuryadmin.param.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * APP系统版本--前端参数模型
 *
 * @author sanjin
 * @Date 2020-08-25
 */
@ApiModel(description = "APP系统版本--前端参数模型")
@Data
public class ParamSysAppVersion{


    /**
     * 平台
     */
    @ApiModelProperty(name = "platform", required = true,
            value = "平台 ios|android")
    @NotBlank(message = "平台不允许为空")
    @Pattern(regexp = "^(ios)|(android)$", message = "平台字段--参数错误")
    private String platform;

    /**
     * 版本号
     */
    @ApiModelProperty(name = "appVersion", required = true, value = "版本号;")
    @NotBlank(message = "版本号不允许为空")
    @Pattern(regexp = "^([1-9]\\d|[1-9])(\\.([1-9]\\d|\\d)){2}$", message = "版本号格式错误")
    private String appVersion;
}
