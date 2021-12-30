package com.luxuryadmin.entity.pro;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 *发货表 bean
 *@author taoqimin
 *@Date 2021-09-24 16:28:40
 */
@Data
@ApiModel(description="发货表")
public class ProDeliver {


	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Integer id;

	/**
	 * 商品id
	 */
	@ApiModelProperty(value = "商品id")
	private Integer fkProProductId;

	/**
	 * 锁单id
	 */
	@ApiModelProperty(value = "锁单id")
	private Integer fkProLockRecordId;
	/**
	 * 订单id
	 */
	@ApiModelProperty(value = "订单id")
	private Integer fkOrdOrderId;
	/**
	 * 店铺id
	 */
	@ApiModelProperty(value = "店铺id")
	private Integer fkShpShopId;

	/**
	 * 数量
	 */
	@ApiModelProperty(value = "数量")
	private Integer num;

	/**
	 * 发货人id
	 */
	@ApiModelProperty(value = "发货人id")
	private Integer fkShpUserId;
	/**
	 * 发货编号(用时间戳)
	 */
	@ApiModelProperty(value = "发货编号(用时间戳)")
	private String number;
	/**
	 * 0：未发货，1：已发货
	 */
	@ApiModelProperty(value = "0：未发货，1：已发货")
	private Integer state;
	/**
	 * 物流单号
	 */
	@ApiModelProperty(value = "物流单号")
	private String logisticsNumber;
	/**
	 * 物流公司
	 */
	@ApiModelProperty(value = "物流公司")
	private String logisticsCompany;
	/**
	 * 发货凭证图
	 */
	@ApiModelProperty(value = "发货凭证图")
	private String deliverImgs;
	/**
	 * 发货时间
	 */
	@ApiModelProperty(value = "发货时间")
	private Date deliverTime;
	/**
	 * 发货方式(目前五种)：ME_CLAIM:自取，FLASH_SEND:闪送，SF_EXPRESS:顺丰，OTHER_PEOPLE_TAKE:他人代取，OTHER:其他
	 */
	@ApiModelProperty(value = "发货方式(目前五种)：ME_CLAIM:自取，FLASH_SEND:闪送，SF_EXPRESS:顺丰，OTHER_PEOPLE_TAKE:他人代取，OTHER:其他")
	private String deliverType;
	/**
	 * 发货订单来源：ORDER(订单)、LOCK_RECORD（锁单）
	 */
	@ApiModelProperty(value = "发货订单来源：ORDER(订单)、LOCK_RECORD（锁单）")
	private String deliverSource;
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

	/**
	 * 收货地址id
	 */
	@ApiModelProperty(value = "收货地址id")
	private Integer fkOrdAddressId;

}
