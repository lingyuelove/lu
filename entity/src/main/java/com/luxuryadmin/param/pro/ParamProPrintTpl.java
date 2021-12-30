package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamProPrintTpl
 * @Author: ZhangSai
 * Date: 2021/8/12 10:54
 */
@ApiModel(description = "打印模板-app端查询")
@Data
public class ParamProPrintTpl extends ParamToken {
    @ApiModelProperty(value = "商品业务id", name = "bizId", required = false)
    private String bizId;

    @ApiModelProperty(value = "临时仓id", name = "tempId", required = false)
    private String tempId;

    @ApiModelProperty(value = "店铺id", name = "shopId", hidden = true)
    private Integer shopId;

}
