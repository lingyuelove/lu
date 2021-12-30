package com.luxuryadmin.param.adminconfig;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class ParamAdminConfig {

    @ApiModelProperty(value = "体验时间", required = true, name = "experienceTime")
    @NotNull(message = "体验时间不能为空")
    private Integer experienceTime;

    @ApiModelProperty(value = "邀请奖励时间", required = true, name = "awardTime")
    @NotNull(message = "邀请奖励时间不能为空")
    private Integer awardTime;

    @ApiModelProperty(value = "云仓编号", required = true, name = "cloudWarehouse")
    @NotBlank(message = "云仓编号不能为空")
    private String cloudWarehouseNum;

    @ApiModelProperty(value = "店铺编号", required = true, name = "shopNum")
    @NotBlank(message = "店铺编号不能为空")
    private String shopNum;

    @ApiModelProperty(value = "首页地址", required = true, name = "homeUrl")
    @NotBlank(message = "首页地址不能为空")
    private String homeUrl;

    @ApiModelProperty(value = "appId", required = true, name = "appId")
    @NotBlank(message = "appId不能为空")
    private String appId;
}
