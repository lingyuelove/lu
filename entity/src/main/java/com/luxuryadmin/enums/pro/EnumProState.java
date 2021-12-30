package com.luxuryadmin.enums.pro;

/**
 * 商品状态枚举类
 *
 * @author monkey king
 * @date 2019-12-26 17:07:53
 */
public enum EnumProState {

    /**
     * 已删除
     */
    DELETE(-90, "已删除", "客户端不再显示"),

    /**
     * 未上架(库存)
     */
    STAND_BY_10(10, "未上架", "首次入库"),
    /**
     * 未上架(上架后人工下架)
     */
    STAND_BY_11(11, "未上架", "再次入库-上架后人工下架"),

    /**
     * 未上架(商品属于质押期间)
     */
    STAND_BY_12(12, "未上架", "商品属于质押期间"),

    /**
     * 未上架(质押商品已到期)
     */
    STAND_BY_13(13, "未上架", "质押商品已到期"),

    /**
     * 已上架
     */
    RELEASE_20(20, "已上架", "首次上架"),

    /**
     * 已上架
     */
    RELEASE_21(21, "已上架", "再次上架"),


    /**
     * 正在交易
     */
    DEALING_30(30, "正在交易", "锁单状态"),


    /**
     * 已售出(零售)
     */
    SALE_40(40, "已售出(零售)", "卖给客户"),
    /**
     * 已售出(代理)
     */
    SALE_41(41, "已售出(代理)", "卖给代理"),
    /**
     * 已售出(友商)
     */
    SALE_42(42, "已售出(友商)", "卖给友商"),
    /**
     * 已售出(其它)
     */
    SALE_43(43, "已售出(其它)", "卖给其它"),
    /**
     * 已赎回
     */
    SALE_44(44, "已赎回", "质押商品已赎回");


    /**
     * 代码(存入数据库)
     */
    private Integer code;
    /**
     * 代码名称(显示作用)
     */
    private String name;

    /**
     * 代码说明(对代码进行详细的补充说明)
     */
    private String description;

    EnumProState(int code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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

    public static String getStateName(String type) {

        try {
            type = Integer.parseInt(type) + "";
        } catch (NumberFormatException e) {
            return type;
        }
        switch (type) {
            case "10":
            case "11":
            case "12":
                type = STAND_BY_10.getName();
                break;
            case "20":
                type = RELEASE_20.getName();
                break;
            case "30":
                type = DEALING_30.getName();
                break;
            case "40":
                type = SALE_40.getName();
                break;
            case "41":
                type = SALE_41.getName();
                break;
            case "42":
                type = SALE_42.getName();
                break;
            case "-90":
                type = DELETE.getName();
                break;
            default:
                type = "其他";
                break;
        }
        return type;
    }

    @Override
    public String toString() {
        return code + " " + name + "" + description;
    }
}
