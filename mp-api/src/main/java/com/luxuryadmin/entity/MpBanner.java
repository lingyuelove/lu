package com.luxuryadmin.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 *banner表 bean
 *@author zhangsai
 *@Date 2021-11-24 13:56:01
 */
@Data
@ApiModel(description="banner表")
public class MpBanner {


	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Integer id;
	/**
	 * 图片路径
	 */
	@ApiModelProperty(value = "图片路径")
	private String url;
	/**
	 * 图片名称
	 */
	@ApiModelProperty(value = "图片名称")
	private String bannerName;
	/**
	 * 跳转类型
	 */
	@ApiModelProperty(value = "跳转类型")
	private String skipType;
	/**
	 * 跳转地址
	 */
	@ApiModelProperty(value = "跳转地址")
	private String skipAddress;

	@ApiModelProperty(value = "appId")
	private String appId;
	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序")
	private Integer sore;
	/**
	 * 是否展示：0不展示。1展示
	 */
	@ApiModelProperty(value = "是否展示：0不展示。1展示")
	private Integer state;
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
	 * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
	 */
	@ApiModelProperty(value = "是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件")
	private Character del;

}
