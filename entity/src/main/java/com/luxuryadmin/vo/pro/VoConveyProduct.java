package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoConveyProduct
 * @Author: ZhangSai
 * Date: 2021/11/24 17:27
 */
@Data
@ApiModel(value="商品传送表集合显示实体参数", description="商品传送表集合显示实体参数")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoConveyProduct {
    @ApiModelProperty(value = "商品价格类型（initPrice：成本价，tradePrice：友商价，agencyPrice：代理价，salePrice：销售价）", name = "defaultPrice")
    private String defaultPrice;
    @ApiModelProperty(value = "商品列表", name = "productLoads")
    private List<VoProductLoad> productLoads;
}
