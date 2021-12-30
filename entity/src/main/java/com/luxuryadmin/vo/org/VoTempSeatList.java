package com.luxuryadmin.vo.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 店铺机构仓排序位置分组集合显示
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="店铺机构仓排序位置分组集合显示", description="店铺机构仓排序位置分组集合显示")
public class VoTempSeatList {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id", name = "id")
    private Integer id;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id", name = "fkShpShopId")
    private Integer shopId;

    /**
     * 分组名称
     */
    @ApiModelProperty(value = "分组名称", name = "name")
    private String name;
}
