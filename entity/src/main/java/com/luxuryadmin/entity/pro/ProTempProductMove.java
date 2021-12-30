package com.luxuryadmin.entity.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 *临时仓商品移动历史表 bean
 *@author zhangsai
 *@Date 2021-09-24 17:46:58
 */
@Data
@ApiModel(description="临时仓商品移动历史表")
public class ProTempProductMove {


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
	 * 商品id
	 */
	@ApiModelProperty(value = "商品id")
	private Integer fkProProductId;
	/**
	 * 移出仓库id
	 */
	@ApiModelProperty(value = "移出仓库id")
	private Integer fkRemoveProTempId;
	/**
	 * 移入仓库id
	 */
	@ApiModelProperty(value = "移入仓库id")
	private Integer fkEnterProTempId;
	/**
	 * 转出数量
	 */
	@ApiModelProperty(value = "转出数量")
	private Integer removeNum;
	/**
	 * 超出数量 默认0
	 */
	@ApiModelProperty(value = "超出数量 默认0")
	private Integer surplusNum;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date insertTime;
	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private Integer insertAdmin;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;

}
