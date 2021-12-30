package com.luxuryadmin.intercepter;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


public abstract class AbstractZyInterceptor extends HandlerInterceptorAdapter {

    /**
     * 获取匹配路径
     * @return
     */
    public abstract String[] getPathPatterns();

    /**
     * 获取优先级
     * @return
     */
    public abstract int getPriority();
}
