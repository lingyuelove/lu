package com.luxuryadmin.common.utils.weixxin.mpsdk;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

/**
 * @PackgeName: com.luxuryadmin.common.utils.weixxin.mpsdk
 * @ClassName: HttpUtils
 * @Author: ZhangSai
 * Date: 2021/7/7 15:52
 */
@Slf4j
public class HttpUtils {
    /**
     * 发送https请求
     * @param requestUrl  请求地址
     * @param requestMethod  请求方式 get post
     * @param outputStr      提交的数据
     * @return JSONObject  (通过JSONobject.getKey("key")的方式获取JSON对象的属性值)
     */
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr){
        String jsonObject=null;
        try {
            //创建SSLContext对象 并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);

            //设置请求方式
            conn.setRequestMethod(requestMethod);

            //当outputStr 不为null时 向输出流写数据
            if(null!=outputStr){
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 读取返回数据
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new  InputStreamReader(inputStream,"utf-8");
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while((str=bufferedReader.readLine())!=null){
                buffer.append(str);
            }

            //释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream=null;
            conn.disconnect();
            jsonObject= buffer.toString();


        } catch (Exception e) {
            log.error("Http请求异常");
            e.printStackTrace();
        }

        return jsonObject;
    }
}
