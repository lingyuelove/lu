package com.luxuryadmin.entity.pro;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 *商品位置列表 bean
 *@author zhangsai
 *@Date 2021-11-05 11:37:01
 */
@Data
@ApiModel(description="商品位置列表")
public class ProDynamic {


	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Integer id;
	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称")
	private String name;
	/**
	 * 名称
	 */
	@ApiModelProperty(value = "图片")
	private String url;
	/**
	 * 是否初始化，1是，0否
	 */
	@ApiModelProperty(value = "是否初始化，0是，1否")
	private Integer isInitialize;
	/**
	 * 排序。只对初始化数据有效
	 */
	@ApiModelProperty(value = "排序。只对初始化数据有效")
	private Integer sort;
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
