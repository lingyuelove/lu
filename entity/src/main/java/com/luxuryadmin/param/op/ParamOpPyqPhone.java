package com.luxuryadmin.param.op;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrator
 */
@Data
public class ParamOpPyqPhone {
    /**
     * app版本号
     */
    @ApiModelProperty(name = "token", required = true, value = "APP版本号")
    private String token;

    /**
     * 网络链接
     */
    @ApiModelProperty(name = "excelHttpUrl", required = false, value = "网络链接")
    private String excelHttpUrl;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", required = false, value = "备注")
    private String remark;

    /**
     * 批次
     */
    @ApiModelProperty(name = "batch", required = false, value = "批次")
    private String batch;
}
