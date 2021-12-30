package com.luxuryadmin.vo.admin.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 登录记录
 *
 * @author monkey king
 * @date 2020-12-18 21:14:21
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoShpUserToken {

    private String loginIp;

    private String loginPlace;

    private String insertTime;

    private String deviceId;

    private String platform;

    private String phoneType;

    private String phoneSystem;

    private String appVersion;
}
