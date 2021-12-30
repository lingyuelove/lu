package com.luxuryadmin.vo.stat;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoStateShopRank {

    /**
     * 排名
     */
    @ApiModelProperty(name = "rank", required = false, value = "排名")
    private Integer rank;

    /**
     * 店铺名称
     */
    @ApiModelProperty(name = "shopName", required = false, value = "店铺名称")
    private String shopName;

    /**
     * 展示数据
     */
    @ApiModelProperty(name = "showData", required = false, value = "展示数据")
    private String showData;

}
