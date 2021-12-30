package com.luxuryadmin.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 *商家联盟审核表 bean
 *@author zhangsai
 *@Date 2021-11-24 13:56:01
 */
@Data
@ApiModel(description="商家联盟审核表")
public class MpUnionVerify {


	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Integer id;
	/**
	 * 用户id
	 */
	@ApiModelProperty(value = "用户id")
	private Integer fkMpUserId;
	/**
	 * 类目 腕表 箱包 腕表和箱包
	 */
	@ApiModelProperty(value = "类目 腕表 箱包 腕表和箱包")
	private String fkProClassifyCode;
	/**
	 * 营业执照路径
	 */
	@ApiModelProperty(value = "营业执照路径")
	private String licenseImgUrl;
	/**
	 * 店铺认证图片
	 */
	@ApiModelProperty(value = "店铺认证图片")
	private String validImgUrl;
	/**
	 * 股东认证图片
	 */
	@ApiModelProperty(value = "股东认证图片")
	private String stockImgUrl;
	/**
	 * 店铺授权图片
	 */
	@ApiModelProperty(value = "店铺授权图片")
	private String empowerImgUrl;
	/**
	 * 其他人员认证身份证
	 */
	@ApiModelProperty(value = "其他人员认证身份证")
	private String otherUserImgUrl;
	/**
	 * 状态 0 未审核 1已通过 2未通过
	 */
	@ApiModelProperty(value = "状态 0 未审核 1已通过 2未通过")
	private String state;
	/**
	 * 店铺身份 法人 股东 都不是
	 */
	@ApiModelProperty(value = "店铺身份 法人 股东 都不是")
	private String shopUserType;
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
	 * 新增人
	 */
	@ApiModelProperty(value = "新增人")
	private Integer insertAdmin;
	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人")
	private Integer updateAdmin;
	/**
	 * 删除 0 未删除 1已删除
	 */
	@ApiModelProperty(value = "删除 0 未删除 1已删除")
	private String del;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;
	/**
	 * 审核失败原因
	 */
	@ApiModelProperty(value = "审核失败原因")
	private String failRemark;

}
