package com.luxuryadmin.enums.op;

/**
 * 消息是否点击枚举
 *
 * @author sanjin145
 * @date 2020-07-13 16:16:59
 */
public enum EnumOpMessageClickState {
    /**
     * 未点击未读
     */
    NOT_CLICK("0", "未点击", "未读"),
    /**
     * 已点击已读
     */
    HAVE_CLICK("1", "已点击", "已读");

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


    EnumOpMessageClickState(String code, String name, String description) {
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
}
