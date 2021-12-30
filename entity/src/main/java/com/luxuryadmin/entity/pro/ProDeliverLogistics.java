package com.luxuryadmin.entity.pro;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 *发货物流表 bean
 *@author zhangsai
 *@Date 2021-10-14 17:20:49
 */
@Data
@ApiModel(description="发货物流表")
public class ProDeliverLogistics{


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
	 * 发货id
	 */
	@ApiModelProperty(value = "发货状态 1:物流异常 2:暂无物流信息 10:已发货 20:已揽件 30:运输中 40:派件中 41:派件异常 50:已签收 60:已退回/转寄")
	private Integer logisticsState;
	/**
	 * 物流单号
	 */
	@ApiModelProperty(value = "物流单号")
	private String logisticsNumber;
	@ApiModelProperty(value = "手机号后四位  物流查询使用")
	private String phone;
	/**
	 * 物流信息
	 */
	@ApiModelProperty(value = "物流信息")
	private String context;
	@ApiModelProperty(value = "插入时间")
	private Date insertTime;

}
