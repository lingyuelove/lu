package com.luxuryadmin.entity.shp;


import com.alibaba.excel.util.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 *商铺会员变动表 bean
 *@author zhangsai
 *@Date 2021-06-08 13:45:04
 */
@Data
@ApiModel(description="商铺会员变动表")
public class ShpAlterHistory {


	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Integer id;
	/**
	 * 商铺表shp_shop表主键id
	 */
	@ApiModelProperty(value = "商铺表shp_shop表主键id")
	private Integer fkShpShopId;
	/**
	 * 商铺添加会员时间类型；0：管理员添加；1：分享添加
	 */
	@ApiModelProperty(value = "商铺添加会员时间类型；0：管理员添加；1：分享添加")
	private String code;
	/**
	 * 会员添加时间（以小时为单位）
	 */
	@ApiModelProperty(value = "会员添加时间（以小时为单位）")
	private BigDecimal hours;
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
