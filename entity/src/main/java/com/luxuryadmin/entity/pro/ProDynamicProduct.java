package com.luxuryadmin.entity.pro;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 *动态列表商品信息 bean
 *@author zhangsai
 *@Date 2021-11-05 11:45:33
 */
@Data
@ApiModel(description="动态列表商品信息")
public class ProDynamicProduct {


	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Integer id;
	/**
	 * 列表id
	 */
	@ApiModelProperty(value = "列表id")
	private Integer fkProDynamicId;
	/**
	 * 商品id
	 */
	@ApiModelProperty(value = "商品id")
	private Integer fkProProductId;
	/**
	 * 状态：10：正常，20已售罄，30已锁单，40已售出，50已删除
	 */
	@ApiModelProperty(value = "状态：10：正常，20已售罄，30已锁单，40已删除")
	private Integer state;
	/**
	 * 店铺id
	 */
	@ApiModelProperty(value = "店铺id")
	private Integer fkShpShopId;
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

}
