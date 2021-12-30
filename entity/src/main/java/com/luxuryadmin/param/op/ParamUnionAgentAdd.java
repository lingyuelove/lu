package com.luxuryadmin.param.op;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ParamUnionAgentAdd extends ParamToken {

    @ApiModelProperty(name = "shpUserId", required = true, value = "工作人员id",example="工作人员id")
    @NotNull(message="工作人员id不能为空")
    private Integer employeeId;

    @ApiModelProperty(name = "shpUserId", required = true, value = "代理人员id",example="代理人员id")
    @NotNull(message="代理人员id不能为空")
    private Integer shpUserId;

    @ApiModelProperty(name = "validDay", required = true, value = "有效期（天）",example="有效期（天）")
    @NotNull(message="有效期不能为空")
    private Integer validDay;

    @ApiModelProperty(hidden = true)
    private Integer currentUserId;

    @ApiModelProperty(name = "agentSwitch", required = true, value = "代理人员开关: 0:关 | 1:开")
    @NotNull(message="商家联盟分享开关: 0:关 | 1:开")
    private String agentSwitch;
}
