package com.luxuryadmin.vo.op;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Banner原生页面VO类
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoOpBannerNativePage {

    /**
     * 原生页面编号
     */
    @ApiModelProperty(name = "nativePageCode", required = false, value = "原生页面编号")
    private String nativePageCode;

    /**
     * 原生页面展示名称
     */
    @ApiModelProperty(name = "nativePageShowName", required = false, value = "原生页面展示名称")
    private String nativePageShowName;

}

