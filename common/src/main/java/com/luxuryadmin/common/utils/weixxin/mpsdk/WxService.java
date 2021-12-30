package com.luxuryadmin.common.utils.weixxin.mpsdk;

import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.aliyun.AliHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author monkey king
 * @date 2020-08-07 23:08:33
 */
@Slf4j
@Component
public class WxService {

    public String getAccessToken() {
        //授予形式
        String grantType = "client_credential";
        //接口地址拼接参数
        String getTokenApi = "https://api.weixin.qq.com/cgi-bin/token";

        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("grant_type", grantType);
        paramMap.put("appid", "wx059deb8190307329");
        paramMap.put("secret", "431c19ea66e9e66be3329103837d584d");

        String tokenJsonStr = HttpUtil.get(getTokenApi, paramMap);
        log.info("获取token后请求返回：" + tokenJsonStr);
        JSONObject tokenJson = JSONObject.parseObject(tokenJsonStr);
        String token = tokenJson.get("access_token").toString();
        System.out.println("获取到的TOKEN : " + token);

        return token;
    }


    /**
     * 用于小程序
     * 登录凭证校验,获取session_key
     *
     * @return
     */
    public WxEntity code2Session(String jsCode) throws Exception {

        String appId = "wx0c98b8377098e483";

        String appSecret = "ad59be282fcdf950e14b9071d7d0f404";

        return code2Session(jsCode, appId, appSecret);
    }


    /**
     * 用于小程序
     * 登录凭证校验,获取session_key
     *
     * @return
     */
    public WxEntity code2Session(String jsCode, String appId, String appSecret) throws Exception {
        //授予形式
        String grantType = "authorization_code";
        //接口地址拼接参数
        String getTokenApi = "https://api.weixin.qq.com/sns/jscode2session";

        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        StringBuilder sb = new StringBuilder();
        sb.append("?grant_type=");
        sb.append(grantType);
        sb.append("&appid=" + appId);
        sb.append("&secret=" + appSecret);
        sb.append("&js_code=");
        sb.append(jsCode);
        HttpResponse post = AliHttpUtils.doGet(getTokenApi + sb.toString(), null, null, null);
        int statusCode = post.getStatusLine().getStatusCode();
        WxEntity wxEntity = new WxEntity();
        if (statusCode == HttpStatus.SC_OK) {
            String str = EntityUtils.toString(post.getEntity(), "UTF-8");
            JSONObject jsonObject;
            try {
                log.info("获取session_key后请求返回：" + str);
                jsonObject = JSONObject.parseObject(str);
            } catch (Exception e) {
                throw new MyException("not JSON format! ");
            }
            Object sessionKey = jsonObject.get("session_key");
            if (!LocalUtils.isEmptyAndNull(sessionKey)) {
                Object openid = jsonObject.get("openid");
                Object unionid = jsonObject.get("unionid");
                wxEntity.setSessionKey(sessionKey.toString());
                wxEntity.setOpenId(openid.toString());
                wxEntity.setUnionId(unionid.toString());
                return wxEntity;
            }
            throw new MyException("获取sessionKey失败;[" + jsonObject.get("errmsg") + "]");
        }
        return wxEntity;
    }


    public static String getAccessTokenApplets() {
        //授予形式
        String grantType = "client_credential";
        //接口地址拼接参数
        String getTokenApi = "https://api.weixin.qq.com/cgi-bin/token";

        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("grant_type", grantType);
        paramMap.put("appid", "wx0c98b8377098e483");
        paramMap.put("secret", "ad59be282fcdf950e14b9071d7d0f404");
        getTokenApi = getTokenApi + "?grant_type=client_credential" + "&appid=wx0c98b8377098e483&secret=ad59be282fcdf950e14b9071d7d0f404";
        String tokenJsonStr = HttpUtils.httpsRequest(getTokenApi, "GET", null);
        log.info("获取token后请求返回：" + tokenJsonStr);
        JSONObject jsonObject = JSONObject.parseObject(tokenJsonStr);
        String token = jsonObject.get("access_token").toString();
        System.out.println("获取到的TOKEN : " + token);

        return token;
    }


    /**
     * 获取用户手机号
     *
     * @param iv
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public String getUserPhone(String iv, String data, String key) throws Exception {
        String result = WxUtil.decode(iv, data, key);
        JSONObject json = JSONObject.parseObject(result);
        return json.getString("purePhoneNumber");
    }


    public static void main(String[] args) {
        WxService accessToken = new WxService();
        //accessToken.code2Session("041ige000brVAL1yiE300U6HuL2ige0i");
    }
}
