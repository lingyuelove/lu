package com.luxuryadmin.enums.fin;


/**
 * @Classname EnumFinShopRecordType
 * @Description 店铺记账流水类型枚举
 * @Date 2020/10/20 17:00
 * @Created by sanjin145
 */
public enum EnumFinShopRecordType {

    /**
     * 收入
     */
    SYSTEM("system", "系统创建", "系统创建"),
    /**
     * 支出
     */
    MANUAL("manual", "人工创建", "人工创建");

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


    EnumFinShopRecordType(String code, String name, String description) {
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

    //通过枚举类值获得枚举类实例
    public static String geNameByCode(String code) {
        for(EnumFinShopRecordType type : EnumFinShopRecordType.values()){
            if(type.getCode().equals(code)){
                return type.name;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return code + " " + name + "" + description;
    }

}
