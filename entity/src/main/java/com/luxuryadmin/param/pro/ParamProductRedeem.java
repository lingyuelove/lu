package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 质押商品赎回操作
 */
@Data
public class ParamProductRedeem extends ParamToken {

    /**
     * 商品Id
     */
    @ApiModelProperty(name = "prodId", required = true,
            value = "商品Id")
    @NotBlank(message = "prodId不允许为空")
    private String prodId;

    /**
     * 赎回价格
     */
    @ApiModelProperty(name = "redeemPrice", required = true, value = "赎回价格;")
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "赎回价格--参数错误")
    private String redeemPrice;

}
