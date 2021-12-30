package com.luxuryadmin.enums.op;

/**
 * 消息推送平台枚举
 *
 * @author sanjin145
 * @date 2020-07-23 14:02:59
 */
public enum EnumOpMessagePushPlatform {
    /**
     * 所有平台
     */
    ALL("all", "所有平台", "所有平台"),
    /**
     * IOS
     */
    IOS("ios", "IOS", "IOS"),
    /**
     * 安卓
     */
    ANDROID("android", "安卓", "安卓"),
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


    EnumOpMessagePushPlatform(String code, String name, String description) {
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

    public static EnumOpMessagePushPlatform getEnumByCode(String code){
        for(EnumOpMessagePushPlatform enumOpMessageType : EnumOpMessagePushPlatform.values()){
            if(enumOpMessageType.getCode().equals(code)){
                return enumOpMessageType;
            }
        }
        return null;
    }
}
