package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
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
@ApiModel(description = "临时仓-修改商品详情-一般商品列表查询参数实体")
@Data
public class ParamProTempUpdate {

    /**
     * 登录标识符
     */
    @ApiModelProperty(value = "登录标识符", name = "token", required = true)
    private String token;

    /**
     * 临时仓商品的id
     */
    @ApiModelProperty(name = "id", required = true,
            value = "从接口showProTempProductEditInfo处获得此id")
    @NotBlank(message = "id不允许为空")
    private String id;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称", name = "name", required = false)
    @Length(max = 50, message = "商品名称长度必须≤50个字符")
    private String name;

    /**
     * 商品描述
     */
    @ApiModelProperty(value = "商品描述", name = "description", required = false)
    @Length(max = 250, message = "商品描述长度必须≤250个字符")
    private String description;


    /**
     * 成本价
     */
    @ApiModelProperty(value = "成本价;", name = "initPrice", required = false)
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "成本价-格式错误")
    @Length(max = 15, message = "价格超出范围!")
    private String initPrice;

    /**
     * 友商价
     */
    @ApiModelProperty(value = "友商价;", name = "tradePrice", required = false)
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "友商价-格式错误")
    @Length(max = 15, message = "价格超出范围!")
    private String tradePrice;

    /**
     * 代理价
     */
    @ApiModelProperty(value = "代理价;", name = "agencyPrice", required = false)
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "代理价-格式错误")
    @Length(max = 15, message = "价格超出范围!")
    private String agencyPrice;

    /**
     * 销售价(卖客价)
     */
    @ApiModelProperty(value = "卖客售价(卖客价);", name = "salePrice", required = false)
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "销售价-格式错误")
    @Length(max = 15, message = "价格超出范围!")
    private String salePrice;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量", name = "num", required = false)
    @Pattern(regexp = "^[0-9]+$", message = "数量-格式错误")
    @Max(value = 9999999, message = "数量超出限制范围!")
    private String num;


    /**
     * shopId
     */
    @ApiModelProperty(hidden = true)
    private Integer shopId;

    @ApiModelProperty(value = "商品唯一标识",name = "bizId",required = true)
//    @NotBlank(message = "商品唯一标识不允许为空")
    private String bizId;

    @ApiModelProperty(value = "修改商品信息时选中的价格类型（initPrice：成本价，tradePrice：友商价，agencyPrice：代理价，salePrice：销售价）",name = "tempId",required = false)
    private String priceType;
    @ApiModelProperty(value = "临时仓id",name = "tempId",required = false)
    private String tempId;


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
