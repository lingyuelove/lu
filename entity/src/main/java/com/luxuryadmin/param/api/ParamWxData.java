package com.luxuryadmin.param.api;

import com.luxuryadmin.param.common.ParamBasic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author monkey king
 * @date 2021-04-26 21:16:43
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "微信加密数据")
@Data
public class ParamWxData extends ParamBasic {

    /**
     * encryptedData
     */
    @ApiModelProperty(value = "encryptedData", name = "encryptedData", required = true)
    private String encryptedData;

    /**
     * sessionKey
     */
    @ApiModelProperty(value = "sessionKey", name = "encryptedData", required = true)
    private String sessionKey;

    /**
     * 微信的unionid
     */
    @ApiModelProperty(value = "微信的unionid", name = "unionId", required = true)
    @NotBlank(message = "[unionId]参数错误")
    private String unionId;

    /**
     * iv
     */
    @ApiModelProperty(value = "iv", name = "iv", required = true)
    private String iv;

    /**
     * 店铺编号
     */
    @ApiModelProperty(value = "店铺编号", name = "shopNumber", required = true)
    @Pattern(regexp = "^[0-9]{4,}$", message = "[shopNumber]参数错误")
    private String shopNumber;


    /**
     * 机构id
     */
    @ApiModelProperty(value = "机构id", name = "orgId", required = true)
    @Pattern(regexp = "^[0-9]{5,}$", message = "[orgId]参数错误")
    private String orgId;
}