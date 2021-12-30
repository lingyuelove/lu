package com.luxuryadmin.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 *邀请记录 bean
 *@author zhangsai
 *@Date 2021-11-24 13:56:01
 */
@ApiModel(description="邀请记录")
@Data
public class MpUserInvite {


	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Integer id;
	/**
	 * 邀请人id
	 */
	@ApiModelProperty(value = "邀请人id")
	private Integer fkInviteMpUserId;
	/**
	 * 邀请用户id
	 */
	@ApiModelProperty(value = "邀请用户id")
	private Integer fkBeInviteMpUserId;
	/**
	 * 邀请状态：0:禁用；1:正常; 
	 */
	@ApiModelProperty(value = "邀请状态：0:禁用；1:正常; ")
	private String state;
	/**
	 * 奖励状态：10:无奖励；20:已发放全部奖励
	 */
	@ApiModelProperty(value = "奖励状态：10:无奖励；20:已发放全部奖励")
	private String rewardState;
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
