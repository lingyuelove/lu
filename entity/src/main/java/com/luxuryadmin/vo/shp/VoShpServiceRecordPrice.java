package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @PackgeName: com.luxuryadmin.vo.shp
 * @ClassName: VoShpServiceRecordPrice
 * @Author: ZhangSai
 * Date: 2021/6/21 16:13
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="获取维修保养的成本和成交价还有数量", description="获取维修保养的成本和成交价还有数量")
public class VoShpServiceRecordPrice {
    @ApiModelProperty(value = "维修数量", name = "serviceNum")
    private Integer serviceNum;

    @ApiModelProperty(value = "维修成本", name = "serviceInitPrice")
    private BigDecimal serviceInitPrice;

    @ApiModelProperty(value = "成交价格", name = "serviceFinishPrice")
    private BigDecimal serviceFinishPrice;


    @ApiModelProperty(value = "集合总计维修成本", name = "totalCost")
    private BigDecimal totalCost;
}
