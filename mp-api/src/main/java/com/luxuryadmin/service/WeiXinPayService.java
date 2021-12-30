package com.luxuryadmin.service;


import com.luxuryadmin.param.pay.ParamPay;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface WeiXinPayService {

    /**
     * 微信小程序支付
     */
    Map<String, Object> wxMpPay(ParamPay paramPay);

}
