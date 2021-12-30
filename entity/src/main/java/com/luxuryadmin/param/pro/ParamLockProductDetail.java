package com.luxuryadmin.param.pro;

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
public class ParamLockProductDetail extends ParamToken {

    /**
     * 商品业务逻辑id
     */
    @ApiModelProperty(name = "lockId", required = true, value = "锁单记录表的id")
    @NotBlank(message = "lockId--参数错误")
    private String lockId;

}
