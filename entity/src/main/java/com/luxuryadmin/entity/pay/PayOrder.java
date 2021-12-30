package com.luxuryadmin.entity.pay;

import lombok.Data;

import java.util.Date;

/**
 * 支付订单
 *
 * @author monkey king
 * @date 2021/03/21 18:40:02
 */
@Data
public class PayOrder {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 店铺ID
     */
    private Integer fkShpShopId;

    /**
     * 用户id
     */
    private Integer fkShpUserId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 第三方支付订单号
     */
    private String transactionId;

    /**
     * 平台订单号
     */
    private String orderNo;

    /**
     * 总金额(分)
     */
    private Long totalMoney;

    /**
     * 折扣金额(分)
     */
    private Long discountMoney;

    /**
     * 实际支付金额(分)
     */
    private Long realMoney;

    /**
     * 订单类型,vip:会员费,source:资源包
     */
    private String orderType;

    /**
     * -99:删除支付订单;10:待支付,11:主动取消支付,12:超时自动取消; 20:支付失败; 30:退款中,31:退款成功,32:退款失败; 33:已支付;40:支付成功
     */
    private Integer state;

    /**
     * 微信用户openId
     */
    private String openId;

    /**
     * 支付通道; weixin、alipay、other
     */
    private String payChannel;

    /**
     * 支付平台; android、ios、pc、admin
     */
    private String payPlatform;

    /**
     * 交易类型:公众号支付,扫码支付,app支付,h5支付,刷脸支付,付款码支付,线下支付
     */
    private String tradeType;

    /**
     * 创建类型; 0:用户自动创建,1:后台管理员手动创建,2:活动
     */
    private String createType;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 交易结束时间
     */
    private Date finishTime;

    /**
     * 创建人用户id
     */
    private Integer insertAdmin;

    /**
     * 修改人用户id
     */
    private Integer updateAdmin;

    /**
     * 备注
     */
    private String remark;
}