package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 临时仓id的参数模型
 *
 * @author by Administrator
 * @Classname ParamProduct
 * @Description TODO
 * @date 2021-01-18 18:47:19
 */
@ApiModel(description = "临时仓id的参数模型")
@Data
public class ParamProTempId {

    /**
     * 登录标识符
     */
    @ApiModelProperty(value = "登录标识符", name = "token", required = true)
    private String token;


    @ApiModelProperty(value = "临时仓id", name = "proTempId", required = true)
    @NotBlank(message = "临时仓ID不能为空")
    @Pattern(regexp = "^[0-9]+$", message = "临时仓id--参数错误")
    private String proTempId;



}
