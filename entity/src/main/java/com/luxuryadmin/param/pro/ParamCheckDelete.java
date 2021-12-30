package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @PackgeName: com.luxuryadmin.api.pro
 * @ClassName: ParamCheckDelete
 * @Author: ZhangSai
 * Date: 2021/10/14 11:20
 */

@Data
public class ParamCheckDelete extends ParamToken {
    @ApiModelProperty(value = "临时仓id", name = "proTempId", required = true)
    @NotBlank(message = "临时仓id，用逗号分隔不为空")
    private String id;
}
