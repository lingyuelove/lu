package com.luxuryadmin.param.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luxuryadmin.common.aop.check.DateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 操作店铺所需的验证码
 * @author Administrator
 */
@ApiModel(description = "一键物理删除")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParamShopImageCode {

    /**
     * 登录标识符
     */
    @ApiModelProperty(value = "登录标识符", name = "token", required = true)
    private String token;

    /**
     * 4位长度的图形验证码
     */
    @ApiModelProperty(value = "图形验证码", name = "password", required = false)
    @Pattern(regexp = "^[a-zA-Z0-9]{4}$", message = "图形验证码错误")
    private String imageCode;

}
