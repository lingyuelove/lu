package com.luxuryadmin.push;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 极光配置文件
 * @author: sanjin145
 * @date: 2020-07-13 13:46
 * @email: dingxin_hz@163.com
 * @version: V1.0
 */
@Configuration
@Data
@Slf4j
public class JiGuangConfig {

    /**
     * 极光推送 masterSecret
     */
    @Value("${jiguang.masterSecret}")
    private String masterSecret;

    /**
     * 极光推送 appKey
     */
    @Value("${jiguang.appKey}")
    private String appKey;


    @Bean("jpushClient")
    public JPushClient generateJPushClient() {
        return new JPushClient(
                masterSecret, appKey, null, ClientConfig.getInstance());
    }

}


