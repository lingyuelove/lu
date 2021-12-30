package com.luxuryadmin.param.ord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 删除订单--前端接收参数模型
 *
 * @author sanjin
 * @Date 2020-08-21
 */
@ApiModel(description = "删除订单--前端接收参数模型")
@Data
public class ParamOrderDelete {

    /**
     * token;登录标识符
     */
    @ApiModelProperty(name = "token", required = true, value = "token;登录标识符")
    private String token;


    /**
     * 商品业务逻辑id
     */
    @ApiModelProperty(value = "订单编号;多个用英文逗号(,)隔开", name = "bizId", required = true)
    @NotBlank(message = "订单编号不允许为空")
    private String orderBizId;

    /**
     * 删除备注
     */
    @ApiModelProperty(value = "删除备注", name = "deleteRemark", required = false)
    private String deleteRemark;

    @ApiModelProperty(value = "店铺id", name = "shopId",hidden = true)
    private Integer shopId;
    @ApiModelProperty(value = "用户id", name = "userId",hidden = true)
    private Integer userId;
}
