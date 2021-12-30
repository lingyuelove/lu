package com.luxuryadmin.controller.callback;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.config.WxPayProperties;
import com.luxuryadmin.entity.MpPayOrder;
import com.luxuryadmin.enums.EnumPayChannel;
import com.luxuryadmin.service.MpPayOrderService;
import com.luxuryadmin.service.MpUserInviteService;
import com.luxuryadmin.util.AesUtil;
import com.luxuryadmin.util.XMLUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/callback")
@Api(tags = "MP.回调接口", description = "回调接口 ")
public class WeiXinPayCallbackController extends BaseController {

    @Autowired
    private MpPayOrderService mpPayOrderService;

    @Autowired
    private MpUserInviteService mpUserInviteService;

    @Autowired
    private WxPayProperties wxPayProperties;

    @RequestMapping("/wxPayCallback")
    @Transactional(rollbackFor = Exception.class)
    public Object wxPayCallback(@RequestBody String body) throws IOException {
        Map<String, Object> returnMap = new HashMap<>();
        log.info("支付回调数据：" + body);
        JSONObject unJson = new JSONObject(body);
        //内层数据 resource
        JSONObject resource = unJson.getJSONObject("resource");
        String ciphertext = resource.getString("ciphertext");
        String nonce = resource.getString("nonce");
        String associatedData = resource.getString("associated_data");

        AesUtil aesUtil = new AesUtil(wxPayProperties.getKey().getBytes());
        String str;
        try {
            str = aesUtil.decryptToString(associatedData.getBytes(), nonce.getBytes(), ciphertext);
        } catch (GeneralSecurityException e) {
            returnMap.put("return_code", "FAIL");
            returnMap.put("return_msg", "密文解析失败" + unJson.getString("id"));
            String strXml = XMLUtil.mapToXml(returnMap);
            return strXml;
        }

        //内层解密之后数据
        JSONObject jsonDecode = new JSONObject(str);
        //订单编号
        String out_trade_no = jsonDecode.getString("out_trade_no");
        MpPayOrder mpPayOrder = mpPayOrderService.getOrderInfoByOrderNo(out_trade_no);
        if (mpPayOrder == null) {
            returnMap.put("return_code", "FAIL");
            returnMap.put("return_msg", "订单不存在,out_trade_no:" + out_trade_no);
            String strXml = XMLUtil.mapToXml(returnMap);
            return strXml;
        }
        Date date = new Date();
        if (jsonDecode.getString("trade_state").equals("SUCCESS")) {
            mpPayOrder.setTransactionId(jsonDecode.getString("transaction_id"));
            JSONObject amount = jsonDecode.getJSONObject("amount");
            Integer payer_total = amount.getInt("payer_total");
            mpPayOrder.setRealMoney(new BigDecimal(payer_total));
            mpPayOrder.setState(40);
            mpPayOrder.setPayChannel(EnumPayChannel.WEIXIN.getCode());
            mpPayOrder.setTradeType("h5支付");
            mpPayOrder.setUpdateTime(date);
            mpPayOrder.setPayTime(date);
            mpPayOrder.setFinishTime(date);
        } else {
            mpPayOrder.setState(20);
            mpPayOrder.setPayChannel(EnumPayChannel.WEIXIN.getCode());
            mpPayOrder.setUpdateTime(date);
        }
        //修改订单信息
        mpPayOrderService.updateOrderInfo(mpPayOrder);
        //插入邀请记录、奖励时长信息
        mpUserInviteService.updateUserInviteAndAddDayInfo(mpPayOrder.getFkMpUserId());
        returnMap.put("return_code", "SUCCESS");
        returnMap.put("return_msg", "OK");
        String strXml = XMLUtil.mapToXml(returnMap);
        return strXml;
    }

}
