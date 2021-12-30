package com.luxuryadmin.vo.pro;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author monkey king
 * @date 2020-06-02 15:32:36
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoProLockRecord {

    /**
     * 锁单记录id
     */
    private Integer lockId;

    /**
     * 商品id
     */
    private String proId;

    /**
     * 商品的业务逻辑id
     */
    private String bizId;


    /**
     * 锁单用户id
     */
    private Integer lockUserId;

    /**
     * 锁单人昵称
     */
    private String lockUser;

    /**
     * 收货地址id
     */
    private String addressId;

    /**
     * 锁单数量
     */
    private Integer lockNum;

    /**
     * 锁单时间
     */
    private Date insertTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 锁单原因
     */
    private String lockReason;

    /**
     * 预付定金
     */
    private String preMoney;

    private String address;
    /**
     * 预成交价
     */
    private String preFinishMoney;

    @ApiModelProperty(name = "sonRecordId", value = "锁单内部关联id 判断是否为寄卖传送关联锁单",hidden = true)
    private Integer sonRecordId;

    @ApiModelProperty(name = "conveyLockType", value = "寄卖锁单关联类型 lock锁单关联 order：订单关联 ",hidden = true)
    private String conveyLockType;
}
