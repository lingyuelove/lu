package com.luxuryadmin.entity.shp;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 *商铺分享进度时长累计表 bean
 *@author Mong
 *@Date 2021-05-31 16:44:37
 */
@ApiModel(description="商铺分享进度时长累计表")
@Data
public class ShpShareTotal {


	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Integer id;
	/**
	 * 商铺表shp_shop主键id
	 */
	@ApiModelProperty(value = "商铺表shp_shop主键id")
	private Integer fkShpShopId;
	/**
	 * 商铺添加时长类型表shp_share_type类型code
	 */
	@ApiModelProperty(value = "商铺添加时长类型表shp_share_type类型code")
	private Integer fkShpShareTypeCode;
	/**
	 * 商铺用户表shp_user主键id
	 */
	@ApiModelProperty(value = "商铺用户表shp_user主键id")
	private Integer fkShpUserId;
	/**
	 * 操作记录次数
	 */
	@ApiModelProperty(value = "操作记录次数")
	private Integer totalCount;
	/**
	 * 当日操作累计时长
	 */
	@ApiModelProperty(value = "当日操作累计时长")
	private BigDecimal totalHours;
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
	 * 添加用户_管理员id
	 */
	@ApiModelProperty(value = "添加用户_管理员id")
	private Integer insertAdmin;
	/**
	 * 修改用户_管理员id
	 */
	@ApiModelProperty(value = "修改用户_管理员id")
	private Integer updateAdmin;
	/**
	 * 版本号;用于更新时对比操作;
	 */
	@ApiModelProperty(value = "版本号;用于更新时对比操作;")
	private Integer versions;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;



}
