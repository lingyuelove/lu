package com.luxuryadmin.service.impl;

import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.config.WeiXinConfig;
import com.luxuryadmin.service.ServiceService;
import com.luxuryadmin.service.TokenService;
import com.luxuryadmin.util.MessageUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;


/**
 * 小程序用户表 serverImpl
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:01
 */
@Service
@Transactional
@Log4j2
public class ServiceServiceImpl implements ServiceService {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private TokenService tokenService;


    /**
     * 小程序客服
     *
     * @param isGet
     */
    @Override
    public void wxMpService(boolean isGet, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isGet) {
            Map<String, String[]> parameterMap = request.getParameterMap();
            String timestamp = parameterMap.get("timestamp")[0];
            String nonce = parameterMap.get("nonce")[0];
            String echostr = parameterMap.get("echostr")[0];
            String signature = parameterMap.get("signature")[0];
            log.info("signature:" + signature);
            String thisSignature = sortAndEncrypt(WeiXinConfig.mp_service_token, timestamp, nonce);
            log.info("thisSignature:" + thisSignature);
            if (signature.equals(thisSignature)) {
                response.getOutputStream().write(echostr.getBytes());
            }
        } else {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            String result = acceptMessage(request);
            PrintWriter out = response.getWriter();
            out.print(result);
            out.close();
        }
    }

    /**
     * 捕获消息
     *
     * @param request
     * @return
     */
    private String acceptMessage(HttpServletRequest request) {
        String respMessage = "";
        try {
            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml(request);
            log.info(">>>>>>>>>>>>>" + requestMap);
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            log.info("fromUserName = " + fromUserName + " , ToUserName = " + toUserName );
            respMessage = sendKfMessage(fromUserName, toUserName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respMessage;
    }
    /**
     * 发送到客服中心
     *
     * @param fromUserName

     * @param toUserName
     * @return
     * @throws Exception
     */
    public static String sendKfMessage(String fromUserName, String toUserName){
        String responseMessage = "<xml>\n" +
                "     <ToUserName><![CDATA[" + fromUserName + "]]></ToUserName>\n" +
                "     <FromUserName><![CDATA[" + toUserName + "]]></FromUserName>\n" +
                "     <CreateTime>" + System.currentTimeMillis() + "</CreateTime>\n" +
                "     <MsgType><![CDATA[transfer_customer_service]]></MsgType>\n" +
                " </xml>\n";
        log.info("xml:>>>" + responseMessage);
        return responseMessage;

    }

    /**
     * 签名
     *
     * @param token
     * @param timestamp
     * @param nonce
     * @return
     */
    public static String sortAndEncrypt(String token, String timestamp, String nonce) {
        ArrayList<String> list = new ArrayList<String>();
        list.add(token);
        list.add(timestamp);
        list.add(nonce);
        Collections.sort(list);
        return DigestUtils.shaHex(list.get(0) + list.get(1) + list.get(2));
    }
}
