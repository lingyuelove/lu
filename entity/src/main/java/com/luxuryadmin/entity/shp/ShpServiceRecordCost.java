package com.luxuryadmin.entity.shp;

import java.util.Date;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 *服务成本表 bean
 *@author zhangsai
 *@Date 2021-10-19 14:48:46
 */
@Data
@ApiModel(description="服务成本表")
public class ShpServiceRecordCost {


	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Integer id;
	/**
	 * 店铺ID
	 */
	@ApiModelProperty(value = "店铺ID")
	private Integer fkShpShopId;
	/**
	 * 店铺服务记录ID
	 */
	@ApiModelProperty(value = "店铺服务记录ID")
	private Integer fkShpServiceRecordId;
	/**
	 * 维修内容
	 */
	@ApiModelProperty(value = "维修内容")
	private String repairContent;
	/**
	 * 服务成本
	 */
	@ApiModelProperty(value = "服务成本")
	private Double serviceCost;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date insertTime;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private Date updateTime;



}
