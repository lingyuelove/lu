package com.luxuryadmin.param.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @PackgeName: com.luxuryadmin.param.shp
 * @ClassName: ParamSerRecordCostAdd
 * @Author: ZhangSai
 * Date: 2021/10/19 14:58
 */
@Data
@ApiModel(description = "服务成本新增或编辑")
public class ParamServiceRecordCostAddOrUp{
    /**
     * 维修内容
     */
    @ApiModelProperty(value = "维修内容")
    private String repairContent;
    /**
     * 服务成本
     */
    @ApiModelProperty(value = "服务成本")
    private String serviceCost;
}
