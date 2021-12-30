package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamClassifySonForAdminQuery
 * @Author: ZhangSai
 * Date: 2021/8/20 17:27
 */
@ApiModel(description = "品牌，系列分类检索类")
@Data
public class ParamClassifySonForAdminQuery {
    /**
     * 商品系列分类pro_sub_series表分类名称
     */
    @ApiModelProperty(name = "subSeriesName", required = false,value = "商品系列分类pro_sub_series表系列名称查询型号时必传")
    private String subSeriesName;

    /**
     * 商品品牌分类pro_classify_sub表品牌名称
     */
    @ApiModelProperty(name = "classifySubName", required = true,value = "商品品牌分类pro_classify_sub表品牌名称")
    private String classifySubName;



}
