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
 * 业务逻辑层(实现)
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
     * 未登录标识
     */
    public static final String not_pass = "NOT_PASS";

    /**
     * 刷新页面标识符
     */
    public static final String unload = "UNLOAD";

    public static final String ok_qrcode_scan = "OK_QRCODE_SCAN";
    /**
     * 二维码过期时间
     */
    public static final Long out_time = 300L;
    /**
     * 默认登录上一次店铺
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
        //用户不存在;返回给用户的消息要统称为-用户名密码错误
        if (LocalUtils.isEmptyAndNull(voShpUserSalt)) {
            EnumCode enumCode = EnumCode.ERROR_USERNAME;
            if (isPwdLogin) {
                enumCode = EnumCode.ERROR_USERNAME_PASSWORD;
            }
            throw new MyException(enumCode);
        }
        if (!ConstantCommon.ONE.equals(voShpUserSalt.getState())) {
            throw new MyException("该帐号已被禁用,请联系客服!");
        }
        return voShpUserSalt;
    }

    @Override
    public BaseResult shpUsernameLogin(String username) {
        return shpUsernameLogin(username, false);
    }

    @Override
    public BaseResult shpUsernameLogin(String username, boolean isPwdLogin) {
        //true:用帐号密码登录 | false:短信验证码登录
        VoShpUserSalt voShpUserSalt = getShpUserSalt(username, isPwdLogin);
        //登录成功之后 选择要登录的店铺
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
//        //帐号密码登录
//        if ("18434363494".equals(username)) {
//            log.error("***********【苹果审核用手机号验证码登录】2shpLogin: *****************");
//            log.error("***********【苹果审核用手机号验证码登录】3voShpUserSalt="+ JSON.toJSONString(voShpUserSalt));
//            log.error("*********** 【苹果审核用手机号验证码登录】4validatePwd:\r\n" +
//                    "password={}\r\n,voShpUserSalt.getPassword()={}\r\n,voShpUserSalt.getSalt()={} " +
//                    "*****************",password,voShpUserSalt.getPassword(),voShpUserSalt.getSalt());
//        }

        boolean validatePwd = validatePwd(password, voShpUserSalt.getPassword(), voShpUserSalt.getSalt());
        if (!validatePwd) {
            //用户密码错误
            return BaseResult.errorResult(EnumCode.ERROR_USERNAME_PASSWORD);
        }
        //登录成功之后 选择要登录的店铺
        voShpUserSalt.setUsername(username);
        HashMap hashMap = chooseLoginShop(voShpUserSalt, isAdminLogin);
        return BaseResult.okResult(hashMap);
    }

    //@Override
    //public BaseResult shpAdminLogin(String username, String password) {
    //    VoShpUserSalt voShpUserSalt = shpLogin(username, true);
    //    boolean validatePwd = validatePwd(password, voShpUserSalt.getPassword(), voShpUserSalt.getSalt());
    //    if (!validatePwd) {
    //        //用户密码错误
    //        return BaseResult.errorResult(EnumCode.ERROR_USERNAME_PASSWORD);
    //    }
    //    //登录成功之后 选择要登录的店铺
    //    voShpUserSalt.setUsername(username);
    //    HashMap hashMap = chooseLoginShop(voShpUserSalt);
    //    return BaseResult.okResult(hashMap);
    //}

    /**
     * 选择店铺进行登录,如果只有一个店铺则直接登录;
     *
     * @param voShpUserSalt
     * @param isAdminLogin  是否后台登录; true:后台登录 | false:app登录
     * @return
     */
    private HashMap chooseLoginShop(VoShpUserSalt voShpUserSalt, boolean isAdminLogin) {
        final String token = generatorToken(isAdminLogin);
        int userId = voShpUserSalt.getUserId();
        //用户默认登录的店铺ID;shopId为0,则代表没有默认登录的店铺;
        final int dbShopId = voShpUserSalt.getShopId();
        int shopId = dbShopId;
        //是否默认登录上次店铺
        String defaultLogin = voShpUserSalt.getDefaultLogin();
        //返回结果集
        HashMap<String, Object> hashMap = LocalUtils.getHashMap(token);
        boolean isDefaultLogin = DEFAULT_LOGIN.equals(defaultLogin) && dbShopId != 0;
        boolean chooseShop = false;
        String username = voShpUserSalt.getUsername();
        Integer userNumber = voShpUserSalt.getUserNumber();
        //默认登录上一个店铺
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
                    //上述已有店铺没找到当前关联的店铺,证明已经被该店铺移除
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

        //重新选择店铺
        if (chooseShop) {
            for (VoShopBase voShopBase : voShopBasesList) {
                //头像路径
                voShopBase.setShopHeadImgUrl(servicesUtil.formatImgUrl(voShopBase.getShopHeadImgUrl()));
            }
            //没有任何店铺关联
            shopId = 0;
            //shopNumber = 0;
            defaultLogin = "0";
            hashMap.put("objList", voShopBasesList);
        }

        //把默认登录的店铺设置为0;
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

        //发送短信的枚举类型
        String sysType = sendSmsType.getCode();
        //redis-key值
        String sendYzmLeftTime = ConstantRedisKey.getSendYzmLeftTime(username);
        String yzmDayTotalKey = ConstantRedisKey.getYzmDayTotal(username);
        String ipYzmDayTotalKey = ConstantRedisKey.getYzmDayTotal(ip);
        String imageCodeKey = ConstantRedisKey.getImageCode(username);
        String smsCodeKey = ConstantRedisKey.getYzmSmsCodeKey(sysType, username);
        String smsLimitKey = ConstantRedisKey.SMS_LIMIT;
        String ipSmsLimitKey = ConstantRedisKey.IP_SMS_LIMIT;
        //redis-value值
        String sendYzmLeftTimeValue = redisUtil.get(sendYzmLeftTime);
        String yzmDayTotalValue = redisUtil.get(yzmDayTotalKey);
        String ipYzmDayTotalValue = redisUtil.get(ipYzmDayTotalKey);
        String imageCodeValue = redisUtil.get(imageCodeKey);
        String smsCodeValue = redisUtil.get(smsCodeKey);
        String smsLimitValue = redisUtil.get(smsLimitKey);
        String ipSmsLimitValue = redisUtil.get(ipSmsLimitKey);

        //验证码倒计时未结束
        if (!LocalUtils.isEmptyAndNull(sendYzmLeftTimeValue)) {
            long timeout = redisUtil.getExpire(sendYzmLeftTime);
            String getYzmInfo = timeout + "秒后可获取验证码";
            return BaseResult.defaultErrorWithMsg(getYzmInfo);
        }

        //获取数据库配置的短信限制值;手机号限制;默认1次
        int smsLimit = LocalUtils.isEmptyAndNull(smsLimitValue) ? 1 : Integer.parseInt(smsLimitValue);
        //获取数据库配置的短信限制值;ip段限制;默认1次
        int ipSmsLimit = LocalUtils.isEmptyAndNull(ipSmsLimitValue) ? 1 : Integer.parseInt(ipSmsLimitValue);

        //初始化 手机号日发短信总数
        if (LocalUtils.isEmptyAndNull(yzmDayTotalValue)) {
            yzmDayTotalValue = "0";
        }

        //初始化 ip日发短信总数
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

        //24小时内已经获取验证码达到N次,需要进行图形验证码验证
        boolean emptyImageCode = LocalUtils.isEmptyAndNull(imageCode);

        //获取短息次数是否超出当天最大次数,如果是则需要校验图形验证码;
        boolean isSmsLimit = (yzmDayTotal > smsLimit) || (ipYzmDayTotal > ipSmsLimit);
        if (emptyImageCode && isSmsLimit) {
            return BaseResult.errorResult(EnumCode.ERROR_NEED_IMAGE_CODE);
        }

        if (isSmsLimit) {
            //校验图形验证码
            if (!imageCode.equalsIgnoreCase(imageCodeValue)) {
                return BaseResult.errorResult(EnumCode.ERROR_IMAGE_CODE);
            }
            //图形验证码校验通过之后则清除;
            redisUtil.delete(imageCodeKey);
        }

        //该用户是否已存在;
        existsShpUser(sendSmsType, username);

        //获取验证码;如果没有已存在的验证码则重新生成;
        if (LocalUtils.isEmptyAndNull(smsCodeValue)) {
            smsCodeValue = opSmsRecordService.generatorValidateCode();
        }
        boolean isDev = ConstantCommon.DEV.equals(ConstantCommon.springProfilesActive);
        if (isDev) {
            //开发环境;固定验证码;
            smsCodeValue = "123456";
        }
        //发送验证码逻辑
        opSmsRecordService.sendSms(username, smsCodeValue, sendSmsType, ip, null);

        //发验证码,添加发送验证码记录
        BaseResult baseResult = opSmsRecordService.sendValidateCode(smsCodeValue);

        //设置60秒后可再次获取验证码
        redisUtil.setExMINUTES(sendYzmLeftTime, ip, 1);
        //设置验证码5分钟有效
        redisUtil.setExMINUTES(smsCodeKey, smsCodeValue, 5);

        return baseResult;
    }

    @Override
    public ShpUser registerShpUser(String username, String inviteCode, String remark, String nickName) {
        //前端参数
        //该注册用户是否已存在;
        existsShpUser(EnumSendSmsType.REGISTER, username);


        //注册邀请码是否存在
        boolean emptyInviteCode = LocalUtils.isEmptyAndNull(inviteCode);
        Integer inviteUserId = 0;
        String channelCode;
        Integer opChannelId = null;


        //小程序有访问记录,按照运营规则,此人算是小程序首次访问的分享人的用户;
        Integer existsUserId = proShareSeeUserService.existsWxUserReturnUserId("1", username);
        if (!LocalUtils.isEmptyAndNull(existsUserId)) {
            inviteUserId = existsUserId;
        }

        if (!emptyInviteCode && existsUserId == null) {
            //2020-08-11 新增渠道邀请码判断，如果邀请码位数为3
            //长度不为3，则走普通用户邀请码判断逻辑
            int inviteCodeLength = inviteCode.length();
            if (inviteCodeLength == 3) {
                channelCode = inviteCode;
                //2020-08-11 sanjin
                //如果渠道码不为空，则查询op_channel表中是否有对应记录
                //有的话，则插入op_channel.id,没有的话则不对渠道码进行操作
                OpChannel opChannel = opChannelMapper.selectByChannelCode(channelCode);
                if (LocalUtils.isEmptyAndNull(opChannel)) {
                    throw new MyException(EnumCode.ERROR_NOT_INVITE_CODE);
                }
                opChannelId = opChannel.getId();
            } else {
                //用户邀请码判断逻辑
                inviteUserId = getShpUserIdByNumber(Integer.parseInt(inviteCode));
                if (LocalUtils.isEmptyAndNull(inviteUserId)) {
                    log.info("========邀请码不存在:" + inviteCode);
                    throw new MyException(EnumCode.ERROR_NOT_INVITE_CODE);
                }
            }
        }
        //该用户不存在,且注册新用户
        ShpUser shpUser = addShpUser(username, remark, opChannelId, nickName);
        if (shpUser == null) {
            throw new MyException(EnumCode.ERROR_ADMIN);
        }
        int beInviteUserId = shpUser.getId();
        //如果邀请码不为空,并且渠道id为空
        //表示传入的参数是[用户邀请码],绑定用户邀请关系
        if (!emptyInviteCode && LocalUtils.isEmptyAndNull(opChannelId) || existsUserId != null) {
            //添加好友邀请关系;启动线程;不阻塞主线程注册成功;
            //Integer finalInviteUserId = inviteUserId;
            //ScheduledExecutorService executorService = ThreadUtils.getInstance().executorService;
            //executorService.execute(() -> {
            //    log.info("========【添加邀请关系】==========邀请人:" + finalInviteUserId + " | 被邀请人:" + beInviteUserId);
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
        //注册成功-返回用户token;新注册,暂时没有店铺,故没有店铺id和店铺编码
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
        //该注册用户是否已存在;
        existsShpUser(EnumSendSmsType.RESET_PASSWORD, username);
        VoShpUserSalt voShpUserSalt = getVOShpUserSaltByUsername(username);
        //是否首次设置密码
        String firstSetPassword = "register";
        int dbShpUserId = voShpUserSalt.getUserId();
        //未存在过密码;首次设置新密码
        if (firstSetPassword.equals(sendSmsType.getCode())) {
            String oldPassword = voShpUserSalt.getPassword();
            int userId = paramNewPwd.getTokenUserId();
            if (dbShpUserId != userId) {
                //不是同一个用户
                return BaseResult.defaultErrorWithMsg("请勿篡改ta人密码!");
            }
            //首次设置密码时,如果旧密码存在则不允许设置;
            if (!LocalUtils.isEmptyAndNull(oldPassword)) {
                //存在旧密码
                return BaseResult.defaultErrorWithMsg("密码已存在,请进行【忘记密码】操作!");
            }
        }
        updateShpUserPassword(dbShpUserId, newPassword);
        return BaseResult.defaultOkResultWithMsg("密码设置成功");
    }

    @Override
    public void updateShpUserPassword(int userId, String newPassword) {
        //获取32位盐
        String salt = PBKDF2Util.getSaltDefault();
        EnumSaltType saltType = EnumSaltType.SHP_MODEL;
        try {
            //对密码进行加盐操作
            newPassword = PBKDF2Util.getEncryptedPassword(newPassword, salt);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MyException("密码加盐失败!");
        }
        //设置用户密码
        ShpUser shpUser = new ShpUser();
        shpUser.setId(userId);
        shpUser.setPassword(newPassword);
        shpUser.setUpdateTime(new Date());
        shpUserMapper.updateObject(shpUser);
        //获取数据库的盐值表;
        SysSalt dbSysSalt = sysSaltService.getSysSaltByUserIdAndType(userId, saltType);
        //是否存在盐值表;
        if (LocalUtils.isEmptyAndNull(dbSysSalt)) {
            //新增用户盐值表
            SysSalt sysSalt = sysSaltService.createSysSalt(userId, saltType, salt);
            sysSaltService.saveSysSalt(sysSalt);
        } else {
            //存在盐值表
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
            //删除该用户相关的key
            if (isAdminLogin) {
                //后台退出登录
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
                //app退出登录
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

//            //设置邀请人用户编号
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
        //为当前的手机号码注册新帐号;
        String remark = "密码随机生成";
        ShpUser shpUser = registerShpUser(username, null, remark, null);
        int userId = shpUser.getId();
        //生成6位存数字的密码;
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
         * step1.参数校验
         */
        String errorMsg = "";
        if (null == shpUserId || StringUtil.isBlank(jiGuangRegId)) {
            errorMsg = "更新店铺用户极光注册ID异常，【用户ID】或【jiGuangRegId】为空";
            log.error(errorMsg);
            throw new Exception(errorMsg);
        }

        //设置用户密码
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
            throw new MyException("原密码错误!");
        }
        updateShpUserPassword(modifyPwd.getUserId(), modifyPwd.getNewPassword());
    }

    /**
     * 根据用户名列表查询用户ID
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
        //邀请人数
        Integer invitePersonNum = shpUserInviteService.getInvitePersonCountByUserId(userId);
        //邀请人详情列表
        PageHelper.startPage(pageNo, 10);
        List<VoInviteDetail> inviteDetailList = shpUserInviteService.getInviteDetailListByUserId(userId);
        for (VoInviteDetail voInviteDetail : inviteDetailList) {

            //设置手机号
            String phone = voInviteDetail.getPhone();
            phone = DESEncrypt.decodeUsername(phone);
            String hidePhone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            voInviteDetail.setPhone(hidePhone);

            //设置头像地址
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
     * 根据用户手机号获取用户信息
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
     * 一键登录接口
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
                //注册
                baseResult = registerShpUserAndLogin(httpRespPhone, null, null);
                if (ConstantCommon.OK.equals(baseResult.getCode())) {
                    redisUtil.setExMINUTES(paramVerify.getNoSetPassword() + httpRespPhone, paramVerify.getIpAddress(), 5);
                }
            } else {
                //登录
                //String username = DESEncrypt.decodeUsername(shpUser.getUsername());
                baseResult = shpUsernameLogin(httpRespPhone);
            }
            return baseResult;
        } else {
            throw new MyException("一键登录失败");
        }

    }

    /**
     * 轮训登录
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
            throw new MyException("暂未登录");
        }
        if (value.equals(ok_qrcode_scan)) {
            return BaseResult.okResult("二维码已扫描，请确认登陆", EnumCode.OK_QRCODE_SCAN);
        }
        ShpUser shpUser = (ShpUser) shpUserMapper.getObjectById(Integer.parseInt(value));
        if (shpUser == null) {
            throw new MyException("用户不存在");
        }
        String username = DESEncrypt.decodeUsername(shpUser.getUsername());
        VoShpUserSalt voShpUserSalt = shpUserMapper.getVOShpUserSaltByUsername(shpUser.getUsername());
        //登录成功之后 选择要登录的店铺
        voShpUserSalt.setUsername(username);
        HashMap hashMap = chooseLoginShop(voShpUserSalt, true);
        return BaseResult.okResult(hashMap);
    }

    /**
     * 获取登录二维码数据
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
     * 注册新用户;<br/>
     * 作为一个事务处理;
     *
     * @param username 用户名
     * @param remark   备注
     * @return
     */
    public ShpUser addShpUser(String username, String remark, Integer opChannelId, String nickName) {
        try {
            String redisUsername = username;
            ShpUserNumber shpUserNumber = shpUserNumberService.getShpUserNumberOverId();
            ShpUser shpUser = createDefaultShpUser(username, shpUserNumber.getNumber(), remark, opChannelId, nickName);
            log.info("=====睡眠===start");
            log.info("=====睡眠===end");
            int userId = saveShpUserReturnId(shpUser);
            String key = ConstantRedisKey.ALL_USERNAME;
            String allUsername = redisUtil.get(key);
            redisUtil.set(key, allUsername + "," + redisUsername);
            shpUserNumberService.usedShpUserNumber(userId, shpUserNumber);
            return shpUser;
        } catch (Exception e) {
            log.error("=======【注册用户失败】====: " + e.getMessage(), e);
            //对于try catch的方法里面; 事务要回滚必须显性操作回滚;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new MyException("注册用户失败: " + e.getMessage());
        }
    }

    /**
     * 生成新实体 ShpUser
     *
     * @param username 用户名
     * @return
     */
    public ShpUser createDefaultShpUser(String username, int number, String remark, Integer opChannelId, String nickName) {
        if (LocalUtils.isEmptyAndNull(nickName)) {
            nickName = "尾号" + LocalUtils.getLastPhoneNumber(username);
        }
        //对称加密
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
     * 该用户是否已存在;
     *
     * @param username 用户名
     */
    private void existsShpUser(EnumSendSmsType sendSmsType, String username) {
        //注册用户是否已存在
        boolean isExists = existsShpUserByUsername(username);
        //根据业务类型判断
        switch (sendSmsType) {
            case REGISTER:
                if (isExists) {
                    log.info("========用户名已存在:" + username);
                    throw new MyException(EnumCode.ERROR_EXIST_USER);
                }
                break;
            case LOGIN:
            case RESET_PASSWORD:
                //该注册用户是否已存在;
                if (!isExists) {
                    log.info("========用户不存在:" + username);
                    throw new MyException(EnumCode.ERROR_NOT_EXIST_USER);
                }
                break;
            default:
                break;
        }

    }

    /**
     * 生成用户token;为期30天;
     *
     * @param userId ShpUser.id
     * @return
     */
    private void getShpUserToken(final String newToken, final int userId, final int userNumber,
                                 final String username, final int shopId, boolean isAdminLogin) {

        if (isAdminLogin) {
            //后台登录;
            String shpAdminTokenKey = RedisKeyAdminLogin.getShpAdminTokenKey(username);

            String oldToken = redisUtil.get(shpAdminTokenKey);

            //后台登录的key
            String newUserIdKey = RedisKeyAdminLogin.getShpUserIdRedisKeyByToken(newToken);
            String newUserNumberKey = RedisKeyAdminLogin.getUserNumberRedisKeyByToken(newToken);
            String newUsernameKey = RedisKeyAdminLogin.getShpUsernameRedisKeyByToken(newToken);
            String newShopIdKey = RedisKeyAdminLogin.getShopIdRedisKeyByToken(newToken);

            //重新登录; 相当于把之前的旧key全部删除
            exitLogin(oldToken, true);
            //后台登录只保持3小时,3小时无任何操作,则需要重新登录
            int minutes = 60 * 3;
            //存储新key
            redisUtil.setExMINUTES(shpAdminTokenKey, newToken, minutes);
            redisUtil.setExMINUTES(newUserIdKey, userId + "", minutes);
            redisUtil.setExMINUTES(newUserNumberKey, userNumber + "", minutes);
            redisUtil.setExMINUTES(newUsernameKey, username, minutes);
            redisUtil.setExMINUTES(newShopIdKey, shopId + "", minutes);


        } else {
            //app登录;
            //eg: key=shp_token_15112304365
            String shpTokenUsernameKey = ConstantRedisKey.getShpTokenKey(username);
            String oldToken = redisUtil.get(shpTokenUsernameKey);
            //新key值

            String newUserIdKey = ConstantRedisKey.getShpUserIdRedisKeyByToken(newToken);
            String newUserNumberKey = ConstantRedisKey.getUserNumberRedisKeyByToken(newToken);
            String newUsernameKey = ConstantRedisKey.getShpUsernameRedisKeyByToken(newToken);
            String newShopIdKey = ConstantRedisKey.getShopIdRedisKeyByToken(newToken);

            //重新登录; 相当于把之前的旧key全部删除
            exitLogin(oldToken);
            int day = 30;
            //存储新key
            redisUtil.setEx(shpTokenUsernameKey, newToken, day);
            redisUtil.setEx(newUserIdKey, userId + "", day);
            redisUtil.setEx(newUserNumberKey, userNumber + "", day);
            redisUtil.setEx(newUsernameKey, username, day);
            redisUtil.setEx(newShopIdKey, shopId + "", day);
        }


    }

//    /**
//     * 校验是否是店长
//     * @param fkShpShopId
//     * @param fkUserId
//     * @return
//     */
//    @Override
//    public Boolean isShopOwner(Integer fkShpShopId, Integer fkUserId) throws Exception{
//        ShpShop shpShop = shpShopService.selectById("" + fkShpShopId);
//        if(null == shpShop){
//            throw new Exception("数据库中没有查询到对应的店铺信息。【店铺ID】="+fkShpShopId);
//        }
//
//        Integer shopOwnerUserId = shpShop.getFkShpUserId();
//        if(null == shopOwnerUserId){
//            throw new Exception("数据库中没有查询到对应的店长信息。【店铺ID】="+fkShpShopId);
//        }
//
//        return shopOwnerUserId.equals(fkUserId);
//    }

    private String generatorToken(boolean isAdminLogin) {
        String token = LocalUtils.getUUID();
        return isAdminLogin ? "admin" + token : token;
    }

    /**
     * 校验密码是否和数据库一致
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
