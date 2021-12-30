package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoProSubService
 * @Author: ZhangSai
 * Date: 2021/8/4 14:07
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(value="商品分类", description="商品分类")
public class VoProSubSeries {
    /**
     * 二级分类id
     */
    @ApiModelProperty(value = "主键id", name = "id")
    private Integer id;


    @ApiModelProperty(name = "classifySubName",value = "品牌名称")
    private String classifySubName;

    @ApiModelProperty(name = "subSeriesName",value = "系列名称")
    private String subSeriesName;

    @ApiModelProperty(name = "seriesModelName",value = "型号名称")
    private String seriesModelName;

    @ApiModelProperty(value = "类型;0:系统自带;1:用户自建")
    private String type;
}
