package com.luxuryadmin.entity.pro;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 *单个店铺补充信息不适用关联表 bean
 *@author zhangsai
 *@Date 2021-08-05 14:29:05
 */
@Data
@ApiModel(description="单个店铺补充信息不适用关联表")
public class ProClassifyTypeShop {


	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Integer id;
	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Integer fkShpShopId;
	/**
	 * 补充信息id
	 */
	@ApiModelProperty(value = "补充信息id")
	private Integer fkProClassifyTypeId;
	/**
	 * 类型;1:一级分类;2:二级分类;3:三级分类
	 */
	@ApiModelProperty(value = "类型;1:一级分类;2:二级分类;3:三级分类")
	private String type;
	/**
	 * 默认 0 不适用
	 */
	@ApiModelProperty(value = "默认 0 不适用")
	private Integer state;
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
