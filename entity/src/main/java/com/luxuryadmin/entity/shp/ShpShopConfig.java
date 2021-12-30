package com.luxuryadmin.entity.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 *店铺配置表 bean
 *@author zhangsai
 *@Date 2021-07-01 11:16:02
 */
@Data
@ApiModel(description="店铺配置表")
public class ShpShopConfig{

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
	 * 店铺编号
	 */
	@ApiModelProperty(value = "店铺编号")
	private String shopNumber;
	/**
	 * 是否开启小程序访客功能 0未开启 1已开启
	 */
	@ApiModelProperty(value = "是否开启小程序访客功能 0未开启 1已开启")
	private String openShareUser;
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
