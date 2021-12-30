package com.luxuryadmin.enums.sys;

/**
 * 用户属性模板;<br/>
 * 用户开店后都会有自己一套属性;默认使用模板数据;
 *
 * @author monkey king
 * @date 2019-12-26 18:52:22
 */
public enum EnumTableName {

    /**
     * 商品属性表
     */
    PRO_ATTRIBUTE("pro_attribute", "商品属性表", "商品属性表"),
    /**
     * 商品分类表
     */
    PRO_CLASSIFY("pro_classify", "商品分类表", "商品分类表"),
    /**
     * 商品状态表
     */
    PRO_STATE("pro_state", "商品状态表", "商品状态表"),
    /**
     * 订单类型表
     */
    ORD_TYPE("ord_type", "订单类型表", "订单类型表"),
    /**
     * 资金分类表
     */
    FIN_CLASSIFY("fin_classify", "资金分类表", "资金分类表"),
    /**
     * 用户类型表
     */
    SHP_USER_TYPE("shp_user_type", "用户类型表", "用户类型表");

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


    EnumTableName(String code, String name, String description) {
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
}
