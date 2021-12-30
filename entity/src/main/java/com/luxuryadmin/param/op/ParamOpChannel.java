package com.luxuryadmin.param.op;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Administrator
 */
@Data
public class ParamOpChannel {
    /**
     * 渠道对外显示的outsideCode
     */
    @ApiModelProperty(name = "outsideCode", required = true, value = "渠道对外显示的outsideCode")
    @NotNull(message = "[outsideCode]参数错误")
    private String outsideCode;

    /**
     * 当前页
     */
    @ApiModelProperty(name = "pageNum", required = true, value = "当前页;默认为1")
    @Min(value = 1, message = "当前页不能小于1")
    @Max(value = 9999, message = "当前页超出范围")
    private String pageNum;
}
