package com.luxuryadmin.common.utils.aliyun;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 发送短信工具类<br/>
 * 正式生产环境<br/>
 * 返回结果: <br/>
 * {"Message":"OK","RequestId":"A2B8AC9C-C791-45C2-A024-C584CCCE8AFA","BizId":"494119679489004980^0","Code":"OK"}<br/>
 * <a href="https://error-center.aliyun.com/status/product/Dysmsapi?spm=a2c4g.11186623.2.15.112a56e0VUsJyB">查看更多错误码</a>
 *
 * @author Administrator
 */
@Slf4j
public class SendSms {

    private static final String ACCESS_KEY_ID = "LTAI4G6KzTMV5Kq19QTrqy4x";
    private static final String SECRET = "pvZOq1tsN6BjcwRX1Q2qVLyX7VgHw0";
    private static final String REGION_ID = "cn-hangzhou";
    private static final String SIGN_NAME = "奢当家";
    private IAcsClient client;

    private SendSms() {
        DefaultProfile profile = DefaultProfile.getProfile(REGION_ID, ACCESS_KEY_ID, SECRET);
        client = new DefaultAcsClient(profile);

    }

    private static class Inner {
        private static final SendSms INSTANCE = new SendSms();
    }

    private static SendSms getInstance() {
        return SendSms.Inner.INSTANCE;
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
            CommonRequest request = getSendSmsRequest(phone, templateCode, templateParam);
            CommonResponse response = getInstance().client.getCommonResponse(request);
            String responseData = response.getData();
            log.debug("=======发送短信结果: " + responseData);
            return responseData;
        } catch (Exception e) {
            log.error("======发送短信失败: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取发短信的请求头
     *
     * @param phone
     * @param templateCode
     * @param templateParam
     * @return
     */
    private static CommonRequest getSendSmsRequest(String phone, String templateCode, String templateParam) {
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
        return request;
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
        String templateCode = "SMS_195861938";
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
        String templateCode = "SMS_205616451";
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
        String templateCode = "SMS_195871902";
        String templateParam = getTemplateParam(validCode);
        return sendSms(phone, templateCode, templateParam);
    }

    /**
     * 发送绑定帐号验证码
     *
     * @param phone     手机号
     * @param validCode 验证码
     * @return
     */
    public static String sendBindCountSms(String phone, String validCode) {
        String templateCode = "SMS_199195727";
        String templateParam = getTemplateParam(validCode);
        return sendSms(phone, templateCode, templateParam);
    }

    /**
     * 发送店铺添加员工短信
     *
     * @param phone    手机号
     * @param shopName 店铺名称
     * @param password 密码
     * @return
     */
    public static String sendShopAddUserSms(String phone, String shopName, String password) {
        String templateCode = "SMS_195722646";
        StringBuilder sb = new StringBuilder();
        sb.append("{shopName:'");
        sb.append(shopName);
        sb.append("',password:'");
        sb.append(password);
        sb.append("',code:'h5/download.html'}");
        System.out.println(sb.toString());
        return sendSms(phone, templateCode, sb.toString());
    }

    /**
     * 发送一键删除短信
     *
     * @param phone 接收短信手机号
     * @param code  验证码
     * @return
     */
    public static String sendOnKeyDeleteSms(String phone, String code) {
        String templateCode = "SMS_205616453";
        String templateParam = getTemplateParam(code);
        return sendSms(phone, templateCode, templateParam);
    }

    /**
     * 发送注销店铺短信
     *
     * @param phone 接收短信手机号
     * @param code  验证码
     * @return
     */
    public static String sendDestroyShopSms(String phone, String code) {
        String templateCode = "SMS_205621448";
        String templateParam = getTemplateParam(code);
        return sendSms(phone, templateCode, templateParam);
    }

    /**
     * 发送成功注销店铺短信
     *
     * @param phone    接收短信手机号
     * @param shopName 店铺名称
     * @return
     */
    public static String sendDestroySuccessSms(String phone, String shopName) {
        String templateCode = "SMS_215352204";
        StringBuilder sb = new StringBuilder();
        sb.append("{shopName:'");
        sb.append(shopName);
        sb.append("'}");
        System.out.println(sb.toString());
        return sendSms(phone, templateCode, sb.toString());
    }

    /**
     * 返回短信完整内容
     *
     * @param smsCode
     * @param sendSmsType
     * @param hashMap
     * @return
     */
    public static String getSmsContent(String smsCode, String sendSmsType, Map<String, String> hashMap) {
        String smsContent = "【" + SIGN_NAME + "】";
        String shopName;
        switch (sendSmsType) {
            case "register":
                //账号注册
                smsContent += "您正在用此手机号注册账号，验证码为：" + smsCode + "，5分钟内有效！";
                break;
            case "resetPassword":
                //重置密码
                smsContent += "您的验证码为：" + smsCode + "，您正在进行密码重置操作，如非本人操作，请忽略本短信！";
                break;
            case "login":
                //登录验证码
                smsContent += "验证码为：" + smsCode + "，您正在登录，若非本人操作，请勿略该短信；5分钟内有效。";
                break;
            case "bindCount":
                //绑定微信
                smsContent += "您正在绑定微信帐号，验证码为：" + smsCode + "，如非本人操作，请忽略本短信！";
                break;
            case "shopRegisterUser":
                //店铺添加员工
                shopName = hashMap.get("shopName");
                String password = hashMap.get("newPassword");
                smsContent += "您已被添加为“%s”的员工，账号为您的手机号，初始登录密码为%s，快登录看看吧~APP下载地址：https://www.luxuryadmin.com/h5/download.html";
                smsContent = String.format(smsContent, shopName, password);
                break;
            case "oneKeyDelete":
                //一键删除
                smsContent += "您的店铺正在执行一键删除操作，验证码为：" + smsCode + "，若非本人操作，请忽略，5分钟内有效！";
                break;
            case "destroyShop":
                //店铺注销
                smsContent += "您的店铺正在被注销，验证码为：" + smsCode + "，若非本人操作，请忽略，5分钟内有效！";
                break;
            case "destroySuccess":
                //店铺注销
                shopName = hashMap.get("shopName");
                smsContent += "您的奢当家店铺“" + shopName + "”已成功注销，所有关于该店铺的数据已被销毁。";
                break;


            default:
                smsContent = "";
                break;
        }
        return smsContent;
    }

    public static void main(String[] args) {
        String smsContent = "您已被添加为“%s”的员工，账号为您的手机号，初始登录密码为%s，快登录看看吧~APP下载地址：https://www.luxuryadmin.com/download.html";
        smsContent = String.format(smsContent, "奢当家店铺", "123456");
        System.out.println(smsContent);
    }

}