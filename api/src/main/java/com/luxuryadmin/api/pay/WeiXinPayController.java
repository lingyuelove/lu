package com.luxuryadmin.api.pay;

import com.alibaba.fastjson.JSONObject;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.aliyun.AliHttpUtils;
import com.luxuryadmin.entity.pay.PayOrder;
import com.luxuryadmin.entity.shp.ShpShop;
import com.luxuryadmin.param.pay.ParamQueryWxOrder;
import com.luxuryadmin.param.pay.ParamWxCreateOrder;
import com.luxuryadmin.service.pay.AesUtilWeiXin;
import com.luxuryadmin.service.pay.PayOrderService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.vo.pay.VoPayOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付<br/>
 *
 * <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_3_1.shtml">微信支付开发文档 </a>
 *
 * @author qwy
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/wxPay")
@Api(tags = {"H001.【支付】模块"}, description = "/shop/wxPay |用户支付模块,非APP内支付,不需要登录")
public class WeiXinPayController extends BaseController {


    public static final String ENCODING = "UTF-8";

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private ShpShopService shpShopService;

    /**
     * 微信h5支付;
     *
     * @return Result
     */
    @ApiOperation(
            value = "微信h5支付",
            notes = "微信h5支付",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestMapping("/h5/callbackPayH5")
    public String callbackPayH5(@RequestBody String reqJson) {
        log.info("====接收到微信支付的回调通知========");
        HashMap<String, String> map = new HashMap<>(16);
        map.put("code", "FAIL");
        map.put("message", "失败");
        JSONObject jsonObject = JSONObject.parseObject(reqJson);
        log.debug("params: " + jsonObject);
        String eventType = jsonObject.getString("event_type");

        try {
            //回调成功
            if ("TRANSACTION.SUCCESS".equals(eventType)) {
                //后面要把此APIv3的密钥放进数据库
                String apiVeKey = "43f71e0c8a08459197427588c425125d";
                String resource = jsonObject.getString("resource");
                JSONObject resJson = JSONObject.parseObject(resource);
                String associatedData = resJson.getString("associated_data");
                String nonce = resJson.getString("nonce");
                String cipherText = resJson.getString("ciphertext");
                AesUtilWeiXin aesUtilWeiXin = new AesUtilWeiXin(apiVeKey.getBytes());
                String cipherTextJson = aesUtilWeiXin.decryptToString(associatedData.getBytes(), nonce.getBytes(), cipherText);
                //解密后的数据和查询订单一个格式
                JSONObject dataJson = JSONObject.parseObject(cipherTextJson);
                String orderNo = dataJson.getString("out_trade_no");
                String attach = dataJson.getString("attach");
                JSONObject attachJson = JSONObject.parseObject(attach);
                String userId = attachJson.getString("userId");
                String shopId = attachJson.getString("shopId");
                String payFor = attachJson.getString("payFor");
                PayOrder payOrder = payOrderService.getPayOrderByOrderNo(Integer.parseInt(userId), orderNo);
                if (null != payOrder) {
                    if (LocalUtils.isBetween(payOrder.getState(), 10, 39)) {
                        //封装数据
                        packSuccessPayOder(dataJson, payOrder);
                    }

                    //之后成功之后的业务逻辑;续费会员;

                    //目前续费一年起
                    if ("vip".equals(payFor)) {
                        int month = 12;
                        Date nowDate = new Date();
                        //1.找到店铺.设置成为会员,增加到期时间;
                        ShpShop shop = shpShopService.getShpShopById(shopId);
                        if (null != shop) {
                            shop.setIsMember("yes");
                            //0:非会员; 1:体验会员;2:正式会员;3:靓号会员
                            shop.setMemberState(2);
                            shop.setUpdateTime(nowDate);
                            //付费月数:  1:月付; 3:季付; 6:半年付; 12:年付; 36:三年付;
                            shop.setPayMonth(month);
                            //累计付费月数
                            shop.setTotalMonth(shop.getTotalMonth() + month);
                            //付费使用开始时间
                            shop.setPayStartTime(nowDate);

                            //付费使用结束时间
                            if (LocalUtils.isEmptyAndNull(shop.getPayEndTime())) {
                                //付费时,把体验时间续上;
                                Date tryEndTime = shop.getTryEndTime();
                                if (tryEndTime == null) {
                                    tryEndTime = nowDate;
                                }
                                if (nowDate.after(tryEndTime)) {
                                    tryEndTime = nowDate;
                                }
                                shop.setPayEndTime(tryEndTime);
                            }
                            Date newPayEndTime = DateUtil.addMonthsFromOldDate(shop.getPayEndTime(), month).getTime();
                            shop.setPayEndTime(newPayEndTime);
                            Date newPayEndTimeOld = null == shop.getPayEndTimeOld() ? newPayEndTime :
                                    DateUtil.addMonthsFromOldDate(shop.getPayEndTimeOld(), month).getTime();
                            shop.setPayEndTimeOld(newPayEndTimeOld);
                            shpShopService.updateShpShop(shop);
                            //更新redis的店铺信息,如:是否为会员,会员到期时间;
                            int myShopId = Integer.parseInt(shopId);

                            //是否会员; no:非会员 | yes:会员
                            String isMemberKey = ConstantRedisKey.getShopMemberRedisKeyByShopId(myShopId);
                            redisUtil.set(isMemberKey, shop.getIsMember());

                            //会员状态: 0:非会员(会员已过期); 1:体验会员;2:正式会员;3:靓号会员
                            String memberStateKey = ConstantRedisKey.getMemberStateRedisKeyByShopId(myShopId);
                            redisUtil.set(memberStateKey, "2");

                            //会员过期时间
                            String vipExpireRedisKey = ConstantRedisKey.getVipExpireRedisKeyByShopId(myShopId);
                            redisUtil.set(vipExpireRedisKey, DateUtil.formatShort(shop.getPayEndTime()));

                            //回调成功,需要清除防重复提交的key
                            String payKey = "_pay_" + shopId + "_" + userId;
                            redisUtil.delete(payKey);

                            map.put("code", "SUCCESS");
                            map.put("message", "成功");
                        }
                    }

                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return JSONObject.toJSON(map).toString();
    }


    /**
     * 微信h5支付;<br/>
     * 创建待支付订单;调用微信支付的下单接口https://api.mch.weixin.qq.com/v3/pay/transactions/h5
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "微信h5支付",
            notes = "微信h5支付",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @PostMapping("/h5/createPayOrder")
    public BaseResult createPayOrder(@RequestParam Map<String, String> params,
                                     @Valid ParamWxCreateOrder wxOrder, BindingResult result) {
        servicesUtil.validControllerParam(result);
        isLogin();
        int shopId = getShopId();
        int userId = getUserId();
        //随机字符串,防重复提交;
        String randomStr = wxOrder.getRandomStr();
        String payKey = "_pay_" + shopId + "_" + userId;
        String payValue = redisUtil.get(payKey);

        //随机字符串不为空且和上次提交的一致
        if (!LocalUtils.isEmptyAndNull(payValue) && payValue.equals(randomStr)) {
            return BaseResult.defaultErrorWithMsg("页面已失效,请重新刷新页面!");
        }
        redisUtil.setExMINUTES(payKey, randomStr, 5);

        try {
            String sign = wxOrder.getSign();
            String decodeSign = DESEncrypt.decodeUsername(sign);
            JSONObject basicParam = (JSONObject) JSONObject.parse(decodeSign);

            //设备平台: ios, android, pc
            String platform = basicParam.getString("platform");
            //付费详情
            String description = basicParam.getString("description");
            //金额(元)换算单位为(分)
            String totalStr = basicParam.getString("total");
            long total = LocalUtils.calcNumber(totalStr, "*", "100").longValue();

            String payChannel = "weixin";
            //创建订单;
            PayOrder noPayOrder = payOrderService.createNoPayOrder(shopId, userId, description,
                    "vip", total, 0L, total, payChannel,
                    platform, "h5支付", "0");
            //订单号
            String orderNo = noPayOrder.getOrderNo();

            HashMap<String, String> attachMap = new HashMap<>(16);
            attachMap.put("userId", userId + "");
            attachMap.put("shopId", shopId + "");
            attachMap.put("payFor", "vip");

            HashMap<String, Object> map = new HashMap<>(16);
            map.put("appid", "wx2fb86b0461f39f70");
            map.put("mchid", "1596587771");
            map.put("description", description);
            map.put("out_trade_no", orderNo);
            Date nowDate = new Date();
            String dateTimeTimezone = DateUtil.formatTimezone(nowDate);
            map.put("time_expire", dateTimeTimezone);
            map.put("attach", JSONObject.toJSON(attachMap).toString());
            //开发环境和测试环境
            boolean isPro = ConstantCommon.PRO.equalsIgnoreCase(ConstantCommon.springProfilesActive);
            if (isPro) {
                //正是环境
                map.put("notify_url", "https://m.luxuryadmin.com/shop/wxPay/h5/callbackPayH5");
            } else {
                //非正式环境
                map.put("notify_url", "https://test-m.luxuryadmin.com/shop/wxPay/h5/callbackPayH5");
            }
            //订单金额信息
            HashMap<String, Object> amountObj = new HashMap<>(16);
            amountObj.put("total", total);
            amountObj.put("currency", "CNY");
            map.put("amount", amountObj);

            //支付场景描述
            HashMap<String, Object> sceneInfoObj = new HashMap<>(16);
            sceneInfoObj.put("payer_client_ip", getIpAddr());
            //h5场景信息
            HashMap<String, Object> h5InfoObj = new HashMap<>(16);
            h5InfoObj.put("type", platform);
            sceneInfoObj.put("h5_info", h5InfoObj);
            map.put("scene_info", sceneInfoObj);

            String json = JSONObject.toJSONString(map);
            log.info("******: " + json);

            //请求支付,h5统一下单接口;
            String url = "https://api.mch.weixin.qq.com/v3/pay/transactions/h5";
            String str = weixinHttp("POST", url, json);
            log.debug("post: " + str);
            if (!LocalUtils.isEmptyAndNull(str)) {
                JSONObject jsonObject = JSONObject.parseObject(str);
                Object h5Url = jsonObject.get("h5_url");
                //返回前端的交易参数
                Map<String, Object> resultMap = new HashMap<>(16);
                resultMap.put("h5Url", h5Url);
                resultMap.put("orderNo", orderNo);
                resultMap.put("createTime", DateUtil.format(nowDate));
                resultMap.put("payWay", payChannel);
                resultMap.put("money", totalStr);
                return BaseResult.okResult(resultMap);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        redisUtil.delete(payKey);
        return BaseResult.defaultErrorWithMsg("创建订单失败!");
    }

    /**
     * 微信支付--查询订单<br/>
     * https://api.mch.weixin.qq.com/v3/pay/transactions/id/{transaction_id}
     *
     * @return Result
     */
    @ApiOperation(
            value = "查询订单",
            notes = "查询订单",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @GetMapping("/h5/queryOrder")
    public BaseResult queryOrder(@Valid ParamQueryWxOrder queryWxOrder, BindingResult result) {
        servicesUtil.validControllerParam(result);
        isLogin();
        int userId = getUserId();
        int shopId = getShopId();
        String msg = "支付失败";
        try {
            String orderNo = queryWxOrder.getOrderNo();
            PayOrder payOrder = payOrderService.getPayOrderByOrderNo(userId, orderNo);
            if (null == payOrder) {
                return BaseResult.defaultErrorWithMsg("订单不存在!");
            }
            Integer state = payOrder.getState();
            //回调已经更新了支付订单,且支付成功,则不需要再去第三方查询;
            VoPayOrder voPayOrder = new VoPayOrder();
            String goodsName = payOrder.getGoodsName();
            String payChannel = payOrder.getPayChannel();
            String realMoney = LocalUtils.calcNumber(payOrder.getRealMoney(), "/", 100).toString();
            voPayOrder.setGoodsName(goodsName);
            voPayOrder.setOrderNo(orderNo);
            voPayOrder.setPayChannel(payChannel);
            voPayOrder.setRealMoney(realMoney);
            if (state >= 40) {
                voPayOrder.setTransactionId(payOrder.getTransactionId());
                voPayOrder.setFinishTime(DateUtil.format(payOrder.getFinishTime()));
                return BaseResult.okResult(voPayOrder);
            }
            //请求支付,h5统一下单接口;
            String url = "https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/" + orderNo;
            url += "?mchid=1596587771";
            String str = weixinHttp("GET", url, null);
            log.debug("=====str: " + str);
            if (!LocalUtils.isEmptyAndNull(str)) {
                JSONObject jsonObject = JSONObject.parseObject(str);
                //封装数据
                packSuccessPayOder(jsonObject, payOrder);
                //返回端上的信息
                voPayOrder.setTransactionId(payOrder.getTransactionId());
                voPayOrder.setFinishTime(DateUtil.format(payOrder.getFinishTime()));
                //清除支付的redisKey
                String payKey = "_pay_" + shopId + "_" + userId;
                redisUtil.delete(payKey);
                return BaseResult.okResult(voPayOrder);
            }
        } catch (Exception e) {
            msg = "支付异常: " + e.getMessage();
            log.error(e.getMessage(), e);
        }
        return BaseResult.defaultErrorWithMsg(msg);

    }

    /**
     * 微信回调或者查询订单时,返回的json格式进行解析和保存
     *
     * @param callBackJson 查询订单返回的json格式 | 解析微信回调通知的resource的json格式
     * @param payOrder
     * @throws Exception
     */
    private void packSuccessPayOder(JSONObject callBackJson, PayOrder payOrder) throws Exception {
        String tradeState = callBackJson.getString("trade_state");
        String msg = callBackJson.getString("trade_state_desc");
        //判断支付结果
        if ("success".equals(tradeState.toLowerCase())) {
            String amountJson = callBackJson.getString("amount");
            int total = Integer.parseInt(JSONObject.parseObject(amountJson).get("total").toString());
            //判断支付金额和微信返回金额是否一致
            Long realMoney = payOrder.getRealMoney();
            if (realMoney != total) {
                log.error("=====支付金额前后不一致,平台金额: {},微信金额: {}", realMoney, total);
                throw new MyException("====支付金额异常===");
            }
            //支付成功
            String successTime = callBackJson.getString("success_time");
            String transactionId = callBackJson.getString("transaction_id");
            String payer = callBackJson.getString("payer");
            String openId = JSONObject.parseObject(payer).getString("openid");
            Date finishTime = DateUtil.parseTimezone(successTime);
            //更新数据库订单信息
            payOrder.setTransactionId(transactionId);
            payOrder.setState(40);
            payOrder.setOpenId(openId);
            payOrder.setPayTime(finishTime);
            payOrder.setFinishTime(finishTime);
            payOrder.setUpdateTime(new Date());
            payOrderService.saveOrUpdatePayOrder(payOrder);
        } else {
            msg = "支付失败: " + tradeState + " | " + msg;
            log.info(msg);
            throw new MyException(msg);
        }
    }


    private void isLogin() {
        int userId = getUserId();
        if (userId == 0) {
            throw new MyException("请返回APP进行登录!");
        }
        int shopId = getShopId();
        if (shopId == 0) {
            throw new MyException("请先注册店铺再进行付费!");
        }
    }


    /**
     * 获取私钥。
     *
     * @return 私钥对象
     */
    private PrivateKey getPrivateKey() throws IOException {
        InputStream in = WeiXinPayController.class.getClassLoader().getResourceAsStream("apiclient_key.pem");
        if (in == null) {
            throw new MyException("找不到私钥文件!");
        }
        //URL url = WeiXinPayController.class.getClassLoader().getResource("apiclient_key.pem");
        //File file = new File(url.getFile());
        //String filename = file.getPath();
        //log.info("====私钥文件路径: " + filename);
        // String content = new String(Files.readAllBytes(Paths.get(filename)), "utf-8");
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


    private String getToken(String method, HttpUrl url, String body) throws Exception {
        String nonceStr = LocalUtils.getUUID();
        long timestamp = System.currentTimeMillis() / 1000;
        String message = buildMessage(method, url, timestamp, nonceStr, body);
        log.debug("message: " + message);
        String signature = sign(message.getBytes(ENCODING));
        log.debug("messageLength: " + message.length());

        String yourMerchantId = "1596587771";
        String yourCertificateSerialNo = "235EF20B8E4605053686624041FF3FE2E043C736";
        return "mchid=\"" + yourMerchantId + "\","
                + "nonce_str=\"" + nonceStr + "\","
                + "timestamp=\"" + timestamp + "\","
                + "serial_no=\"" + yourCertificateSerialNo + "\","
                + "signature=\"" + signature + "\"";
    }

    private String sign(byte[] message) throws Exception {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(getPrivateKey());
        sign.update(message);
        return Base64.getEncoder().encodeToString(sign.sign());
    }

    private String buildMessage(String method, HttpUrl url, long timestamp, String nonceStr, String body) {
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
     * 功能说明：向指定URL发送POST方法的请求
     * 修改说明：
     *
     * @param method        请求方式
     * @param url           发送请求的URL
     * @param jsonData      请求参数，请求参数应该是Json格式字符串的形式。
     * @param encoding      设置响应信息的编码格式，如utf-8
     * @param authorization 授权
     * @param postmanToken  票证
     * @return URL所代表远程资源的响应结果
     * @throws IOException
     * @author zhenglibing
     * @date 2018年1月8日 上午10:54:55
     */
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
            log.error("======发送 POST 请求出现异常！", e);
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

    /**
     * 微信支付接口请求方法封装
     *
     * @param method
     * @param url
     * @param bodyJsonData
     * @return
     * @throws Exception
     */
    private String weixinHttp(String method, String url, String bodyJsonData) throws Exception {
        HttpUrl httpurl = HttpUrl.parse(url);
        String authorization = "WECHATPAY2-SHA256-RSA2048 " + getToken(method, httpurl, bodyJsonData);
        log.info("===authorization: " + authorization);

        if ("post".equals(method.toLowerCase())) {
            return sendPost(method, url, bodyJsonData, ENCODING, authorization, null);
        } else {
            return AliHttpUtils.doGet(url, authorization);
        }
    }
}
