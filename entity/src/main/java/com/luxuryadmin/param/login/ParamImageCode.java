package com.luxuryadmin.param.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 图形验证码-前端接收参数模型
 *
 * @author monkey king
 * @Date 2019-12-26 02:15:20
 */
@ApiModel(description = "图形验证码")
public class ParamImageCode extends ParamUsername {

    /**
     * 4位长度的图形验证码
     */
    @ApiModelProperty(value = "图形验证码", name = "password", required = false)
    @Pattern(regexp = "^[a-zA-Z0-9]{4}$", message = "图形验证码错误")
    private String imageCode;

    public String getImageCode() {
        return imageCode;
    }

    public void setImageCode(String imageCode) {
        this.imageCode = imageCode;
    }
}
