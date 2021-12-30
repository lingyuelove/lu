package com.luxuryadmin.common.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * 接口参数的基础信息
 *
 * @author monkey king
 * @date 2020-12-11 20:55:14
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BasicParam {

    /**
     * deviceId 设备唯一ID : 手机的IMEI值; 除非刷机;否则重装系统该值也不会变
     */
    private String deviceId;

    /**
     * API版本
     */
    private String apiVersion;

    /**
     * APP版本
     */
    private String appVersion;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * platform 平台类型：pc，android，ios，wap
     */
    private String platform;

    /**
     * phoneType 手机类型: iPhone,huawei, xiaomi, oppo
     */
    private String phoneType;

    /**
     * phoneSystem 手机系统 iOS 11.2, MIU,
     */
    private String phoneSystem;

    /**
     * netType 网络类型 2G,3G,4G,wifi
     */
    private String netType;

    /**
     * 渠道号
     */
    private String channel;

    /**
     * 签名
     */
    private String sign;

    /**
     * ip
     */
    private String ip;

    /**
     * 显示条数
     */
    private Integer pageSize;

    /**
     * 页数
     */
    private Integer pageNum;
}
