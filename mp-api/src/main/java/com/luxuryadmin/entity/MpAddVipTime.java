package com.luxuryadmin.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 *追加时长表 bean
 *@author zhangsai
 *@Date 2021-11-24 13:56:01
 */
@Data
@ApiModel(description="追加时长表")
public class MpAddVipTime{


	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Integer id;
	/**
	 * 用户id
	 */
	@ApiModelProperty(value = "用户id")
	private Integer fkMpUserId;

	@ApiModelProperty(value = "被邀请用户di")
	private Integer fkBeMpUserId;
	/**
	 * 开始时间
	 */
	@ApiModelProperty(value = "开始时间")
	private Date startTime;
	/**
	 * 最终到期时间
	 */
	@ApiModelProperty(value = "最终到期时间")
	private Date finalEndTime;
	/**
	 * 增加天数
	 */
	@ApiModelProperty(value = "增加天数")
	private Integer addDay;
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
