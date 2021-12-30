package com.luxuryadmin.param.biz;

import com.luxuryadmin.vo.shp.VoShpWechat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 友商商品是否可见--前端接收参数模型
 *
 * @author monkey king
 * @date 2020-07-29 20:06:21
 */
@ApiModel(description = "友商商品是否可见--前端接收参数模型")
@Data
public class ParamUpdateLeaguerVisible extends ParamUpdateLeaguer {

    /**
     * 是否允许友商查看店铺商品; 0:不允许; 1:允许
     */
    @ApiModelProperty(value = "是否允许友商查看店铺商品; 0:不允许; 1:允许", required = true)
    @Pattern(regexp = "^[01]$", message = "[visible]参数非法!")
    @NotBlank(message = "[visible]不允许为空!")
    private String visible;


    /**
     * 是否置顶; 0:不置顶; 1:置顶
     */
    @ApiModelProperty(value = "是否置顶; 0:不置顶; 1:置顶", required = true)
    @Pattern(regexp = "^[01]$", message = "[top]参数非法!")
    @NotBlank(message = "[top]不允许为空!")
    private String top;

    /**
     * 是否可以查看友商价
     */
    @ApiModelProperty(value="是否可以查看销售价 1|可以 0|不可以")
    @NotNull(message = "[isCanSeeSalePrice]不允许为空!")
    private Integer isCanSeeSalePrice;

    /**
     * 是否可以查看友商价
     */
    @ApiModelProperty(value="是否可以查看友商价 1|可以 0|不可以")
    @NotNull(message = "[isCanSeeTradePrice]不允许为空!")
    private Integer isCanSeeTradePrice;

    /**
     * 是否可以查看友商价
     */
    @ApiModelProperty(value="是否看友商店铺商品 1|可以 0|不可以")
    @NotNull(message = "[notSeeGoods]不允许为空!")
    private Integer notSeeGoods;
}
