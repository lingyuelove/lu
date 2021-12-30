package com.luxuryadmin.config;

import com.luxuryadmin.intercepter.AbstractZyInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Comparator;
import java.util.List;


@Configuration
@Slf4j
public class SpringWebConfig implements WebMvcConfigurer {

    @Autowired
    private List<AbstractZyInterceptor> interceptors;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        interceptors.sort(Comparator.comparing(AbstractZyInterceptor::getPriority));
        interceptors.forEach(item -> {
            registry.addInterceptor(item).addPathPatterns(item.getPathPatterns());
        });
    }
}
