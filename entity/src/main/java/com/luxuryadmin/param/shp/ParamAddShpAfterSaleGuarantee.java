package com.luxuryadmin.param.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description: 添加商品售后保障标签
 * @author: walkingPotato
 * @date: 2020-08-20 15:07
 * @email: dingxin_hz@163.com
 * @version: V1.0
 */
@Data
@ApiModel(description = "新增店铺售后保障标签")
public class ParamAddShpAfterSaleGuarantee {

    /**
     * 售后保障名称
     */
    @ApiModelProperty(name = "shopName", required = true,
            value = "售后保障名称")
    @NotBlank(message = "售后保障名称不允许为空")
    private String guaranteeName;


}
