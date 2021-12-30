package com.luxuryadmin.common.utils.sf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamLogistics
 * @Author: ZhangSai
 * Date: 2021/10/14 17:33
 */
@Data
@ApiModel(value = "顺丰地址的接入", description = "顺丰地址的接入")
public class LogisticsRouteResp {
    @ApiModelProperty(name = "物流单号", value = "mailNo")
    private String mailNo;
    @ApiModelProperty(name = "物流信息", value = "routes")
    private List<LogisticsRoute> routes;
}
