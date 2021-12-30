package com.luxuryadmin.common.utils.youmeng;

import com.alibaba.fastjson.JSONObject;
import com.luxuryadmin.common.exception.MyException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * 友盟Http请求
 */
public class UMHttps {


    /**
     * 友盟一键登录Http请求
     *
     * @param token
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static CloseableHttpResponse httpResponse(String token, String platform) throws NoSuchAlgorithmException, InvalidKeyException {
        String url;
        if (platform.equals("IOS")) {
            url = UMConfig.UM_IOS_APP_KEY;
        } else {
            url = UMConfig.UM_ANDROID_APP_KEY;
        }
        HttpPost httpPost = new HttpPost(UMConfig.UM_URL_PREFIX + url);
        /**
         * body
         */
        JSONObject object = new JSONObject();
        object.put("token", token);
        StringEntity stringEntity = new StringEntity(object.toJSONString(), StandardCharsets.UTF_8);
        httpPost.setEntity(stringEntity);
        /**
         * header
         */
        httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("X-Ca-Version", "1");
        httpPost.setHeader("X-Ca-Signature-Headers", "X-Ca-Version,X-Ca-Stage,X-Ca-Key,X-Ca-Timestamp");
        httpPost.setHeader("X-Ca-Stage", "RELEASE");
        httpPost.setHeader("X-Ca-Key", UMConfig.APP_KEY);
        httpPost.setHeader("X-Ca-Timestamp", String.valueOf(System.currentTimeMillis()));
        httpPost.setHeader("X-Ca-Nonce", UUID.randomUUID().toString());
        httpPost.setHeader("Content-MD5", Base64.encodeBase64String(DigestUtils.md5(object.toJSONString())));
        /**
         * sign
         */
        String stringToSign = getSignString(httpPost);
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        byte[] keyBytes = UMConfig.APP_SECRET.getBytes(StandardCharsets.UTF_8);
        hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256"));
        String sign = new String(Base64.encodeBase64(hmacSha256.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8))));
        httpPost.setHeader("X-Ca-Signature", sign);
        /**
         * execute
         */
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            CloseableHttpResponse response = httpclient.execute(httpPost);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("" + e.getMessage());
        }
    }


    /**
     * 获取加密串
     *
     * @param httpPost
     * @return
     */
    private static String getSignString(HttpPost httpPost) {
        Header[] headers = httpPost.getAllHeaders();
        Map<String, String> map = new HashMap<>();
        for (Header header : headers) {
            map.put(header.getName(), header.getValue());
        }
        return httpPost.getMethod() + "\n" +
                map.get("Accept") + "\n" +
                map.get("Content-MD5") + "\n" +
                map.get("Content-Type") + "\n\n" +
                "X-Ca-Key:" + map.get("X-Ca-Key") + "\n" +
                "X-Ca-Stage:" + map.get("X-Ca-Stage") + "\n" +
                "X-Ca-Timestamp:" + map.get("X-Ca-Timestamp") + "\n" +
                "X-Ca-Version:" + map.get("X-Ca-Version") + "\n" +
                httpPost.getURI().getPath() + "?" + httpPost.getURI().getQuery();
    }
}
