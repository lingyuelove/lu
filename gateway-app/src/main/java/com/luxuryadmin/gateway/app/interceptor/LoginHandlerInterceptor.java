package com.luxuryadmin.gateway.app.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.encrypt.AESUtils;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.*;
import com.luxuryadmin.service.pro.ProCheckService;
import com.luxuryadmin.service.shp.ShpPermUserRefService;
import com.luxuryadmin.service.shp.ShpUserPermissionRefService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author monkey king
 * @date 2019-12-13 16:15:52
 */
@Slf4j
@Service
public class LoginHandlerInterceptor extends BaseController implements HandlerInterceptor {
    @Autowired
    private ServicesUtil servicesUtil;

    @Autowired
    private ShpUserPermissionRefService shpUserPermissionRefService;

    @Autowired
    private ShpPermUserRefService shpPermUserRefService;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ProCheckService checkService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("=====顺序6=====LoginHandlerInterceptor======preHandle============");
        //临时处理.做版本校验;
        String appVersion = request.getParameter("appVersion");
        String platform = request.getParameter("platform");
        //移动端
        if (("ios".equalsIgnoreCase(platform) || "android".equalsIgnoreCase(platform))) {
            boolean version = VersionUtils.compareVersion(appVersion, "2.6.6") >= 0;
            if (!version) {
                String msg = redisUtil.get(ConstantRedisKey.UPGRADE_MSG);
                String url = redisUtil.get(ConstantRedisKey.UPGRADE_URL);
                BaseResult baseResult = BaseResult.errorNeedUpgrade(msg, url);

                String dataJsonObject = JSON.toJSONString(baseResult.getData(), SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
                String dataJsonString = LocalUtils.isEmptyAndNull(dataJsonObject) ? "" : dataJsonObject;
                log.debug("=====dataJsonString=========: " + dataJsonString);
                String aesString = AESUtils.encrypt(dataJsonString);
                baseResult.setData(aesString);

                String json = new Gson().toJson(baseResult);
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().print(json);
                return false;
            }
        } else {
            //pc端
            String uri = request.getRequestURI();
            List<String> whiteUrl = new ArrayList<>();
            whiteUrl.add("/shop/admin/changeShop");
            whiteUrl.add("/shop/admin/myCenter");
            whiteUrl.add("/shop/admin/exitLogin");
            if (!whiteUrl.contains(uri) && !shopIsMember()) {
                throw new MyException("PC端体验已结束,目前只支持正式会员使用哦,请到APP端开通会员~");
            }
        }

        String vipExpire = getVipExpire();

        //不拦截苹果审核帐号
        if (!"18434363494".equals(getUsername())) {
            if (!LocalUtils.isEmptyAndNull(vipExpire)) {
                BaseResult check = servicesUtil.checkShopExpired(getUserPayUrl(), DateUtil.parseShort(vipExpire));

                String payUrlKey = ConstantRedisKey.PAY_URL;
                String payUrl = redisUtil.get(payUrlKey);
                //店铺已经过期
                if (payUrl.contains(getRequestURI()) && "expired".equals(check.getCode())) {

                    String dataJsonObject = JSON.toJSONString(check.getData(), SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
                    String dataJsonString = LocalUtils.isEmptyAndNull(dataJsonObject) ? "" : dataJsonObject;
                    log.debug("=====dataJsonString=========: " + dataJsonString);
                    String aesString = AESUtils.encrypt(dataJsonString);
                    check.setData(aesString);


                    String json = new Gson().toJson(check);
                    response.setContentType("text/json;charset=utf-8");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().print(json);
                    return false;
                }
            }
        }
        if ("ios".equalsIgnoreCase(platform) || "android".equalsIgnoreCase(platform)) {
            String checkUrlKey = ConstantRedisKey.CHECK_URL;
            String checkUrl = redisUtil.get(checkUrlKey);
            if (checkUrl.contains(getRequestURI())) {
                String token = LocalUtils.getToken(request);
                int shopId = servicesUtil.getShopIdByToken(token);
                checkService.getStateByShopId(shopId);
            }
        }

        String token = LocalUtils.getToken(request);
        int userId, shopId;
        userId = servicesUtil.getShpUserIdByToken(token);
        shopId = servicesUtil.getShopIdByToken(token);
        servicesUtil.refreshLoginKey(token);
        List<String> permList;
        if (VersionUtils.compareVersion(LocalUtils.isEmptyAndNull(appVersion) ? "2.6.6" : appVersion, "2.6.7") >= 0) {
            //新版权限
            permList = shpPermUserRefService.listPermUserByRedis(shopId, userId, false);
        } else {
            //旧版权限
            permList = shpUserPermissionRefService.listUserPermissionByRedis(shopId, userId, false);
        }
        log.debug("==========={}-{}拥有的权限:{}", shopId, userId, permList);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.debug("=====顺序7=====LoginHandlerInterceptor======postHandle============");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.debug("=====顺序8=====LoginHandlerInterceptor======afterCompletion============");
    }
}