package com.luxuryadmin.common.utils.weixxin.mpsdk;


import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;


import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.codec.binary.Base64;

import org.springframework.web.client.RestTemplate;


public class TestImageBinary {


    public static String getQrCodeImgUrl(String scene,
                                  String page,
                                  Boolean is_hyaline,
                                  Boolean auto_color) {
        RestTemplate restTemplate = new RestTemplate();


        //然后调用微信官方api生成二维码
        String createQrCodeUrl = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" +  WxService.getAccessTokenApplets();
        //此处我是使用的阿里巴巴的fastJson
        JSONObject createQrParam = new JSONObject();
        createQrParam.put("scene", scene);
        createQrParam.put("page", page);
        createQrParam.put("is_hyaline", is_hyaline);
        createQrParam.put("auto_color", auto_color);

        PrintWriter out = null;
        InputStream in = null;
        String base64Code = null;
        try {
            URL realUrl = new URL(createQrCodeUrl);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数,利用connection的输出流，去写数据到connection中，我的参数数据流出我的电脑内存到connection中，让connection把参数帮我传到URL中去请求。
            out.print(createQrParam);
            // flush输出流的缓冲
            out.flush();
            //获取URL的connection中的输入流，这个输入流就是我请求的url返回的数据,返回的数据在这个输入流中，流入我内存，我将从此流中读取数据。
            in = conn.getInputStream();
            //定义个空字节数组
            byte[] data = null;
            // 读取图片字节数组
            try {
                //创建字节数组输出流作为中转仓库，等待被写入数据
                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                byte[] buff = new byte[100];
                int rc = 0;
                while ((rc = in.read(buff, 0, 100)) > 0) {
                    //向中转的输出流循环写出输入流中的数据
                    swapStream.write(buff, 0, rc);
                }
                //此时connection的输入流返回给我们的请求结果数据已经被完全地写入了我们定义的中转输出流swapStream中
                data = swapStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
             base64Code = new String(Objects.requireNonNull(Base64.encodeBase64(data)));
            //Base64转byte[]数组
//            System.out.println(base64Code);
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
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
        return base64Code;
    }

}
