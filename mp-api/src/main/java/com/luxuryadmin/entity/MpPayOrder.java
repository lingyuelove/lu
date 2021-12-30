package com.luxuryadmin.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 *支付订单 bean
 *@author zhangsai
 *@Date 2021-11-24 13:56:01
 */
@Data
@ApiModel(description="支付订单")
public class MpPayOrder {


	/**
	 * 主键ID
	 */
	@ApiModelProperty(value = "主键ID")
	private Integer id;
	/**
	 * 用户id
	 */
	@ApiModelProperty(value = "用户id")
	private Integer fkMpUserId;
	/**
	 * 第三方支付订单号
	 */
	@ApiModelProperty(value = "第三方支付订单号")
	private String transactionId;
	/**
	 * 平台订单号
	 */
	@ApiModelProperty(value = "平台订单号")
	private String orderNo;
	/**
	 * 总金额(分)
	 */
	@ApiModelProperty(value = "总金额(分)")
	private BigDecimal totalMoney;
	/**
	 * 折扣金额(分)
	 */
	@ApiModelProperty(value = "折扣金额(分)")
	private BigDecimal discountMoney;
	/**
	 * 实际支付金额(分)
	 */
	@ApiModelProperty(value = "实际支付金额(分)")
	private BigDecimal realMoney;
	/**
	 * 会员类型：mpvip：小程序会员。sdjvip
	 */
	@ApiModelProperty(value = "会员类型：mpvip：小程序会员。sdjvip")
	private String vipType;
	/**
	 * 订单类型,vip:会员费,source:资源包
	 */
	@ApiModelProperty(value = "订单类型,vip:会员费,source:资源包")
	private String orderType;
	/**
	 * -99:删除支付订单;10:待支付,11:主动取消支付,12:超时自动取消; 20:支付失败; 30:退款中,31:退款成功,32:退款失败; 33:已支付;40:支付成功
	 */
	@ApiModelProperty(value = "-99:删除支付订单;10:待支付,11:主动取消支付,12:超时自动取消; 20:支付失败; 30:退款中,31:退款成功,32:退款失败; 33:已支付;40:支付成功")
	private Integer state;
	/**
	 * 支付通道; weixin、alipay、other
	 */
	@ApiModelProperty(value = "支付通道; weixin、alipay、other")
	private String payChannel;
	/**
	 * 交易类型:公众号支付,扫码支付,app支付,h5支付,刷脸支付,付款码支付
	 */
	@ApiModelProperty(value = "交易类型:公众号支付,扫码支付,app支付,h5支付,刷脸支付,付款码支付")
	private String tradeType;
	/**
	 * 创建类型; 0:用户自动创建,1:后台管理员手动创建
	 */
	@ApiModelProperty(value = "创建类型; 0:用户自动创建,1:后台管理员手动创建")
	private String createType;
	/**
	 * 插入时间
	 */
	@ApiModelProperty(value = "插入时间")
	private Date insertTime;
	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	private Date updateTime;
	/**
	 * 支付时间
	 */
	@ApiModelProperty(value = "支付时间")
	private Date payTime;
	/**
	 * 交易结束时间
	 */
	@ApiModelProperty(value = "交易结束时间")
	private Date finishTime;
	/**
	 * 创建人用户id
	 */
	@ApiModelProperty(value = "创建人用户id")
	private Integer insertAdmin;
	/**
	 * 修改人用户id
	 */
	@ApiModelProperty(value = "修改人用户id")
	private Integer updateAdmin;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;

}
