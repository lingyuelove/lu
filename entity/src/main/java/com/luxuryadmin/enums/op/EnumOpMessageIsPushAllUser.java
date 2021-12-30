package com.luxuryadmin.enums.op;

/**
 * 消息发送方式枚举
 *
 * @author sanjin145
 * @date 2020-07-23 14:08:59
 */
public enum EnumOpMessageIsPushAllUser {
    /**
     * 全部用户
     */
    YES("yes", "全部用户", "全部用户"),

    /**
     * 部分用户
     */
    NO("no", "部分用户", "部分用户"),
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


    EnumOpMessageIsPushAllUser(String code, String name, String description) {
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

    public static EnumOpMessageIsPushAllUser getEnumByCode(String code){
        for(EnumOpMessageIsPushAllUser enumOpMessageType : EnumOpMessageIsPushAllUser.values()){
            if(enumOpMessageType.getCode().equals(code)){
                return enumOpMessageType;
            }
        }
        return null;
    }
}
