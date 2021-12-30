package com.luxuryadmin.enums.op;

/**
 * Banner原生页面类型
 *
 * @author sanjin145
 * @date 2020-07-13 22:43:59
 */
public enum EnumOpBannerNativePage {
    /**
     * 首页-轮播
     */
    INVITE_PAGE("invitePage", "邀请页面", "邀请页面"),
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


    EnumOpBannerNativePage(String code, String name, String description) {
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

    public static EnumOpBannerNativePage getEnumByCode(String code){
        for(EnumOpBannerNativePage enumOpMessageType : EnumOpBannerNativePage.values()){
            if(enumOpMessageType.getCode().equals(code)){
                return enumOpMessageType;
            }
        }
        return null;
    }
}
