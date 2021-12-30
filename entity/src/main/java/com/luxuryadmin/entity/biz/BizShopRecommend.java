package com.luxuryadmin.entity.biz;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 *添加友商推荐 bean
 *@author zhangsai
 *@Date 2021-05-27 11:42:44
 */
@ApiModel(description="添加友商推荐")
@Data
public class BizShopRecommend {


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
	 * 店铺编号
	 */
	@ApiModelProperty(value = "店铺编号")
	private String fkShpShopNumber;
	/**
	 * 状态  0 不推荐 1 推荐
	 */
	@ApiModelProperty(value = "状态  0 不推荐 1 推荐")
	private String state;
	/**
	 * 推荐次数上限 -1不限制
	 */
	@ApiModelProperty(value = "推荐次数上限")
	private Integer recommendNum;
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
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private Integer insertAdmin;
	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人")
	private Integer updateAdmin;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;

}
