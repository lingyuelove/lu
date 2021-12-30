package com.luxuryadmin.param.login;

import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.param.common.ParamBasic;
import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 一键登录
 *
 * @author taoqimin
 * @Date 2021-09-23
 */
@ApiModel(description = "一键登录获取信息")
@Data
public class ParamVerify extends ParamBasic {

    @ApiModelProperty(value = "友盟token", name = "token", required = true)
    @NotBlank(message = "token不能为空")
    private String umToken;

    @ApiModelProperty(hidden = true)
    private String noSetPassword;

    @ApiModelProperty(hidden = true)
    private String ipAddress;

    @ApiModelProperty(hidden = true)
    private BasicParam getBasicParam;
}
