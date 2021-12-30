package com.luxuryadmin.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.constant.RedisKeyAdminLogin;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author monkey king
 * @Date 2019/12/14 19:45
 */
@Component
@Slf4j
public class ServicesUtil {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 获取userId--App端
     *
     * @param token
     * @return
     */
    public int getShpUserIdByToken(String token) {
        String shpUserId;
        if (!LocalUtils.isEmptyAndNull(token) && token.startsWith("admin")) {
            //后台登录的token
            shpUserId = redisUtil.get(RedisKeyAdminLogin.getShpUserIdRedisKeyByToken(token));
        } else {
            shpUserId = redisUtil.get(ConstantRedisKey.getShpUserIdRedisKeyByToken(token));
        }
        if (LocalUtils.isEmptyAndNull(shpUserId)) {
            throw new MyException(EnumCode.ERROR_NO_LOGIN);
        }
        return Integer.parseInt(shpUserId);
    }
    /**
     * 获取username--Admin端
     *
     * @param token
     * @return
     */
    public String getShpUserNameByToken(String token) {

        String key = ConstantRedisKey.getAdminTokenKey(token);
        String userStrJson = redisUtil.get(key);
        if (LocalUtils.isEmptyAndNull(userStrJson)) {
            throw new MyException(EnumCode.ERROR_NO_LOGIN);
        }
        return userStrJson;
    }

    /**
     * 如果对象为null,则抛出未登录异常
     *
     * @param object
     */
    public void emptyThenGoLogin(Object object) {
        if (LocalUtils.isEmptyAndNull(object)) {
            throw new MyException(EnumCode.ERROR_NO_LOGIN);
        }
    }

    /**
     * 获取shopId--App端
     *
     * @param token
     * @return
     */
    public int getShopIdByToken(String token) {

        String shopId;
        if (!LocalUtils.isEmptyAndNull(token) && token.startsWith("admin")) {
            //后台登录的token
            shopId = redisUtil.get(RedisKeyAdminLogin.getShopIdRedisKeyByToken(token));
        } else {
            shopId = redisUtil.get(ConstantRedisKey.getShopIdRedisKeyByToken(token));
        }
        if (LocalUtils.isEmptyAndNull(shopId)) {
            throw new MyException(EnumCode.ERROR_NO_LOGIN);
        }
        return Integer.parseInt(shopId);
    }


    /**
     * 验证pojo参数是否为null
     */
    public  void validControllerParam(BindingResult result) {
        if (null != result && result.hasErrors()) {
            throw new MyException(result.getFieldError().getDefaultMessage());
        }
    }


    public String getAttributeCn(String attributeUs, boolean showLongName) {
        //属性名称：10:自有商品; 20:寄卖商品; 30:质质押商品 40:其它
        String attributeCn = "未知属性";
        if (!LocalUtils.isEmptyAndNull(attributeUs)) {
            switch (attributeUs) {
                case "10":
                    attributeCn = showLongName ? "自有商品" : "自";
                    break;
                case "20":
                    attributeCn = showLongName ? "寄卖商品" : "寄";
                    break;
                case "30":
                    attributeCn = showLongName ? "质押商品" : "押";
                    break;
                default:
                    attributeCn = showLongName ? "其它商品" : "其";
                    break;
            }
        }
        return attributeCn;
    }

    public String getClassifyCn(String classifyUs) {
        //属性名称：10:自有商品; 20:寄卖商品; 30:质质押商品 40:其它
        String attributeCn = "未知分类";
        if (!LocalUtils.isEmptyAndNull(classifyUs)) {
            switch (classifyUs) {
                case "WB":
                    attributeCn = "腕表";
                    break;
                case "XB":
                    attributeCn = "箱包";
                    break;
                case "ZB":
                    attributeCn = "珠宝";
                    break;
                case "XX":
                    attributeCn = "鞋靴";
                    break;
                case "PS":
                    attributeCn = "配饰";
                    break;
                case "FS":
                    attributeCn = "服饰";
                    break;
                case "QT":
                    attributeCn = "其它";
                    break;
                default:
                    break;
            }
        }
        return attributeCn;
    }

    public String getStateCn(String stateUs) {
        String stateCn = null;
        if (!LocalUtils.isEmptyAndNull(stateUs)) {
            int state = Integer.parseInt(stateUs);
            if (LocalUtils.isBetween(state, 10, 19)) {
                stateCn = "未上架";
            } else if (LocalUtils.isBetween(state, 20, 39)) {
                stateCn = "已上架";
            } else if (LocalUtils.isBetween(state, 40, 49)) {
                stateCn = "已售罄";
            } else if (state == -90) {
                stateCn = "已删除";
            } else {
                stateCn = "未知状态";
            }
        }
        return stateCn;
    }

    public void validVid(String key, String vid) {
        String vidRedisValue = redisUtil.get(key);
        if (LocalUtils.isEmptyAndNull(vidRedisValue) || !vidRedisValue.equals(vid)) {
            throw new MyException("页面已失效,请后退且重新进入该页面!  ");
        }
    }

    /**
     * 判断该用户是否有权限
     *
     * @param permission
     * @return
     */
    public boolean hasPermission(String userPerms, String permission) {
        return !LocalUtils.isEmptyAndNull(userPerms) && userPerms.contains(permission);
    }

    /**
     * 判断该用户是否有权限
     *
     * @param shopId
     * @param userId
     * @param permission
     * @return
     */
    public boolean hasPermission(int shopId, int userId, String permission) {
        //用户权限缓存
        String userPerms = getUserPerms(shopId, userId);
        log.debug("---权限调试: {}, shopId: {}, userId: {}, shpUserPermValue: {}", permission, shopId, userId, userPerms);
        if (!LocalUtils.isEmptyAndNull(userPerms) && userPerms.contains(permission)) {
            return true;
        }
        return false;
    }

    public String getUserPerms(int shopId, int userId) {
        String shpUserPermKey = ConstantRedisKey.getPermKeyByShopIdUserId(shopId, userId);
        String shpUserPermValue = redisUtil.get(shpUserPermKey);
        return null == shpUserPermValue ? "" : shpUserPermValue;
    }

    /**
     * 把用户的redis缓存变更为需要更新的状态;
     *
     * @param shopId
     * @param userId
     */
    public void updateRedisUserPerm(int shopId, int userId) {
        String userPermKey = ConstantRedisKey.getPermKeyByShopIdUserId(shopId, userId);
        String permJson = redisUtil.get(userPermKey);
        if (!LocalUtils.isEmptyAndNull(permJson) && !ConstantCommon.NO_PERM.equals(permJson)) {
            Map<String, Object> hashMap = (Map<String, Object>) JSON.parse(permJson);
            hashMap.put(ConstantRedisKey.NEED_UPDATE_PERM, "1");
            //重置有效时间
            redisUtil.setEx(userPermKey, JSON.toJSONString(hashMap), 15);
        }
    }

    /**
     * 获取员工上限
     *
     * @return
     */
    public int getEmployeeLimit(int shopId) {
        //系统默认限制数量大小
        String employeeLimitKey = ConstantRedisKey.EMPLOYEE_LIMIT;
        String employeeLimitValue = redisUtil.get(employeeLimitKey);
        if (LocalUtils.isEmptyAndNull(employeeLimitValue)) {
            employeeLimitValue = "50";
            redisUtil.set(employeeLimitKey, employeeLimitValue);
        }
        String shopEmployeeLimitKey = ConstantRedisKey.getEmployeeLimitRedisKeyByShopId(shopId);
        String shopEmployeeLimitValue = redisUtil.get(shopEmployeeLimitKey);
        if (LocalUtils.isEmptyAndNull(shopEmployeeLimitValue)) {
            shopEmployeeLimitValue = "0";
            redisUtil.set(shopEmployeeLimitKey, shopEmployeeLimitValue);
        }
        //如果店铺员工数量有限制的话,则按照店铺员工数量的上限做标准
        employeeLimitValue = ConstantCommon.ZERO.equals(shopEmployeeLimitValue) ? employeeLimitValue : shopEmployeeLimitValue;
        return Integer.parseInt(employeeLimitValue);
    }

    public String formatImgUrl(String imgUrl, boolean isCompress, boolean isSmall) {
        if (isCompress) {
            if (!LocalUtils.isEmptyAndNull(imgUrl) && !imgUrl.contains("http")) {
                String key = isSmall ? ConstantRedisKey.IMG_SIZE_SMALL : ConstantRedisKey.IMG_SIZE_BIG;
                String imgSize = redisUtil.get(key);
                return ConstantCommon.ossDomain + imgUrl + imgSize;
            }
            return imgUrl;
        } else if (!LocalUtils.isEmptyAndNull(imgUrl) && !imgUrl.contains("http")) {
            return ConstantCommon.ossDomain + imgUrl;
        } else {
            return imgUrl;
        }
    }

    /**
     * 不压缩图片
     *
     * @param imgUrl
     * @return
     */
    public String formatImgUrl(String imgUrl) {
        return formatImgUrl(imgUrl, false, false);
    }

    /**
     * 压缩图片
     *
     * @param imgUrl
     * @param isSmall
     * @return
     */
    public String formatImgUrl(String imgUrl, boolean isSmall) {
        return formatImgUrl(imgUrl, true, isSmall);
    }


    /**
     * 验证验证码是否和服务器上的一致
     *
     * @param username        手机号
     * @param paramSmsCode    前端验证码
     * @param sendSmsTypeCode SendSmsType.code
     */
    public void validateServerSmsCode(String username, String paramSmsCode, String sendSmsTypeCode) {
        String smsCodeRedisKey = ConstantRedisKey.getYzmSmsCodeKey(sendSmsTypeCode, username);
        String smsCodeRedisValue = redisUtil.get(smsCodeRedisKey);
        //端上短信验证码是否和服务器上的一致
        if (!paramSmsCode.equals(smsCodeRedisValue)) {
            log.info("========reset_password短信验证码不正确: paramSmsCode:" + paramSmsCode + " | serverSmsCode:" + smsCodeRedisValue);
            throw new MyException(EnumCode.ERROR_SMS_CODE);
        }
    }


    /**
     * 检查店铺是否过期
     *
     * @param payUrl    用户支付url
     * @param vipExpire 店铺有效期
     * @return
     */
    public BaseResult checkShopExpired(String payUrl, Date vipExpire) {
        String resultMsg = "您的店铺正常使用!";
        String colorMsg = "";
        String btnTxtLeft = "";
        String btnTxtRight = "开通会员";
        String btnRightUrl = payUrl;

        //ok:正常 | soon:快过期 |  expired:已过期
        String state = EnumCode.OK.getCode();
        Date now = new Date();
        int differDays = DateUtil.getDifferDays(now, vipExpire);
        differDays--;
        //还剩10天进行提醒;
        if (differDays <= 10 && differDays > 0) {
            state = "soon";
            resultMsg = "您的店铺会员离到期仅剩" + differDays + "天，为了不影响您正常使用，请及时续费！";
            colorMsg = differDays + "天";
            btnTxtLeft = "再用用";
        } else if (differDays <= 0) {
            state = "expired";
            resultMsg = "您的店铺会员已过期，此功能已暂停使用，请续费恢复使用！";
            btnTxtLeft = "残忍离开";
        } else {
            btnTxtRight = "";
            btnRightUrl = "";
        }
        HashMap<String, Object> hashMap = new HashMap<>(16);
        hashMap.put("resultMsg", resultMsg);
        hashMap.put("colorMsg", colorMsg);
        hashMap.put("btnTxtLeft", btnTxtLeft);
        hashMap.put("btnTxtRight", btnTxtRight);
        hashMap.put("btnRightUrl", btnRightUrl);
        // BaseResult.okResult(hashMap)
        BaseResult baseResult = BaseResult.okResult(hashMap);
        baseResult.setCode(state);
        return baseResult;
    }


    public void refreshLoginKey(String token) {
        if (!LocalUtils.isEmptyAndNull(token)) {
            if (token.startsWith("admin")) {
                //刷新后台登录的key
                String oldUserIdKey = RedisKeyAdminLogin.getShpUserIdRedisKeyByToken(token);
                String oldUserNumberKey = RedisKeyAdminLogin.getUserNumberRedisKeyByToken(token);
                String olsShpUsernameKey = RedisKeyAdminLogin.getShpUsernameRedisKeyByToken(token);
                String username = redisUtil.get(olsShpUsernameKey);
                String shpTokenUsernameKey = RedisKeyAdminLogin.getShpAdminTokenKey(username);
                String olsShopIdKey = RedisKeyAdminLogin.getShopIdRedisKeyByToken(token);

                //分钟为单位
                int times = 120;
                redisUtil.expire(shpTokenUsernameKey, times);
                redisUtil.expire(oldUserIdKey, times);
                redisUtil.expire(oldUserNumberKey, times);
                redisUtil.expire(olsShpUsernameKey, times);
                redisUtil.expire(olsShopIdKey, times);

            }
        }
    }

    /**
     * 是否拥有线上云仓的权限
     *
     * @param shopNumber
     * @return
     */
    public boolean hasSpecialPerm(String shopNumber) {
        //店铺所有权限 String version99
        String onlineValue = redisUtil.get("sys_config:online");
        if (!LocalUtils.isEmptyAndNull(onlineValue)) {
            String[] onlineShop = onlineValue.split(",");
            for (String s : onlineShop) {
                if (s.equals(shopNumber)) {
                    return true;
                }
            }
        }
        return false;
    }


}
