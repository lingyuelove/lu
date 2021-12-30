package com.luxuryadmin.common.utils.aliyun;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 获取手机归属地;调用阿里云接口
 *
 * @author monkey king
 */
@Slf4j
public class CellphoneRegisterLocationUtil {

    /**
     * 读取返回结果
     *
     * @param is
     * @return
     * @throws IOException
     */
    private static String read(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = br.readLine()) != null) {
            line = new String(line.getBytes(StandardCharsets.UTF_8));
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    public static String getCellPhoneRegisterLocation(String cellphone) {
        String json = null;
        try {
            long st = System.currentTimeMillis();
            String host = "http://plocn.market.alicloudapi.com";
            String path = "/plocn";
            String appcode = "275307adb0514888b9a60b0ccc62fe98";
            String no = cellphone;
            String type = "zto";
            String urlSend = host + path + "?n=" + no + "&type=" + type;
            /*【1】 ~ 【6】 需要修改为对应的 可以参考商品详情 */
            URL url = new URL(urlSend);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //格式Authorization:APPCODE (中间是英文空格)
            httpURLConnection.setRequestProperty("Authorization", "APPCODE " + appcode);
            //获取服务器响应状态码 200 正常；400 权限错误 ； 403 次数用完；
            int httpCode = httpURLConnection.getResponseCode();
            //获取返回的json
            json = read(httpURLConnection.getInputStream());
            System.out.println(json);
            long et = System.currentTimeMillis();
            System.out.println(et - st);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("getCellPhoneRegisterLocation: 手机号: " + cellphone + " " + e.getMessage(), e + "; 返回结果: " + json);
        }
        return json;
    }

    public static void main(String[] args) {
        long st = System.currentTimeMillis();
        String json = getCellPhoneRegisterLocation("15112304365");
        long et = System.currentTimeMillis();
        System.out.println(et - st);

    }
}
