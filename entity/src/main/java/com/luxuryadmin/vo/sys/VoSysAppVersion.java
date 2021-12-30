package com.luxuryadmin.vo.sys;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoSysAppVersion {

    /**
     * 是否需要更新
     */
    @ApiModelProperty(name = "isNeedUpdate", required = false,
            value = "true|需要更新 false|不需要更新")
    private Boolean isNeedUpdate;

    /**
     * 是否强制更新
     */
    @ApiModelProperty(name = "isForce", required = false,
            value = "true|强制更新 false|不强制更新")
    private Boolean isForce;

    /**
     * 【发现有新版本】标题
     */
    @ApiModelProperty(name = "isForce", required = false,
            value = "【发现有新版本】标题")
    private String findTitle;

    /**
     * 【新版本V2.1.1】标题
     */
    @ApiModelProperty(name = "isForce", required = false,
            value = "【新版本V2.1.1】标题")
    private String versionTitle;

    /**
     * 【新版本V2.1.1】标题
     */
    @ApiModelProperty(name = "updateInfo", required = false,
            value = "更新内容")
    private String updateInfo;

    /**
     * 【新版本V2.1.1】标题
     */
    @ApiModelProperty(name = "updateBtnText", required = false,
            value = "更新按钮文本")
    private String updateBtnText;

    /**
     * 更新链接
     */
    @ApiModelProperty(name = "updateUrl", required = false,
            value = "更新链接")
    private String updateUrl;

}
