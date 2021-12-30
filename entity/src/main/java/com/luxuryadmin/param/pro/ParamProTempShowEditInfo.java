package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

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
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "临时仓-修改商品详情-一般商品列表查询参数实体")
@Data
public class ParamProTempShowEditInfo extends ParamProTempId {

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id", name = "proId", required = true)
    @NotBlank(message = "proId不允许为空")
    private String proId;

    @ApiModelProperty(value = "商品价格类型（initPrice：成本价，tradePrice：友商价，agencyPrice：代理价，salePrice：销售价）", name = "priceType", required = true)
    private String priceType;

    /**
     * API版本
     */
    @ApiModelProperty(name = "apiVersion", value = "基础参数2：API版本")
    @Pattern(regexp = "^[0-9.]+$", message = "[apiVersion]格式错误")
    private String apiVersion;

    /**
     * APP版本
     */
    @ApiModelProperty(name = "appVersion", value = "基础参数3：APP版本")
    @Pattern(regexp = "^[0-9.]+$", message = "[appVersion]格式错误")
    private String appVersion;

}
