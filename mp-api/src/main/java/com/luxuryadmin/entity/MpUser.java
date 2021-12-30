package com.luxuryadmin.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 *小程序用户表 bean
 *@author zhangsai
 *@Date 2021-11-24 13:56:01
 */
@Data
@ApiModel(description="小程序用户表")
public class MpUser {


	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Integer id;
	/**
	 * 微信名
	 */
	@ApiModelProperty(value = "微信名")
	private String wxNickname;
	/**
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名")
	private String username;
	/**
	 * 昵称
	 */
	@ApiModelProperty(value = "昵称")
	private String nickname;
	/**
	 * 状态  0：禁用   1：正常
	 */
	@ApiModelProperty(value = "状态  0：禁用   1：正常")
	private String state;
	/**
	 * 头像
	 */
	@ApiModelProperty(value = "头像")
	private String headImgUrl;
	/**
	 * 主体类型
	 */
	@ApiModelProperty(value = "主体类型")
	private String masterType;

	@ApiModelProperty(value = "会员类型:sdj,mp")
	private String vipType;

	/**
	 * 是否是会员 yes|是会员 no|不是会员
	 */
	@ApiModelProperty(value = "是否是会员 yes|是会员 no|不是会员")
	private String isMember;
	/**
	 * open_id
	 */
	@ApiModelProperty(value = "open_id")
	private String openId;
	/**
	 * 0:非会员; 1:体验会员;2:正式会员;3:靓号会员
	 */
	@ApiModelProperty(value = "0:非会员; 1:体验会员;2:正式会员;3:靓号会员")
	private Integer memberState;
	/**
	 * 试用开始时间
	 */
	@ApiModelProperty(value = "试用开始时间")
	private Date tryStartTime;
	/**
	 * 试用结束时间
	 */
	@ApiModelProperty(value = "试用结束时间")
	private Date tryEndTime;
	/**
	 * 付费使用开始时间
	 */
	@ApiModelProperty(value = "付费使用开始时间")
	private Date payStartTime;
	/**
	 * 付费使用结束时间
	 */
	@ApiModelProperty(value = "付费使用结束时间")
	private Date payEndTime;
	/**
	 * 登录ip
	 */
	@ApiModelProperty(value = "登录ip")
	private String loginIp;
	/**
	 * 省
	 */
	@ApiModelProperty(value = "省")
	private String province;
	/**
	 * 市
	 */
	@ApiModelProperty(value = "市")
	private String city;
	/**
	 * 区
	 */
	@ApiModelProperty(value = "区")
	private String district;
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
	private String del;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;

}
