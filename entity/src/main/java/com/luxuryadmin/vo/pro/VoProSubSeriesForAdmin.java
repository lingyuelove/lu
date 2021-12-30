package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoProSubSeriesForAdmin
 * @Author: ZhangSai
 * Date: 2021/8/17 16:39
 */
@Data
@ApiModel(value="商品系列分类集合显示", description="商品系列分类集合显示")
public class VoProSubSeriesForAdmin {
    @ApiModelProperty(value = "主键id", name = "id")
    private Integer id;

    @ApiModelProperty(name = "classifySubName",value = "品牌名称")
    private String classifySubName;

    @ApiModelProperty(value = " 商品分类code; 和分类列表对应; 默认0:无分类;逗号分隔")
    private String classifyCode;

    /**
     * 商品分类中文名称; 和分类列表对应; 默认0:无分类;
     */
    @ApiModelProperty(value = " 商品分类中文名称; 和分类列表对应; 默认0:无分类;")
    private String classifyCodeName;

    @ApiModelProperty(name = "subSeriesName",value = "系列名称")
    private String subSeriesName;

    @ApiModelProperty(name = "seriesModelName",value = "型号名称")
    private String seriesModelName;
}
