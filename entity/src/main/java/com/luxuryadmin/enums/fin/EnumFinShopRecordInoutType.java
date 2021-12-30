package com.luxuryadmin.enums.fin;

/**
 * @Classname EnumFinShopRecordInoutType
 * @Description 枚举
 * @Date 2020/10/20 17:00
 * @Created by sanjin145
 */
public enum EnumFinShopRecordInoutType {

    /**
     * 收入
     */
    IN("in", "收入", "收入"),
    /**
     * 支出
     */
    OUT("out", "支出", "支出");

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


    EnumFinShopRecordInoutType(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    /**
     * 根据Code获取名称
     * @param code
     * @return
     */
    public static String getNameByCode(String code) {
        for (EnumFinShopRecordInoutType value : EnumFinShopRecordInoutType.values()) {
            if(value.getCode().equals(code)){
                return value.getName();
            }
        }
        return "";
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
