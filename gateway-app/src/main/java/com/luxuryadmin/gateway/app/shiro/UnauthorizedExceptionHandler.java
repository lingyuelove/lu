package com.luxuryadmin.gateway.app.shiro;

import com.google.gson.Gson;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.exception.ControllerException;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.aliyun.RobotHelperUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * @author monkey king
 * Happy Coding, Happy Life
 * @Description: 自定义异常处理类
 * <p>
 * -------------------------------------------------jar包引入 Start
 * | - lombok
 * <dependency>
 * <groupId>org.projectlombok</groupId>
 * <artifactId>lombok</artifactId>
 * <scope>provided</scope>
 * </dependency>
 * ..........................................................
 * | - gson
 * <dependency>
 * <groupId>com.google.code.gson</groupId>
 * <artifactId>gson</artifactId>
 * </dependency>
 * -------------------------------------------------jar包引入 End
 * <p>
 * @date 2019-12-02 19:44:50
 */
@Slf4j
@RestControllerAdvice
@Order(0)//因为项目中有多个RestControllerAdvice注解;所以要使用顺序
public class UnauthorizedExceptionHandler {


    /**
     *
     *
     * @param e ControllerException
     * @return
     */
    @ExceptionHandler(UnauthorizedException.class)
    public BaseResult unauthorizedHandler(@NotNull Exception e) {
        BaseResult result;
        result = BaseResult.errorResult(EnumCode.ERROR_NO_SHOP_PERMISSION);
        //返回端上数据; 把null改为""字符串;
        result.setData("");
        result.setTimestamp(System.currentTimeMillis() + "");
        log.info("异常拦截====== exceptionHandler " + new Timestamp(System.currentTimeMillis()) + " ========");
        return LocalUtils.returnSignBaseResult(result);
    }









}
