package com.luxuryadmin.param.biz;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 搜索友商店铺--前端接收参数模型
 *
 * @author monkey king
 * @date 2020-07-29 20:06:21
 */
@ApiModel(description = "搜索友商店铺--前端接收参数模型")
public class ParamShopNumberToken extends ParamToken {

    /**
     * 搜索号码(店铺编号或手机号)
     * shopNumber
     */
    @ApiModelProperty(value = "搜索号码(店铺编号或手机号)", name = "shopNumber", required = true)
    @Pattern(regexp = "^[0-9]{4,11}$", message = "搜索号码格式不正确!")
    @NotBlank(message = "搜索号码不允许为空!")
    private String shopNumber;

    public String getShopNumber() {
        return shopNumber;
    }

    public void setShopNumber(String shopNumber) {
        this.shopNumber = shopNumber;
    }
}
