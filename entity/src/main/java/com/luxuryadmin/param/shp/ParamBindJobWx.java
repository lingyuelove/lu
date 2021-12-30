package com.luxuryadmin.param.shp;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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
public class ParamBindJobWx extends ParamToken {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id", name = "shopId", required = true)
    @Pattern(regexp = "^[0-9]{4,}$", message = "店铺id格式错误")
    @NotBlank(message = "shopId不允许为空")
    private String shopId;

    /**
     * 微信工作id
     */
    @ApiModelProperty(value = "微信工作id", name = "sysJobWxId", required = true)
    @Pattern(regexp = "^[0-9]+$", message = "店铺id格式错误")
    @NotBlank(message = "[sysJobWxId]不允许为空")
    private String sysJobWxId;


}