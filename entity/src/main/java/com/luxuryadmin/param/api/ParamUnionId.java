package com.luxuryadmin.param.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author monkey king
 * @date 2021-04-28 23:29:19
 */
@ApiModel(description = "获取微信手机号并且存在机构白名单")
@Data
public class ParamUnionId {

    /**
     * 微信的unionid
     */
    @ApiModelProperty(value = "微信的unionid", name = "unionId", required = false)
    private String unionId;

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
