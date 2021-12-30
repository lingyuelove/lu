package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品锁单--前端接收参数模型
 *
 * @author monkey king
 * @date 2019-12-23 13:56:43
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "商品锁单--前端接收参数模型")
@Data
public class ParamProductLock extends ParamProductUnlock {

    /**
     * 锁单记录id
     */
    @ApiModelProperty(name = "lockId", value = "锁单记录id")
    private String lockId;

    /**
     * 锁单原因
     */
    @ApiModelProperty(name = "lockReason", value = "锁单原因")
    private String lockReason;


    /**
     * 预付定金
     */
    @ApiModelProperty(name = "preMoney", value = "预付定金")
    private String preMoney;

    /**
     * 预计成交
     */
    @ApiModelProperty(name = "preFinishMoney", value = "预计成交")
    private String preFinishMoney;

    @ApiModelProperty(name = "address", value = "收货地址")
    private String address;

    @ApiModelProperty(name = "sonRecordId", value = "锁单内部关联id 判断是否为寄卖传送关联锁单 默认不传 寄卖传送关联新增时使用",hidden = true)
    private Integer sonRecordId;

    @ApiModelProperty(name = "conveyLockType", value = "寄卖锁单关联类型 lock锁单关联 order：订单关联 默认不传 寄卖传送关联新增时使用",hidden = true)
    private String conveyLockType;
}
