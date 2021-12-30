package com.luxuryadmin.param.ord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 取消开单--前端接收参数模型
 *
 * @author sanjin
 * @Date 2020-08-21
 */
@ApiModel(description = "取消开单--前端接收参数模型")
@Data
public class ParamOrderCancel {

    /**
     * token;登录标识符
     */
    @ApiModelProperty(name = "token", required = true, value = "token;登录标识符")
    private String token;


    /**
     * 商品业务逻辑id
     */
    @ApiModelProperty(value = "商品业务逻辑id;", name = "bizId", required = true)
    private String orderBizId;

    /**
     * 取消原因
     */
    @ApiModelProperty(value = "取消原因;", name = "cancelReason", required = false)
    private String cancelReason;

}
