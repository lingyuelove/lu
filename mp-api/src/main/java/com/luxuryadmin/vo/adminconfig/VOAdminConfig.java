package com.luxuryadmin.vo.adminconfig;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class VOAdminConfig {

    @ApiModelProperty(value = "体验时间", required = true, name = "experienceTime")
    private Integer experienceTime;

    @ApiModelProperty(value = "邀请奖励时间", required = true, name = "awardTime")
    private Integer awardTime;

    @ApiModelProperty(value = "云仓编号", required = true, name = "cloudWarehouse")
    private String cloudWarehouseNum;

    @ApiModelProperty(value = "店铺编号", required = true, name = "shopNum")
    private String shopNum;

    @ApiModelProperty(value = "首页地址", required = true, name = "homeUrl")
    private String homeUrl;

    @ApiModelProperty(value = "appId", required = true, name = "appId")
    private String appId;

}
