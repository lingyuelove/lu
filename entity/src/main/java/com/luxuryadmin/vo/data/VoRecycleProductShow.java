package com.luxuryadmin.vo.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.data
 * @ClassName: VoRecycleProductShow
 * @Author: ZhangSai
 * Date: 2021/12/1 14:56
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="回收列表-- 回收的商品显示", description="回收列表-- 回收的商品显示")
public class VoRecycleProductShow {

    @ApiModelProperty(value = "回收的商品集合显示", name = "list")
    List<VoRecycleProductList> list;

    @ApiModelProperty(value = "总库存", name = "totalNum")
    private String totalNum;
    @ApiModelProperty(value = "总成本", name = "totalPrice")
    private String totalPrice;

    @ApiModelProperty(value = "总库存名字", name = "totalNumName")
    private String totalNumName;
    @ApiModelProperty(value = "总成本名字", name = "totalPriceName")
    private String totalPriceName;
}
