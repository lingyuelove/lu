package com.luxuryadmin.common.aop.check;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @RequestRequire: AOP校验参数合法性
 *
 * 是否对参数类校验{@link #check()}
 * 请求当前接口所需要的参数{@link #require()}
 * 传递参数的对象类型{@link #parameter()}
 *
 * @date 2019-12-02 19:44:50
 * @author monkey king
 * Happy Coding, Happy Life
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestRequire {

    /**
     * 是否对参数类校验,默认false不校验
     *
     * @return
     *
     * @date 2019-12-02 19:44:50
     * @author monkey king
     */
    public boolean check() default false;

    /**
     * 请求当前接口所需要的参数,多个以小写的逗号隔开
     *
     * @return
     *
     * @date 2019-12-02 19:44:50
     * @author monkey king
     */
    public String require() default "";

    /**
     * 传递参数的对象类型
     *
     * @return
     *
     * @date 2019-12-02 19:44:50
     * @author monkey king
     */
    public Class<?> parameter() default Object.class;
}
