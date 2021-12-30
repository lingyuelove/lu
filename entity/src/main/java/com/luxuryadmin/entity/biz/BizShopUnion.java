package com.luxuryadmin.entity.biz;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 *biz_shop_union bean
 *@author zhangsai
 *@Date 2021-07-16 17:58:54
 */
@Data
@ApiModel(description="biz_shop_union")
public class BizShopUnion {


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
	 * 添加状态 -10 已退出 10已加入
	 */
	@ApiModelProperty(value = "添加状态 -10 已退出 10已加入")
	private Integer state;


	@ApiModelProperty(value = "添加类型 10 买家 20 卖家")
	private String type;
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
	 * insert_admin
	 */
	@ApiModelProperty(value = "insert_admin")
	private Integer insertAdmin;
	/**
	 * update_admin
	 */
	@ApiModelProperty(value = "update_admin")
	private Integer updateAdmin;
	/**
	 * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
	 */
	@ApiModelProperty(value = "是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件")
	private Character del;
	/**
	 * remark
	 */
	@ApiModelProperty(value = "remark")
	private String remark;


}
