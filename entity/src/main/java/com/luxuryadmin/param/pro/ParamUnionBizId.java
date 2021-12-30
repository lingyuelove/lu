package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamBasic;
import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 需要商品业务逻辑进行操作--前端接收参数模型
 *
 * @author monkey king
 * @date 2019-12-23 13:56:43
 */
@ApiModel(description = "需要商品业务逻辑进行操作--前端接收参数模型")
@Data
public class ParamUnionBizId extends ParamBasic {

    /**
     * 商品业务逻辑id
     */
    @ApiModelProperty(name = "bizId", required = true, value = "商品业务逻辑id;多个用英文分号隔开")
    @NotBlank(message = "bizId--参数错误")
    private String bizId;

    @ApiModelProperty(value = "店铺id", name = "shopId", required = true)
    @Pattern(regexp = "^[0-9,]+$", message = "[shopId]参数错误")
    @NotBlank(message = "[shopId]参数错误")
    private String shopId;

    @ApiModelProperty(value = "userPhone", name = "userPhone", required = false)
    private String userPhone;

    /**
     * 微信的openId
     */
    @ApiModelProperty(value = "微信的openId")
    private String openId;

}
