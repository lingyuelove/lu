package com.luxuryadmin.service.op;

import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.entity.op.OpSmsRecord;
import com.luxuryadmin.enums.login.EnumSendSmsType;
import com.luxuryadmin.enums.sys.EnumSaltType;

import java.util.Map;

/**
 * 发送短信记录表
 *
 * @author monkey king
 * @date 2019-12-05 14:53:46
 */
public interface OpSmsRecordService {

    /**
     * new一个实体
     *
     * @param userId   用户id
     * @param saltType 模块类型;枚举类; {@link EnumSaltType}
     * @param salt     盐值
     * @return
     */
    OpSmsRecord createOpSmsRecord(int userId, EnumSaltType saltType, String salt);

    /**
     * 添加OpSmsRecord入库
     *
     * @param opSmsRecord {@link OpSmsRecord}
     * @return id
     */
    int saveOrUpdateOpSmsRecord(OpSmsRecord opSmsRecord);

    /**
     * 生成6位数的验证码
     * @return
     */
    String generatorValidateCode();

    /**
     * 发送短信验证码
     * @param validateCode 验证码
     * @return
     */
    BaseResult sendValidateCode(String validateCode);


    /**
     * 发送验证码
     *
     * @param phone
     * @param smsCode
     * @param sendSmsType
     * @param ip
     * @param paramMap
     */
    void sendSms(String phone, String smsCode, EnumSendSmsType sendSmsType, String ip, Map<String, String> paramMap);
}
