package com.luxuryadmin.entity.ord;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 *订单打印模板表 bean
 *@author zhangsai
 *@Date 2021-09-26 16:00:53
 */
@Data
@ApiModel(description="订单打印模板表")
public class OrdPrintTpl{


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
	 * 导出文本
	 */
	@ApiModelProperty(value = "导出文本")
	private String context;
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
	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private Integer insertAdmin;
	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人")
	private Integer updateAdmin;

}
