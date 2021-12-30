package com.luxuryadmin.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 *访客记录 bean
 *@author zhangsai
 *@Date 2021-11-24 13:56:01
 */
@Data
@ApiModel(description="访客记录")
public class MpVisitorRecord {


	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Integer id;
	/**
	 * 被访问人id
	 */
	@ApiModelProperty(value = "被访问人id")
	private Integer fkBeMpUserId;
	/**
	 * 访客用户id
	 */
	@ApiModelProperty(value = "访客用户id")
	private Integer fkMpUserId;
	/**
	 * 主体类型。什么小程序
	 */
	@ApiModelProperty(value = "主体类型。什么小程序")
	private String masterType;
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
