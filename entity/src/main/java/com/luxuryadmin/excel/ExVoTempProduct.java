package com.luxuryadmin.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.net.URL;

/**
 * @PackgeName: com.luxuryadmin.excel
 * @ClassName: ExVoTempProduct
 * @Author: ZhangSai
 * Date: 2021/10/21 16:45
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ContentStyle(horizontalAlignment = HorizontalAlignment.CENTER)
@HeadStyle(horizontalAlignment = HorizontalAlignment.CENTER)
@ContentRowHeight(75)
public class ExVoTempProduct {
    /**
     * 商品首图
     */
    @ExcelProperty(value = "商品首图")
    @ColumnWidth(15)
    private URL url;

    /**
     * 商品名称
     */
    @ExcelProperty(value = "商品名称")
    @ColumnWidth(50)
    private String name;
    /**
     * 商品属性;英文;
     */
    @ExcelProperty(value = "商品属性")
    @ColumnWidth(50)
    private String attributeUs;

    /**
     * 商品分类;英文;
     */
    @ExcelProperty(value = "商品分类")
    @ColumnWidth(50)
    private String classifyCn;

    /**
     * 价格
     */
    @ExcelProperty(value = "到手价格")
    @ColumnWidth(12)
    private String initPrice;

    @ExcelProperty(value = "结算价格")
    @ColumnWidth(12)
    private String showPrice;

    /**
     * 适用人群
     */
    @ExcelProperty(value = "适用人群")
    @ColumnWidth(12)
    private String targetUser;
    /**
     * 总库存
     */
    @ExcelProperty(value = "库存数量")
    @ColumnWidth(12)
    private Integer totalNum;

    /**
     * 商品状态;中文;
     */
    @ExcelProperty(value = "商品状态")
    @ColumnWidth(12)
    private String stateCn;

    /**
     * 商品状态;中文;
     */
    @ExcelProperty(value = "时间")
    @ColumnWidth(12)
    private String showTime;

    /**
     * 商品描述
     */
    @ExcelProperty(value = "商品描述")
    @ColumnWidth(50)
    private String description;
}
