package com.luxuryadmin.vo.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 销售排行榜VO
 * @author: walkingPotato
 * @date: 2020-07-29 18:27
 * @email: dingxin_hz@163.com
 * @version: V1.0
 */
@Data
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoSaleRank {

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", name = "userId", required = false)
    private Integer userId;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称", name = "nickName", required = false)
    private String nickName;

    /**
     * 用户角色
     */
    @ApiModelProperty(value = "用户角色", name = "roleName", required = false)
    private String roleName;

    /**
     * 头像URL
     */
    @ApiModelProperty(value = "头像URL", name = "headImgUrl", required = false)
    private String headImgUrl;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量", name = "saleCount", required = false)
    private Integer saleCount;

    /**
     * 销售额
     */
    @ApiModelProperty(value = "销售额", name = "saleAmount", required = false)
    private Double saleAmount;

    /**
     * 毛利润
     */
    @ApiModelProperty(value = "毛利润", name = "grossProfit", required = false)
    private Double grossProfit;

    /**
     * 毛利润
     */
    @ApiModelProperty(value = "排名", name = "ranking", required = false)
    private Integer ranking;

}
