package com.luxuryadmin.entity.pro;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 *商品传送表 bean
 *@author zhangsai
 *@Date 2021-11-22 15:12:44
 */
@Data
@ApiModel(description="商品传送表")
public class ProConvey {


	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Integer id;
	/**
	 * 发送方店铺id字段,主键id
	 */
	@ApiModelProperty(value = "发送方店铺id字段,主键id")
	private Integer fkSendShopId;
	/**
	 * 接收方店铺id
	 */
	@ApiModelProperty(value = "接收方店铺id")
	private Integer fkReceiveShopId;
	/**
	 * 编码
	 */
	@ApiModelProperty(value = "编码")
	private String number;
	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称")
	private String name;
	/**
	 * 发送状态
	 */
	@ApiModelProperty(value = "发送状态 0待提取 1已提取 2已确认")
	private String sendState;
	/**
	 * 接收状态
	 */
	@ApiModelProperty(value = "接收状态 0待确认 1已确认")
	private String receiveState;
	/**
	 * 发送方删除
	 */
	@ApiModelProperty(value = "发送方删除")
	private String sendDel;
	/**
	 * 接收方删除
	 */
	@ApiModelProperty(value = "接收方删除")
	private String receiveDel;
	/**
	 * 提取人
	 */
	@ApiModelProperty(value = "提取人")
	private Integer receiveUserId;
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

	@ApiModelProperty(value = "提取时间")
	private Date receiveTime;
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
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;

}
