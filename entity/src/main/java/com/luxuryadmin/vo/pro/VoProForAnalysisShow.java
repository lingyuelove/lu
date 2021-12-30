package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoProForAnalysisForApp
 * @Author: ZhangSai
 * Date: 2021/9/7 17:12
 */
@Data
@ApiModel(value="统计数据--价格品类统计", description="统计数据--价格品类统计")
public class VoProForAnalysisShow {
    @ApiModelProperty(name = "totalPrice", value = "统计数据-总成本")
    private String showTotalPrice;
    @ApiModelProperty(name = "attributeList", value = "统计数据-属性分析")
    private List<VoProClassifyForAnalysis> attributeList;
    @ApiModelProperty(name = "classify", value = "统计数据-品类分析")
    private List<VoProClassifyForAnalysis> classifyList;
}
