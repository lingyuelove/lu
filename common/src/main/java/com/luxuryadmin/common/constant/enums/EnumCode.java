package com.luxuryadmin.common.constant.enums;

/**
 * @author monkey king
 * Happy Coding, Happy Life
 * @Description: 请求码信息枚举
 * @date 2019-12-02 19:44:50
 */
public enum EnumCode {
    //================================ ok ================================//
    OK("ok", "请求成功"),
    OK_NO_DATA("ok_no_data", "没有数据"),
    //OK_NOT_EXIST_USER_SHOP("ok_not_exist_user", "用户不存在,继续添加,则将此手机号注册为新账号"),
    OK_NOT_EXIST_USER_SHOP("ok_not_exist_user", "将此手机号注册为新账号"),
    OK_LOGOUT("ok", "登出成功"),
    //临时仓转仓报错内容专门提示
    OK_MOVE_TEMP("ok_move_temp", "转出后部分商品临时仓库存大于该商品真实总库存，系统已自动调整库存为该商品最大库存量!"),
    OK_QRCODE_SCAN("ok_qrcode_scan", "二维码已扫描"),
    //================================ END ok ================================//

    //================================ error ================================//
    ERROR("error", "请求失败"),
    /**
     * 请求异常,请联系管理员
     */
    ERROR_ADMIN("error", "请求异常,请联系管理员!"),
    ERROR_SYSTEM_INIT("error", "系统初始化失败,请联系管理员!"),
    ERROR_REQUEST_PARAM("error_request_param", "参数错误"),
    ERROR_NO_LOGIN("error_no_login", "请先登录"),
    ERROR_NO_BIND("error_no_bind", "请先绑定帐号"),
    ERROR_INVOKE_LIMIT("error_invoke_limit", "调用次数超限"),
    ERROR_TIMEOUT("error_timeout", "访问超时"),
    ERROR_NO_SERVER("error_no_server", "服务不可用"),
    ERROR_NO_PERMISSION("error_no_permission", "403：无权限！"),
    ERROR_NO_SHOP_PERMISSION("error_no_shop_permission", "没有权限，请联系店长开通~"),
    ERROR_VALIDATE_PARAM("error_validate_param", "参数校验失败"),
    ERROR_SIGN("error_sign", "签名失败"),
    ERROR_TOKEN_DISABLE("error_token_disable", "登录失效"),
    ERROR_EXIST_USER("error_exist_user", "用户名已存在"),
    ERROR_NEED_IMAGE_CODE("error_need_image_code", "请输入图形验证码"),
    ERROR_IMAGE_CODE("error_image_code", "图形验证码错误"),
    ERROR_SMS_CODE("error_sms_code", "短信验证码错误"),
    ERROR_NOT_EXIST_USER("error_not_exist_user", "用户不存在"),
    ERROR_NOT_INVITE_CODE("error_not_invite_code", "邀请码不存在"),
    ERROR_FORMAT_USERNAME("error_format_username", "帐号格式错误"),
    ERROR_FORMAT_PASSWORD("error_format_password", "密码格式错误"),
    ERROR_PASSWORD("error_password", "用户密码错误"),
    ERROR_USERNAME("error_username", "用户名错误"),
    ERROR_USERNAME_PASSWORD("error_username_password", "用户名或密码错误"),
    ERROR_USER_EXCEPTION("error_user_exception", "用户帐号异常无法登录"),
    USER_ACCOUNT_EXPIRED("USER_ACCOUNT_EXPIRED", "账号不可用，已被禁用"),
    USER_ACCOUNT_LOCKED("USER_ACCOUNT_LOCKED", "账号被锁定或者禁用"),
    USER_CREDENTIALS_EXPIRED("USER_CREDENTIALS_EXPIRED", "密码过期"),
    ERROR_MP_EXPIRED("error_mp_expired", "页面已失效;请下载app进行访问!"),
    ERROR_MP_LIMIT("error_mp_limit", "请下载 奢当家 APP使用！"),
    ERROR_MOVE_LOCK("error_move_lock", "请到锁单商品模块进行解锁!"),
    /**
     * 扫码登录，
     */
    ERROR_EXPIRED("error_expired", "二维码已失效!"),
    VIP_PAST("vip_past", "会员已过期!"),
    /**
     * 用户controller层异常拦截处理
     */
    ERROR_CONTROL("error_control", "0x0001 出错啦~请联系管理员!");
    //================================ END error ================================//


    private String code;

    private String message;

    EnumCode(String code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public static boolean isSuccess(String code) {
        return EnumCode.OK.getCode().equals(code);
    }

    @Override
    public String toString() {
        return code + " " + message;
    }

}
