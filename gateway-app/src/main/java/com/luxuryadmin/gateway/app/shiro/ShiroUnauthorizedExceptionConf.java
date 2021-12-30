package com.luxuryadmin.gateway.app.shiro;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.Properties;

/**拦截shiro异常,已被@RestControllerAdvice注解替代
 * @author monkey king
 * @date 2020-06-18 23:00:41
 */
@Configuration
public class ShiroUnauthorizedExceptionConf {

    //@Bean
    //public SimpleMappingExceptionResolver resolver() {
    //    SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
    //    Properties properties = new Properties();
    //    properties.put("org.apache.shiro.authz.UnauthorizedException", "/accessDenied");
    //    resolver.setExceptionMappings(properties);
    //    return resolver;
    //}

}