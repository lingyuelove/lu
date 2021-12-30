package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamProductOrOrderForDelete
 * @Author: ZhangSai
 * Date: 2021/7/2 14:13
 */
@ApiModel(description = "删除模块--删除订单/商品")
@Data
public class ParamProductOrOrderForDelete extends ParamToken {
    /**
     * 订单ID
     */
    @ApiModelProperty(name = "orderNumber", value = "订单编号")
    private String orderNumber;

    /**
     * 商品唯一标识符,业务id,此id对外开放
     */
    @ApiModelProperty(name = "bizId", value = "商品唯一标识符,业务id")
    private String bizId;

    @ApiModelProperty(name = "deleteType", required = true, value = "删除类型 1:商品 2:订单")
    @NotBlank(message = "删除类型不能为空")
    private String deleteType;
    @ApiModelProperty(name = "shopId", hidden = true, value = "店铺id")
    private Integer shopId;
    @ApiModelProperty(name = "userId", hidden = true, value = "用户id")
    private Integer userId;
}
