package com.luxuryadmin.param.biz;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * 友商是否可以看见价格查询更新参数
 * @author sanjin145
 */
@Data
public class ParamCanSeeLeaguerPriceInfo {

    /**
     * 是否可以查看销售价
     */
    @ApiModelProperty(value="是否可以查看销售价 1|可以 0|不可以")
    private Boolean isCanSeeSalePrice;

    /**
     * 是否可以查看友商价
     */
    @ApiModelProperty(value="是否可以查看友商价 1|可以 0|不可以")
    private Boolean isCanSeeTradePrice;

    /**
     * 查看星标友商;0:查看全部友商商品 | 1:只查看星标友商商品
     */
    @ApiModelProperty(value="查看星标友商;0:查看全部友商商品 | 1:只查看星标友商商品")
    @Pattern(regexp = "^[01]?$", message = "onlyShowTopLeaguer--参数错误")
    private String onlyShowTopLeaguer ;

    @ApiModelProperty(value="优质友商推荐 0 不推荐 1 推荐")
    @Pattern(regexp = "^[01]?$", message = "recommend--参数错误")
    private String recommend ;

}
