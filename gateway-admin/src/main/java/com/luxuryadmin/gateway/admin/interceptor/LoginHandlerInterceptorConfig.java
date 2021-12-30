package com.luxuryadmin.gateway.admin.interceptor;

import com.luxuryadmin.service.shp.ProQualityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 配置拦截器--拦截路径
 *
 * @author monkey king
 * @date 2019-12-13 16:36:46
 */
@Configuration
@Slf4j
public class LoginHandlerInterceptorConfig implements WebMvcConfigurer {
    @Resource
    private LoginHandlerInterceptor loginHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.debug("=====顺序0=====LoginHandlerInterceptorConfig======addInterceptors============");
        log.info("###############配置拦截路径#################");
        //已弃用此方法;转用Shiro权限框架;
        InterceptorRegistration ir = registry.addInterceptor(loginHandlerInterceptor);
        List<String> excludeURLS = new ArrayList<>();
        excludeURLS.add("/ord/**");
        excludeURLS.add("/sys/**");
        excludeURLS.add("/biz/**");
        excludeURLS.add("/pro/**");
        excludeURLS.add("/op/**");
        excludeURLS.add("/mem/**");
        ir.addPathPatterns(excludeURLS);

    }
}
