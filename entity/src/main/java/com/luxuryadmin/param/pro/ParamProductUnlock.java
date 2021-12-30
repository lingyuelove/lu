package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamBasic;
import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 商品解锁--前端接收参数模型
 *
 * @author monkey king
 * @date 2019-12-23 13:56:43
 */
@ApiModel(description = "商品解锁--前端接收参数模型")
@Data
public class ParamProductUnlock extends ParamBasic {


    /**
     * 锁单数量
     */
    @ApiModelProperty(name = "lockNum", required = true, value = "锁单数量;不填默认为1")
    @Min(value = 1, message = "锁单数量不能小于1")
    @Max(value = 999, message = "锁单数量最大为999")
    @NotBlank(message = "锁单数量不允许为空")
    private String lockNum;


    /**
     * 锁单记录id
     */
    @ApiModelProperty(name = "lockId", required = false, value = "锁单记录id")
    @Pattern(regexp = "^\\d{5,9}$", message = "lockId--参数错误")
    private String lockId;


    /**
     * 登录标识符
     */
    @ApiModelProperty(name = "token", required = true, value = "登录标识符")
    @NotBlank(message = "token--参数错误")
    private String token;

    /**
     * 商品业务逻辑id
     */
    @ApiModelProperty(name = "bizId", required = true, value = "商品业务逻辑id;")
    @NotBlank(message = "bizId--参数错误")
    private String bizId;

    @ApiModelProperty(name = "address", value = "收货地址")
    private String address;

    /**
     * 当前登录店铺id
     */
    @ApiModelProperty(name = "bizId", hidden = true)
    private int shopId;

    /**
     * 当前登录用户id
     */
    @ApiModelProperty(name = "bizId", hidden = true)
    private int userId;
}
