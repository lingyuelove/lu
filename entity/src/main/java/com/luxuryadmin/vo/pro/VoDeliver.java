package com.luxuryadmin.vo.pro;

import com.luxuryadmin.common.utils.sf.LogisticsRoute;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoDeliver
 * @Author: ZhangSai
 * Date: 2021/10/15 15:23
 */
@Data
@ApiModel(description="发货物流表")
public class VoDeliver {
    @ApiModelProperty(value = "0：未发货，1：已发货")
    private Integer state;
    /**
     * 发货id
     */
    @ApiModelProperty(value = "发货状态 1:物流异常 2 暂无物流信息 10:已发货 20:已揽件 30:运输中 40:派件中 41:派件异常 50:已签收 60:已退回/转寄")
    private Integer logisticsState;
    @ApiModelProperty(value = "发货状态中文名")
    private String logisticsStateCname;
    /**
     * 物流单号
     */
    @ApiModelProperty(value = "物流单号")
    private String logisticsNumber;

    @ApiModelProperty(value = "物流信息")
    private String context;
    @ApiModelProperty(value = "快递名称")
    private String expressName;

    @ApiModelProperty(value = "商品名称")
    private String productName;
    @ApiModelProperty(value = "物流信息")
    private  List<LogisticsRoute> routes;
}
