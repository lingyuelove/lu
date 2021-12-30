package com.luxuryadmin.entity.pro;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 *商品分类系列表 bean
 *@author zhangsai
 *@Date 2021-08-04 13:45:01
 */
@Data
@ApiModel(description="商品分类系列表")
public class ProSubSeries {
	/**
	 * 主键Id,逻辑id,软件内部关联
	 */
	@ApiModelProperty(value = "主键Id,逻辑id,软件内部关联")
	private Integer id;
	/**
	 * 分类名称;限长20个汉字
	 */
	@ApiModelProperty(value = "系列分类名称;限长20个汉字")
	private String name;
	/**
	 * 商品品牌分类pro_classify_sub表品牌名称
	 */
	@ApiModelProperty(value = "商品品牌分类pro_classify_sub表品牌名称")
	private String fkProClassifySubName;
	/**
	 * shp_shop的id字段,主键id
	 */
	@ApiModelProperty(value = "shp_shop的id字段,主键id")
	private Integer fkShpShopId;

	/**
	 * 类型;0:系统自带;1:用户自建
	 */
	@ApiModelProperty(value = "类型;0:系统自带;1:用户自建")
	private String type;
	/**
	 * 状态;-1:已删除;0:未使用;1:使用中
	 */
	@ApiModelProperty(value = "状态;-1:已删除;0:未使用;1:使用中")
	private Integer state;
	/**
	 * 序号排序
	 */
	@ApiModelProperty(value = "序号排序")
	private Integer sort;
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
