package com.luxuryadmin.param.biz;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 显示分享商品--前端接收参数模型
 *
 * @author monkey king
 * @Date 2019-12-26 02:15:20
 */
@ApiModel(description = "显示分享商品--前端接收参数模型")
public class ParamDisplayShareProduct {

    /**
     * 商品业务逻辑id
     */
    @ApiModelProperty(value = "商品业务逻辑id;", name = "bizIds", required = true)
    private String bizIds;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id", name = "userNumber", required = true)
    @Pattern(regexp = "^\\d{1,9}$", message = "shopId格式错误")
    @NotBlank(message = "shopId不允许为空")
    private String shopId;

    /**
     * 代理编号
     */
    @ApiModelProperty(value = "代理编号", name = "agencyNo", required = true)
    @Pattern(regexp = "^\\d{1,9}$", message = "agencyNo格式错误")
    @NotBlank(message = "agencyNo不允许为空")
    private String agencyNo;

    public String getBizIds() {
        return bizIds;
    }

    public void setBizIds(String bizIds) {
        this.bizIds = bizIds;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getAgencyNo() {
        return agencyNo;
    }

    public void setAgencyNo(String agencyNo) {
        this.agencyNo = agencyNo;
    }
}
