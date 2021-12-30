package com.luxuryadmin.enums.op;

/**
 * 消息跳转类型枚举
 *
 * @author sanjin145
 * @date 2020-07-23 16:14:59
 */
public enum EnumOpMessageJumpType {
    /**
     * 不跳转
     */
    NO_JUMP("nojump", "不跳转", "不跳转"),

    /**
     * 跳转H5
     */
    H5("h5", "h5", "h5"),

    /**
     * 跳转原生页面
     */
    NATIVE("native", "原生页面", "原生页面"),

    /**
     * 跳转外部APP
     */
    EXTERNAL_PAGE("externalPage", "外部APP", "外部APP"),
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


    EnumOpMessageJumpType(String code, String name, String description) {
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

    public static EnumOpMessageJumpType getEnumByCode(String code){
        for(EnumOpMessageJumpType enumOpMessageType : EnumOpMessageJumpType.values()){
            if(enumOpMessageType.getCode().equals(code)){
                return enumOpMessageType;
            }
        }
        return null;
    }
}
