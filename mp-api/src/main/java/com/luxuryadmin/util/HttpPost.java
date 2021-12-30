package com.luxuryadmin.util;


import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.controller.notviprot.MpNotVipProtController;
import okhttp3.HttpUrl;
import org.apache.poi.util.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class HttpPost {

    public static final String ENCODING = "UTF-8";

    public static String sendPost(String method, String url, String jsonData, String encoding, String authorization, String postmanToken) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection con = realUrl.openConnection();
            HttpURLConnection conn = (HttpURLConnection) con;
            // 设置通用的请求属性
            conn.setRequestMethod(method);
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("Authorization", authorization);
            conn.setRequestProperty("postman-token", postmanToken);

            // conn.setRequestProperty("Content-Length",
            // String.valueOf(param.length())); //设置长度
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(new OutputStreamWriter(
                    conn.getOutputStream(), encoding));
            // 发送请求参数
            // out.print(param);
            if (null != jsonData) {
                out.write(jsonData);
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            byte[] bresult = result.toString().getBytes();
            result = new StringBuilder(new String(bresult, encoding));
        } catch (Exception e) {
            throw new MyException("调用微信支付订单失败");
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }


    public static String sign(byte[] message) throws Exception {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(getPrivateKey());
        sign.update(message);
        return Base64.getEncoder().encodeToString(sign.sign());
    }

    public static String buildMessage(String method, HttpUrl url, long timestamp, String nonceStr, String body) {
        String canonicalUrl = url.encodedPath();
        if (url.encodedQuery() != null) {
            canonicalUrl += "?" + url.encodedQuery();
        }
        String str = method + "\n"
                + canonicalUrl + "\n"
                + timestamp + "\n"
                + nonceStr + "\n";
        if ("get".equals(method.toLowerCase())) {
            str += "\n";
        } else {
            str += body + "\n";
        }
        return str;

    }

    /**
     * 获取私钥。
     *
     * @return 私钥对象
     */
    public static PrivateKey getPrivateKey() throws IOException {
        InputStream in = MpNotVipProtController.class.getClassLoader().getResourceAsStream("apiclient_key.pem");
        if (in == null) {
            throw new MyException("找不到私钥文件!");
        }
        String content = new String(IOUtils.toByteArray(in), ENCODING);
        try {
            String privateKey = content.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持RSA", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("无效的密钥格式");
        }
    }

}
