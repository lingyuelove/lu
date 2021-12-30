package com.luxuryadmin.gateway.app.shiro;

import com.google.gson.Gson;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.utils.LocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author monkey king
 * @date 2020-06-18 17:05:42
 */
@Slf4j
public class ShpShiroFilter extends AuthenticatingFilter {

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        log.debug("=====顺序9=====ShopShiroFilter======createToken============");
        //获取请求token
        String token = LocalUtils.getToken((HttpServletRequest) request);

        if (LocalUtils.isEmptyAndNull(token)) {
            return null;
        }
        //临时处理,兼容新旧版权限;2021-12-07 17:51:00;v267;迭代若干版本后去掉恢复至原来模样.
        String version = request.getParameter("appVersion");
        log.debug("===version:{}", version);
        return new ShpToken(token + "#" + version);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        log.debug("=====顺序10=====ShpShiroFilter======isAccessAllowed============");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //解决跨域问题
        return "OPTIONS".equals(httpServletRequest.getMethod().toUpperCase());
    }


    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        log.debug("=====顺序11=====ShpShiroFilter======onAccessDenied============");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = LocalUtils.getToken(httpServletRequest);
        try {
            if (LocalUtils.isEmptyAndNull(token)) {
                log.error("****************token获取失败*method[{}]*****", httpServletRequest.getMethod());
                log.error("=====顺序11=====onAccessDenied======invalid token[{}]============", token);
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                String json = new Gson().toJson(BaseResult.defaultErrorWithMsg("invalid token"));
                httpResponse.getWriter().print(json);
                return false;
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return executeLogin(request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=utf-8");
        try {
            //处理登录失败的异常
            BaseResult result = BaseResult.errorResult(EnumCode.ERROR_NO_LOGIN);
            String json = new Gson().toJson(LocalUtils.returnSignBaseResult(result));
            httpResponse.getWriter().print(json);
        } catch (IOException ignored) {

        }

        return false;
    }


}
