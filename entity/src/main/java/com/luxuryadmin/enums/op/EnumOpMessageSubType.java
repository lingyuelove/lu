package com.luxuryadmin.enums.op;

import com.luxuryadmin.vo.op.VoMessageSubType;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息子类型
 *
 * @author sanjin145
 * @date 2020-07-13 16:00:59
 */
public enum EnumOpMessageSubType {

    /**
     * 一、友商消息子类型
     */
    /**
     * 2-1 申请添加友商好友
     */
    APPLY_ADD_FRIEND_SERVICE("applyAddFriendService", "申请添加友商好友", "申请添加友商好友"),

    /**
     * 2-2 添加店铺用户好友成功
     */
    ADD_SHOP_USER_FRIEND_SUCCESS("addShopUserFriendSuccess", "添加店铺用户好友成功", "添加店铺用户好友成功"),
    /**
     * 2-3 联盟审核结果通知
     */
    UNION_VERIFY("unionVerify", "联盟审核结果通知", "联盟审核结果通知"),

    /**
     * 二、商品消息子类型
     */
    /**
     * 3-1 商品售出通知
     */
    SALE_PROD("saleProd", "商品售出", "商品售出通知"),

    /**
     * 3-2 商品上架通知
     */
    RELEASE_PROD("releaseProd", "商品上架", "商品上架通知"),

    /**
     * 3-3 删除商品通知
     */
    DELETE_PROD("deleteProd", "商品删除", "商品删除通知"),

    /**
     * 3-4 修改商品价格
     */
    UPDATE_PROD_PRICE("updateProdPrice", "修改商品", "修改商品价格通知"),

    /**
     * 3-4.1 成本变动
     */
    UPDATE_PROD_INIT_PRICE("updateProdInitPrice", "成本变动", "修改商品成本通知"),

    /**
     * 3-5 质押商品赎回
     */
    REDEEM_PROD("redeemProd", "质押赎回", "质押商品赎回通知"),

    /**
     * 3-6 质押商品到期
     */
    EXPIRE_PROD("expireProd", "质押到期","质押商品到期通知"),

    /**
     * 3-7 商品入库通知
     */
    UPLOAD_PROD("uploadProd", "商品入库", "商品入库通知"),

    /**
     * 3-8 商品入库通知
     */
    LOCK_PROD("lockProd", "商品锁单", "商品锁单通知"),
    /**
     * 3-8 寄卖取回
     */
    RECYCLE_PROD("recycleProd", "寄卖取回", "寄卖取回通知"),
    /**
     * 三、订单消息子类型
     */
    /**
     * 2-1 删除订单
     */
    CANCEL_ORDER("cancelOrder", "订单退货", "取消订单通知"),

    /**
     * 2-2 更新订单
     */
    UPDATE_ORDER("updateOrder", "订单修改", "更新订单通知"),

    /**
     * 3-1 每日日报
     */
    DAILY_REPORT("dailyReport", "店铺日报", "店铺日报通知"),

    /**
     * 3-2 每月月报
     */
    DAILY_REPORT_MONTH("dailyReportForMonth", "店铺月报", "店铺月报通知"),

    /**
     * 3-9 到期商品提醒
     */
    EXPIRED_PRODUCT("expiredProduct", "到期商品提醒", "店铺到期提醒商品"),

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


    EnumOpMessageSubType(String code, String name, String description) {
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

    /**
     * 根据Code获取名称Name
     * @param code
     * @return
     */
    public static String getNameByCode(String code){
        for (EnumOpMessageSubType value : EnumOpMessageSubType.values()) {
            if(value.getCode().equals(code)){
                return value.getName();
            }
        }
        return "";
    }

    /**
     * 根据Name获取Code
     * @param name
     * @return
     */
    public static String getCodeByName(String name){
        for (EnumOpMessageSubType value : EnumOpMessageSubType.values()) {
            if(value.getName().equals(name)){
                return value.getCode();
            }
        }
        return "";
    }

    /**
     * 获取所有店铺消息子类型中文名称
     * @return
     */
    public static List<VoMessageSubType> getAllShopMsgSubTypeCnName(){
        List<VoMessageSubType> subTypeCnNameList = new ArrayList<>();
        for (EnumOpMessageSubType value : EnumOpMessageSubType.values()) {
            //排除所有友商的子类型，判断条件为Code里是否包含“Friend”字符串
            if(value.getCode().indexOf("Friend") == -1){
                VoMessageSubType vo = new VoMessageSubType();
                vo.setCode(value.getCode());
                vo.setCnName(value.getName());
                subTypeCnNameList.add(vo);
            }
        }
        return subTypeCnNameList;
    }

}
