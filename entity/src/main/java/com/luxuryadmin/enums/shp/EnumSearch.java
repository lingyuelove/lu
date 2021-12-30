package com.luxuryadmin.enums.shp;

/**
 * 搜索框搜索提示語
 *
 * @author taoqimin
 * @date 2021-11-01
 */
public enum EnumSearch {
    //枚举
    SEARCH_FIRST("SEARCH_FIRST", "友商相册", "请输入关键词搜索"),
    SEARCH_SECOND("SEARCH_SECOND", "已删除", "请输入关键词搜索"),
    SEARCH_THIRDLY("SEARCH_THIRDLY", "在售商品", "搜索商品名称/描述/备注/独立编码"),
    SEARCH_FOURTHLY("SEARCH_FOURTHLY", "临时仓", "搜索商品名称/描述/备注/独立编码"),
    SEARCH_FIFTH("SEARCH_FIFTH", "仓库", "搜索商品名称/描述/备注/独立编码"),
    SEARCH_SIXTH("SEARCH_SIXTH", "质押商品", "搜索商品名称/描述/备注/独立编码"),
    SEARCH_SEVENTH("SEARCH_SEVENTH", "商品盘点", "搜索商品名称/描述/备注/独立编码"),
    SEARCH_EIGHTH("SEARCH_EIGHTH", "分享商品", "搜索商品名称/描述/备注/独立编码"),
    SEARCH_NINTH("SEARCH_NINTH", "锁单中", "支持搜索商品名称/描述/独立编码/系统编号"),
    SEARCH_TENTH("SEARCH_TENTH", "订单管理", "搜索商品名称/描述/备注/独立编码/订单编号"),
    SEARCH_ELEVENTH("SEARCH_ELEVENTH", "维修保养", "请输入保单名称/独立编码/关键字搜索"),
    SEARCH_TWELFTH("SEARCH_TWELFTH", "发货管理", "备注/商品名称/收货地址"),
    SEARCH_THIRTEENTH("SEARCH_THIRTEENTH", "友商店铺", "搜索店铺编号/店铺名称"),
    SEARCH_FOURTEENTH("SEARCH_FOURTEENTH", "到期商品", "搜索商品名称/描述/备注/独立编码"),
    SEARCH_FIFTEENTH("SEARCH_FIFTEENTH", "商品位置", ""),
    SEARCH_SIXTEEN("SEARCH_SIXTEEN", "寄卖传送", "搜索商品名称/描述/备注/独立编码"),
    SEARCH_SEVENTEEN("SEARCH_SEVENTEEN", "回收详情", "搜索商品名称/描述/备注/独立编码"),
    SEARCH_EIGHTEEN("SEARCH_EIGHTEEN", "员工管理", "请输入昵称、手机号进行搜索"),
    SEARCH_DEFAULT("SEARCH_DEFAULT", "新增输入框时", "请输入关键词搜索");

    private String code;

    private String name;

    private String description;


    EnumSearch(String code, String name, String description) {
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

    @Override
    public String toString() {
        return code + " " + name + "" + description;
    }
}
