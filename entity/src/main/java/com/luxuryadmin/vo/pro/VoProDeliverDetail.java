package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: taoqimin
 * Date: 2021-09-26
 */
@Data
@ApiModel(value="发货详情", description="发货详情")
public class VoProDeliverDetail {

    @ApiModelProperty(value = "主键Id")
    private Integer deliverId;

    @ApiModelProperty(value = "商品id")
    private String bizId;

    @ApiModelProperty(value = "锁单人员名称/开单人员名称")
    private String disposeUserName;

    @ApiModelProperty(value = "锁单时间/开单时间")
    private String disposeTime;

    @ApiModelProperty(value = "数量")
    private Integer num;

    @ApiModelProperty(value = "发货方式(目前五种)：ME_CLAIM:自取，FLASH_SEND:闪送，SF_EXPRESS:顺丰，OTHER_PEOPLE_TAKE:他人代取，OTHER:其他")
    private String deliverType;

    @ApiModelProperty(value = "发货人员")
    private String deliverNickname;

    @ApiModelProperty(value = "发货时间")
    private String deliverTime;

    @ApiModelProperty(value = "发货凭证图")
    private List<String> deliverImgs;
    @ApiModelProperty(value = "物流单号")
    private String logisticsNumber;
    @ApiModelProperty(value = "发货状态 1:物流异常 10:已发货 20:已揽件 30:运输中 40:派件中 41:派件异常 50:已签收 60:已退回/转寄")
    private Integer logisticsState;
    @ApiModelProperty(value = "发货状态中文名")
    private String logisticsStateCname;
    @ApiModelProperty(value = "手机后四位", name = "phone")
    private String phone;
    private String deliverImgStr;
}
