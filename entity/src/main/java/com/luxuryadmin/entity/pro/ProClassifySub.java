package com.luxuryadmin.entity.pro;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 *商品二级分类表 bean
 *@author Mong
 *@Date 2021-05-27 11:37:31
 */
@ApiModel(description="商品二级分类表")
@Data
public class ProClassifySub {


	/**
	 * 主键Id,逻辑id,软件内部关联
	 */
	@ApiModelProperty(value = "主键Id,逻辑id,软件内部关联")
	private Integer id;
	/**
	 * 模板id+店铺编号+本店增加id个数(字符串拼接);eg:WB100001
	 */
	@ApiModelProperty(value = "模板id+店铺编号+本店增加id个数(字符串拼接);eg:WB100001")
	private String code;
	/**
	 * 分类名称;限长20个汉字
	 */
	@ApiModelProperty(value = "分类名称;限长20个汉字")
	private String name;
	/**
	 * 对code进行详细的补充说明
	 */
	@ApiModelProperty(value = "对code进行详细的补充说明")
	private String description;

	/**
	 * 图标地址
	 */
	@ApiModelProperty(value = "iconUrl")
	private String iconUrl;

	/**
	 * 商品一级分类pro_classify表主键id
	 */
	@ApiModelProperty(value = "商品一级分类pro_classify表主键id")
	private Integer fkProClassifyId;
	/**
	 * 商品分类code; 和分类列表对应; 默认0:无分类;
	 */
	@ApiModelProperty(value = " 商品分类id; 和分类列表对应; 默认0:无分类;")
	private String fkProClassifyCode;
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
	@ApiModelProperty(value = "状态;-1:已删除;0:未使用;1:使用中，，0:隐藏;1:显示")
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
	 * 版本号;用于更新时对比操作;
	 */
	@ApiModelProperty(value = "版本号;用于更新时对比操作;")
	private Integer versions;
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
