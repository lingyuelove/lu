package com.luxuryadmin.common.exception;

/**
 * 该类只在Controller层使用;避免把系统大段异常文字返回给前端;
 *
 * @author monkey king
 */
public class ControllerException extends RuntimeException {


    private Throwable throwable;

    /**
     * 捕捉controller层的异常
     *
     * @param message
     * @param throwable
     */
    public ControllerException(String message, Throwable throwable) {
        super(message);
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
