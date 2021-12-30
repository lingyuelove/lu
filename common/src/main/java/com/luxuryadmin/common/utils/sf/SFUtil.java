package com.luxuryadmin.common.utils.sf;

import java.io.UnsupportedEncodingException;
import java.util.*;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.JsonArray;

import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.sf.csim.express.service.CallExpressServiceTools;
import com.sf.csim.express.service.EspServiceCode;
import com.sf.csim.express.service.HttpClientUtil;
import lombok.Data;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.Contended;

/**
 * @PackgeName: com.luxuryadmin.common.utils.sf
 * @ClassName: SFUtil
 * @Author: ZhangSai
 * Date: 2021/10/11 14:39
 */

public class SFUtil {

    //您在丰桥自建API应用：路由推送接口的顾客编码为：ZSdc9KUm1，校验码为：oFnUQHlKS0yaZoFg3CKH20VApXR2M2Mx，请妥善保管您的顾客编码和校验码。
    //您在丰桥自建API应用：路由推送接口的顾客编码为：SDJIEX2huu，校验码为：mYQgtN21oP2bmqEAdEBjxzosrN4ektcf，公司账号路由查询接口。
    //丰桥新沙箱测试顾客编码  ZSvhN6spY(校验顺丰单号是否正确)
    //校验码                          t8PvDg9v7gF1XgenXWrhAqWLOsKO7YA6
    private static final String CLIENT_CODE = "SDJIEX2huu";
    //此处替换为您在丰桥平台获取的校验码
    private static final String CHECK_WORD = "mYQgtN21oP2bmqEAdEBjxzosrN4ektcf";

    //沙箱环境的地址
    private static final String CALL_URL_BOX = "https://sfapi-sbox.sf-express.com/std/service";
    //生产环境的地址
    private static final String CALL_URL_PROD = "https://sfapi.sf-express.com/std/service";

    /**
     * 根据物流单号查询该单号是否存在
     * @param waybillNo
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String inspectOrderId(String waybillNo) throws UnsupportedEncodingException {
        //丰桥新沙箱测试顾客编码  ZSvhN6spY(校验顺丰单号是否正确)
//        String CLIENT_CODE = "ZSvhN6spY";
//        String CHECK_WORD = "t8PvDg9v7gF1XgenXWrhAqWLOsKO7YA6";
        Map<String, String> params = new HashMap();
        // 顾客编码 ，对应丰桥上获取的clientCode
        params.put("partnerID", CLIENT_CODE);
        params.put("requestID", UUID.randomUUID().toString().replace("-", ""));
        // 接口服务码
        params.put("serviceCode", "EXP_RECE_VALIDATE_WAYBILLNO");
        String timeStamp = LocalUtils.getTimestamp();
        params.put("timestamp", timeStamp);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("waybillNo", waybillNo);
        String msgData = jsonObject.toString();
        params.put("msgData", msgData);
        //数据签名
        params.put("msgDigest", CallExpressServiceTools.getMsgDigest(msgData, timeStamp, CHECK_WORD));
        String result = HttpClientUtil.post(CALL_URL_PROD, params);
        com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(result);

        String apiResultData = json.getString("apiResultData");
        com.alibaba.fastjson.JSONObject jsonResultData = com.alibaba.fastjson.JSONObject.parseObject(apiResultData);
        String errorCode = jsonResultData.getString("errorCode");
        if (!"S0000".equals(errorCode)){
            throw new MyException(jsonResultData.getString("errorMsg"));
        }
        String msgDataResult = jsonResultData.getString("msgData");
        System.out.println("===调用地址 ===" + CALL_URL_PROD);
        System.out.println("===调用地址 ===" + result);
        return msgDataResult;
    }
    public static List<LogisticsRouteResp> listShow(List<String> waybillNo,String checkPhoneNo) throws UnsupportedEncodingException {
        //丰桥新沙箱测试顾客编码  ZSvhN6spY(校验顺丰单号是否正确)
        EspServiceCode testService = EspServiceCode.EXP_RECE_SEARCH_ROUTES;
//        String CLIENT_CODE = "ZSdc9KUm1";
//        String CHECK_WORD = "oFnUQHlKS0yaZoFg3CKH20VApXR2M2Mx";
        Map<String, String> params = new HashMap();
        // 顾客编码 ，对应丰桥上获取的clientCode
        params.put("partnerID", CLIENT_CODE);
        params.put("requestID", UUID.randomUUID().toString().replace("-", ""));
        // 接口服务码
        params.put("serviceCode", testService.getCode());
        String timeStamp = LocalUtils.getTimestamp();
        params.put("timestamp", timeStamp);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("language", "0");
        jsonObject.put("trackingType", "1");
        jsonObject.put("trackingNumber", waybillNo);
        jsonObject.put("methodType", "1");
        jsonObject.put("checkPhoneNo", checkPhoneNo);
        String msgData = jsonObject.toString();
        params.put("msgData", msgData);
        //数据签名
        params.put("msgDigest", CallExpressServiceTools.getMsgDigest(msgData, timeStamp, CHECK_WORD));
        String result = HttpClientUtil.post(CALL_URL_PROD, params);
        com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(result);
        String apiResultData = json.getString("apiResultData");
        com.alibaba.fastjson.JSONObject jsonResultData = com.alibaba.fastjson.JSONObject.parseObject(apiResultData);
        String errorCode = jsonResultData.getString("errorCode");
        if (!"S0000".equals(errorCode)){
            throw new MyException(jsonResultData.getString("errorMsg"));
        }
        String msgDataResult = jsonResultData.getString("msgData");
        com.alibaba.fastjson.JSONObject routeRespsData = com.alibaba.fastjson.JSONObject.parseObject(msgDataResult);
        String routeResps = routeRespsData.getString("routeResps");
        System.out.println("===调用地址 ===" + CALL_URL_PROD);
        System.out.println("===调用地址 ===" + result);
        System.out.println("===调用地址 ===" + msgDataResult);
//        JSONArray objects = JSON.parseArray(msgDataResult);
        List<LogisticsRouteResp> logisticsRouteResps=JSON.parseArray(routeResps,LogisticsRouteResp.class);
//        List < LogisticsRouteResp > list =  objects.toJavaList(LogisticsRouteResp.class);
        System.out.println("===调用地址 ===" + logisticsRouteResps);
        if (LocalUtils.isEmptyAndNull(logisticsRouteResps)){
            return logisticsRouteResps;
        }
        logisticsRouteResps.forEach(logisticsRouteResp -> {
            showState(logisticsRouteResp.getRoutes());
        });
        return logisticsRouteResps;
    }

    public static void showState(List<LogisticsRoute> routes){
        if (LocalUtils.isEmptyAndNull(routes)){
            return;
        }
        routes.forEach(logisticsRoute -> {
            getOpCodeCname(logisticsRoute);
        });
    }

    public static LogisticsRoute getOpCodeCname(LogisticsRoute logisticsRoute){
        String opCodeCname =null;
        String opCode =null;
        String sfCode = logisticsRoute.getOpCode();
        switch (logisticsRoute.getOpCode()) {
            case "130":
            case "123":
            case "607":
            case "50":
            case "54":
                opCodeCname = "已揽件";
                opCode="20";
                break;
            case "33":
            case "70":
                opCodeCname = "派件异常";
                opCode="41";
                break;
            case "30":
            case "31":
            case "3036":
            case "36":
            case "105":
            case "106":
                opCodeCname = "运输中";
                opCode="30";
                break;
            case "44":
            case "204":
                opCodeCname = "派件中";
                opCode="40";
                break;
            case "80":
            case "8000":
                opCodeCname = "已签收";
                opCode="50";
                break;
            case "99":
            case "648":
                opCodeCname = "已退回/转寄";
                opCode="60";
                break;
            default:
                opCodeCname = "物流异常";
                opCode="1";
                break;
        }
        logisticsRoute.setOpCodeCname(opCodeCname);
        logisticsRoute.setSfCode(sfCode);
        logisticsRoute.setOpCode(opCode);
        return logisticsRoute;
    }

    public static String getOpCodeCname(String logisticsState){
        String opCodeCname;
        switch (logisticsState) {
            case "2":
                opCodeCname = "暂无物流信息";
                break;
            case "10":
                opCodeCname = "已发货";
                break;
            case "20":
                opCodeCname = "已揽件";
                break;
            case "30":
                opCodeCname = "运输中";
                break;
            case "40":
                opCodeCname = "派件中";
                break;
            case "41":
                opCodeCname = "派件异常";
                break;
            case "50":
                opCodeCname = "已签收";
                break;
            case "60":
                opCodeCname = "已退回/转寄";
                break;
            default:
                opCodeCname = "暂无物流信息";
                break;
        }
        return opCodeCname;
    }
    public static void main(String[] args) throws UnsupportedEncodingException {
        EspServiceCode testService = EspServiceCode.EXP_RECE_CREATE_ORDER; //下订单
        //	EspServiceCode testService = EspServiceCode.EXP_RECE_SEARCH_ORDER_RESP; //订单结果查询
        //  EspServiceCode testService = EspServiceCode.EXP_RECE_UPDATE_ORDER;//订单取消
        //	EspServiceCode testService = EspServiceCode.EXP_RECE_FILTER_ORDER_BSP;//订单筛选
        //   EspServiceCode testService = EspServiceCode.EXP_RECE_SEARCH_ROUTES;// 路由查询
        //	EspServiceCode testService = EspServiceCode.EXP_RECE_GET_SUB_MAILNO;//子单号申请
        //	EspServiceCode testService = EspServiceCode.EXP_RECE_QUERY_SFWAYBILL;//清单运费查询
        //  EspServiceCode testService = EspServiceCode.EXP_RECE_REGISTER_ROUTE;//路由注册
        //	EspServiceCode testService = EspServiceCode.EXP_RECE_CREATE_REVERSE_ORDER;//仓配退货下单
        //	EspServiceCode testService = EspServiceCode.EXP_RECE_CANCEL_REVERSE_ORDER;//仓配退货消单
        //	EspServiceCode testService = EspServiceCode.EXP_RECE_WANTED_INTERCEPT;//截单转寄退回
        //	EspServiceCode testService = EspServiceCode.EXP_RECE_QUERY_DELIVERTM;//时效标准及价格查询
        //	EspServiceCode testService = EspServiceCode.COM_RECE_CLOUD_PRINT_WAYBILLS;//云打印面单接口
        // 	EspServiceCode testService = EspServiceCode.EXP_RECE_SEARCH_PROMITM;//预计派送时间查询
        //	EspServiceCode testService = EspServiceCode.EXP_EXCE_CHECK_PICKUP_TIME;//揽件服务时间查询

        CallExpressServiceTools client = CallExpressServiceTools.getInstance();

        // set common header
        Map<String, String> params = new HashMap<String, String>();

        String timeStamp = String.valueOf(System.currentTimeMillis());
        String msgData = CallExpressServiceTools.packageMsgData(testService);

        params.put("partnerID", CLIENT_CODE);  // 顾客编码 ，对应丰桥上获取的clientCode
        params.put("requestID", UUID.randomUUID().toString().replace("-", ""));
        params.put("serviceCode", testService.getCode());// 接口服务码
        params.put("timestamp", timeStamp);
        params.put("msgData", msgData);
        params.put("msgDigest", CallExpressServiceTools.getMsgDigest(msgData, timeStamp, CHECK_WORD));

        // System.out.println(params.get("requestID"));
        long startTime = System.currentTimeMillis();

        System.out.println("====调用请求：" + params.get("msgData"));
//        String result = HttpClientUtil.post(CALL_URL_BOX, params);

        System.out.println("====调用丰桥的接口服务代码：" + String.valueOf(testService.getCode()) + " 接口耗时：" + String.valueOf(System.currentTimeMillis() - startTime) + "====");
        System.out.println("===调用地址 ===" + CALL_URL_BOX);
        System.out.println("===顾客编码 ===" + CLIENT_CODE);
//        System.out.println("===返回结果：" + result); SF1332945477168 SF1332945477168

        System.out.println("===返回结果1111：" + inspectOrderId("SF1332945477168"));
        List<String> waybillNo =new ArrayList<>();
        waybillNo.add("SF1332945477618");
        waybillNo.add("SF1131827006069");
//        waybillNo.add("SF1131827006078");
        String checkPhoneNo = "0727,6444";
        System.out.println("===返回结果1111：" + listShow(waybillNo,checkPhoneNo));
    }
}
