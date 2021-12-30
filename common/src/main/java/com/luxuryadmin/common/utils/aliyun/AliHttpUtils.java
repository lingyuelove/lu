package com.luxuryadmin.common.utils.aliyun;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.luxuryadmin.common.utils.LocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.jni.Local;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * 此工具类是适用于阿里云服务----查询手机归属地--<br>
 * 如用作其它地方,请自行进行调试;
 *
 * @author monkey king
 * @date 2019-07-26 18:01:35
 */
@Slf4j
public class AliHttpUtils {

    /**
     * get
     *
     * @param host
     * @param path
     * @param headers
     * @param querys
     * @return
     * @throws Exception
     */
    public static HttpResponse doGet(String host, String path, Map<String, String> headers,
                                     Map<String, String> querys) throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpGet request = new HttpGet(buildUrl(host, path, querys));
        if (!LocalUtils.isEmptyAndNull(headers)) {
            for (Map.Entry<String, String> e : headers.entrySet()) {
                request.addHeader(e.getKey(), e.getValue());
            }
        }

        return httpClient.execute(request);
    }

    /**
     * post form
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @param bodys
     * @return
     * @throws Exception
     */
    public static HttpResponse doPost(String host, String path, String method, Map<String, String> headers,
                                      Map<String, String> querys, Map<String, String> bodys) throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (bodys != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();

            for (String key : bodys.keySet()) {
                nameValuePairList.add(new BasicNameValuePair(key, bodys.get(key)));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, "utf-8");
            formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            request.setEntity(formEntity);
        }

        return httpClient.execute(request);
    }

    /**
     * Post String
     *
     * @param host
     * @param path
     * @param headers
     * @param querys
     * @param body
     * @return
     * @throws Exception
     */
    public static HttpResponse doPost(String host, String path, Map<String, String> headers,
                                      Map<String, String> querys, String body) throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (!StringUtils.isEmpty(body)) {
            request.setEntity(new StringEntity(body, "utf-8"));
        }

        return httpClient.execute(request);
    }

    /**
     * Post stream
     *
     * @param host
     * @param path
     * @param headers
     * @param querys
     * @param body
     * @return
     * @throws Exception
     */
    public static HttpResponse doPost(String host, String path, Map<String, String> headers,
                                      Map<String, String> querys, byte[] body) throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (body != null) {
            request.setEntity(new ByteArrayEntity(body));
        }

        return httpClient.execute(request);
    }

    /**
     * Put String
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @param body
     * @return
     * @throws Exception
     */
    public static HttpResponse doPut(String host, String path, String method, Map<String, String> headers,
                                     Map<String, String> querys, String body) throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPut request = new HttpPut(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }
        if (!StringUtils.isEmpty(body)) {
            request.setEntity(new StringEntity(body, "utf-8"));
        }

        return httpClient.execute(request);
    }

    /**
     * Put stream
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @param body
     * @return
     * @throws Exception
     */
    public static HttpResponse doPut(String host, String path, String method, Map<String, String> headers,
                                     Map<String, String> querys, byte[] body) throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPut request = new HttpPut(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (body != null) {
            request.setEntity(new ByteArrayEntity(body));
        }

        return httpClient.execute(request);
    }

    /**
     * Delete
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @return
     * @throws Exception
     */
    public static HttpResponse doDelete(String host, String path, String method, Map<String, String> headers,
                                        Map<String, String> querys) throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpDelete request = new HttpDelete(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        return httpClient.execute(request);
    }

    private static String buildUrl(String host, String path, Map<String, String> querys)
            throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(host);
        if (!StringUtils.isEmpty(path)) {
            sbUrl.append(path);
        }
        if (null != querys) {
            StringBuilder sbQuery = new StringBuilder();
            for (Map.Entry<String, String> query : querys.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append("&");
                }
                if (StringUtils.isEmpty(query.getKey()) && !StringUtils.isEmpty(query.getValue())) {
                    sbQuery.append(query.getValue());
                }
                if (!StringUtils.isEmpty(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (!StringUtils.isEmpty(query.getValue())) {
                        sbQuery.append("=");
                        sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
                    }
                }
            }
            if (0 < sbQuery.length()) {
                sbUrl.append("?").append(sbQuery);
            }
        }

        return sbUrl.toString();
    }

    private static HttpClient wrapClient(String host) {
        HttpClient httpClient = new DefaultHttpClient();
        if (host.startsWith("https://")) {
            sslClient(httpClient);
        }

        return httpClient;
    }

    private static void sslClient(HttpClient httpClient) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] xcs, String str) {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] xcs, String str) {

                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry registry = ccm.getSchemeRegistry();
            registry.register(new Scheme("https", 443, ssf));
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String doGet(String urlSend, String authorization) {
        String json = null;
        try {
            long st = System.currentTimeMillis();
            /*【1】 ~ 【6】 需要修改为对应的 可以参考商品详情 */
            URL url = new URL(urlSend);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Authorization", authorization);
            //httpURLConnection.setRequestProperty("cookie", "Hm_lvt_6cdb2e7d7f6b6e4ba3c6faac6533ab9f=1620714087; PHPSESSID=bb50rft2nffpn6hfhmoc24i06t");
            //获取服务器响应状态码 200 正常；400 权限错误 ； 403 次数用完；
            int httpCode = httpURLConnection.getResponseCode();
            //获取返回的json
            json = read(httpURLConnection.getInputStream());
            log.debug("====doGet: {}", json);
            long et = System.currentTimeMillis();
            System.out.println(et - st);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return json;
    }


    public static String doGet(String urlSend, Map<String, String> headers) {
        String json = null;
        try {
            long st = System.currentTimeMillis();
            /*【1】 ~ 【6】 需要修改为对应的 可以参考商品详情 */
            URL url = new URL(urlSend);
            HttpURLConnection httpURLConnection =  (HttpURLConnection) url.openConnection();
            if (!LocalUtils.isEmptyAndNull(headers)) {
                Set<String> keySet = headers.keySet();
                for (String key : keySet) {
                    httpURLConnection.setRequestProperty(key, headers.get(key));
                }
            }
            int httpCode = httpURLConnection.getResponseCode();
            //获取返回的json
            json = read(httpURLConnection.getInputStream());
            log.debug("====doGet: {}", json);
            long et = System.currentTimeMillis();
            System.out.println(et - st);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return json;
    }



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


    public static void main(String[] args) throws Exception {
        // test2(null);
    }

    public static void test1() throws Exception {
        String host = "https://www.wsxcme.com";
        String path = "/service/account/user_phone_operation.jsp?act=phone_login&client_type=ios&token=(null)&version=2710&channel=international";
        Map<String, String> bodys = new HashMap<>(16);
        bodys.put("phone_number", "13567678880");
        bodys.put("password", "BAO336699");
        HttpResponse post = doPost(host, path, "POST", bodys, bodys, bodys);

        if (post.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String str = EntityUtils.toString(post.getEntity(), "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(str);
            Object errcode = jsonObject.get("errcode");
            Object errmsg = jsonObject.get("errmsg");
            if ("0".equals(errcode.toString())) {
                System.out.println("=====登录成功=====");
                String shop_id = jsonObject.get("shop_id").toString();
                String union_id = jsonObject.get("union_id").toString();
                String token = jsonObject.get("token").toString();
                System.out.println("======shop_id: " + shop_id);
                System.out.println("======union_id: " + union_id);
                System.out.println("======token: " + token);
            } else {
                System.out.println("=========" + errmsg + "======");
            }
        }
        System.out.println(post.getEntity());
    }


    public static void test2(String token, String shopId, String timeStamp) throws Exception {
        String host = "https://www.wsxcme.com";
        String path = "/service/album/get_album_themes_list.jsp?token=" + token + "&shop_id=" + shopId + "&act=single_album&time_stamp=" + timeStamp;
        Map<String, String> bodys = new HashMap<>(16);
        HttpResponse post = doPost(host, path, "POST", bodys, bodys, bodys);

        if (post.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String str = EntityUtils.toString(post.getEntity(), "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(str);
            Object errcode = jsonObject.get("errcode");
            Object errmsg = jsonObject.get("errmsg");
            if ("0".equals(errcode.toString())) {
                //结果集
                String result = jsonObject.get("result").toString();

                JSONObject resultJson = JSONObject.parseObject(result);
                Object goods_list = resultJson.get("goods_list");
                if (!LocalUtils.isEmptyAndNull(goods_list)) {
                    JSONArray jsonArray = JSONArray.parseArray(goods_list.toString());
                    if (LocalUtils.isEmptyAndNull(jsonArray)) {
                        System.out.println("====数据抓取完成======");
                        return;
                    }
                    JSONObject lastGoods = (JSONObject) jsonArray.get(jsonArray.size() - 1);
                    String lastTimestamp = lastGoods.get("time_stamp").toString();
                    System.out.println("=======lastTimestamp: " + lastTimestamp);
                    int i = 0;
                    for (Object obj : jsonArray) {
                        JSONObject objJSON = JSONObject.parseObject(obj.toString());
                        //商品描述
                        Object title = objJSON.get("title");
                        System.out.println((++i) + "=====商品描述: " + title);
                        Object imgs = objJSON.get("imgs");
                        if (!LocalUtils.isEmptyAndNull(imgs)) {
                            JSONArray imgList = JSONArray.parseArray(imgs.toString());
                            for (Object img : imgList) {
                                System.out.println("商品图片地址: " + img);
                            }
                        }
                    }
                    //隔3秒翻一页,防止被鉴定为机器请求
                    //Thread.sleep(3000);
                    //递归调用,加载所有商品
                    //test2(token, shopId, lastTimestamp);
                }
            } else {
                System.out.println("=========" + errmsg + "======");
            }
        }
    }
}
