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
public class ParamOrdReceipt extends ParamToken {

    /**
     * 订单编号
     */
    @ApiModelProperty(name = "orderBizId", required = true, value = "订单编号")
    @NotBlank(message = "orderBizIdo不允许为空")
    private String orderBizId;

}