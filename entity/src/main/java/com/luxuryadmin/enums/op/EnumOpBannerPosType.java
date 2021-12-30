package com.luxuryadmin.enums.op;

/**
 * Banner位置类型枚举
 *
 * @author sanjin145
 * @date 2020-07-13 22:43:59
 */
public enum EnumOpBannerPosType {
    /**
     * 首页-轮播
     */
    INDEX_CAROUSEL("indexCarousel", "首页-轮播", "首页-轮播"),
    /**
     * 首页-弹窗
     */
    INDEX_POPUP_WINDOW("indexPopupWindow", "首页-弹窗", "首页-弹窗"),
    /**
     * 我的-弹窗
     */
    MY_CAROUSEL("myCarousel", "我的-轮播", "我的-轮播"),

    /**
     * 打印设置
     */
    PRINT_SETTINGS("printSettings", "打印-设置", "打印-设置"),

    /**
     * 商家联盟-轮播
     */
    SHOP_UNION("shopUnion", "商家联盟-轮播", "商家联盟-轮播"),
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


    EnumOpBannerPosType(String code, String name, String description) {
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

    public static EnumOpBannerPosType getEnumByCode(String code){
        for(EnumOpBannerPosType enumOpMessageType : EnumOpBannerPosType.values()){
            if(enumOpMessageType.getCode().equals(code)){
                return enumOpMessageType;
            }
        }
        return null;
    }
}
