package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamTempMovePro
 * @Author: ZhangSai
 * Date: 2021/9/24 17:59
 */
@Data
@ApiModel(value="临时仓商品移仓", description="临时仓商品移仓")
public class ParamTempMovePro extends ParamToken {
    @ApiModelProperty(value = "移出的临时仓id--2.6.4", name = "removeTempId", required = true)
    @Pattern(regexp = "^[0-9]+$", message = "移出的临时仓id--参数错误")
    private String removeTempId;
    @ApiModelProperty(value = "移入的临时仓id--2.6.4", name = "enterTempId", required = true)
    @Pattern(regexp = "^[0-9]+$", message = "移入的临时仓id--参数错误")
    private String enterTempId;
    @ApiModelProperty(value = "商品的tempProductId(临时仓商品id)集合 分号分割--2.6.4", name = "tempProductIds", required = true)
    @NotBlank( message = "移入的临时仓id--参数错误")
    private String tempProductIds;
    @ApiModelProperty(value = "用户id--2.6.4", name = "userId", hidden = true )
    private Integer userId;
    @ApiModelProperty(value = "店铺id--2.6.4", name = "shopId", hidden = true )
    private Integer shopId;
}
