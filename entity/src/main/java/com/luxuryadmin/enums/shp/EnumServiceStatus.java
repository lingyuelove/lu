package com.luxuryadmin.enums.shp;

/**
 * @Classname EnumServiceStatus
 * @Description TODO
 * @Date 2020/9/18 17:51
 * @Created by sanjin145
 */
public enum EnumServiceStatus {

    //枚举
    IN_SERVICE("inService", "进行中", "进行中"),
    FINISH("finish", "已完成", "已完成"),
    CANCEL("cancel", "已取消", "已取消");

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


    EnumServiceStatus(String code, String name, String description) {
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
