package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 创建临时仓的参数模型
 *
 * @author by Administrator
 * @Classname ParamProduct
 * @Description TODO
 * @date 2021-01-18 01:51:01
 */
@Data
@ApiModel(description = "临时仓-一般商品列表查询参数实体")
public class ParamProTempProductQuery extends ParamProductQuery {

    @ApiModelProperty(value = "临时仓id", name = "proTempId", required = true)
    @NotBlank(message = "临时仓ID不能为空")
    @Pattern(regexp = "^[0-9]+$", message = "临时仓id--参数错误")
    private String proTempId;

    @ApiModelProperty(value = "商品价格类型（initPrice：成本价，tradePrice：友商价，agencyPrice：代理价，salePrice：销售价）", name = "defaultPrice", required = true)
    private String defaultPrice;

    /*@ApiModelProperty(value = "是否为登录后初次请求接口，10：是；99：否",name = "firstRequest",required = true)
    private String firstRequest;*/

}
