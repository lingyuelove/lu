package com.luxuryadmin.param.common;

/**
 * @author monkey king
 * @date 2020-07-29 17:20:40
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;


/**
 * 基础参数--前端接收参数模型
 *
 * @author monkey king
 * @date 2021-02-06 02:56:24
 */
@Data
@ApiModel(description = "基础参数")
public class ParamBasic {

    /**
     * deviceId 设备唯一ID : 手机的IMEI值; 除非刷机;否则重装系统该值也不会变
     */
    @ApiModelProperty(name = "deviceId",
            value = "基础参数1：deviceId 设备唯一ID : 手机的IMEI值; 除非刷机;否则重装系统该值也不会变")
    private String deviceId;

    /**
     * API版本
     */
    @ApiModelProperty(name = "apiVersion", value = "基础参数2：API版本")
    @Pattern(regexp = "^[0-9.]+$", message = "[apiVersion]格式错误")
    private String apiVersion;

    /**
     * APP版本
     */
    @ApiModelProperty(name = "appVersion", value = "基础参数3：APP版本")
    @Pattern(regexp = "^[0-9.]+$", message = "[appVersion]格式错误")
    private String appVersion;

    /**
     * 时间戳
     */
    @ApiModelProperty(name = "timestamp", value = "基础参数4：时间戳")
    @Pattern(regexp = "^\\d{5,}$", message = "[timestamp]格式错误")
    private String timestamp;

    /**
     * platform 平台类型：pc，android，ios，wap
     */
    @ApiModelProperty(name = "platform", value = "基础参数5：平台类型：pc，android，ios，wap")
    @Pattern(regexp = "^(pc)|(android)|(ios)|(wap)$", message = "[platform]格式错误")
    private String platform;

    /**
     * phoneType 手机类型: iPhone,huawei, xiaomi, oppo
     */
    @ApiModelProperty(name = "phoneType", value = "基础参数6：手机类型: iPhone,huawei, xiaomi, oppo")
    private String phoneType;

    /**
     * phoneSystem 手机系统 iOS 11.2, MIU,
     */
    @ApiModelProperty(name = "phoneSystem", value = "基础参数7：手机系统 iOS 11.2, MIU,")
    private String phoneSystem;

    /**
     * netType 网络类型 2G,3G,4G,5G,wifi
     */
    @ApiModelProperty(name = "netType", value = "基础参数8：网络类型 2G,3G,4G,5G,wifi")
    private String netType;

    /**
     * 渠道号
     */
    @ApiModelProperty(name = "channel", value = "基础参数9：渠道号")
    private String channel;

    ///**
    // * 显示条数
    // */
    //@ApiModelProperty(name = "pageSize", value = "每页显示条数")
    //@Pattern(regexp = "^\\d{2,}$", message = "[pageSize]格式错误")
    //private String pageSize;

    /**
     * 当前页
     */
    @ApiModelProperty(name = "pageNum", required = false, value = "当前页;默认为1")
    @Min(value = 1, message = "当前页不能小于1")
    @Max(value = 99999, message = "当前页最大为99999")
    @Pattern(regexp = "^\\d+$", message = "[pageNum]格式错误")
    private String pageNum;

    /**
     * 签名
     */
    @ApiModelProperty(name = "sign", value = "基础参数：签名")
    private String sign;
}


