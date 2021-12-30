package com.luxuryadmin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kuaker.pay.wx")
public class WxPayProperties {

    /**
     * 回调地址
     */
    private String notifyUrl;

    /**
     * 统一下单
     */
    private String placeUrl;

    /**
     * 登录url
     */
    private String loginUrl;

    private String appid;

    private String secret;

    private String mchid;

    private String key;

    /**
     * 证书编号
     */
    private String certificateSerialNo;

}
