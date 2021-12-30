package com.luxuryadmin.enums.sys;

/**
 * SysSalt表的类型枚举
 *
 * @author monkey king
 * @date 2019-12-26 17:53:56
 */
public enum EnumSaltType {

    //枚举
    SYS_MODEL(-1, "sys模块用户", "sys模块用户"),
    SHP_MODEL(0, "shp模块用户", "shp模块用户"),
    BIZ_MODEL(1, "biz模块用户", "biz模块用户");

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

    EnumSaltType(int code, String name, String description) {
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
