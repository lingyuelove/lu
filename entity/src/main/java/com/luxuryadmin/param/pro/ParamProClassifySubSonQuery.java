package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamProClassifySubSonQuery
 * @Author: ZhangSai
 * Date: 2021/8/5 15:05
 */
@ApiModel(description = "品牌，系列分类检索类")
@Data
public class ParamProClassifySubSonQuery extends ParamProClassifySubSunQuery{
    /**
     * 商品系列分类pro_sub_series表分类名称
     */
    @ApiModelProperty(name = "subSeriesName", required = true,value = "商品系列分类pro_sub_series表系列名称")
    private String subSeriesName;
}
