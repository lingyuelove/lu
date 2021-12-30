package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 商品过期提醒新增类
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="商品过期提醒新增类", description="商品过期提醒新增类")
public class ParamExpiredNoticeAdd {
    /**
     * 产品属性表的code集合 自有商品:10;寄卖商品:20;其他商品:40
     */
    @ApiModelProperty(value = "产品属性表的code集合 自有商品:10;寄卖商品:20;其他商品:40", name = "attributeCodes", required = true)
    @NotBlank(message = "商品过期提醒参数不为空")
    private String attributeCodes;

    /**
     * 商品分类名称; 和分类列表对应; 默认0:无分类;
     */
    @ApiModelProperty(value = "商品分类名称", name = "classifyCodes", required = false)
//    @NotBlank(message = "商品过期提醒参数不为空")
    private String classifyCodes;

    /**
     * 设置过期天数
     */
    @ApiModelProperty(value = "设置过期天数", name = "expiredDay", required = true)
    @NotNull(message = "商品过期提醒参数不为空")
    private Integer expiredDay;
}
