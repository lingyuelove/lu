package com.luxuryadmin.enums.pro;

/**
 * 商品属性表模板-枚举类
 *
 * @author monkey king
 * @date 2019-12-26 17:40:09
 */
public enum EnumProAttribute {

    /**
     * 本店自己的商品
     */
    OWN(10, "自有商品", "店铺自己的商品"),
    /**
     * 第三方放在本店寄卖的商品
     */
    ENTRUST(20, "寄卖商品", "第三方放在本店寄卖的商品"),
    /**
     * 质押(典当)的商品
     */
    PAWN(30, "质押商品", "质押(典当)的商品"),
    /**
     * 其它类型
     */
    OTHER(40, "其它", "其它类型");

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

    EnumProAttribute(int code, String name, String description) {
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

    @Override
    public String toString() {
        return code + " " + name + "" + description;
    }
}
