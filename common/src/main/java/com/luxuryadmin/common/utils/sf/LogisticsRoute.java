package com.luxuryadmin.common.utils.sf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamLogisticsRouteResp1
 * @Author: ZhangSai
 * Date: 2021/10/14 17:34
 */
@Data
@ApiModel(value = "顺丰地址详细内容", description = "顺丰地址详细内容")
public class LogisticsRoute {
    @ApiModelProperty(name = "地址", value = "acceptAddress")
    private String acceptAddress;
    @ApiModelProperty(name = "时间", value = "acceptTime")
    private String acceptTime;
    @ApiModelProperty(name = "内容", value = "remark")
    private String remark;
    @ApiModelProperty(name = "状态", value = "opCode")
    private String opCode;
    @ApiModelProperty(name = "状态名称", value = "opCodeCname")
    private String opCodeCname;
    @ApiModelProperty(name = "状态", value = "opCode")
    private String sfCode;
}
