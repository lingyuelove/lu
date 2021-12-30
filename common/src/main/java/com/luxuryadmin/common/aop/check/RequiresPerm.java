package com.luxuryadmin.common.aop.check;

import org.springframework.context.annotation.Configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @PackgeName: com.luxuryadmin.common.aop.check
 * @ClassName: RequiresPerm
 * @Author: ZhangSai
 * Date: 2021/10/28 18:36
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPerm {
    String[] value();

    String logical() default "OR";
}
