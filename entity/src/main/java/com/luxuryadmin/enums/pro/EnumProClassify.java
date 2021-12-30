package com.luxuryadmin.enums.pro;

/**
 * 商品分类表枚举类
 *
 * @author monkey king
 * @date 2019-12-26 17:43:01
 */
public enum EnumProClassify {
    //枚举
    WB("WB", "腕表", "腕表"),
    XB("XB", "箱包", "箱包"),
    ZB("ZB", "珠宝", "珠宝"),
    XX("XX", "鞋靴", "鞋靴"),
    PS("PS", "配饰", "配饰"),
    FS("FS", "服饰", "服饰"),
    QT("QT", "其它", "其它");

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


    EnumProClassify(String code, String name, String description) {
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
