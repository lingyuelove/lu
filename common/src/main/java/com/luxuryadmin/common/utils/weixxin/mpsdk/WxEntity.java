package com.luxuryadmin.common.utils.weixxin.mpsdk;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author monkey king
 * @date 2021-04-26 21:09:56
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="微信接口返回实体", description="微信接口返回实体")
public class WxEntity {
    /**
     * 微信的openId
     */
    @ApiModelProperty(value = "微信的open_id", name = "openId")
    private String openId;

    private String insertTime;

    private String updateTime;

    /**
     *微信的refresh_token
     */
    @ApiModelProperty(value = "微信的refresh_token", name = "refreshToken")
    private String refreshToken;

    /**
     *微信的access_token
     */
    @ApiModelProperty(value = "微信的access_token", name = "accessToken")
    private String accessToken;

    /**
     *微信的js_code
     */
    @ApiModelProperty(value = "微信的js_code", name = "jsCode")
    private String jsCode;

    /**
     *微信的session_key
     */
    @ApiModelProperty(value = "微信的session_key", name = "accessToken")
    private String sessionKey;

    /**
     *微信的unionid
     */
    @ApiModelProperty(value = "微信的unionid", name = "unionId")
    private String unionId;
}
