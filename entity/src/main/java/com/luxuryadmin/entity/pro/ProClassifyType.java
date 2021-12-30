package com.luxuryadmin.entity.pro;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 *商品补充信息分类表 bean
 *@author zhangsai
 *@Date 2021-08-03 10:45:15
 */
@Data
@ApiModel(description="商品补充信息分类表")
public class ProClassifyType {


	/**
	 * 主键Id,逻辑id,软件内部关联
	 */
	@ApiModelProperty(value = "主键Id,逻辑id,软件内部关联")
	private Integer id;
	/**
	 * 模板id+店铺编号+本店增加id个数(字符串拼接);eg:WB100001
	 */
	@ApiModelProperty(value = "英文首字母大写名称")
	private String code;
	/**
	 * 分类名称;限长50个汉字
	 */
	@ApiModelProperty(value = "分类名称;限长50个汉字")
	private String name;
	/**
	 * 对code进行详细的补充说明
	 */
	@ApiModelProperty(value = "对code进行详细的补充说明")
	private String description;
	/**
	 * 图标地址
	 */
	@ApiModelProperty(value = "图标地址")
	private String iconUrl;

	/**
	 * 商品系列表name
	 */
	@ApiModelProperty(value = "商品系列表英文拼写")
	private String fkProClassifyCode;
	/**
	 * shp_shop的id字段,主键id; -9为公用系统分类;
	 */
	@ApiModelProperty(value = "shp_shop的id字段,主键id; -9为公用系统分类;")
	private Integer fkShpShopId;
	/**
	 * 商品补充信息分类表主键id,供二级三级使用
	 */
	@ApiModelProperty(value = "商品补充信息分类表主键id,供二级三级使用")
	private Integer proClassifyTypeId;
	/**
	 * 类型;1:一级分类;2:二级分类;3:三级分类
	 */
	@ApiModelProperty(value = "类型;1:一级分类;2:二级分类;3:三级分类")
	private String type;
	/**
	 * 类型;1:下拉框;2:单行输入框;3:单选框;4:复选框;5:单选标签;6:复选标签;
	 */
	@ApiModelProperty(value = "类型;1:下拉框;2:单行输入框;3:单选框;4:复选框;5:单选标签;6:多行输入框;")
	private String choseType;
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
