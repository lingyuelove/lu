package com.luxuryadmin.common.exception;

import com.luxuryadmin.common.constant.enums.EnumCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @Description: 通用异常,传递ResultCode
 *
 * -------------------------------------------------jar包引入 Start
 * | - lombok
 *     <dependency>
 *         <groupId>org.projectlombok</groupId>
 *         <artifactId>lombok</artifactId>
 *         <scope>provided</scope>
 *     </dependency>
 * -------------------------------------------------jar包引入 End
 *
 * @date 2019-12-02 19:44:50
 * @author monkey king
 * Happy Coding, Happy Life
 */
@Getter
@Setter
public class MyException extends RuntimeException {

    private EnumCode enumCode;

    public MyException() {
        super();
        this.enumCode = EnumCode.ERROR;
    }

    public MyException(String message) {
        super(message);
        this.enumCode = null;
    }

    public MyException(@NotNull EnumCode enumCode) {
        super(enumCode.getMessage());
        this.enumCode = enumCode;
    }

    public MyException(@NotNull EnumCode enumCode, Throwable cause) {
        super(cause);
        this.enumCode = enumCode;
    }

    public MyException(@NotNull EnumCode enumCode, String message) {
        super(message);
        this.enumCode = enumCode;
    }

}
