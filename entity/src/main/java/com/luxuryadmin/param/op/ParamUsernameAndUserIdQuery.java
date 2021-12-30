package com.luxuryadmin.param.op;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author taoqimin
 */
@Data
public class ParamUsernameAndUserIdQuery extends ParamToken {

    @ApiModelProperty(name = "username",value = "用戶名")
    private String username;

    @ApiModelProperty(name = "employeeId",value = "工作人员id")
    private Integer employeeId;
}
