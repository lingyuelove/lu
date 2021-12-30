package com.luxuryadmin.param.weixin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class ParamGetPhone {

    @ApiModelProperty(value = "分享者id", required = false, name = "shareId")
    private Integer shareId;

    @ApiModelProperty(value = "openid", required = true, name = "openid")
    @NotBlank(message = "openid不能为空")
    private String openId;

    @ApiModelProperty(value = "初始向量", required = true, name = "iv")
    @NotBlank(message = "iv不能为空")
    private String iv;

    @ApiModelProperty(value = "加密的敏感数据", required = true, name = "encryptedData")
    @NotBlank(message = "encryptedData不能为空")
    private String encryptedData;

    @ApiModelProperty(value = "会话密钥", required = true, name = "sessionKey")
    @NotBlank(message = "sessionKey不能为空")
    private String sessionKey;

}
