package com.luxuryadmin.service.shp.impl;

import com.alibaba.fastjson.JSONObject;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantWeChat;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.aliyun.AliHttpUtils;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.ShpBindCount;
import com.luxuryadmin.entity.shp.ShpUser;
import com.luxuryadmin.enums.login.EnumOtherLoginType;
import com.luxuryadmin.mapper.shp.ShpBindCountMapper;
import com.luxuryadmin.service.shp.ShpBindCountService;
import com.luxuryadmin.service.shp.ShpUserService;
import com.luxuryadmin.vo.shp.VoUserShopBase;
import com.luxuryadmin.vo.shp.VoWeChatUserinfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author monkey king
 * @date 2020-08-07 18:17:00
 */
@Slf4j
@Service
public class ShpBindCountServiceImpl implements ShpBindCountService {

    @Resource
    private ShpBindCountMapper shpBindCountMapper;

    @Autowired
    private ShpUserService shpUserService;


    @Override
    public void saveBindCount(int userId, String username, String openId, EnumOtherLoginType type) {
        ShpBindCount shpBindCount = new ShpBindCount();
        shpBindCount.setFkShpUserId(userId);
        shpBindCount.setUsername(username);
        shpBindCount.setOpenId(openId);
        shpBindCount.setInsertTime(new Date());
        shpBindCount.setUpdateTime(shpBindCount.getInsertTime());
        shpBindCount.setType(type.getName());
        //已绑定
        shpBindCount.setState("1");
        shpBindCountMapper.saveObject(shpBindCount);
    }

    @Override
    public void saveOrUpdateBindCount(ShpBindCount shpBindCount) {
        if (LocalUtils.isEmptyAndNull(shpBindCount.getId())) {
            shpBindCountMapper.saveObject(shpBindCount);
        } else {
            shpBindCountMapper.updateObject(shpBindCount);
        }
    }

    @Override
    public boolean existsBindCount(EnumOtherLoginType type, String username) {
        return shpBindCountMapper.existsBindCount(type.getName(), username) > 0;
    }

    @Override
    public ShpBindCount getBindCountByOpenId(EnumOtherLoginType type, String openId) {
        return shpBindCountMapper.getBindCountByOpenId(type.getName(), openId);
    }

    @Override
    public ShpBindCount getBindCountByUsername(EnumOtherLoginType type, String username) {
        return shpBindCountMapper.getBindCountByUsername(type.getName(), username);
    }

    @Override
    public ShpBindCount bindWeChatLogin(String appId, String appSecret, String code) throws Exception {
        String host = "https://api.weixin.qq.com/sns/oauth2/access_token";
        Map<String, String> body = new HashMap<>(16);
        body.put("appid", appId);
        body.put("secret", appSecret);
        body.put("code", code);
        body.put("grant_type", "authorization_code");
        HttpResponse post = AliHttpUtils.doGet(host, null, body, body);
        int statusCode = post.getStatusLine().getStatusCode();
        ShpBindCount sbc = new ShpBindCount();
        if (statusCode == HttpStatus.SC_OK) {
            String str = EntityUtils.toString(post.getEntity(), "UTF-8");
            JSONObject jsonObject;
            try {
                jsonObject = JSONObject.parseObject(str);
            } catch (Exception e) {
                throw new MyException("not JSON format! ");
            }
            String openId = jsonObject.getString("openid");
            String accessToken = jsonObject.getString("access_token");
            String refreshToken = jsonObject.getString("refresh_token");
            if (LocalUtils.isEmptyAndNull(openId)) {
                String errCode = jsonObject.getString("errcode");
                String errMsg = jsonObject.getString("errmsg");
                throw new MyException("微信授权错误: " + errCode + "!" + errMsg);
            }

            sbc.setOpenId(openId);
            sbc.setAccessToken(accessToken);
            sbc.setRefreshToken(refreshToken);
        }
        return sbc;
    }

    @Override
    public VoWeChatUserinfo getWeChatUserinfo(String accessToken, String openId) throws Exception {
        String host = "https://api.weixin.qq.com/sns/userinfo";
        Map<String, String> body = new HashMap<>(16);
        body.put("access_token", accessToken);
        body.put("openid", openId);
        HttpResponse post = AliHttpUtils.doGet(host, null, body, body);
        int statusCode = post.getStatusLine().getStatusCode();
        VoWeChatUserinfo ui = new VoWeChatUserinfo();
        if (statusCode == HttpStatus.SC_OK) {
            String str = EntityUtils.toString(post.getEntity(), "UTF-8");
            JSONObject jsonObject;
            try {
                jsonObject = JSONObject.parseObject(str);
            } catch (Exception e) {
                throw new MyException("not JSON format! ");
            }
            openId = jsonObject.getString("openid");
            String nickname = jsonObject.getString("nickname");
            String sex = jsonObject.getString("sex");
            String province = jsonObject.getString("province");
            String city = jsonObject.getString("city");
            String country = jsonObject.getString("country");
            String headImgUrl = jsonObject.getString("headimgurl");
            String unionId = jsonObject.getString("unionid");
            if (LocalUtils.isEmptyAndNull(openId)) {
                String errCode = jsonObject.getString("errcode");
                String errMsg = jsonObject.getString("errmsg");
                throw new MyException("获取微信用户信息错误: " + errCode + "!" + errMsg);
            }
            ui.setOpenId(openId);
            ui.setNickname(nickname);
            ui.setSex(sex);
            ui.setProvince(province);
            ui.setCity(city);
            ui.setCountry(country);
            ui.setHeadImgUrl(headImgUrl);
            ui.setUnionId(unionId);
        }
        return ui;
    }

    @Override
    public void updateUserinfoFromWeChat(String openId, String accessToken, String refreshToken, ShpBindCount sbc, EnumOtherLoginType type, int userId) throws Exception {
        ShpUser shpUser = new ShpUser();
        VoUserShopBase voUser = shpUserService.getShpUserBaseByUserIdAndShopId(userId);
        String username = voUser.getUsername();
        username = DESEncrypt.decodeUsername(username);
        Date date = new Date();
        if ("wx".equals(type.getName())) {
            //请求微信个人信息接口;获取图片,昵称;
            VoWeChatUserinfo ui = getWeChatUserinfo(accessToken, openId);
            if (ui == null) {
                throw new MyException("获取用户微信信息失败!");
            }
            String dbNickname = voUser.getNickname();
            String dbHeadUrl = voUser.getUserHeadImgUrl();
            //使用的是默认头像
            if (ConstantCommon.DEFAULT_USER_HEAD_IMG.equals(dbHeadUrl)) {
                dbHeadUrl = ui.getHeadImgUrl();
            }
            //尾号和用户开头的昵称为默认昵称
            if (dbNickname.startsWith("尾号") || dbNickname.startsWith("用户")) {
                dbNickname = ui.getNickname();
            }
            shpUser.setNickname(dbNickname);
            shpUser.setHeadImgUrl(dbHeadUrl);
            shpUser.setBindWeChat(1);
        } else {
            //绑定苹果登录
            shpUser.setBindApple(1);
        }
        shpUser.setId(userId);
        shpUser.setUpdateTime(date);
        shpUser.setUpdateAdmin(userId);
        shpUserService.updateShpUser(shpUser);
        //保存授权
        if (null == sbc) {
            sbc = new ShpBindCount();
            sbc.setInsertTime(date);
        }
        sbc.setFkShpUserId(userId);
        sbc.setOpenId(openId);
        sbc.setUsername(username);
        sbc.setType(type.getName());
        sbc.setState("1");
        sbc.setRefreshToken(refreshToken);
        sbc.setAccessToken(accessToken);
        sbc.setUpdateTime(date);
        saveOrUpdateBindCount(sbc);
    }

    public static void main(String[] args) throws Exception {
        String code = "061evg0w31cFJV2Jz02w3KWd2L1evg0z";
        String appId = ConstantWeChat.APP_ID_FOR_APP;
        String appSecret = ConstantWeChat.APP_SECRET_FOR_APP;
        String host = "https://api.weixin.qq.com/sns/oauth2/access_token";
        Map<String, String> body = new HashMap<>(16);
        body.put("appid", appId);
        body.put("secret", appSecret);
        body.put("code", code);
        body.put("grant_type", "authorization_code");
        HttpResponse post = AliHttpUtils.doGet(host, null,  body, body);
        int statusCode = post.getStatusLine().getStatusCode();
        ShpBindCount sbc = new ShpBindCount();
        if (statusCode == HttpStatus.SC_OK) {
            String str = EntityUtils.toString(post.getEntity(), "UTF-8");
            JSONObject jsonObject;
            jsonObject = JSONObject.parseObject(str);
            String openId = jsonObject.getString("openid");
            String accessToken = jsonObject.getString("access_token");
            String refreshToken = jsonObject.getString("refresh_token");
            if (LocalUtils.isEmptyAndNull(openId)) {
                String errCode = jsonObject.getString("errcode");
                String errMsg = jsonObject.getString("errmsg");
                throw new MyException("微信授权错误: " + errCode + "!" + errMsg);
            }

            sbc.setOpenId(openId);
            sbc.setAccessToken(accessToken);
            sbc.setRefreshToken(refreshToken);
            throw new MyException("not JSON format! ");
        }
    }
}
