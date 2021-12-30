package com.luxuryadmin.entity.biz;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 *店铺查看次数表 bean
 *@author zhangsai
 *@Date 2021-05-27 11:42:44
 */
@ApiModel(description="店铺查看次数表")
@Data
public class BizShopSee {


	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Integer id;
	/**
	 * shopId
	 */
	@ApiModelProperty(value = "shopId")
	private Integer fkShpShopId;
	/**
	 * 每日查看次数
	 */
	@ApiModelProperty(value = "每日查看次数")
	private Integer dayCount;
	/**
	 * 被查看的店铺id
	 */
	@ApiModelProperty(value = "被查看的店铺id")
	private Integer fkBeSeenShopId;
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



}
