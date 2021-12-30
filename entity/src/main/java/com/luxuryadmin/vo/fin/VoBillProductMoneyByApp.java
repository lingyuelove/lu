package com.luxuryadmin.vo.fin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 * 商品总价格类
 *
 * @author zhangSai
 * @date 2021/04/23 14:19:52
 */
@Data
@ApiModel(value="商品总价格类", description="商品总价格类")
public class VoBillProductMoneyByApp {

    @ApiModelProperty(name = "attributeCode", value = "10:自有商品;20:寄卖商品;30:质押商品;40:其他商品")
    private String attributeCode;

    @ApiModelProperty(name = "productMoney", value = "商品价格")
    private BigDecimal productMoney;
}
