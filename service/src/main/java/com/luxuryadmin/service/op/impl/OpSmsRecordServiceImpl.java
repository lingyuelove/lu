package com.luxuryadmin.service.op.impl;

import com.alibaba.fastjson.JSONObject;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.encrypt.PBKDF2Util;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ThreadUtils;
import com.luxuryadmin.common.utils.aliyun.SendSms;
import com.luxuryadmin.entity.op.OpSmsRecord;
import com.luxuryadmin.enums.login.EnumSendSmsType;
import com.luxuryadmin.enums.sys.EnumSaltType;
import com.luxuryadmin.mapper.op.OpSmsRecordMapper;
import com.luxuryadmin.service.op.OpSmsRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * @author monkey king
 * @date 2019-12-11 19:34:58
 */
@Slf4j
@Service
public class OpSmsRecordServiceImpl implements OpSmsRecordService {

    @Resource
    private OpSmsRecordMapper opSmsRecordMapper;


    @Override
    public OpSmsRecord createOpSmsRecord(int userId, EnumSaltType saltType, String salt) {
        return null;
    }

    @Override
    public int saveOrUpdateOpSmsRecord(OpSmsRecord opSmsRecord) {
        if (opSmsRecord.getId() != null) {
            opSmsRecordMapper.updateObject(opSmsRecord);
        } else {
            opSmsRecordMapper.saveObject(opSmsRecord);
        }
        return opSmsRecord.getId();
    }

    @Override
    public String generatorValidateCode() {
        String onlyNumber = PBKDF2Util.getSalt(6, "0123456789");
        return onlyNumber;
    }

    @Override
    public BaseResult sendValidateCode(String validateCode) {

        return BaseResult.defaultOkResultWithMsg("验证码发送成功");
    }


    /**
     * 发送短信验证码和带参数的短信<br/>
     * 发送短信,开发环境默认验证码为123456<br/>
     * 短信验证码的key为smsCode<br/>
     * 如:map.put("smsCode","123456")<br/>
     * 其它的值, 随自己自定义,但要和方法里面匹配
     *
     * @param phone
     * @param smsCode
     * @param sendSmsType
     * @param ip
     * @param paramMap    短信需要的多个参数; 用map进行存值,取值
     */
    @Override
    public void sendSms(String phone, String smsCode, EnumSendSmsType sendSmsType, String ip, Map<String, String> paramMap) {
        //发送验证码逻辑
        String smsContent = SendSms.getSmsContent(smsCode, sendSmsType.getCode(), paramMap);
        OpSmsRecord smsRecord = new OpSmsRecord(phone, smsContent, sendSmsType.getCode(), ip);
        saveOrUpdateOpSmsRecord(smsRecord);
        //非开发环境才发短信验证码
        boolean isDev = ConstantCommon.DEV.equals(ConstantCommon.springProfilesActive);
        if (!isDev) {
            //异步处理短信信息
            ThreadUtils.getInstance().executorService.execute(() -> {
                Date respTime;
                String resp = "";
                //发送真实短信验证码
                smsRecord.setSendTime(new Date());
                String shopName;
                switch (sendSmsType) {
                    case REGISTER:
                        resp = SendSms.sendRegisterSms(phone, smsCode);
                        break;
                    case LOGIN:
                        resp = SendSms.sendLoginSms(phone, smsCode);
                        break;
                    case RESET_PASSWORD:
                        resp = SendSms.sendResetPwdSms(phone, smsCode);
                        break;
                    case BIND_COUNT:
                        resp = SendSms.sendBindCountSms(phone, smsCode);
                        break;
                    case SHOP_REGISTER_USER:
                        //短信验证码
                        shopName = paramMap.get("shopName");
                        String username = paramMap.get("newUsername");
                        String pwd = paramMap.get("newPassword");
                        resp = SendSms.sendShopAddUserSms(username, shopName, pwd);
                        break;
                    case ONE_KEY_DELETE:
                        resp = SendSms.sendOnKeyDeleteSms(phone, smsCode);
                        break;
                    case DESTROY_SHOP:
                        resp = SendSms.sendDestroyShopSms(phone, smsCode);
                        break;
                    case DESTROY_SUCCESS:
                        //短信验证码
                        shopName = paramMap.get("shopName");
                        resp = SendSms.sendDestroySuccessSms(phone, shopName);
                    default:
                        break;
                }
                respTime = new Date();
                smsRecord.setRespTime(respTime);
                smsRecord.setResp(resp);
                String state = "30";
                if (!LocalUtils.isEmptyAndNull(resp)) {
                    JSONObject respJson = JSONObject.parseObject(resp);
                    Object code = respJson.get("Code");
                    if (ConstantCommon.OK.equalsIgnoreCase(code + "")) {
                        state = "20";
                    }
                }
                smsRecord.setState(state);
                smsRecord.setUpdateTime(new Date());
                saveOrUpdateOpSmsRecord(smsRecord);
            });
        }
    }
}
