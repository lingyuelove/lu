package com.luxuryadmin.vo.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.vo.data
 * @ClassName: VoDataPower
 * @Author: ZhangSai
 * Date: 2021/6/22 14:10
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="获取数据分析权限", description="获取数据分析权限")
public class VoDataProfit {

    @ApiModelProperty(value = "销售分析", name = "salePower")
    private String saleProfit;

    @ApiModelProperty(value = "回收分析", name = "uPermServiceProfit")
    private String recycleProfit;

    @ApiModelProperty(value = "资产分析", name = "moneyProfit")
    private String moneyProfit;

}
