package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 过期商品列表
 *
 * @author ZhangSai
 * @date   2021/05/11 10:45:48
 */
@Data
@ApiModel(value="过期商品Page", description="过期商品Page")
public class VoExpiredProductByPage {

    @ApiModelProperty(value = "过期商品列表", name = "expiredProductByLists")
    private List<VoExpiredProductByList> expiredProductLists;

    @ApiModelProperty(value = "pageNum", name = "pageNum")
    private Integer pageNum;

    @ApiModelProperty(value = "pageSize", name = "pageSize")
    private Integer pageSize;

    @ApiModelProperty(value = "hasNextPage", name = "hasNextPage")
    private Boolean  hasNextPage;

    @ApiModelProperty(value = "已提醒商品总价值", name = "productTotalPrice")
    private String productTotalPrice;

    @ApiModelProperty(value = "已提醒商品总数", name = "productTotalNum")
    private Integer productTotalNum;

    @ApiModelProperty(value = "已提醒商品总价值名称", name = "productTotalPriceName")
    private String productTotalPriceName;

    @ApiModelProperty(value = "已提醒商品总数名称", name = "productTotalNumName")
    private String productTotalNumName;

    @ApiModelProperty(value = "商品过期提醒权限", name = "uPermStoreTotalPrice")
    private String  uPermStoreTotalPrice;

}
