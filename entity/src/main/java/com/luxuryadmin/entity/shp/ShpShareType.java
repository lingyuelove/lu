package com.luxuryadmin.entity.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 *商铺添加时长类型表 bean
 *@author Mong
 *@Date 2021-05-31 17:27:11
 */
@ApiModel(description="商铺添加时长类型表")
@Data
public class ShpShareType {


	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Integer id;
	/**
	 * 时长规则类型 1 分享添加时长
	 */
	@ApiModelProperty(value = "时长规则类型")
	private String code;
	/**
	 * 时长
	 */
	@ApiModelProperty(value = "时长")
	private BigDecimal hours;

	@ApiModelProperty(value = "可添加的次数")
	private Integer addNum;
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
