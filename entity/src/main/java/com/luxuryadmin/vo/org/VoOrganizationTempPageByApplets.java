package com.luxuryadmin.vo.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 机构临时仓小程序显示
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="机构临时仓小程序显示", description="机构临时仓小程序显示")
public class VoOrganizationTempPageByApplets {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id", name = "id")
    private Integer id;

    /**
     * 机构id
     */
    @ApiModelProperty(value = "机构id", name = "organizationId")
    private Integer organizationId;

    /**
     * 临时仓id
     */
    @ApiModelProperty(value = "临时仓id", name = "tempId")
    private Integer tempId;

    /**
     * 店铺机构仓排序位置分组名称
     */
    @ApiModelProperty(value = "店铺机构仓排序位置分组名称", name = "tempSeatName")
    private String tempSeatName;

    /**
     * 展会位置
     */
    @ApiModelProperty(value = "展会位置", name = "showSeat")
    private String showSeat;

    @ApiModelProperty(value = "店铺名称", name = "shopName")
    private String shopName;

    @ApiModelProperty(value = "商品数量", name = "productCount")
    private Integer productCount;
}
