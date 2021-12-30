package com.luxuryadmin.gateway.app.shiro;

import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.service.shp.ShpPermUserRefService;
import com.luxuryadmin.service.shp.ShpUserPermissionRefService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author monkey king
 * @date 2020-06-18 15:49:59
 */
@Slf4j
public class ShpAuthRealm extends AuthorizingRealm {

    @Autowired
    private ServicesUtil servicesUtil;

    @Autowired
    private ShpUserPermissionRefService shpUserPermissionRefService;

    @Autowired
    private ShpPermUserRefService shpPermUserRefService;

    @Override
    public boolean supports(AuthenticationToken token) {
        log.debug("=====顺序0=====ShopAuthRealm======supports============");
        return token instanceof ShpToken;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) throws AuthorizationException {
        log.debug("=====顺序1=====ShopAuthRealm======doGetAuthorizationInfo============");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String tokenStr = principalCollection.getPrimaryPrincipal().toString();
        String[] split = tokenStr.split("#");
        String token = split[0];
        String version = split[1];
        int userId = servicesUtil.getShpUserIdByToken(token);
        int shopId = servicesUtil.getShopIdByToken(token);
        List<String> permList = new ArrayList<>();
        //新旧版权限兼容;2021-12-07 17:57:46,迭代若干版本后,可恢复原样;
        try {
            if (VersionUtils.compareVersion(LocalUtils.isEmptyAndNull(version) ? "2.6.6" : version, "2.6.7") >= 0) {
                //新版权限
                permList = shpPermUserRefService.listPermUserByRedis(shopId, userId, false);
            } else {
                //旧版权限
                permList = shpUserPermissionRefService.listUserPermissionByRedis(shopId, userId, false);
                //for (int i = 0; i < permList.size(); i++) {
                //    permList.set(i, "9" + permList.get(i));
                //}
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        authorizationInfo.setStringPermissions(new HashSet<>(permList));
        return authorizationInfo;
    }

    /**
     * 认证(登录时调用);判断是否登录状态;
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        log.debug("=====顺序2=====ShopAuthRealm======doGetAuthenticationInfo============");
        String tokenStr = token.getCredentials().toString();
        try {
            String[] split = tokenStr.split("#");
            String t = split[0];
            servicesUtil.getShpUserIdByToken(t);
        } catch (Exception e) {
            log.error("======token失效, 请先登录, token: {}", tokenStr);
            return null;
        }
        return new SimpleAuthenticationInfo(tokenStr, tokenStr, getName());
    }

}
