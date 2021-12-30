package com.luxuryadmin.entity.pro;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 *小程序访客记录表 bean
 *@author zhangsai
 *@Date 2021-07-06 14:11:33
 */
@ApiModel(description="小程序访客记录表")
@Data
public class ProShareSeeUser {


	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Integer id;
	/**
	 * 店铺id
	 */
	@ApiModelProperty(value = "店铺id")
	private Integer fkShpShopId;
	/**
	 * 分享id
	 */
	@ApiModelProperty(value = "分享id")
	private Integer fkProShareId;
	/**
	 * 分享userId
	 */
	@ApiModelProperty(value = "分享userId")
	private Integer fkShpUserId;
	/**
	 * 分享批次
	 */
	@ApiModelProperty(value = "分享批次")
	private String fkProShareBatch;
	/**
	 * 0:【LuxurySir】小程序访客;1:【奢当家】小程序访客
	 */
	@ApiModelProperty(value = "分享类型")
	private String type;
	/**
	 * 昵称
	 */
	@ApiModelProperty(value = "昵称")
	private String nickName;

	/**
	 * 微信手机号
	 */
	private String phone;

	/**
	 * 头像
	 */
	@ApiModelProperty(value = "头像")
	private String avatarUrl;
	/**
	 * 性别  0：未知、1：男、2：女
	 */
	@ApiModelProperty(value = "性别  0：未知、1：男、2：女")
	private String gender;
	/**
	 * 微信的openId
	 */
	@ApiModelProperty(value = "微信的openId")
	private String openId;
	/**
	 * 微信的unionId
	 */
	@ApiModelProperty(value = "微信的unionId")
	private String unionId;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date insertTime;

	/**
	 * 访问ip
	 */
	@ApiModelProperty(value = "访问ip")
	private String ip;
}
