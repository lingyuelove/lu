package com.luxuryadmin.vo.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description: TODO
 * @author: walkingPotato
 * @date: 2020-07-29 18:21
 * @email: dingxin_hz@163.com
 * @version: V1.0
 */
@Data
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoSaleRankHomePageData {

    /**
     * 总销量
     */
    @ApiModelProperty(value = "总销量", name = "totalSaleCount", required = false)
    private String totalSaleCount;

    /**
     * 总销售额
     */
    @ApiModelProperty(value = "总销售额", name = "totalSaleAmount", required = false)
    private String totalSaleAmount;

    /**
     * 总毛利润
     */
    @ApiModelProperty(value = "总毛利润", name = "totalGrossProfit", required = false)
    private String totalGrossProfit;

    /**
     * 毛利率
     */
    @ApiModelProperty(value = "毛利率", name = "grossProfitRate", required = false)
    private String grossProfitRate;

    /**
     * 总成本
     */
    @ApiModelProperty(value = "总成本", name = "totalInitPrice", required = false)
    private String totalInitPrice;

    /**
     * 销售排行榜列表
     */
    @ApiModelProperty(value = "销售排行榜列表", name = "saleRankList", required = false)
    private List<VoSaleRank> saleRankList;


}
