package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamConveyProductUpdate
 * @Author: ZhangSai
 * Date: 2021/11/24 10:24
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "寄卖传送商品页面查询参数实体")
@Data
public class ParamConveyProductUpdate extends ParamToken {
    @ApiModelProperty(value = "主键id", name = "id", required = true)
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
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    @Pattern(regexp = "^[0-9]+$", message = "数量-格式错误")
    @Max(value = 9999999, message = "数量超出限制范围!")
    private String num;
    /**
     * 结算价
     */
    @ApiModelProperty(value = "结算价")
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "结算价-格式错误")
    @Length(max = 15, message = "价格超出范围!")
    private String finishPrice;

    @ApiModelProperty(value = "用户id字段,主键id",hidden = true)
    private Integer userId;

    @ApiModelProperty(value = "店铺id字段,主键id",hidden = true)
    private Integer shopId;
}
