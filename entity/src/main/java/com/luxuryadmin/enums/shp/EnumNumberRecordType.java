package com.luxuryadmin.enums.shp;

/**
 * 编号记录流水类型枚举
 *
 * @author monkey king
 * @date 2019-12-26 17:38:39
 */
public enum EnumNumberRecordType {

    //枚举
    SHOP_USER("shopUser", "店铺用户", "店铺用户"),
    SHOP("shop", "店铺", "店铺");

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


    EnumNumberRecordType(String code, String name, String description) {
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
