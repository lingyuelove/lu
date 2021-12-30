package com.luxuryadmin.gateway.app.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

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
        InterceptorRegistration ir = registry.addInterceptor(loginHandlerInterceptor);
        ir.addPathPatterns("/shop/user/**");
        ir.addPathPatterns("/shop/admin/**");
    }
}
