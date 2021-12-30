package com.luxuryadmin.param.op;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author taoqimin
 */
@Data
public class ParamUnionAgentQuery extends ParamToken {

    @ApiModelProperty(name = "unionSwitch", required = true, value = "开关不能为空")
    @NotBlank(message="开关不能为空")
    private String unionSwitch;

    @ApiModelProperty(name = "id", required = true, value = "id不能为空")
    @NotNull(message="id不能为空")
    private Integer id;

    @ApiModelProperty(hidden = true)
    private Integer currentUserId;

}
