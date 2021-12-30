package com.luxuryadmin.param.sys;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * 系统管理-账号管理
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "系统管理-版本管理")
@Data
public class ParamSysVersionAdd {

    /**
     * 机型ID
     */
    @ApiModelProperty(value = "版本记录ID", name = "id", required = false)
    private Long id;


    /**
     * 机型ID
     */
    @ApiModelProperty(value = "机型ID", name = "fkOpPlatformId", required = true)
    private Integer fkOpPlatformId;

    /**
     * app版本号 xx.xx.xx
     */
    @ApiModelProperty(value = "app版本号", name = "appVersion", required = true)
    @Pattern(regexp = "^([1-9]\\d|[1-9])(\\.([1-9]\\d|\\d)){2}$", message = "版本号格式错误")
    private String appVersion;

    /**
     * 更新地址
     */
    @ApiModelProperty(value = "更新URL", name = "updateUrl", required = true)
    private String updateUrl;

    /**
     * 版本说明,支持h5格式
     */
    @ApiModelProperty(value = "更新提示语", name = "content", required = true)
    private String content;

    /**
     * 强制更新:0:非强制更新  1:强制更新
     */
    @ApiModelProperty(value = "强制更新 0:非强制更新  1:强制更新", name = "forcedUpdate", required = true)
    private String forcedUpdate;

}
