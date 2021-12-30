package com.luxuryadmin.param.shp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Classname ParamShpServiceRecordFinish
 * @Description 店铺服务记录完成更改参数
 * @Date 2020/9/18 20:33
 * @Created by sanjin145
 */
@Data
public class ParamShpServiceRecordFinish {

    /**
     * 服务记录ID
     */
    @ApiModelProperty(name = "serviceRecordId", required = true, value = "服务记录ID")
    @NotNull(message = "serviceRecordId不允许为空")
    private Integer serviceRecordId;

    /**
     * 服务成本
     */
    @ApiModelProperty(name = "costAmount", required = true, value = "服务成本")
    @NotNull(message = "服务成本不允许为空")
    private BigDecimal costAmount;

    /**
     * 实际收费
     */
    @ApiModelProperty(name = "realReceiveAmount", required = true, value = "实际收费")
    @NotNull(message = "实际收费不允许为空")
    private BigDecimal realReceiveAmount;

}
