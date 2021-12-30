package com.luxuryadmin.vo.banner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class VOBannerInfoList {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "url")
    private String url;

    @ApiModelProperty(value = "名称")
    private String bannerName;

    @ApiModelProperty(value = "跳转类型")
    private String skipType;

    @ApiModelProperty(value = "跳转地址")
    private String skipAddress;
}
