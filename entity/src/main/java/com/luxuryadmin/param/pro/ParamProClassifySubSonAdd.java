package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.entity.pro
 * @ClassName: ParamProClassifySubSonAdd
 * @Author: ZhangSai
 * Date: 2021/8/5 14:43
 */
@ApiModel(description = "品牌系列分类添加类")
@Data
public class ParamProClassifySubSonAdd extends ParamProClassifySubSunAdd{
    /**
     * 商品系列分类pro_sub_series表分类名称
     */
    @ApiModelProperty(name = "subSeriesName", required = true,value = "商品系列分类pro_sub_series表分类名称")
    private String subSeriesName;
}
