package com.luxuryadmin.enums.login;

/**
 * 发送短信的枚举类型
 *
 * @author monkey king
 * @date 2019-12-26 17:52:11
 */
public enum EnumSendSmsType {
    /**
     * 注册接口发送验证码的key值
     */
    REGISTER("register", "注册接口", "注册接口发送验证码的key值"),
    /**
     * 登录接口发送验证码的key值
     */
    LOGIN("login", "登录接口", "登录接口发送验证码的key值"),
    /**
     * 重置密码接口发送验证码的key值
     */
    RESET_PASSWORD("resetPassword", "重置密码接口", "重置密码接口发送验证码的key值"),

    /**
     * 发送绑定账户验证码
     */
    BIND_COUNT("bindCount", "微信绑定账户接口", "发送绑定账户验证码"),

    /**
     * 注册用户(店铺)验证码
     */
    SHOP_REGISTER_USER("shopRegisterUser", "注册用户(店铺)", "店铺添加员工时,用户不存在则注册为新用户"),

    /**
     * 店铺一键删除验证码
     */
    ONE_KEY_DELETE("oneKeyDelete", "一键删除", "一键删除"),
    /**
     * 店铺注销验证码
     */
    DESTROY_SHOP("destroyShop", "店铺注销验证码", "店铺注销验证码"),
    /**
     * 店铺注销成功
     */
    DESTROY_SUCCESS("destroySuccess", "店铺注销成功", "店铺注销成功");

    /**
     * 代码(存入数据库)
     */
    private String code;
    /**
     * 代码名称(显示作用)
     */
    private String name;

    /**
     * 代码说明(对代码进行详细的补充说明)
     */
    private String description;


    EnumSendSmsType(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return code + " " + name + "" + description;
    }
}
