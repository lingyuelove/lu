package com.luxuryadmin.param.fin;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 薪资提成明细--前端接收参数模型
 *
 * @author sanjin145
 * @date 2020-10-21 22:31:50
 */
@ApiModel(description = "薪资提成明细")
@Data
public class ParamFinShopRecordId extends ParamToken {

    /**
     * 流水id
     */
    @ApiModelProperty(name = "finShopRecordId", required = true, value = "流水id")
    @NotBlank(message = "[流水id]不允许为空")
    private String finShopRecordId;


}
