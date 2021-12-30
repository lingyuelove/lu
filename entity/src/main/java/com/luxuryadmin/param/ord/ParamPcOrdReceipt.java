package com.luxuryadmin.param.ord;

import com.luxuryadmin.param.common.ParamBasic;
import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 扫码查看pc端电子凭证--查询参数实体
 *
 * @author monkey king
 * @date 2020-09-04 20:28:44
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "扫码查看pc端电子凭证--查询参数实体")
@Data
public class ParamPcOrdReceipt extends ParamBasic {

    /**
     * 订单编号
     */
    @ApiModelProperty(name = "orderBizId", required = true, value = "订单编号")
    @NotBlank(message = "orderBizIdo不允许为空")
    private String orderBizId;

    /**
     * 店铺编号
     */
    @ApiModelProperty(name = "shopNumber", required = true, value = "店铺编号")
    @NotBlank(message = "shopNumber不允许为空")
    private String shopNumber;

}