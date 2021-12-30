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
public class ParamUsernameQuery extends ParamToken {

    @ApiModelProperty(name = "username", required = true, value = "用戶名不能为空")
    @NotBlank(message="用戶名不能为空")
    private String username;

}
