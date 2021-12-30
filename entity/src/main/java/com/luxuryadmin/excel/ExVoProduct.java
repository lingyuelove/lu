package com.luxuryadmin.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import javafx.scene.control.Cell;
import lombok.Data;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.net.URL;
import java.util.Date;

/**
 * 商品导出模板
 *
 * @author Administrator
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ContentStyle(horizontalAlignment = HorizontalAlignment.CENTER)
@HeadStyle(horizontalAlignment = HorizontalAlignment.CENTER)
@ContentRowHeight(75)
public class ExVoProduct {

    /**
     * 商品首图
     */
    @ExcelProperty(value = "商品首图")
    @ColumnWidth(15)
    private URL url;


    /**
     * 系统编码
     */
    @ColumnWidth(12)
    @ExcelProperty(value = "系统编码")
    private String autoNumber;

    /**
     * 商品名称
     */
    @ExcelProperty(value = "商品名称")
    @ColumnWidth(50)
    private String name;

    /**
     * 商品描述
     */
    @ExcelProperty(value = "商品描述")
    @ColumnWidth(50)
    private String description;

    /**
     * 商品分类
     */
    @ExcelProperty(value = "商品分类")
    @ColumnWidth(12)
    private String classify;


    /**
     * 商品状态;中文;
     */
    @ExcelProperty(value = "商品状态")
    @ColumnWidth(12)
    private String state;


    /**
     * 商品属性;中文
     */
    @ExcelProperty(value = "商品属性")
    @ColumnWidth(12)
    private String attribute;


    /**
     * 剩余库存
     */
    @ExcelProperty(value = "剩余库存")
    @ColumnWidth(12)
    private String totalNum;


    /**
     * 成本价(元)
     */
    @ExcelProperty(value = "成本价(元)")
    @ColumnWidth(12)
    private String initPrice;


    /**
     * 友商价(元)
     */
    @ExcelProperty(value = "友商价(元)")
    @ColumnWidth(12)
    private String tradePrice;

    /**
     * 代理价(元)
     */
    @ExcelProperty(value = "代理价(元)")
    @ColumnWidth(12)
    private String agencyPrice;

    /**
     * 销售价(元)
     */
    @ExcelProperty(value = "销售价(元)")
    @ColumnWidth(12)
    private String salePrice;


    /**
     * 保卡;0:没有; 1:有;
     */
    @ExcelProperty(value = "保卡")
    @ColumnWidth(12)
    private String repairCard;


    /**
     * 商品独立编码
     */
    @ExcelProperty(value = "独立编码")
    @ColumnWidth(12)
    private String uniqueCode;

    /**
     * 入库时间
     */
    @ExcelProperty(value = "入库时间")
    @ColumnWidth(20)
    private String insertTime;

    /**
     * 入库人员
     */
    @ExcelProperty(value = "入库人员")
    @ColumnWidth(12)
    private String uploadUser;

    /**
     * 回收人员
     */
    @ExcelProperty(value = "回收人员")
    @ColumnWidth(12)
    private String recycleUser;


    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    @ColumnWidth(50)
    private String remark;

    /**
     * 商品首图
     */
    @ExcelIgnore
    private String smallImg;
}
