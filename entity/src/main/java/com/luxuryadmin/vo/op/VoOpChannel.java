package com.luxuryadmin.vo.op;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoOpChannel {

    /**
     * 渠道ID
     */
    @ApiModelProperty(value = "注册渠道", name = "channelId", required = false)
    private String channelId;

    /**
     * 渠道名称
     */
    @ApiModelProperty(value = "渠道名称", name = "channelName", required = false)
    private String channelName;
}
