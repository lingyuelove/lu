package com.luxuryadmin.enums.pro;

/**
 * 临时仓商品列表是否初始请求枚举
 *
 * @author monkey king
 * @date 2019-12-26 17:07:53
 */
public enum EnumProRequest {

    /**
     * 初次请求
     */
    FIRST(10, "初次请求", "不修改价格"),

    /**
     * 不是初次请求
     */
    NOFIRST(99, "不是初次请求", "修改价格");



    /**
     * 代码(存入数据库)
     */
    private Integer code;
    /**
     * 代码名称(显示作用)
     */
    private String name;

    /**
     * 代码说明(对代码进行详细的补充说明)
     */
    private String description;

    EnumProRequest(int code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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

    public static String getStateName(String type) {

        try {
            type = Integer.parseInt(type) + "";
        } catch (NumberFormatException e) {
            return type;
        }
        switch (type) {
            case "10":
                type = FIRST.getName();
                break;
            case "99":
                type = NOFIRST.getName();
                break;
            default:
                type = "其他";
                break;
        }
        return type;
    }

    @Override
    public String toString() {
        return code + " " + name + "" + description;
    }
}
