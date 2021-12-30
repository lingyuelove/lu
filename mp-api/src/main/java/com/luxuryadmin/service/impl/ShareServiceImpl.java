package com.luxuryadmin.service.impl;

import com.alibaba.fastjson.JSON;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.config.WeiXinConfig;
import com.luxuryadmin.config.WxPayProperties;
import com.luxuryadmin.service.BasicsService;
import com.luxuryadmin.service.ShareService;
import com.luxuryadmin.service.TokenService;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.poi.util.IOUtils;
import org.aspectj.bridge.MessageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


/**
 * 小程序用户表 serverImpl
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:01
 */
@Service
@Transactional
public class ShareServiceImpl implements ShareService {

    @Resource
    private WxPayProperties wxPayProperties;

    @Resource
    private BasicsService basicsService;

    @Resource
    private TokenService tokenService;

    /**
     * 获取分享二维码
     *
     * @return
     */
    @Override
    public String getShareQRCode() {
        Integer userId = basicsService.getUserId();
        String access_token = tokenService.getWXToken();
        String imei = "shareId=" + userId;
        Map<String, Object> params = new HashMap<>();
        params.put("scene", imei);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(WeiXinConfig.getwxacodeunlimit + "?access_token=" + access_token);
        httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
        String body = JSON.toJSONString(params);
        StringEntity entity;
        HttpResponse response;
        String encoded;
        try {
            entity = new StringEntity(body);
            entity.setContentType("image/png");
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost);
            InputStream inputStream = response.getEntity().getContent();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            encoded = Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new MyException("获取二维码失败");
        }
        return encoded;
    }


}
