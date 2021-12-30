package com.luxuryadmin.common.constant;

/**
 * CHK开头,代表是查看权限;<br/>
 * MOD开头, 代表该权限是有修改信息作用
 *
 * @author monkey king
 * @date 2020-07-09 18:48:23
 */
public class ConstantPermission {

    //=============查看=======================


    /**
     * 查看自有商品
     */
    public static final String CHK_OWN_PRODUCT = "pro:check:ownProduct";

    /**
     * 查看寄卖商品
     */
    public static final String CHK_ENTRUST_PRODUCT = "pro:check:entrustProduct";

    /**
     * 查看质押商品
     */
    public static final String CHK_PAWN_PRODUCT = "pro:check:pawnProduct";

    /**
     * 查看其它商品
     */
    public static final String CHK_OTHER_PRODUCT = "pro:check:otherProduct";

    /**
     * 查看在售商品
     */
    public static final String CHK_ON_SALE_PRODUCT = "pro:check:onSaleProduct";

    /**
     * 查看锁单中商品
     */
    public static final String CHK_LOCK_PRODUCT = "pro:check:lockProduct";


    /**
     * 查看已售罄商品
     */
    public static final String CHK_SALE_OUT_PRODUCT = "pro:check:saleOutProduct";

    /**
     * 查看成本价
     */
    public static final String CHK_PRICE_INIT = "pro:price:initPrice";

    /**
     * 查看友商价
     */
    public static final String CHK_PRICE_TRADE = "pro:price:tradePrice";

    /**
     * 查看代理价
     */
    public static final String CHK_PRICE_AGENCY = "pro:price:agencyPrice";

    /**
     * 查看销售价
     */
    public static final String CHK_PRICE_SALE = "pro:price:salePrice";

    /**
     * 查看成交价
     */
    public static final String CHK_PRICE_FINISH = "pro:price:finishPrice";

    /**
     * 查看商品属性
     */
    public static final String CHK_PRODUCT_ATTRIBUTE = "pro:check:productAttribute";

    /**
     * 查看托管客户
     */
    public static final String CHK_PRODUCT_CUSTOMER = "pro:check:productCustomer";

    /**
     * 查看仓库资产
     */
    //public static final String CHK_STORE_TOTAL_PRICE = "pro:check:ownProduct";

    /**
     * 查看全部订单
     */
    public static final String CHK_ALL_ORDER = "ord:listAllOrder";

    /**
     * 查看全部薪资
     */
    public static final String CHK_ALL_SALARY = "fin:check:AllSalary";


    /**
     * 查看维修保养数据汇总
     */
    public static final String PRO_CHECK_UPERMSERVICEPROFIT = "pro:check:uPermServiceProfit";

    /**
     * 查看回收分析权限
     */
    public static final String SALE_RECYCLE_ALL = "sale:recycle:all";


    /**
     * 查看回收站
     */
    public static final String CHK_DELETE_HISTORY = "proOrOrd:list:deleteHistory";


//    /**
//     * 查询销售排行榜首页数据权限
//     */
    public static final String SALE_CHECK_ALL = "sale:check:all";
    /**
     * 查询资产分析数据权限
     */
    public static final String SALE_MONEY_ALL = "sale:money:all";

    /**
     * 查看销售分析
     */
    public static final String CHK_SALE_ALL = "sale:check:all";


    //=============修改=======================

    /**
     * 锁单/解锁
     */
    public static final String MOD_LOCK_PRODUCT = "pro:lockProduct";

    /**
     * 解锁全部
     */
    public static final String MOD_UNLOCK_PRODUCT = "pro:unlockProduct";

    /**
     * 开单
     */
    public static final String MOD_CONFIRM_ORD = "ord:confirmOrd";

    /**
     * 修改商品信息(除名称外)
     */
    public static final String MOD_UPDATE_INFO = "pro:updateProInfo";

    /**
     * 删除商品
     */
    public static final String MOD_DELETE_PRO = "pro:deleteProduct";

    /**
     * 商品入库
     */
    public static final String MOD_UPLOAD_PRODUCT = "pro:uploadProduct";

    /**
     * 商品上架/下架
     */
    public static final String MOD_RELEASE_PRODUCT = "pro:releaseProduct";


    ///**
    // * 商品赎回
    // */
    //public static final String MOD_REDEEM_PRODUCT = "pro:redeemProduct";

    /**
     * 寄卖商品取回
     */
    public static final String PRO_RECYCLE = "pro:recycle:add";

    /**
     * 添加友商
     */
    public static final String MOD_LEAGUER_ADD = "leaguer:check:add";

    /**
     * 删除友商
     */
    public static final String MOD_LEAGUER_DELETE = "leaguer:delete";



    /**
     * 删除订单
     */
    public static final String MOD_DELETE_ORDER = "ord:deleteOrder";
    /**
     * 删除订单
     */
    public static final String ORD_UPDATE_ORDER = "ord:updateOrder";
    /**
     * 删除商品
     */
    public static final String PRO_DELETEPRODUCT = "pro:deleteProduct";

    /**
     * 永久删除订单/商品
     */
    public static final String PROORORD_DELETE_DELETEHISTORY = "proOrOrd:delete:deleteHistory";
    /**
     * 注册店铺
     */
    public static final String MOD_REGISTER_SHOP = "shop:register";

    //============= 接收消息权限 =======================
    //======================= 商品相关 =======================
    /**
     * 商品上架消息 DONE
     */
    public static final String MSG_PRO_ONSHELVES = "msg:pro:onshelves";

    /**
     * 商品入库消息 DONE
     */
    public static final String MSG_PRO_UPLOAD = "pro:check:ownProduct";

    /**
     * 商品删除消息 DONE
     */
    public static final String MSG_PRO_DELETE = "msg:pro:delete";

    /**
     * 商品售出消息 DONE
     */
    //public static final String MSG_PRO_SALE = "msg:pro:sale";

    /**
     * 商品价格变动消息 DONE
     */
    //public static final String MSG_PRO_CHANGE_PRICE = "msg:pro:changePrice";

    /**
     * 质押商品赎回 V2.2.0
     */
    public static final String MSG_PRO_REDEEM = "msg:pro:redeem";

    /**
     * 质押商品到期 V2.2.0
     */
    public static final String MSG_PRO_EXPIRE = "msg:pro:expire";



    /**
     * 商品锁单消息权限
     */
    //public static final String MSG_PRO_LOCK = "msg:pro:lock";

    /**
     * 临时仓转仓权限
     */
    public static final String PRO_TEMP_MOVE = "pro:temp:move";

    /**
     *获取动态权限
     */
    public static final String DYNAMIC_LIST = "dynamic:list";

    /**
     *删除动态权限
     */
    public static final String DYNAMIC_DELETE = "dynamic:delete";

    //======================= 商品相关 =======================
    /**
     * 订单修改消息 DONE
     */
    //public static final String MSG_ORD_UPDATE = "msg:ord:update";

    /**
     * 订单退货消息 DONE
     */
    //public static final String MSG_ORD_RETURN = "msg:ord:return";

    /**
     * 订单修改消息 DONE
     */
    //public static final String MSG_LEAGUER_ADD_APPLY = "msg:leaguer:addApply";



    //======================= 店铺日报 =======================
    /**
     * 店铺日报消息
     */
    //public static final String MSG_SHOP_DAILYREPORT = "msg:shop:dailyReport";

    //======================= 店铺月报 =======================
    /**
     * 店铺月报消息
     */
    //public static final String MSG_SHOP_DAILYREPORT_MONTH = "msg:shop:dailyReportForMonth";

    //=================  友商 ===================
    /**
     * 查看友商信息
     */
    public static final String LEAGUER_CHECK_INFO = "leaguer:check:info";

    /**
     * 查看商家联盟信息权限(废弃)
     */
    public static final String SHOW_PRODUCT_UNIONSHOP = "show:product:unionShop";
    /**
     * 查看商家联盟信息权限
     */
    public static final String SHOW_UNION_UNIONSHOP = "show:union:unionShop";

    //=================  记一笔 ===================
    /**
     * 查看全部记一笔
     */
    public static final String SHOP_CHECK_SHOPBILL = "shop:check:shopBill";


    /**
     * 维修保养编辑权限
     */
    public static final String SALE_SERVICE_UPDATE = "sale:service:update";

}
