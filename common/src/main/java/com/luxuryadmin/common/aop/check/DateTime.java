package com.luxuryadmin.common.aop.check;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author monkey king
 * @date 2020-05-28 20:19:40
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateTimeValidator.class)
public @interface DateTime {

    String message() default "日期时间格式错误";

    String format() default "yyyy-MM-dd HH:mm:ss";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
