package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoProClassifyForAnalysis
 * @Author: ZhangSai
 * Date: 2021/9/7 14:53
 */

@Data
@ApiModel(value="统计数据--价格品类统计", description="统计数据--价格品类统计")
public class VoProClassifyForAnalysis {
    @ApiModelProperty(name = "totalPrice", value = "统计数据-成本")
    private String totalPrice;
    @ApiModelProperty(name = "num", value = "统计数据-数量")
    private Integer num;
    @ApiModelProperty(name = "classifyName", value = "统计数据-分类名称")
    private String classifyName;
    @ApiModelProperty(name = "showPrice", value = "统计数据-展示成本")
    private String showPrice;
    @ApiModelProperty(name = "showProportion", value = "统计数据-展示比例")
    private String showProportion;
    @ApiModelProperty(name = "attributeName", value = "统计数据-属性名称")
    private String  attributeName;
    @ApiModelProperty(name = "totalPriceForHide",hidden = true, value = "统计数据-成本 计算使用")
    private BigDecimal totalPriceForHide;
}
