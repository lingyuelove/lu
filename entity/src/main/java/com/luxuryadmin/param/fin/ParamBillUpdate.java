package com.luxuryadmin.param.fin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 修改帐单类
 *
 * @author zhangSai
 * @date   2021/04/23 14:19:52
 */
@Data
@ApiModel(value="修改帐单类", description="修改帐单类")
public class ParamBillUpdate {

    @ApiModelProperty(value = "账单id", name = "billId", required = true)
    private Integer billId;

    @ApiModelProperty(name = "basicMoney", required = true, value = "资金(分)")
    @NotNull(message = "资金不能为空")
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "友商价-格式错误")
    @Length(max = 15,message = "价格超出范围!")
    private String money;

    /**
     * 对账类型逗号分隔
     */
    @ApiModelProperty(value = "对账类型逗号分隔", name = "types", required = true)
    @NotNull(message = "对账类型不能为空")
    private String types;

    @ApiModelProperty(name = "totalMoney", required = true, value = "总金额(分)")
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "友商价-格式错误")
    @Length(max = 15,message = "价格超出范围!")
    private String totalMoney;

    private Integer shopId;

    private Integer userId;
}
