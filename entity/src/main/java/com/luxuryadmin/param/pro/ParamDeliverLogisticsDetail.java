package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamDeliverLogisticsDetail
 * @Author: ZhangSai
 * Date: 2021/10/20 19:20
 */
@Data
@ApiModel(value="物流详情查询参数", description="物流详情查询参数")
public class ParamDeliverLogisticsDetail extends ParamToken {
    @ApiModelProperty(value = "物流单号", name = "logisticsNumber", required = true)
    private String logisticsNumber;
    @ApiModelProperty(value = "手机后四位", name = "phone", required = true)
    private String phone;
}
