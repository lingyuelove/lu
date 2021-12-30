package com.luxuryadmin.enums.pro;

import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.enums.op.EnumOpMessageSubType;
import com.luxuryadmin.vo.op.VoMessageSubType;
import com.luxuryadmin.vo.pro.VoProduct;
import com.luxuryadmin.vo.pro.VoProductLoad;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.enums.pro
 * @ClassName: EnumProPrintTpl
 * @Author: ZhangSai
 * Date: 2021/8/11 11:52
 */
public enum EnumProPrintTpl {
    //店铺名称
    SHOP_NAME("shopName", "店铺名称", "店铺名称"),
    PRODUCT_NAME("productName", "商品名称", "商品名称"),
    SHOP_PRODUCT_NAME("shopAndProductName", "店铺+商品名称", "店铺+商品名称"),
    REPAIR_CARD_TIME("repairCardTime", "保卡年份", "保卡年份"),
    SALE_PRICE("salePrice", "销售价格", "销售价格"),
    TRADE_PRICE("tradePrice", "同行价格", "同行价格"),
    AUTO_NUMBER("autoNumber", "系统编码", "系统编码"),
    UNION_CODE("uniqueCode", "独立编码", "独立编码"),
    DESCRIPTION("description", "商品描述", "商品描述"),
    QT("QT", "自定义输入行", "自定义输入行");
    private String code;
    private String name;
    private String description;

    EnumProPrintTpl(String code, String name, String description) {
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

    /**
     * 获取所有店铺消息子类型中文名称
     *
     * @return
     */
    public static List<VoMessageSubType> getAllProPrintTplCnName() {
        List<VoMessageSubType> subTypeCnNameList = new ArrayList<>();
        for (EnumProPrintTpl value : EnumProPrintTpl.values()) {
            VoMessageSubType vo = new VoMessageSubType();
            vo.setCode(value.getCode());
            vo.setCnName(value.getName());
            subTypeCnNameList.add(vo);
        }
        return subTypeCnNameList;
    }


    public static String getStateName(String content, VoProductLoad product) {

        List<String> stringList = Arrays.asList(content.split(";"));
        List<String> sList = new ArrayList<>();

        stringList.forEach(s -> {
            String name = null;
            switch (s) {
                case "店铺名称":
                    name = s + ":" + product.getShopName() + "\n";
                    break;
                case "商品名称":
                    name = s + ":" + product.getName() + "\n";
                    break;
                case "店铺+商品名称":
                    name = s + ":" + product.getShopName() + "+" + product.getName() + "\n";
                    break;
                case "保卡年份":
                    name = s + ":" + product.getRepairCardTime() + "\n";
                    break;
                case "销售价格":
                    BigDecimal salePrice = new BigDecimal( product.getSalePrice()).divide(new BigDecimal(100.00));
                    String salePriceToStr = StringUtil.removeEnd(salePrice.toString(), ".00");
                    name = s + ":" +salePriceToStr + "\n";
                    break;
                case "同行价格":
                    BigDecimal tradePrice = new BigDecimal( product.getTradePrice()).divide(new BigDecimal(100.00));
                    String tradePriceToStr = StringUtil.removeEnd(tradePrice.toString(), ".00");
                    name = s + ":" + tradePriceToStr + "\n";
                    break;
                case "系统编码":
                    if (LocalUtils.isEmptyAndNull(product.getAutoNumber())){
                        name = s + ":"+ "\n";
                    }else {
                        name = s + ":" + product.getAutoNumber() + "\n";
                    }
                    break;
                case "独立编码":
                    if (LocalUtils.isEmptyAndNull(product.getUniqueCode())){
                        name = s + ":"+ "\n";
                    }else {
                        name = s + ":" + product.getUniqueCode() + "\n";
                    }

                    break;
                case "商品描述":
                    if (LocalUtils.isEmptyAndNull(product.getDescription())){
                        name = s + ":"+ "\n";
                    }else {
                        name = s + ":" + product.getDescription() + "\n";
                    }
                    break;
                case "自定义输入行":
                    name = "\n";
                    break;
                default:
                    name = null;
                    break;
            }
            if (!LocalUtils.isEmptyAndNull(name)) {
                sList.add(name);
            }
        });
        if (LocalUtils.isEmptyAndNull(sList)) {
            return null;
        }
        String tagNameList = StringUtils.join(sList, "");
        return tagNameList;
    }
}
