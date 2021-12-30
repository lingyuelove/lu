package com.luxuryadmin.gateway.app.config.push;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;
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



    /******************************************************  测试方法  ***********************************************/
    public static void main(String[] args) {

        String TEST_MASTER_SECRET = "065a3f9bfa4286422ded4d75";
        String TEST_APP_KEY = "2076185b89aab000d4e958aa";
        JPushClient jpushClient = new JPushClient(
                TEST_MASTER_SECRET, TEST_APP_KEY, null, ClientConfig.getInstance());

        // For push, all you need do is to build PushPayload object.
        PushPayload payload = buildPushObject_all_all_alert();

        try {
            PushResult result = jpushClient.sendPush(payload);
            log.info("Got result - " + result);

        } catch (APIConnectionException e) {
            // Connection error, should retry later
            log.error("Connection error, should retry later", e);

        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            log.error("Should review the error, and fix the request", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
        }
    }

    public static PushPayload buildPushObject_all_all_alert() {
        return PushPayload.alertAll("奢当家三金极光测试");
    }
}


