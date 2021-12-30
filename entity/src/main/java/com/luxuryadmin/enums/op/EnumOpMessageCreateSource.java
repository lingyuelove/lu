package com.luxuryadmin.enums.op;

/**
 * 消息创建来源枚举
 *
 * @author sanjin145
 * @date 2020-07-23 14:04:59
 */
public enum EnumOpMessageCreateSource {
    /**
     * 店铺消息
     */
    CODE("code", "代码调用", "代码调用"),

    /**
     * 友商消息
     */
    CMS("cms", "后台管理系统", "后台管理系统"),
    ;

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


    EnumOpMessageCreateSource(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public String getCode(){
        return this.code;
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

    public static EnumOpMessageCreateSource getEnumByCode(String code){
        for(EnumOpMessageCreateSource enumOpMessageType : EnumOpMessageCreateSource.values()){
            if(enumOpMessageType.getCode().equals(code)){
                return enumOpMessageType;
            }
        }
        return null;
    }
}
