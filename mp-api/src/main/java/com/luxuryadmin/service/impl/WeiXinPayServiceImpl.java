package com.luxuryadmin.service.impl;

import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.common.utils.aliyun.AliHttpUtils;
import com.luxuryadmin.config.WxPayProperties;
import com.luxuryadmin.entity.MpUser;
import com.luxuryadmin.enums.EnumMasterType;
import com.luxuryadmin.mapper.MpUserMapper;
import com.luxuryadmin.param.pay.ParamPay;
import com.luxuryadmin.service.BasicsService;
import com.luxuryadmin.service.WeiXinPayService;
import com.luxuryadmin.util.HttpPost;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WeiXinPayServiceImpl implements WeiXinPayService {


    @Resource
    private WxPayProperties wxPayProperties;

    @Resource
    private BasicsService basicsService;

    @Resource
    private MpUserMapper mpUserMapper;

    public static final String ENCODING = "UTF-8";

    @Override
    public Map<String, Object> wxMpPay(ParamPay paramPay) {
        Integer userId = basicsService.getUserId();
        if (userId == null) {
            throw new MyException("用户未登录");
        }
        JSONObject payJson = new JSONObject();
        payJson.put("appid", wxPayProperties.getAppid());
        payJson.put("mchid", wxPayProperties.getMchid());
        payJson.put("description", "二奢云展会员费");
        payJson.put("out_trade_no", paramPay.getOrderNo());
        payJson.put("notify_url", wxPayProperties.getNotifyUrl());
        JSONObject amoutJson = new JSONObject();
        amoutJson.put("total", EnumMasterType.BY.getMoney());
        payJson.put("amount", amoutJson);
        JSONObject openIdJson = new JSONObject();
        MpUser mpUser = mpUserMapper.getObjectById(userId);
        openIdJson.put("openid", mpUser.getOpenId());
        payJson.put("payer", openIdJson);
        Map<String, Object> resMap = new HashMap<>();
        try {
            HttpUrl httpurl = HttpUrl.parse(wxPayProperties.getPlaceUrl());
            String nonceStr = LocalUtils.getUUID();
            long timestamp = System.currentTimeMillis() / 1000;

            String token = getToken("POST", httpurl, payJson.toString(), nonceStr, timestamp);
            String authorization = "WECHATPAY2-SHA256-RSA2048 " + token;

            String str = weixinHttp("POST", wxPayProperties.getPlaceUrl(), payJson.toString(), authorization);
            if (StringUtil.isBlank(str)) {
                throw new MyException("下单失败");
            }
            JSONObject jsonObject = new JSONObject(str);
            String st = wxPayProperties.getAppid() + "\n" + timestamp + "\n" + nonceStr + "\n" + "prepay_id=" + jsonObject.getString("prepay_id") + "\n";
            String signature = HttpPost.sign(st.getBytes(ENCODING));
            resMap.put("timeStamp", timestamp + "");
            resMap.put("nonceStr", nonceStr);
            resMap.put("package", "prepay_id=" + jsonObject.getString("prepay_id"));
            resMap.put("paySign", signature);
            return resMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("下单失败");
        }
    }

    public String weixinHttp(String method, String url, String bodyJsonData, String authorization) throws Exception {
        if ("post".equals(method.toLowerCase())) {
            return HttpPost.sendPost(method, url, bodyJsonData, ENCODING, authorization, null);
        } else {
            return AliHttpUtils.doGet(url, authorization);
        }
    }

    public String getToken(String method, HttpUrl url, String body, String nonceStr, long timestamp) throws Exception {
        String message = HttpPost.buildMessage(method, url, timestamp, nonceStr, body);
        String signature = HttpPost.sign(message.getBytes(ENCODING));
        String yourMerchantId = wxPayProperties.getMchid();
        String yourCertificateSerialNo = wxPayProperties.getCertificateSerialNo();
        return "mchid=\"" + yourMerchantId + "\","
                + "nonce_str=\"" + nonceStr + "\","
                + "timestamp=\"" + timestamp + "\","
                + "serial_no=\"" + yourCertificateSerialNo + "\","
                + "signature=\"" + signature + "\"";
    }
}
