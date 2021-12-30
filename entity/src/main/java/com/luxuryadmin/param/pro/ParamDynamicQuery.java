package com.luxuryadmin.param.pro;


import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author taoqimin
 * @date 2021-11-05
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "商品位置列表")
@Data
public class ParamDynamicQuery extends ParamToken {


    /**
     * 排序字段
     */
    @ApiModelProperty(name = "sortKey", required = true, value = "排序字段 updateTime/name")
    @NotBlank(message = "排序字段不能为空")
    private String sortKey;

    /**
     * 排序顺序;desc(降序) | asc(升序)
     */
    @ApiModelProperty(name = "sortValue", required = true, value = "排序顺序 asc/desc")
    @Pattern(regexp = "^(desc)|(asc)$", message = "排序顺序--参数错误")
    @NotBlank(message = "排序顺序不能为空")
    private String sortValue;

    @ApiModelProperty(hidden = true)
    private Integer shopId;

    @ApiModelProperty(name = "dynamicId", required = false, value = "列表id")
    private String dynamicId;


}
