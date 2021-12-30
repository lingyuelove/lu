package com.luxuryadmin.enums.ord;

/**
 * 订单类型
 *
 * @author monkey king
 * @date 2019-12-26 22:43:59
 */
public enum EnumOrdType {
    /**
     * 友商订单
     */
    YS("友商订单", "友商订单", "友商订单"),
    /**
     * 代理订单
     */
    DL("代理订单", "代理订单", "代理订单"),
    /**
     * 客户订单
     */
    KH("客户订单", "客户订单", "客户订单"),
    /**
     * 其它订单
     */
    QT("其它订单", "其它订单", "其它订单");

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


    EnumOrdType(String code, String name, String description) {
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
