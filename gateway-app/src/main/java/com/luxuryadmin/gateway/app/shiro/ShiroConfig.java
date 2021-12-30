package com.luxuryadmin.gateway.app.shiro;


import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置
 *
 * @author monkey king
 * @Date 2020-06-18 15:29:18
 */
@Configuration
@Slf4j
public class ShiroConfig {

    @Bean("shirFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        log.debug("=====顺序3=====ShiroConfig======shirFilter============");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/noLogin");
        shiroFilterFactoryBean.setUnauthorizedUrl("/accessDenied");
        //shiroFilterFactoryBean.setSuccessUrl("/home/index");

        //oauth过滤
        Map<String, Filter> filters = new HashMap<>(16);
        filters.put("authc2", new ShpShiroFilter());
        shiroFilterFactoryBean.setFilters(filters);


        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>(16);
        filterChainDefinitionMap.put("/shop/user/**", "authc2");
        filterChainDefinitionMap.put("/shop/admin/**", "authc2");
        filterChainDefinitionMap.put("/**", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    @Bean("securityManager")
    public SecurityManager securityManager() {
        log.debug("=====顺序4=====ShiroConfig======securityManager============");
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm());
        return securityManager;
    }


    @Bean("shiroRealm")
    public ShpAuthRealm shiroRealm() {
        log.debug("=====顺序5=====ShiroConfig======shiroRealm============");
        ShpAuthRealm shiroRealm = new ShpAuthRealm();
        return shiroRealm;
    }


    /**
     * 开启注解
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);

        return authorizationAttributeSourceAdvisor;
    }
}

