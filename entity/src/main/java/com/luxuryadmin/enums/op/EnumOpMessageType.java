package com.luxuryadmin.enums.op;

/**
 * 消息类型
 *
 * @author sanjin145
 * @date 2020-07-13 22:43:59
 */
public enum EnumOpMessageType {
    /**
     * 店铺消息
     */
    SHOP("shop", "店铺消息", "店铺消息"),
    /**
     * 友商消息
     */
    FRIENDBUSINESS("friendBusiness", "友商消息", "友商消息"),
    /**
     * 系统消息
     */
    SYSTEM("system", "系统消息", "系统消息"),

    /**
     * 其它消息
     */
    OTHER("other", "其它消息", "其它消息,活动消息"),
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


    EnumOpMessageType(String code, String name, String description) {
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

    public static EnumOpMessageType getEnumByCode(String code){
        for(EnumOpMessageType enumOpMessageType : EnumOpMessageType.values()){
            if(enumOpMessageType.getCode().equals(code)){
                return enumOpMessageType;
            }
        }
        return null;
    }
}
