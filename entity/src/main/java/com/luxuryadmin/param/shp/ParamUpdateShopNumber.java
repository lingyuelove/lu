package com.luxuryadmin.param.shp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 修改店铺编码
 *
 * @author Administrator
 * @Classname ParamShpShop
 * @Description TODO
 * @Date 2020/6/24 13:57
 * @Created by Administrator
 */
@Data
public class ParamUpdateShopNumber {

    /**
     * 登录标识符
     */
    @ApiModelProperty(value = "登录标识符", name = "token", required = true)
    @NotBlank(message = "token不允许为空")
    private String token;

    /**
     * 认证状态:
     */
    @ApiModelProperty(value = "新的店铺编号", name = "newShopNumber", required = true)
    @Pattern(regexp = "^[0-9]{4,}$", message = "店铺编号格式错误")
    @NotBlank(message = "newShopNumber不允许为空")
    private String newShopNumber;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id", name = "shopId", required = true)
    @Pattern(regexp = "^[0-9]{4,}$", message = "店铺id格式错误")
    @NotBlank(message = "shopId不允许为空")
    private String shopId;

}