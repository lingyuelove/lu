package com.luxuryadmin.service.shp.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.constant.RedisKeyAdminLogin;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.encrypt.PBKDF2Util;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.common.utils.youmeng.RSAUtil;
import com.luxuryadmin.common.utils.youmeng.UMConfig;
import com.luxuryadmin.common.utils.youmeng.UMHttps;
import com.luxuryadmin.entity.op.OpChannel;
import com.luxuryadmin.entity.shp.ShpUser;
import com.luxuryadmin.entity.shp.ShpUserDetail;
import com.luxuryadmin.entity.shp.ShpUserNumber;
import com.luxuryadmin.entity.shp.ShpUserToken;
import com.luxuryadmin.entity.sys.SysSalt;
import com.luxuryadmin.enums.login.EnumSendSmsType;
import com.luxuryadmin.enums.sys.EnumSaltType;
import com.luxuryadmin.mapper.op.OpChannelMapper;
import com.luxuryadmin.mapper.shp.ShpUserMapper;
import com.luxuryadmin.param.login.*;
import com.luxuryadmin.param.shp.ParamShpUser;
import com.luxuryadmin.service.op.OpSmsRecordService;
import com.luxuryadmin.service.pro.ProShareSeeUserService;
import com.luxuryadmin.service.shp.*;
import com.luxuryadmin.service.sys.SysSaltService;
import com.luxuryadmin.vo.shp.*;
import com.luxuryadmin.vo.usr.VoInviteDetail;
import com.luxuryadmin.vo.usr.VoInviteList;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * ???????????????(??????)
 *
 * @author monkey king
 * @Date 2019/12/01 3:46
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ShpUserServiceImpl implements ShpUserService {
    @Resource
    private ShpUserMapper shpUserMapper;

    @Autowired
    private ShpShopService shpShopService;

    @Autowired
    private ShpUserNumberService shpUserNumberService;

    @Autowired
    private ShpUserDetailService shpUserDetailService;


    @Autowired
    private ShpUserInviteService shpUserInviteService;

    @Autowired
    private ShpUserTokenService shpUserTokenService;

    @Autowired
    private SysSaltService sysSaltService;

    @Autowired
    private OpSmsRecordService opSmsRecordService;

    @Autowired
    private ShpUserRoleRefService shpUserRoleRefService;

    @Resource
    private OpChannelMapper opChannelMapper;


    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ServicesUtil servicesUtil;


    @Autowired
    private ProShareSeeUserService proShareSeeUserService;

    /**
     * ???????????????
     */
    public static final String not_pass = "NOT_PASS";

    /**
     * ?????????????????????
     */
    public static final String unload = "UNLOAD";

    public static final String ok_qrcode_scan = "OK_QRCODE_SCAN";
    /**
     * ?????????????????????
     */
    public static final Long out_time = 300L;
    /**
     * ???????????????????????????
     */
    private static final String DEFAULT_LOGIN = "1";


    @Override
    public VoShpUser getShpUserForLogin(String username) {
        username = DESEncrypt.encodeUsername(username);
        return shpUserMapper.getShpUserForLogin(username);
    }

    @Override
    public VoShpUserSalt getVOShpUserSaltByUsername(String username) {
        username = DESEncrypt.encodeUsername(username);
        return shpUserMapper.getVOShpUserSaltByUsername(username);
    }


    private VoShpUserSalt getShpUserSalt(String username, boolean isPwdLogin) {
        VoShpUserSalt voShpUserSalt = getVOShpUserSaltByUsername(username);
        //???????????????;????????????????????????????????????-?????????????????????
        if (LocalUtils.isEmptyAndNull(voShpUserSalt)) {
            EnumCode enumCode = EnumCode.ERROR_USERNAME;
            if (isPwdLogin) {
                enumCode = EnumCode.ERROR_USERNAME_PASSWORD;
            }
            throw new MyException(enumCode);
        }
        if (!ConstantCommon.ONE.equals(voShpUserSalt.getState())) {
            throw new MyException("?????????????????????,???????????????!");
        }
        return voShpUserSalt;
    }

    @Override
    public BaseResult shpUsernameLogin(String username) {
        return shpUsernameLogin(username, false);
    }

    @Override
    public BaseResult shpUsernameLogin(String username, boolean isPwdLogin) {
        //true:????????????????????? | false:?????????????????????
        VoShpUserSalt voShpUserSalt = getShpUserSalt(username, isPwdLogin);
        //?????????????????? ????????????????????????
        voShpUserSalt.setUsername(username);
        HashMap hashMap = chooseLoginShop(voShpUserSalt, isPwdLogin);
        return BaseResult.okResult(hashMap);
    }

    @Override
    public BaseResult shpPwdLogin(String username, String password) {
        return shpPwdLogin(username, password, false);
    }

    @Override
    public BaseResult shpPwdLogin(String username, String password, boolean isAdminLogin) {
        VoShpUserSalt voShpUserSalt = getShpUserSalt(username, true);
//        //??????????????????
//        if ("18434363494".equals(username)) {
//            log.error("***********?????????????????????????????????????????????2shpLogin: *****************");
//            log.error("***********?????????????????????????????????????????????3voShpUserSalt="+ JSON.toJSONString(voShpUserSalt));
//            log.error("*********** ?????????????????????????????????????????????4validatePwd:\r\n" +
//                    "password={}\r\n,voShpUserSalt.getPassword()={}\r\n,voShpUserSalt.getSalt()={} " +
//                    "*****************",password,voShpUserSalt.getPassword(),voShpUserSalt.getSalt());
//        }

        boolean validatePwd = validatePwd(password, voShpUserSalt.getPassword(), voShpUserSalt.getSalt());
        if (!validatePwd) {
            //??????????????????
            return BaseResult.errorResult(EnumCode.ERROR_USERNAME_PASSWORD);
        }
        //?????????????????? ????????????????????????
        voShpUserSalt.setUsername(username);
        HashMap hashMap = chooseLoginShop(voShpUserSalt, isAdminLogin);
        return BaseResult.okResult(hashMap);
    }

    //@Override
    //public BaseResult shpAdminLogin(String username, String password) {
    //    VoShpUserSalt voShpUserSalt = shpLogin(username, true);
    //    boolean validatePwd = validatePwd(password, voShpUserSalt.getPassword(), voShpUserSalt.getSalt());
    //    if (!validatePwd) {
    //        //??????????????????
    //        return BaseResult.errorResult(EnumCode.ERROR_USERNAME_PASSWORD);
    //    }
    //    //?????????????????? ????????????????????????
    //    voShpUserSalt.setUsername(username);
    //    HashMap hashMap = chooseLoginShop(voShpUserSalt);
    //    return BaseResult.okResult(hashMap);
    //}

    /**
     * ????????????????????????,???????????????????????????????????????;
     *
     * @param voShpUserSalt
     * @param isAdminLogin  ??????????????????; true:???????????? | false:app??????
     * @return
     */
    private HashMap chooseLoginShop(VoShpUserSalt voShpUserSalt, boolean isAdminLogin) {
        final String token = generatorToken(isAdminLogin);
        int userId = voShpUserSalt.getUserId();
        //???????????????????????????ID;shopId???0,????????????????????????????????????;
        final int dbShopId = voShpUserSalt.getShopId();
        int shopId = dbShopId;
        //??????????????????????????????
        String defaultLogin = voShpUserSalt.getDefaultLogin();
        //???????????????
        HashMap<String, Object> hashMap = LocalUtils.getHashMap(token);
        boolean isDefaultLogin = DEFAULT_LOGIN.equals(defaultLogin) && dbShopId != 0;
        boolean chooseShop = false;
        String username = voShpUserSalt.getUsername();
        Integer userNumber = voShpUserSalt.getUserNumber();
        //???????????????????????????
        List<VoShopBase> voShopBasesList = shpShopService.chooseShop(userId, token);
        if (!LocalUtils.isEmptyAndNull(voShopBasesList)) {
            if (voShopBasesList.size() > 1) {
                if (isDefaultLogin) {
                    int j = 0;
                    for (VoShopBase voShopBase : voShopBasesList) {
                        if (dbShopId == voShopBase.getShopId()) {
                            break;
                        } else {
                            j++;
                        }
                    }
                    //????????????????????????????????????????????????,??????????????????????????????
                    if (j == voShopBasesList.size()) {
                        chooseShop = true;
                    }
                } else {
                    chooseShop = true;
                }
            } else {
                VoShopBase voShopBase = voShopBasesList.get(0);
                shopId = voShopBase.getShopId();
                //shopNumber = voShopBase.getShopNumber();
                defaultLogin = "1";
            }
        } else {
            chooseShop = true;
        }

        //??????????????????
        if (chooseShop) {
            for (VoShopBase voShopBase : voShopBasesList) {
                //????????????
                voShopBase.setShopHeadImgUrl(servicesUtil.formatImgUrl(voShopBase.getShopHeadImgUrl()));
            }
            //????????????????????????
            shopId = 0;
            //shopNumber = 0;
            defaultLogin = "0";
            hashMap.put("objList", voShopBasesList);
        }

        //?????????????????????????????????0;
        ShpUser shpUser = packShpUserForLogin(userId, shopId, defaultLogin);
        updateShpUser(shpUser);
        //shopNumber = LocalUtils.isEmptyAndNull(shopNumber) ? 0 : shopNumber;
        getShpUserToken(token, userId, userNumber, username, shopId, isAdminLogin);
        hashMap.put("defaultLogin", defaultLogin);
        hashMap.put("userId", userId);
        return hashMap;
    }


    @Override
    public BaseResult sendSmsValidateCode(ParamImageCode paramImageCode, EnumSendSmsType sendSmsType) {
        String username = paramImageCode.getUsername();
        String imageCode = paramImageCode.getImageCode();
        String ip = paramImageCode.getIp();

        //???????????????????????????
        String sysType = sendSmsType.getCode();
        //redis-key???
        String sendYzmLeftTime = ConstantRedisKey.getSendYzmLeftTime(username);
        String yzmDayTotalKey = ConstantRedisKey.getYzmDayTotal(username);
        String ipYzmDayTotalKey = ConstantRedisKey.getYzmDayTotal(ip);
        String imageCodeKey = ConstantRedisKey.getImageCode(username);
        String smsCodeKey = ConstantRedisKey.getYzmSmsCodeKey(sysType, username);
        String smsLimitKey = ConstantRedisKey.SMS_LIMIT;
        String ipSmsLimitKey = ConstantRedisKey.IP_SMS_LIMIT;
        //redis-value???
        String sendYzmLeftTimeValue = redisUtil.get(sendYzmLeftTime);
        String yzmDayTotalValue = redisUtil.get(yzmDayTotalKey);
        String ipYzmDayTotalValue = redisUtil.get(ipYzmDayTotalKey);
        String imageCodeValue = redisUtil.get(imageCodeKey);
        String smsCodeValue = redisUtil.get(smsCodeKey);
        String smsLimitValue = redisUtil.get(smsLimitKey);
        String ipSmsLimitValue = redisUtil.get(ipSmsLimitKey);

        //???????????????????????????
        if (!LocalUtils.isEmptyAndNull(sendYzmLeftTimeValue)) {
            long timeout = redisUtil.getExpire(sendYzmLeftTime);
            String getYzmInfo = timeout + "????????????????????????";
            return BaseResult.defaultErrorWithMsg(getYzmInfo);
        }

        //???????????????????????????????????????;???????????????;??????1???
        int smsLimit = LocalUtils.isEmptyAndNull(smsLimitValue) ? 1 : Integer.parseInt(smsLimitValue);
        //???????????????????????????????????????;ip?????????;??????1???
        int ipSmsLimit = LocalUtils.isEmptyAndNull(ipSmsLimitValue) ? 1 : Integer.parseInt(ipSmsLimitValue);

        //????????? ???????????????????????????
        if (LocalUtils.isEmptyAndNull(yzmDayTotalValue)) {
            yzmDayTotalValue = "0";
        }

        //????????? ip??????????????????
        if (LocalUtils.isEmptyAndNull(ipYzmDayTotalValue)) {
            ipYzmDayTotalValue = "0";
        }

        int yzmDayTotal = Integer.parseInt(yzmDayTotalValue);
        int ipYzmDayTotal = Integer.parseInt(ipYzmDayTotalValue);

        yzmDayTotal += 1;
        yzmDayTotalValue = yzmDayTotal + "";
        ipYzmDayTotal += 1;
        ipYzmDayTotalValue = ipYzmDayTotal + "";
        redisUtil.setEx(yzmDayTotalKey, yzmDayTotalValue, 1);
        redisUtil.setEx(ipYzmDayTotalKey, ipYzmDayTotalValue, 1);

        //24????????????????????????????????????N???,?????????????????????????????????
        boolean emptyImageCode = LocalUtils.isEmptyAndNull(imageCode);

        //????????????????????????????????????????????????,???????????????????????????????????????;
        boolean isSmsLimit = (yzmDayTotal > smsLimit) || (ipYzmDayTotal > ipSmsLimit);
        if (emptyImageCode && isSmsLimit) {
            return BaseResult.errorResult(EnumCode.ERROR_NEED_IMAGE_CODE);
        }

        if (isSmsLimit) {
            //?????????????????????
            if (!imageCode.equalsIgnoreCase(imageCodeValue)) {
                return BaseResult.errorResult(EnumCode.ERROR_IMAGE_CODE);
            }
            //??????????????????????????????????????????;
            redisUtil.delete(imageCodeKey);
        }

        //????????????????????????;
        existsShpUser(sendSmsType, username);

        //???????????????;????????????????????????????????????????????????;
        if (LocalUtils.isEmptyAndNull(smsCodeValue)) {
            smsCodeValue = opSmsRecordService.generatorValidateCode();
        }
        boolean isDev = ConstantCommon.DEV.equals(ConstantCommon.springProfilesActive);
        if (isDev) {
            //????????????;???????????????;
            smsCodeValue = "123456";
        }
        //?????????????????????
        opSmsRecordService.sendSms(username, smsCodeValue, sendSmsType, ip, null);

        //????????????,???????????????????????????
        BaseResult baseResult = opSmsRecordService.sendValidateCode(smsCodeValue);

        //??????60??????????????????????????????
        redisUtil.setExMINUTES(sendYzmLeftTime, ip, 1);
        //???????????????5????????????
        redisUtil.setExMINUTES(smsCodeKey, smsCodeValue, 5);

        return baseResult;
    }

    @Override
    public ShpUser registerShpUser(String username, String inviteCode, String remark, String nickName) {
        //????????????
        //??????????????????????????????;
        existsShpUser(EnumSendSmsType.REGISTER, username);


        //???????????????????????????
        boolean emptyInviteCode = LocalUtils.isEmptyAndNull(inviteCode);
        Integer inviteUserId = 0;
        String channelCode;
        Integer opChannelId = null;


        //????????????????????????,??????????????????,??????????????????????????????????????????????????????;
        Integer existsUserId = proShareSeeUserService.existsWxUserReturnUserId("1", username);
        if (!LocalUtils.isEmptyAndNull(existsUserId)) {
            inviteUserId = existsUserId;
        }

        if (!emptyInviteCode && existsUserId == null) {
            //2020-08-11 ??????????????????????????????????????????????????????3
            //????????????3??????????????????????????????????????????
            int inviteCodeLength = inviteCode.length();
            if (inviteCodeLength == 3) {
                channelCode = inviteCode;
                //2020-08-11 sanjin
                //????????????????????????????????????op_channel???????????????????????????
                //?????????????????????op_channel.id,??????????????????????????????????????????
                OpChannel opChannel = opChannelMapper.selectByChannelCode(channelCode);
                if (LocalUtils.isEmptyAndNull(opChannel)) {
                    throw new MyException(EnumCode.ERROR_NOT_INVITE_CODE);
                }
                opChannelId = opChannel.getId();
            } else {
                //???????????????????????????
                inviteUserId = getShpUserIdByNumber(Integer.parseInt(inviteCode));
                if (LocalUtils.isEmptyAndNull(inviteUserId)) {
                    log.info("========??????????????????:" + inviteCode);
                    throw new MyException(EnumCode.ERROR_NOT_INVITE_CODE);
                }
            }
        }
        //??????????????????,??????????????????
        ShpUser shpUser = addShpUser(username, remark, opChannelId, nickName);
        if (shpUser == null) {
            throw new MyException(EnumCode.ERROR_ADMIN);
        }
        int beInviteUserId = shpUser.getId();
        //????????????????????????,????????????id??????
        //????????????????????????[???????????????],????????????????????????
        if (!emptyInviteCode && LocalUtils.isEmptyAndNull(opChannelId) || existsUserId != null) {
            //????????????????????????;????????????;??????????????????????????????;
            //Integer finalInviteUserId = inviteUserId;
            //ScheduledExecutorService executorService = ThreadUtils.getInstance().executorService;
            //executorService.execute(() -> {
            //    log.info("========????????????????????????==========?????????:" + finalInviteUserId + " | ????????????:" + beInviteUserId);
            //    ShpUserInvite shpUserInvite = shpUserInviteService.createShpUserInvite(finalInviteUserId, beInviteUserId);
            //    shpUserInviteService.saveShpUserInvite(shpUserInvite);
            //});
            shpUserInviteService.inviteUser(inviteUserId, beInviteUserId);
        }
        return shpUser;
    }

    @Override
    public BaseResult registerShpUserAndLogin(String username, String inviteCode, String nickName) {
        ShpUser shpUser = registerShpUser(username, inviteCode, null, nickName);
        //????????????-????????????token;?????????,??????????????????,???????????????id???????????????
        String token = generatorToken(false);
        getShpUserToken(token, shpUser.getId(), shpUser.getNumber(), username, 0, false);

        HashMap<String, Object> hashMap = new HashMap<>(16);
        hashMap.put("token", token);
        hashMap.put("userId", shpUser.getId());
        return BaseResult.okResult(hashMap);
    }

    @Override
    public BaseResult updateShpUserPassword(ParamNewPwd paramNewPwd, EnumSendSmsType sendSmsType) {
        String username = paramNewPwd.getUsername();
        String newPassword = paramNewPwd.getNewPassword();
        //??????????????????????????????;
        existsShpUser(EnumSendSmsType.RESET_PASSWORD, username);
        VoShpUserSalt voShpUserSalt = getVOShpUserSaltByUsername(username);
        //????????????????????????
        String firstSetPassword = "register";
        int dbShpUserId = voShpUserSalt.getUserId();
        //??????????????????;?????????????????????
        if (firstSetPassword.equals(sendSmsType.getCode())) {
            String oldPassword = voShpUserSalt.getPassword();
            int userId = paramNewPwd.getTokenUserId();
            if (dbShpUserId != userId) {
                //?????????????????????
                return BaseResult.defaultErrorWithMsg("????????????ta?????????!");
            }
            //?????????????????????,???????????????????????????????????????;
            if (!LocalUtils.isEmptyAndNull(oldPassword)) {
                //???????????????
                return BaseResult.defaultErrorWithMsg("???????????????,?????????????????????????????????!");
            }
        }
        updateShpUserPassword(dbShpUserId, newPassword);
        return BaseResult.defaultOkResultWithMsg("??????????????????");
    }

    @Override
    public void updateShpUserPassword(int userId, String newPassword) {
        //??????32??????
        String salt = PBKDF2Util.getSaltDefault();
        EnumSaltType saltType = EnumSaltType.SHP_MODEL;
        try {
            //???????????????????????????
            newPassword = PBKDF2Util.getEncryptedPassword(newPassword, salt);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MyException("??????????????????!");
        }
        //??????????????????
        ShpUser shpUser = new ShpUser();
        shpUser.setId(userId);
        shpUser.setPassword(newPassword);
        shpUser.setUpdateTime(new Date());
        shpUserMapper.updateObject(shpUser);
        //???????????????????????????;
        SysSalt dbSysSalt = sysSaltService.getSysSaltByUserIdAndType(userId, saltType);
        //?????????????????????;
        if (LocalUtils.isEmptyAndNull(dbSysSalt)) {
            //?????????????????????
            SysSalt sysSalt = sysSaltService.createSysSalt(userId, saltType, salt);
            sysSaltService.saveSysSalt(sysSalt);
        } else {
            //???????????????
            dbSysSalt.setSalt(salt);
            dbSysSalt.setUpdateTime(new Date());
            sysSaltService.updateSysSalt(dbSysSalt);
        }
    }

    @Override
    public Integer getShpUserIdByNumber(int number) {
        return shpUserMapper.getShpUserIdByNumber(number);
    }

    @Override
    public Integer getShpUserIdByUsername(String username) {
        username = DESEncrypt.encodeUsername(username);
        return shpUserMapper.getShpUserIdByUsername(username);
    }

    @Override
    public boolean existsShpUserByUsername(String username) {
        username = DESEncrypt.encodeUsername(username);
        int num = shpUserMapper.existsShpUserByUsername(username);
        return num > 0;
    }

    @Override
    public int saveShpUserReturnId(ShpUser shpUser) {
        shpUserMapper.saveObject(shpUser);
        return shpUser.getId();
    }

    @Override
    public VoUserShopBase getShpUserBaseByUserIdAndShopId(int userId) {
        VoUserShopBase voUserShopBase = shpUserMapper.getShpUserBaseInfoByUserId(userId);
        servicesUtil.emptyThenGoLogin(voUserShopBase);
        return voUserShopBase;
    }

    @Override
    public ShpUser packShpUserForLogin(int userId, int shopId, String defaultLogin) {
        ShpUser shpUser = new ShpUser();
        shpUser.setId(userId);
        shpUser.setFkShpShopId(shopId);
        shpUser.setUpdateTime(new Date());
        shpUser.setDefaultLogin(defaultLogin);
        return shpUser;
    }

    @Override
    public void exitLogin(String token) {
        exitLogin(token, false);
    }

    @Override
    public void exitLogin(String token, boolean isAdminLogin) {
        if (!LocalUtils.isEmptyAndNull(token)) {
            //????????????????????????key
            if (isAdminLogin) {
                //??????????????????
                String oldUserIdKey = RedisKeyAdminLogin.getShpUserIdRedisKeyByToken(token);
                String oldUserNumberKey = RedisKeyAdminLogin.getUserNumberRedisKeyByToken(token);
                String olsShpUsernameKey = RedisKeyAdminLogin.getShpUsernameRedisKeyByToken(token);
                String username = redisUtil.get(olsShpUsernameKey);
                String shpTokenUsernameKey = RedisKeyAdminLogin.getShpAdminTokenKey(username);
                String olsShopIdKey = RedisKeyAdminLogin.getShopIdRedisKeyByToken(token);
                redisUtil.delete(shpTokenUsernameKey);
                redisUtil.delete(oldUserIdKey);
                redisUtil.delete(oldUserNumberKey);
                redisUtil.delete(olsShpUsernameKey);
                redisUtil.delete(olsShopIdKey);
            } else {
                //app????????????
                String oldUserIdKey = ConstantRedisKey.getShpUserIdRedisKeyByToken(token);
                String oldUserNumberKey = ConstantRedisKey.getUserNumberRedisKeyByToken(token);
                String olsShpUsernameKey = ConstantRedisKey.getShpUsernameRedisKeyByToken(token);
                String username = redisUtil.get(olsShpUsernameKey);
                String shpTokenUsernameKey = ConstantRedisKey.getShpTokenKey(username);
                String olsShopIdKey = ConstantRedisKey.getShopIdRedisKeyByToken(token);
                redisUtil.delete(shpTokenUsernameKey);
                redisUtil.delete(oldUserIdKey);
                redisUtil.delete(oldUserNumberKey);
                redisUtil.delete(olsShpUsernameKey);
                redisUtil.delete(olsShopIdKey);
            }
        }
    }

    @Override
    public void updateShpUser(ShpUser shpUser) {
        shpUserMapper.updateObject(shpUser);
    }

    @Override
    public VoEmployee geVoEmployeeByUsername(String username) {
        username = DESEncrypt.encodeUsername(username);
        return shpUserMapper.geVoEmployeeByUsername(username);
    }

    @Override
    public List<VoShpUser> queryShpUserList(ParamShpUser paramShpUser) {
        if (!StringUtils.isEmpty(paramShpUser.getPhone())) {
            paramShpUser.setPhone(DESEncrypt.encodeUsername(paramShpUser.getPhone()));
        }
        List<VoShpUser> list = shpUserMapper.listShpUser(paramShpUser);
        for (VoShpUser voShpUser : list) {
            voShpUser.setUsername(DESEncrypt.decodeUsername(voShpUser.getUsername()));
            voShpUser.setPhone(DESEncrypt.decodeUsername(voShpUser.getPhone()));

//            //???????????????????????????
//            String inviteUserId = voShpUser.getInviteUserId();
//            if (!LocalUtils.isEmptyAndNull(inviteUserId)) {
//                VoUserShopBase inviteUserShopBase = shpUserMapper.getShpUserBaseInfoByUserId(Integer.parseInt(inviteUserId));
//                if (null != inviteUserShopBase) {
//                    String inviteUserNumber = "" + inviteUserShopBase.getUserNumber();
//                    voShpUser.setInviteUserNumber(inviteUserNumber);
//                }
//            }
        }
        return list;
    }

    @Override
    public ShpUser getObjectById(String id) {
        return (ShpUser) shpUserMapper.getObjectById(LocalUtils.strParseInt(id));
    }

    @Override
    public ShpUserDetail selectDetailById(String id) {
        return shpUserDetailService.selectByUserId(id);
    }

    @Override
    public VoShpUserInfo selectInfoById(int shopId, String id) {
        ShpUserToken shpUserToken = shpUserTokenService.getShpUserTokenByUserId(Integer.parseInt(id));
        if (LocalUtils.isEmptyAndNull(shpUserToken)) {
            return null;
        } else {
            VoShpUserInfo shpUserInfo = new VoShpUserInfo();
            BeanUtils.copyProperties(shpUserToken, shpUserInfo);
            List<VoShopBase> shpShops = shpShopService.chooseShop(shpUserInfo.getFkShpUserId(), shpUserToken.getToken());
            shpUserInfo.setVoShopBases(shpShops);
            if (null == shpUserInfo.getFkShpShopId()) {
                shpUserInfo.setFkShpShopId(shopId);
            }
            List<VoUserRoleRef> voUserRoleRefs = shpUserRoleRefService.listUserRoleRefByUserId(shpUserInfo.getFkShpShopId(), shpUserInfo.getFkShpUserId());
            shpUserInfo.setUserRoleRefs(voUserRoleRefs);
            return shpUserInfo;
        }
    }

    @Override
    public List<VoShpUser> queryShpUserRelList(ParamShpUser paramShpUser) {
        if (paramShpUser.getUsername() != null) {
            paramShpUser.setUsername(DESEncrypt.encodeUsername(paramShpUser.getUsername()));
        }
        List<VoShpUser> list = shpUserMapper.listShpUserRel(paramShpUser);
        for (VoShpUser voShpUser : list) {
            voShpUser.setUsername(DESEncrypt.decodeUsername(voShpUser.getUsername()));
            voShpUser.setPhone(DESEncrypt.decodeUsername(voShpUser.getPhone()));
        }
        return list;
    }

    @Override
    public int registerShpUserWithRandomPwd(String shopName, String username, String ip) {
        //???????????????????????????????????????;
        String remark = "??????????????????";
        ShpUser shpUser = registerShpUser(username, null, remark, null);
        int userId = shpUser.getId();
        //??????6?????????????????????;
        String pwd = opSmsRecordService.generatorValidateCode();
        String pwdMd5 = DESEncrypt.md5Hex(pwd);
        updateShpUserPassword(userId, pwdMd5);
        Map<String, String> map = new HashMap<>();
        map.put("newUsername", username);
        map.put("newPassword", pwd);
        map.put("shopName", shopName);
        opSmsRecordService.sendSms(username, null, EnumSendSmsType.SHOP_REGISTER_USER, ip, map);
        return userId;
    }

    @Override
    public Boolean updateShpUserJiGuangRegId(Integer shpUserId, String jiGuangRegId) throws Exception {
        /**
         * step1.????????????
         */
        String errorMsg = "";
        if (null == shpUserId || StringUtil.isBlank(jiGuangRegId)) {
            errorMsg = "??????????????????????????????ID??????????????????ID?????????jiGuangRegId?????????";
            log.error(errorMsg);
            throw new Exception(errorMsg);
        }

        //??????????????????
        ShpUser shpUser = new ShpUser();
        shpUser.setId(shpUserId);
        shpUser.setJgPushRegId(jiGuangRegId);
        shpUser.setUpdateTime(new Date());
        return shpUserMapper.updateObject(shpUser) > 0;
    }

    @Override
    public List<Integer> selectAllValidUserId() {
        return shpUserMapper.selectAllValidUserId();
    }

    @Override
    public void modifyPassword(ParamModifyPwd modifyPwd) {
        VoShpUserSalt user = getVOShpUserSaltByUsername(modifyPwd.getUsername());
        boolean validatePwd = validatePwd(modifyPwd.getOldPassword(), user.getPassword(), user.getSalt());
        if (!validatePwd) {
            throw new MyException("???????????????!");
        }
        updateShpUserPassword(modifyPwd.getUserId(), modifyPwd.getNewPassword());
    }

    /**
     * ?????????????????????????????????ID
     *
     * @param usernameList
     * @return
     */
    @Override
    public List<Integer> selectUserIdListByUsernameList(List<String> usernameList) {
        return shpUserMapper.selectUserIdListByUsernameList(usernameList);
    }


    @Override
    public String getNicknameByUserId(int userId) {
        return shpUserMapper.getNicknameByUserId(userId);
    }

    @Override
    public String getUsernameByUserId(int userId) {
        String username = shpUserMapper.getUsernameByUserId(userId);
        if (!LocalUtils.isEmptyAndNull(username)) {
            username = DESEncrypt.decodeUsername(username);
        }
        return username;
    }

    @Override
    public VoEmployee getEmployeeForSalary(int shopId, int userId) {
        return shpUserMapper.getEmployeeForSalary(shopId, userId);
    }

    @Override
    public VoInviteList getInviteList(int userId, Integer pageNo) {
        VoInviteList voInviteList = new VoInviteList();
        //????????????
        Integer invitePersonNum = shpUserInviteService.getInvitePersonCountByUserId(userId);
        //?????????????????????
        PageHelper.startPage(pageNo, 10);
        List<VoInviteDetail> inviteDetailList = shpUserInviteService.getInviteDetailListByUserId(userId);
        for (VoInviteDetail voInviteDetail : inviteDetailList) {

            //???????????????
            String phone = voInviteDetail.getPhone();
            phone = DESEncrypt.decodeUsername(phone);
            String hidePhone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            voInviteDetail.setPhone(hidePhone);

            //??????????????????
            voInviteDetail.setHeadImgUrl(ConstantCommon.ossDomain + voInviteDetail.getHeadImgUrl());
        }

        voInviteList.setInvitePersonNum("" + invitePersonNum);
        voInviteList.setInviteDetailList(inviteDetailList);
        return voInviteList;
    }

    @Override
    public void updateUserNumberForMillion() {
        List<ShpUser> userList = shpUserMapper.listUserNumber();

        if (LocalUtils.isEmptyAndNull(userList)) {
            return;
        }
        for (ShpUser shpUser : userList) {
            Integer number = shpUser.getNumber();
            if (number < 100000) {
                String a = "99" + number;
                shpUser.setNumber(Integer.parseInt(a));
            } else if (number < 1000000) {
                String a = "9" + number;
                shpUser.setNumber(Integer.parseInt(a));
            }
            shpUserMapper.updateObject(shpUser);
        }
    }

    @Override
    public Integer getValidShpUserNumByShopId(Integer shopId) {
        return shpUserMapper.countValidRegPersonNumByShopId(shopId);
    }

    @Override
    public List<Integer> listUserIdByOpChannelId(Integer channelId) {
        return shpUserMapper.listUserIdByOpChannelId(channelId);
    }

    /**
     * ???????????????????????????????????????
     *
     * @param username
     * @return
     */
    @Override
    public VoShpUser getUserInfoByUsername(String username) {
        username = DESEncrypt.encodeUsername(username);
        VoShpUser voShpUser = shpUserMapper.getUserInfoByUsername(username);
        if (voShpUser != null) {
            voShpUser.setUsername(DESEncrypt.decodeUsername(voShpUser.getUsername()));
        }
        return voShpUser;
    }


    public static void main(String[] args) {

        String a = "{HTTP/1.1 500 Internal Server Error [Date: Sun, 26 Sep 2021 09:25:17 GMT, Content-Type: application/json;charset=UTF-8, Transfer-Encoding: chunked, Connection: keep-alive, Keep-Alive: timeout=25, Expires: 0, Cache-Control: no-cache, no-store, max-age=0, must-revalidate, Server: Tengine, X-XSS-Protection: 1; mode=block, Pragma: no-cache, X-Frame-Options: DENY, X-Ca-Request-Id: 5E37AC94-BD3B-4BBF-9D06-19C8D0144FC9, X-Content-Type-Options: nosniff, X-Content-Type-Options: nosniff] ResponseEntityProxy{[Content-Type: application/json;charset=UTF-8,Chunked: true]}}";
        String aesEncryptKey = JSONObject.parseObject(a).getJSONObject("data").getString("aesEncryptKey");
        String mobile = JSONObject.parseObject(a).getJSONObject("data").getString("mobile");
    }

    /**
     * ??????????????????
     *
     * @param paramVerify
     */
    @Override
    public BaseResult getVerifyInfo(ParamVerify paramVerify) {
        CloseableHttpResponse closeableHttpResponse;
        try {
            closeableHttpResponse = UMHttps.httpResponse(paramVerify.getUmToken(), paramVerify.getPlatform());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new MyException(e.getMessage());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new MyException(e.getMessage());
        }
        JSONObject jsonObject;
        try {
            String entityUtil = EntityUtils.toString(closeableHttpResponse.getEntity());
            jsonObject = JSONObject.parseObject(entityUtil);
        } catch (IOException e) {
            e.printStackTrace();
            throw new MyException(e.getMessage());
        }
        Boolean success = jsonObject.getBoolean("success");

        if (success) {
            String httpRespPhone;
            JSONObject data = jsonObject.getJSONObject("data");
            String mobile = data.getString("mobile");
            String aesEncryptKey = data.getString("aesEncryptKey");
            byte[] aesKey;
            try {
                String pricateKey;
                if (paramVerify.getPlatform().equals("IOS")) {
                    pricateKey = UMConfig.IOS_PRICATE_KEY;
                } else {
                    pricateKey = UMConfig.ANDROID_PRICATE_KEY;
                }
                aesKey = RSAUtil.privateDecryptRSA(aesEncryptKey, pricateKey);
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(e.getMessage());
            }
            try {
                httpRespPhone = new String(RSAUtil.decryptAES(RSAUtil.base642Byte(mobile), RSAUtil.loadKeyAES(aesKey)));
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(e.getMessage());
            }
            String phone = DESEncrypt.encodeUsername(httpRespPhone);
            ShpUser shpUser = shpUserMapper.getUserByPhone(phone);
            BaseResult baseResult;
            if (shpUser == null) {
                //??????
                baseResult = registerShpUserAndLogin(httpRespPhone, null, null);
                if (ConstantCommon.OK.equals(baseResult.getCode())) {
                    redisUtil.setExMINUTES(paramVerify.getNoSetPassword() + httpRespPhone, paramVerify.getIpAddress(), 5);
                }
            } else {
                //??????
                //String username = DESEncrypt.decodeUsername(shpUser.getUsername());
                baseResult = shpUsernameLogin(httpRespPhone);
            }
            return baseResult;
        } else {
            throw new MyException("??????????????????");
        }

    }

    /**
     * ????????????
     *
     * @param param
     * @return
     */
    @Override
    public BaseResult circulationLogin(ParamQRCodeLoginQuery param) {
        //String[] split = param.getCodeKey().split(":");
        String value = redisUtil.get(param.getCodeKey());
        if (StringUtil.isBlank(value)) {
            return BaseResult.errorResult(EnumCode.ERROR_EXPIRED);
        }
        if (StringUtil.isNotBlank(param.getUnload()) && param.getUnload().equals(unload)) {
            redisUtil.setExSECONDS(param.getCodeKey(), unload, out_time);
            return BaseResult.okResult();
        }
        if (value.equals(not_pass)) {
            throw new MyException("????????????");
        }
        if (value.equals(ok_qrcode_scan)) {
            return BaseResult.okResult("????????????????????????????????????", EnumCode.OK_QRCODE_SCAN);
        }
        ShpUser shpUser = (ShpUser) shpUserMapper.getObjectById(Integer.parseInt(value));
        if (shpUser == null) {
            throw new MyException("???????????????");
        }
        String username = DESEncrypt.decodeUsername(shpUser.getUsername());
        VoShpUserSalt voShpUserSalt = shpUserMapper.getVOShpUserSaltByUsername(shpUser.getUsername());
        //?????????????????? ????????????????????????
        voShpUserSalt.setUsername(username);
        HashMap hashMap = chooseLoginShop(voShpUserSalt, true);
        return BaseResult.okResult(hashMap);
    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    @Override
    public String getLoginQRCode() {
        String redisKey = "login:" + UUID.randomUUID().toString();
        redisUtil.setExSECONDS(redisKey, not_pass, out_time);
        return redisKey;
    }


    /**
     * ???????????????;<br/>
     * ????????????????????????;
     *
     * @param username ?????????
     * @param remark   ??????
     * @return
     */
    public ShpUser addShpUser(String username, String remark, Integer opChannelId, String nickName) {
        try {
            String redisUsername = username;
            ShpUserNumber shpUserNumber = shpUserNumberService.getShpUserNumberOverId();
            ShpUser shpUser = createDefaultShpUser(username, shpUserNumber.getNumber(), remark, opChannelId, nickName);
            log.info("=====??????===start");
            log.info("=====??????===end");
            int userId = saveShpUserReturnId(shpUser);
            String key = ConstantRedisKey.ALL_USERNAME;
            String allUsername = redisUtil.get(key);
            redisUtil.set(key, allUsername + "," + redisUsername);
            shpUserNumberService.usedShpUserNumber(userId, shpUserNumber);
            return shpUser;
        } catch (Exception e) {
            log.error("=======????????????????????????====: " + e.getMessage(), e);
            //??????try catch???????????????; ???????????????????????????????????????;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new MyException("??????????????????: " + e.getMessage());
        }
    }

    /**
     * ??????????????? ShpUser
     *
     * @param username ?????????
     * @return
     */
    public ShpUser createDefaultShpUser(String username, int number, String remark, Integer opChannelId, String nickName) {
        if (LocalUtils.isEmptyAndNull(nickName)) {
            nickName = "??????" + LocalUtils.getLastPhoneNumber(username);
        }
        //????????????
        username = DESEncrypt.encodeUsername(username);
        ShpUser shpUser = new ShpUser();
        shpUser.setUsername(username);
        shpUser.setPhone(username);
        shpUser.setType("0");
        shpUser.setState("1");
        shpUser.setNumber(number);
        opChannelId = null == opChannelId ? 0 : opChannelId;
        shpUser.setFkOpChannelId(opChannelId);
        shpUser.setInsertTime(new Date());
        shpUser.setNickname(nickName);
        shpUser.setRemark(remark);
        shpUser.setHeadImgUrl(ConstantCommon.DEFAULT_USER_HEAD_IMG);
        return shpUser;
    }


    /**
     * ????????????????????????;
     *
     * @param username ?????????
     */
    private void existsShpUser(EnumSendSmsType sendSmsType, String username) {
        //???????????????????????????
        boolean isExists = existsShpUserByUsername(username);
        //????????????????????????
        switch (sendSmsType) {
            case REGISTER:
                if (isExists) {
                    log.info("========??????????????????:" + username);
                    throw new MyException(EnumCode.ERROR_EXIST_USER);
                }
                break;
            case LOGIN:
            case RESET_PASSWORD:
                //??????????????????????????????;
                if (!isExists) {
                    log.info("========???????????????:" + username);
                    throw new MyException(EnumCode.ERROR_NOT_EXIST_USER);
                }
                break;
            default:
                break;
        }

    }

    /**
     * ????????????token;??????30???;
     *
     * @param userId ShpUser.id
     * @return
     */
    private void getShpUserToken(final String newToken, final int userId, final int userNumber,
                                 final String username, final int shopId, boolean isAdminLogin) {

        if (isAdminLogin) {
            //????????????;
            String shpAdminTokenKey = RedisKeyAdminLogin.getShpAdminTokenKey(username);

            String oldToken = redisUtil.get(shpAdminTokenKey);

            //???????????????key
            String newUserIdKey = RedisKeyAdminLogin.getShpUserIdRedisKeyByToken(newToken);
            String newUserNumberKey = RedisKeyAdminLogin.getUserNumberRedisKeyByToken(newToken);
            String newUsernameKey = RedisKeyAdminLogin.getShpUsernameRedisKeyByToken(newToken);
            String newShopIdKey = RedisKeyAdminLogin.getShopIdRedisKeyByToken(newToken);

            //????????????; ????????????????????????key????????????
            exitLogin(oldToken, true);
            //?????????????????????3??????,3?????????????????????,?????????????????????
            int minutes = 60 * 3;
            //?????????key
            redisUtil.setExMINUTES(shpAdminTokenKey, newToken, minutes);
            redisUtil.setExMINUTES(newUserIdKey, userId + "", minutes);
            redisUtil.setExMINUTES(newUserNumberKey, userNumber + "", minutes);
            redisUtil.setExMINUTES(newUsernameKey, username, minutes);
            redisUtil.setExMINUTES(newShopIdKey, shopId + "", minutes);


        } else {
            //app??????;
            //eg: key=shp_token_15112304365
            String shpTokenUsernameKey = ConstantRedisKey.getShpTokenKey(username);
            String oldToken = redisUtil.get(shpTokenUsernameKey);
            //???key???

            String newUserIdKey = ConstantRedisKey.getShpUserIdRedisKeyByToken(newToken);
            String newUserNumberKey = ConstantRedisKey.getUserNumberRedisKeyByToken(newToken);
            String newUsernameKey = ConstantRedisKey.getShpUsernameRedisKeyByToken(newToken);
            String newShopIdKey = ConstantRedisKey.getShopIdRedisKeyByToken(newToken);

            //????????????; ????????????????????????key????????????
            exitLogin(oldToken);
            int day = 30;
            //?????????key
            redisUtil.setEx(shpTokenUsernameKey, newToken, day);
            redisUtil.setEx(newUserIdKey, userId + "", day);
            redisUtil.setEx(newUserNumberKey, userNumber + "", day);
            redisUtil.setEx(newUsernameKey, username, day);
            redisUtil.setEx(newShopIdKey, shopId + "", day);
        }


    }

//    /**
//     * ?????????????????????
//     * @param fkShpShopId
//     * @param fkUserId
//     * @return
//     */
//    @Override
//    public Boolean isShopOwner(Integer fkShpShopId, Integer fkUserId) throws Exception{
//        ShpShop shpShop = shpShopService.selectById("" + fkShpShopId);
//        if(null == shpShop){
//            throw new Exception("????????????????????????????????????????????????????????????ID???="+fkShpShopId);
//        }
//
//        Integer shopOwnerUserId = shpShop.getFkShpUserId();
//        if(null == shopOwnerUserId){
//            throw new Exception("????????????????????????????????????????????????????????????ID???="+fkShpShopId);
//        }
//
//        return shopOwnerUserId.equals(fkUserId);
//    }

    private String generatorToken(boolean isAdminLogin) {
        String token = LocalUtils.getUUID();
        return isAdminLogin ? "admin" + token : token;
    }

    /**
     * ????????????????????????????????????
     *
     * @param password
     * @param encryptedPassword
     * @param salt
     * @return
     */
    private boolean validatePwd(String password, String encryptedPassword, String salt) {
        boolean authenticate = false;
        try {
            authenticate = PBKDF2Util.authenticate(password, encryptedPassword, salt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authenticate;
    }
}
