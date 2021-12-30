package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 发货列表
 *
 * @author taoqimin
 * @date   2021-09-25
 */
@Data
@ApiModel(value="发货列表", description="发货列表")
public class VoProDeliverByPage extends VoProductLoad{

    @ApiModelProperty(value = "订单id", name = "orderId")
    private String orderBizId;

    @ApiModelProperty(value = "发货详细地址", name = "toAddress")
    private String address;

    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;

    @ApiModelProperty(value = "状态（0：未发货，1：已发货）", name = "state")
    private Integer state;

    @ApiModelProperty(value = "发货人", name = "deliverNickname")
    private String deliverNickname;

    @ApiModelProperty(value = "发货方式", name = "deliveryWays")
    private List<String> deliveryWays;

    @ApiModelProperty(value = "发货来源", name = "deliverSource")
    private String deliverSource;

    @ApiModelProperty(value = "发货方式", name = "deliverType")
    private String deliverType;

}
