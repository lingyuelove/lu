package com.luxuryadmin.gateway.admin.interceptor;

import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.service.sys.SysPermissionService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;


/**
 * 权限1 判断token
 *
 * @author monkey king
 * @date 2019-12-13 16:15:52
 */
@Slf4j
@Service
public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private List<LoginHandlerInterceptorConfig> list;
    @Autowired
    private ServicesUtil servicesUtil;
    @Autowired
    private SysPermissionService sysPermissionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("=====顺序6=====LoginHandlerInterceptor======preHandle============");
//        String token = LocalUtils.getToken(request);
//        int userId = servicesUtil.getShpUserIdByToken(token);

        String token = request.getHeader("Authorization");
        //如果header中不存在token，则从参数中获取token
        if (LocalUtils.isEmptyAndNull(token)) {
            token = request.getParameter("token");
        }
        String userName = servicesUtil.getShpUserNameByToken(token);
        log.info("===========登录信息有效=================" + userName);

        HandlerMethod method = (HandlerMethod) handler;
        RequiresPerm isPermission = method.getMethod().getAnnotation(RequiresPerm.class);
        if (isPermission == null) {
            return true;
        }
        String[] strings = isPermission.value();
        String logical = isPermission.logical();
        log.info("权限信息" + isPermission);
        //拿用户权限。通过token、
        //根据请求链接和token判断用户是否有权限
        return sysPermissionService.getPermissionByUserAndUrl(userName,strings,logical);
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