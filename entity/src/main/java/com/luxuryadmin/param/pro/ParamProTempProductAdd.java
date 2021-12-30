package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 临时仓id的参数模型
 *
 * @author by Administrator
 * @Classname ParamProduct
 * @Description TODO
 * @date 2021-01-18 18:47:19
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "临时仓id的参数模型")
@Data
public class ParamProTempProductAdd extends ParamProTempId {


    /**
     * 商品id
     */
    @ApiModelProperty(name = "proIds", required = true, value = "商品id,多个用英文逗号隔开;")
    @Pattern(regexp = "^[0-9,]{5,}$", message = "商品id--参数错误")
    private String proIds;

}
