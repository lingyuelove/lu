package com.luxuryadmin.entity.pro;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 *pro_convey_product bean
 *@author zhangsai
 *@Date 2021-11-22 15:05:52
 */
@Data
@ApiModel(description="pro_convey_product")
public class ProConveyProduct {


	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Integer id;
	/**
	 * shp_shop的id字段,主键id
	 */
	@ApiModelProperty(value = "shp_shop的id字段,主键id")
	private Integer fkShpShopId;
	/**
	 * 商品id
	 */
	@ApiModelProperty(value = "商品id")
	private Integer fkProProductId;

	@ApiModelProperty(value = "接收商品id")
	private Integer receiveProductId;
	/**
	 * 商品传送表的id字段,主键id
	 */
	@ApiModelProperty(value = "商品传送表的id字段,主键id")
	private Integer fkProConveyId;
	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	private String name;
	/**
	 * 商品描述
	 */
	@ApiModelProperty(value = "商品描述")
	private String description;
	/**
	 * 商品数量
	 */
	@ApiModelProperty(value = "商品数量")
	private Integer num;

	@ApiModelProperty(value = "商品数量 确定转移时进行保存")
	private Integer oldNum;
	/**
	 * 结算价
	 */
	@ApiModelProperty(value = "结算价")
	private BigDecimal finishPrice;
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
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;


}
