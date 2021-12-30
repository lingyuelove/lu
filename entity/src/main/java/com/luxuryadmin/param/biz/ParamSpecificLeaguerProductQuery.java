package com.luxuryadmin.param.biz;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 商品(具体某个友商)页面查询参数实体--前端接收参数模型
 *
 * @author monkey king
 * @date 2020-08-03 20:04:43
 */
@ApiModel(description = "商品(友商)页面查询参数实体")
public class ParamSpecificLeaguerProductQuery extends ParamLeaguerProductQuery {

    /**
     * 友商店铺id
     */
    @ApiModelProperty(name = "leaguerShopId", required = true, value = "友商店铺id")
    @Pattern(regexp = "^[0-9]{5,8}$", message = "[leaguerShopId]--参数错误")
    @NotBlank(message = "[leaguerShopId]--不允许为空")
    private String leaguerShopId;

    @ApiModelProperty(name = "type", required = false, value = " leaguer友商店铺 union商家联盟")
    private String type;
    @ApiModelProperty(name = "userId", hidden = true, value = " userId")
    private Integer userId;

    public String getLeaguerShopId() {
        return leaguerShopId;
    }

    public void setLeaguerShopId(String leaguerShopId) {
        this.leaguerShopId = leaguerShopId;
    }

    public String getType() {
        return type;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setType(String type) {
        this.type = type;
    }

}
