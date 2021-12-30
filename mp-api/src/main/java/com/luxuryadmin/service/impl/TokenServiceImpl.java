package com.luxuryadmin.service.impl;

import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.common.utils.weixxin.mpsdk.HttpUtils;
import com.luxuryadmin.config.WeiXinConfig;
import com.luxuryadmin.config.WxPayProperties;
import com.luxuryadmin.context.Cont;
import com.luxuryadmin.dto.WeiTokenDTO;
import com.luxuryadmin.service.BasicsService;
import com.luxuryadmin.service.TokenService;
import com.luxuryadmin.util.JointUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * 小程序用户表 serverImpl
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:01
 */
@Service
@Transactional
public class TokenServiceImpl implements TokenService {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private BasicsService basicsService;

    @Resource
    private WxPayProperties wxPayProperties;

    /**
     * 获取token
     *
     * @param
     */
    @Override
    public String getToken(String username, Integer userId) {
        clearToken(username);
        String uuid = Cont.getUUID();
        //给维护人员看的token
        redisUtil.setEx(getMaintainKey(username), uuid, Cont.day);
        //给系统搜索的token
        redisUtil.setEx(getSystemUserIdKey(uuid), userId.toString(), Cont.day);
        redisUtil.set(Cont.MP_USER + uuid + Cont.USERNAME, username);
        return uuid;
    }

    /**
     * 清除缓存token
     *
     * @param username
     */
    @Override
    public void clearToken(String username) {
        String maintainValue = redisUtil.get(getMaintainKey(username));
        redisUtil.delete(Cont.MP_USER + maintainValue + Cont.USERNAME);
        redisUtil.delete(getMaintainKey(username));
        if (StringUtil.isBlank(maintainValue)) {
            return;
        }
        redisUtil.delete(getSystemUserIdKey(maintainValue));
        redisUtil.delete(Cont.MP_USER + maintainValue + Cont.END_TIME);

    }

    /**
     * 微信token
     */
    @Override
    public String getWXToken() {
        WeiTokenDTO weiTokenDTO = new WeiTokenDTO();
        weiTokenDTO.setAppid(wxPayProperties.getAppid());
        weiTokenDTO.setSecret(wxPayProperties.getSecret());
        weiTokenDTO.setGrant_type("client_credential");
        String urlStr = JointUtil.urlJoint(weiTokenDTO, WeiXinConfig.token);
        String resInfo = HttpUtils.httpsRequest(urlStr, "GET", null);
        JSONObject resJson = JSONObject.fromObject(resInfo);
        String parameter = "access_token";
        if (!resJson.has(parameter)) {
            throw new MyException("获取二维码失败");
        }
        String access_token = resJson.getString("access_token");
        return access_token;
    }

    /**
     * 维护人员token key
     *
     * @param username
     * @return
     */
    public String getMaintainKey(String username) {
        return Cont.MP_USER + username;
    }

    /**
     * 系统搜索token key
     *
     * @param uuid
     * @return
     */
    @Override
    public String getSystemUserIdKey(String uuid) {
        return Cont.MP_USER + uuid + Cont.USER_ID;
    }
}
