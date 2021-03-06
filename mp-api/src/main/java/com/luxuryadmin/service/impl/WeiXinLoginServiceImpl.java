package com.luxuryadmin.service.impl;

import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.common.utils.weixxin.mpsdk.HttpUtils;
import com.luxuryadmin.config.WeiXinConfig;
import com.luxuryadmin.config.WxPayProperties;
import com.luxuryadmin.context.Cont;
import com.luxuryadmin.dto.WeiXinLoginDTO;
import com.luxuryadmin.entity.MpUser;
import com.luxuryadmin.entity.MpUserInvite;
import com.luxuryadmin.entity.MpVisitorRecord;
import com.luxuryadmin.enums.*;
import com.luxuryadmin.mapper.MpUserInviteMapper;
import com.luxuryadmin.mapper.MpUserMapper;
import com.luxuryadmin.mapper.MpVisitorRecordMapper;
import com.luxuryadmin.mapper.shp.ShpUserMapper;
import com.luxuryadmin.param.weixin.ParamGetPhone;
import com.luxuryadmin.param.weixin.ParamLogin;
import com.luxuryadmin.service.TokenService;
import com.luxuryadmin.service.WeiXinLoginService;
import com.luxuryadmin.util.IpUtil;
import com.luxuryadmin.util.JointUtil;
import com.luxuryadmin.util.WechatDecryptDataUtil;
import com.luxuryadmin.vo.wx.VOLogin;
import com.luxuryadmin.vo.wx.VOWeixinPhoneDecryptInfo;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;


@Service
@Slf4j
public class WeiXinLoginServiceImpl implements WeiXinLoginService {

    @Resource
    private WxPayProperties wxPayProperties;

    @Resource
    private MpUserMapper mpUserMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ShpUserMapper shpUserMapper;

    @Resource
    private TokenService tokenService;

    @Resource
    private MpVisitorRecordMapper mpVisitorRecordMapper;

    @Resource
    private MpUserInviteMapper mpUserInviteMapper;

    /**
     * ????????????
     *
     * @param paramLogin
     */
    @Override
    public VOLogin wxLogin(ParamLogin paramLogin) {
        WeiXinLoginDTO weiXinLoginDTO = new WeiXinLoginDTO();
        weiXinLoginDTO.setAppid(wxPayProperties.getAppid());
        weiXinLoginDTO.setSecret(wxPayProperties.getSecret());
        weiXinLoginDTO.setJs_code(paramLogin.getJsCode());
        weiXinLoginDTO.setGrant_type(WeiXinConfig.grantType);
        //??????????????????
        String urlStr = JointUtil.urlJoint(weiXinLoginDTO, wxPayProperties.getLoginUrl());
        //????????????
        String resInfo = HttpUtils.httpsRequest(urlStr, "GET", null);
        JSONObject resJson = JSONObject.fromObject(resInfo);
        log.info("????????????????????????????????????" + resJson);
        if (!resJson.has("openid")) {
            throw new MyException("????????????");
        }
        VOLogin voLogin = (VOLogin) JSONObject.toBean(resJson, VOLogin.class);
        return voLogin;

    }

    /**
     * ??????????????????????????????
     *
     * @param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public VOWeixinPhoneDecryptInfo getPhoneInfo(ParamGetPhone param, HttpServletRequest request) {
        //???????????????
        String aes;
        try {
            aes = WechatDecryptDataUtil.decryptData(param.getEncryptedData(), param.getSessionKey(), param.getIv());
        } catch (Exception e) {
            throw new MyException("????????????");
        }
        VOWeixinPhoneDecryptInfo info;
        log.info("????????????????????????" + aes);
        if (StringUtil.isBlank(aes)) {
            throw new MyException("??????????????????");
        } else {
            JSONObject resJson = JSONObject.fromObject(aes);
            info = (VOWeixinPhoneDecryptInfo) JSONObject.toBean(resJson, VOWeixinPhoneDecryptInfo.class);
            if (!info.getWatermark().getAppid().equals(wxPayProperties.getAppid())) {
                throw new MyException("?????????appId");
            }
        }
        String ipAddress = IpUtil.getIpAddress(request);
        String phone = info.getPurePhoneNumber();
        String username = DESEncrypt.encodeUsername(phone);
        Integer count = shpUserMapper.getCountByUsername(username);
        //??????openid????????????????????????????????????
        MpUser user;
        user = mpUserMapper.getUserInfoByOpenId(param.getOpenId());
        Date date = new Date();
        String token = null;
        if (user != null) {
            //openId??????
            user.setUsername(username);
            user.setOpenId(param.getOpenId());
            user.setLoginIp(ipAddress == null ? "" : ipAddress);
            user.setUpdateTime(date);
            sdjVip(phone, count, user);
        } else {
            //openId?????????
            user = mpUserMapper.getUserInfoByUsername(username);
            if (user != null) {
                //???????????????
                user.setLoginIp(ipAddress == null ? "" : ipAddress);
                user.setUpdateTime(date);
                user.setOpenId(param.getOpenId());
                user.setOpenId(param.getOpenId());
                sdjVip(phone, count, user);
            } else {
                user = new MpUser();
                //??????????????????
                user.setUsername(username);
                user.setState(EnumUserState.NORMAL.getCode());
                user.setMasterType(EnumMasterType.BY.name());

                user.setOpenId(param.getOpenId());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                String experienceTime = redisUtil.get(Cont.MP_BAOYANG_CONFIG_EXPERIENCETIME);
                cal.add(Calendar.DAY_OF_MONTH, +Integer.parseInt(experienceTime));
                Date time = cal.getTime();
                user.setTryStartTime(date);
                user.setTryEndTime(time);
                boolean isCount = false;
                //????????????????????????????????????
                if (count > 0) {
                    //??????????????????????????????????????????
                    user.setIsMember(EnumIsMember.YES.getCode());
                    user.setMemberState(EnumMemberState.official_vip.getCode());
                    user.setVipType(EnumVipType.SDJ_VIP.getCode());
                    redisUtil.set(Cont.MP_USER + phone + Cont.SDJ_VIP, Cont.IS_SDJ_VIP);
                } else {
                    //???????????????????????????
                    user.setIsMember(EnumIsMember.NO.getCode());
                    user.setMemberState(EnumMemberState.experience_vip.getCode());
                    user.setVipType(EnumVipType.MP_VIP.getCode());
                    isCount = true;
                }
                user.setLoginIp(ipAddress == null ? "" : ipAddress);
                user.setProvince("");
                user.setCity("");
                user.setDistrict("");
                user.setInsertTime(date);
                mpUserMapper.saveObject(user);
                if (isCount) {
                    //????????????????????????redis?????????
                    token = tokenService.getToken(phone, user.getId());
                    redisUtil.setEx(Cont.MP_USER + token + Cont.END_TIME, DateUtil.format(time), Long.parseLong(experienceTime));
                }
            }
        }
        if (EnumUserState.FORBIDDEN.getCode().equals(user.getState())) {
            throw new MyException("??????{" + user.getWxNickname() + "}?????????");
        }
        if (EnumMemberState.experience_vip.getCode().equals(user.getMemberState()) && EnumIsMember.NO.getCode().equals(user.getIsMember()) && date.getTime() >= user.getTryEndTime().getTime()) {
            //?????????????????????????????????????????????????????????????????????
            user.setMemberState(EnumMemberState.mistake_vip.getCode());
            mpUserMapper.updateObject(user);
            String maintainValue = redisUtil.get(Cont.MP_USER + phone);
            if (StringUtil.isNotBlank(maintainValue)) {
                redisUtil.delete(Cont.MP_USER + maintainValue + Cont.END_TIME);
            }
        }
        //??????????????????
        MpVisitorRecord mpVisitorRecord = new MpVisitorRecord();
        if (param.getShareId() != null) {
            mpVisitorRecord.setFkBeMpUserId(param.getShareId());
            //???????????????
            MpUser shareUserInfo = mpUserMapper.getObjectById(param.getShareId());
            //????????????????????????????????????
            MpUserInvite userInvite = mpUserInviteMapper.getUserInviteByBeUserId(user.getId());
            if (count <= 0 && EnumIsMember.NO.getCode().equals(user.getIsMember()) && userInvite == null && EnumIsMember.YES.getCode().equals(shareUserInfo.getIsMember())
                    && EnumVipType.MP_VIP.getCode().equals(shareUserInfo.getVipType()) && date.getTime() == user.getInsertTime().getTime()) {
                //??????????????????
                MpUserInvite mpUserInvite = new MpUserInvite();
                mpUserInvite.setFkInviteMpUserId(param.getShareId());
                mpUserInvite.setFkBeInviteMpUserId(user.getId());
                mpUserInvite.setInsertTime(date);
                mpUserInviteMapper.saveObject(mpUserInvite);
            }
        }
        mpVisitorRecord.setFkMpUserId(user.getId());
        mpVisitorRecord.setMasterType(EnumMasterType.BY.name());
        mpVisitorRecord.setInsertTime(date);
        mpVisitorRecordMapper.saveObject(mpVisitorRecord);
        if (StringUtil.isBlank(token)) {
            //token ???????????????
            token = tokenService.getToken(phone, user.getId());
            if (EnumIsMember.NO.getCode().equals(user.getIsMember()) && user.getTryEndTime().getTime() > date.getTime()) {
                long residueTime = user.getTryEndTime().getTime() - date.getTime();
                redisUtil.setExSECONDS(Cont.MP_USER + token + Cont.END_TIME, DateUtil.format(user.getTryEndTime()), residueTime / 1000);
            }
        }
        info.setToken(token);
        info.setUserId(user.getId());
        return info;
    }

    private void sdjVip(String phone, Integer count, MpUser user) {
        if (EnumIsMember.NO.getCode().equals(user.getIsMember()) && count > 0) {
            user.setIsMember(EnumIsMember.YES.getCode());
            user.setMemberState(EnumMemberState.official_vip.getCode());
            user.setVipType(EnumVipType.SDJ_VIP.getCode());
            redisUtil.set(Cont.MP_USER + phone + Cont.SDJ_VIP, Cont.IS_SDJ_VIP);
        }
        mpUserMapper.updateObject(user);
    }
}
