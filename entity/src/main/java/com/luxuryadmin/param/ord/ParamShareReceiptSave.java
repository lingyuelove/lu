package com.luxuryadmin.param.ord;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 保存分享凭证记录--查询参数实体
 *
 * @author monkey king
 * @date 2020-09-04 20:28:44
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "保存分享凭证记录--查询参数实体")
@Data
public class ParamShareReceiptSave extends ParamToken {

    /**
     * 订单编号
     */
    @ApiModelProperty(name = "orderNo", required = true, value = "订单编号")
    @NotBlank(message = "orderNo不允许为空")
    private String orderNo;

    /**
     * 是否显示店铺商品
     */
    @ApiModelProperty(name = "showProduct", required = true,
            value = "是否显示店铺商品;0:不显示; 1:显示")
    @Pattern(regexp = "^[01]$", message = "showProduct--参数错误")
    @NotBlank(message = "showProduct不允许为空")
    private String showProduct;
    @ApiModelProperty(name = "showProduct", required = false,
            value = "是否显示分享物流信息;0:不显示; 1:显示")
    @Pattern(regexp = "^[01]$", message = "showProduct--参数错误")
    private String showDeliver;
    /**
     * 收据类型
     */
    @ApiModelProperty(name = "receiptType", required = true, value = "收据类型 dzpz|电子凭证 service|店铺服务")
    private String receiptType;

    @ApiModelProperty(name = "showType", required = false, value = "展示类型 all 全部 context:只展示内容 默认all")
    private String showType;
}