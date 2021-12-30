package com.luxuryadmin.param.ord;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * 临时仓订单搜索
 *
 * @author zhangSai
 * @date   2021/04/21 13:56:16
 */
@Data
public class ParamOrdForTempSearch {

    /**
     * token;登录标识符
     */
    @ApiModelProperty(name = "token", required = true, value = "token;登录标识符")
    private String token;

    /**
     * 当前页
     */
    @ApiModelProperty(name = "pageNum", required = true, value = "当前页;默认为1")
    @Min(value = 1, message = "当前页不能小于1")
    @Max(value = 999, message = "当前页最大为999")
    private String pageNum;

    @ApiModelProperty(name = "searchName", required = false, value = "搜索商品名称；系统编号；独立编码")
    private String searchName;
    @ApiModelProperty(name = "shopId", required = false, value = "店铺id后端自取")
    private Integer shopId;

    @ApiModelProperty(name = "tempId", required = true, value = "临时仓id")
    @Pattern(regexp = "^[0-9]{5,9}$", message = "[tempId]参数非法!")
    @NotBlank(message = "[tempId]不允许为空!")
    private String tempId;
}
