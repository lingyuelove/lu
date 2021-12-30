package com.luxuryadmin.entity.pro;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 *商品规格表 bean
 *@author zhangsai
 *@Date 2021-09-16 17:48:52
 */
@Data
@ApiModel(description="商品规格表")
public class ProStandard {


	/**
	 * 主键Id,逻辑id,软件内部关联;不要把此id暴露在前端;
	 */
	@ApiModelProperty(value = "主键Id,逻辑id,软件内部关联;不要把此id暴露在前端;")
	private Integer id;
	/**
	 * 商品主键Id,逻辑id,软件内部关联;不要把此id暴露在前端;
	 */
	@ApiModelProperty(value = "商品主键Id,逻辑id,软件内部关联;不要把此id暴露在前端;")
	private Integer fkProProductId;
	/**
	 * shp_shop的id字段,主键id
	 */
	@ApiModelProperty(value = "shp_shop的id字段,主键id")
	private Integer fkShpShopId;
	/**
	 * 机芯类型
	 */
	@ApiModelProperty(value = "机芯类型")
	private String watchCoreType;
	/**
	 * 表壳材质
	 */
	@ApiModelProperty(value = "表壳材质")
	private String watchcase;
	/**
	 * 表盘直径
	 */
	@ApiModelProperty(value = "表盘直径")
	private String watchcaseSize;
	/**
	 * 材质
	 */
	@ApiModelProperty(value = "材质")
	private String material;
	/**
	 * 尺寸;一般有长宽高
	 */
	@ApiModelProperty(value = "尺寸;一般有长宽高")
	private String objectSize;
	/**
	 * 尺码;一般指衣服和鞋的大小
	 */
	@ApiModelProperty(value = "尺码;一般指衣服和鞋的大小")
	private String clothesSize;
	/**
	 * 国内公价(元)
	 */
	@ApiModelProperty(value = "国内公价(元)")
	private String publicPrice;
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
