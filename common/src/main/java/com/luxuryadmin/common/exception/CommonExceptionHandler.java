package com.luxuryadmin.common.exception;

import com.google.gson.Gson;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.aliyun.RobotHelperUtil;
import lombok.extern.slf4j.Slf4j;
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
 * MyException拦截{@link #exceptionHandler(MyException)}
 * @date 2019-12-02 19:44:50
 */
@Slf4j
@RestControllerAdvice
@Order(1)//因为项目中有多个RestControllerAdvice注解;所以要使用顺序
public class CommonExceptionHandler {
    @Autowired
    private Gson gson;


    /**
     * 拦截MyException异常
     *
     * @param e
     * @return
     * @date 2019/7/2
     * @author monkey king
     */
    @ExceptionHandler(MyException.class)
    public BaseResult exceptionHandler(@NotNull MyException e) {
        EnumCode enumCode = e.getEnumCode();
        BaseResult result;
        if (null != enumCode) {
            result = BaseResult.errorResult(enumCode, e.getMessage());
        } else {
            result = BaseResult.defaultErrorWithMsg(e.getMessage());
            //返回端上数据; 把null改为""字符串;
            result.setData("");
        }
        result.setTimestamp(System.currentTimeMillis() + "");
        //String msg = "MyException项目环境: " + ConstantCommon.springProfilesActive + "\n";
        //RobotHelperUtil.sendMessageByText(msg + LocalUtils.getStackTrace(e), null, false);
        log.info("异常拦截-请求返回值: " + gson.toJson(result));
        log.info("异常拦截====== After request biz handle " + new Timestamp(System.currentTimeMillis()) + " ========");
        return LocalUtils.returnSignBaseResult(result);
    }

    /**
     * 拦截控制层异常
     *
     * @param e ControllerException
     * @return
     */
    @ExceptionHandler(ControllerException.class)
    public BaseResult controllerHandler(@NotNull Exception e) {
        BaseResult result;
        result = BaseResult.defaultErrorWithMsg(e.getMessage());
        //返回端上数据; 把null改为""字符串;
        result.setData("");
        result.setTimestamp(System.currentTimeMillis() + "");
        if (!(e instanceof MyException)) {
            //result = BaseResult.defaultErrorWithMsg("系统繁忙，稍后请重试!!");
            log.error(e.getMessage(), ((ControllerException) e).getThrowable());
            String msg = "controllerHandler项目环境1: " + ConstantCommon.springProfilesActive + "\n";
            RobotHelperUtil.sendMessageByText(msg + LocalUtils.getStackTrace(e), null, false);
        }
        //log.info("异常拦截====== ControllerException " + new Timestamp(System.currentTimeMillis()) + " ========");
        return LocalUtils.returnSignBaseResult(result);
    }


    /**
     * Exception异常拦截
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public BaseResult exceptionHandler(@NotNull Exception e) {
        BaseResult result;
        result = BaseResult.defaultErrorWithMsg("系统繁忙，稍后请重试！");
        //返回端上数据; 把null改为""字符串;
        result.setData("");
        result.setTimestamp(System.currentTimeMillis() + "");
        if (!(e instanceof MyException)) {
            log.error(e.getMessage(), e);
            String msg = "exceptionHandler项目环境2: " + ConstantCommon.springProfilesActive + "\n";
            RobotHelperUtil.sendMessageByText(msg + LocalUtils.getStackTrace(e), null, false);
        }
        log.info("异常拦截====== exceptionHandler " + new Timestamp(System.currentTimeMillis()) + " ========");
        return LocalUtils.returnSignBaseResult(result);
    }

}
