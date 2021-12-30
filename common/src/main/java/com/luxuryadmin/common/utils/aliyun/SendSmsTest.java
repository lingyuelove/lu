package com.luxuryadmin.common.utils.aliyun;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;

/**
 * 发送短信工具类<br/>
 * 返回结果: <br/>
 * {"Message":"OK","RequestId":"A2B8AC9C-C791-45C2-A024-C584CCCE8AFA","BizId":"494119679489004980^0","Code":"OK"}<br/>
 * <a href="https://error-center.aliyun.com/status/product/Dysmsapi?spm=a2c4g.11186623.2.15.112a56e0VUsJyB">查看更多错误码</a>
 *
 * @author Administrator
 */
@Slf4j
public class SendSmsTest {

    private static final String ACCESS_KEY_ID = "LTAI4Fsx1tRUxbMdoRQceD3G";
    private static final String SECRET = "6siXbJSk9GZywBqYU1qDMfgzSMduCy";
    private static final String REGION_ID = "cn-hangzhou";
    private static final String SIGN_NAME = "奢品管家";
    private IAcsClient client;

    private SendSmsTest() {
        DefaultProfile profile = DefaultProfile.getProfile(REGION_ID, ACCESS_KEY_ID, SECRET);
        client = new DefaultAcsClient(profile);

    }

    private static class Inner {
        private static final SendSmsTest INSTANCE = new SendSmsTest();
    }

    private static SendSmsTest getInstance() {
        return SendSmsTest.Inner.INSTANCE;
    }

    public static void main(String[] args) {
        long st = System.currentTimeMillis();
        SendSmsTest.sendRegisterSms("15112304365", "661392");
        long et = System.currentTimeMillis();
        System.out.println(et - st);
    }

    /**
     * 发送短信
     *
     * @param phone         手机号
     * @param templateCode  短信模板(参考阿里云短信服务)
     * @param templateParam JSON值模板(参考阿里云短信服务)
     * @return
     */
    private static String sendSms(String phone, String templateCode, String templateParam) {
        try {
            CommonRequest request = new CommonRequest();
            request.setSysMethod(MethodType.POST);
            request.setSysDomain("dysmsapi.aliyuncs.com");
            request.setSysVersion("2017-05-25");
            request.setSysAction("SendSms");
            request.putQueryParameter("RegionId", REGION_ID);
            request.putQueryParameter("PhoneNumbers", phone);
            request.putQueryParameter("SignName", SIGN_NAME);
            request.putQueryParameter("TemplateCode", templateCode);
            request.putQueryParameter("TemplateParam", templateParam);
            CommonResponse response = getInstance().client.getCommonResponse(request);
            String responseData = response.getData();
            log.debug("=======发送短信结果: " + responseData);
            return responseData;
        } catch (Exception e) {
            log.error("======发送短信失败: " + e.getMessage(), e);
        }
        return null;
    }

    private static String getTemplateParam(String validCode) {
        return "{'code':'" + validCode + "'}";
    }

    /**
     * 发送注册验证码
     *
     * @param phone     手机号
     * @param validCode 验证码
     * @return
     */
    public static String sendRegisterSms(String phone, String validCode) {
        String templateCode = "SMS_182677621";
        String templateParam = getTemplateParam(validCode);
        return sendSms(phone, templateCode, templateParam);
    }

    /**
     * 发送登录验证码
     *
     * @param phone     手机号
     * @param validCode 验证码
     * @return
     */
    public static String sendLoginSms(String phone, String validCode) {
        String templateCode = "SMS_182667921";
        String templateParam = getTemplateParam(validCode);
        return sendSms(phone, templateCode, templateParam);
    }

    /**
     * 发送重置密码验证码
     *
     * @param phone     手机号
     * @param validCode 验证码
     * @return
     */
    public static String sendResetPwdSms(String phone, String validCode) {
        String templateCode = "SMS_182677789";
        String templateParam = getTemplateParam(validCode);
        return sendSms(phone, templateCode, templateParam);
    }

    /**
     * 发送店铺添加员工短信
     *
     * @param phone     手机号
     * @param shopName 店铺名称
     * @param password 密码
     * @return
     */
    public static String sendShopAddUserSms(String phone, String shopName, String password) {
        String templateCode = "SMS_195722646";
        StringBuilder sb = new StringBuilder();
        sb.append("{shopName:'");
        sb.append(shopName);
        sb.append(",password:'");
        sb.append(password);
        sb.append("',code:'download.html'}");
        return sendSms(phone, templateCode, sb.toString());
    }
}