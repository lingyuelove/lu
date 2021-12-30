package com.luxuryadmin.entity.pro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品锁单记录表
 *
 * @author monkey king
 * @date   2020/05/29 19:16:14
 */
@Data
public class ProLockRecord {
    /**
     * 主键Id,逻辑id,软件内部关联;
     */
    private Integer id;

    /**
     * pro_product的主键Id,
     */
    private Integer fkProProductId;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * 商品逻辑id
     */
    private String proBizId;

    /**
     * 0:锁单中;1:已解锁
     */
    private Integer state;

    /**
     * ;锁单人员id;shp_user的id字段,主键id
     */
    private Integer lockUserId;

    /**
     * ;解锁人员id;shp_user的id字段,主键id
     */
    private Integer unlockUserId;

    /**
     * 收货地址id
     */
    private Integer fkOrdAddressId;
    /**
     * 预付定金
     */
    private BigDecimal preMoney;

    /**
     * 预付定金
     */
    private BigDecimal preFinishMoney;

    /**
     * 锁单数量
     */
    private Integer lockNum;

    /**
     * 插入时间
     */
    private Date insertTime;

    private Date updateTime;

    /**
     * 解锁时间
     */
    private Date unlockTime;

    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
     */
    private String del;

    /**
     * 备注
     */
    private String remark;
    @ApiModelProperty(name = "address", value = "收货地址 编辑锁单时传入 非系统参数",hidden = true)
    private String address;

    @ApiModelProperty(name = "sonRecordId", value = "锁单内部关联id 判断是否为寄卖传送关联锁单",hidden = true)
    private Integer sonRecordId;

    @ApiModelProperty(name = "conveyLockType", value = "寄卖锁单关联类型 lock锁单关联 order：订单关联 ",hidden = true)
    private String conveyLockType;
}