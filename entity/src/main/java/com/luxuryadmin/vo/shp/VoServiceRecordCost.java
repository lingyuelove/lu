package com.luxuryadmin.vo.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @PackgeName: com.luxuryadmin.vo.shp
 * @ClassName: VoServiceRecordCost
 * @Author: ZhangSai
 * Date: 2021/10/20 18:41
 */
@Data
@ApiModel(description="服务成本表")
public class VoServiceRecordCost {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private String id;

    /**
     * 店铺服务记录ID
     */
    @ApiModelProperty(value = "店铺服务记录ID")
    private String serviceRecordId;
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
    @ApiModelProperty(value = "服务成本")
    private Double serviceCostPrice;

}
