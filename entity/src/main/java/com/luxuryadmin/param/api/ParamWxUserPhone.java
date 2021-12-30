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
public class ParamWxUserPhone extends ParamBasic {

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
     * iv
     */
    @ApiModelProperty(value = "iv", name = "iv", required = true)
    private String iv;

}