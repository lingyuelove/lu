package com.luxuryadmin.param.op;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ParamAppBannerQuery {
    /**
     * app版本号
     */
    @ApiModelProperty(name = "appVersion", required = true, value = "APP版本号")
    @NotNull(message="APP版本号不能为空")
    private String appVersion;

    /**
     * 平台 ios|android
     */
    @ApiModelProperty(name = "platform", required = true, value = "平台",allowableValues = "ios,android")
    @NotNull(message="平台不能为空")
    private String platform;

    /**
     * banner位置 indexCarousel|首页轮播 indexPopupWindow|首页弹窗 myCarousel|我的页面轮播
     */
    @ApiModelProperty(name = "pos", required = true, value = "位置</br>indexCarousel|首页轮播</br>indexPopupWindow|首页弹窗</br>myCarousel|我的弹窗</br>printSettings|打印设置</br>shopUnion|商家联盟",
            allowableValues = "indexCarousel,indexPopupWindow,myCarousel,printSettings,shopUnion")
    @NotNull(message="位置不能为空")
    private String pos;
}
