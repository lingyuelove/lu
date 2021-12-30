package com.luxuryadmin.enums.op;

/**
 * 消息推送状态枚举
 *
 * @author sanjin145
 * @date 2020-07-23 14:10:59
 */
public enum EnumOpMessagePushState {
    /**
     * 店铺消息
     */
    NOT_PUSH("no_push", "未发送", "未发送"),

    /**
     * 友商消息
     */
    HAVE_PUSH("have_push", "已发送", "已发送"),
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


    EnumOpMessagePushState(String code, String name, String description) {
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

    public static EnumOpMessagePushState getEnumByCode(String code){
        for(EnumOpMessagePushState enumOpMessageType : EnumOpMessagePushState.values()){
            if(enumOpMessageType.getCode().equals(code)){
                return enumOpMessageType;
            }
        }
        return null;
    }
}
