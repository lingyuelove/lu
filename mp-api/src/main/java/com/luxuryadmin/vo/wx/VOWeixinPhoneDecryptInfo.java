package com.luxuryadmin.vo.wx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VOWeixinPhoneDecryptInfo {

    @ApiModelProperty(value = "用户绑定的手机号（国外手机号会有区号）", name = "phoneNumber")
    private String phoneNumber;

    @ApiModelProperty(value = "没有区号的手机号", name = "purePhoneNumber")
    private String purePhoneNumber;

    @ApiModelProperty(value = "区号", name = "countryCode")
    private int countryCode;

    private VOWeixinWaterMark watermark;

    private String token;

    private Integer userId;
}
