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
public class ParamIdQuery extends ParamToken {

    @ApiModelProperty(name = "id", required = true, value = "id不能为空")
    @NotNull(message="id不能为空")
    private String id;

}
