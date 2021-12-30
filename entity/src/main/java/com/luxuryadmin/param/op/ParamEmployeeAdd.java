package com.luxuryadmin.param.op;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author taoqimin
 * @date 2021-09-16 23:00:23
 */
@Data
public class ParamEmployeeAdd extends ParamToken {

    @ApiModelProperty(name = "shpUserId", required = true, value = "工作人员id", example = "工作人员id")
    @NotNull(message = "工作人员id不能为空")
    private Integer shpUserId;

    @ApiModelProperty(name = "unionSwitch", required = true, value = "0:关 | 1:开", example = "商家联盟分享开关: 0:关 | 1:开")
    @NotBlank(message = "商家联盟分享开关不能为空")
    private String unionSwitch;


    @ApiModelProperty(name = "type", value = "0:技术部门;1:运营部;默认为 1")
    private String type;

    @ApiModelProperty(hidden = true)
    private Integer currentUserId;

}
