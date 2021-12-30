package com.luxuryadmin.enums.ord;

/**
 * @author Administrator
 * @Classname EnumOrdTypeType
 * @Description TODO
 * @Date 2020/9/24 15:10
 */
public enum EnumOrdTypeType {

    /**
     * 系统创建
     */
    SYSTEM("0", "系统创建", "系统创建"),
    /**
     * 用户创建
     */
    USER("1", "用户创建", "用户创建");

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


    EnumOrdTypeType(String code, String name, String description) {
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
