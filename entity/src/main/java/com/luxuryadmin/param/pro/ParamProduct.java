package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamProduct
 * @Author: ZhangSai
 * Date: 2021/8/5 16:52
 */
@ApiModel(description = "商品初始化")
@Data
public class ParamProduct extends ParamToken {
    /**
     * 商品唯一标识符,业务id,此id对外开放
     */
    @ApiModelProperty(value = "bizId", name = "商品唯一标识符,业务id", required = false)
    private String bizId;
}
