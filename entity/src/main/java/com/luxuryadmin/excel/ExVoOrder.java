package com.luxuryadmin.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.net.URL;

/**
 * 商品导出模板
 *
 * @author Administrator
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ContentStyle(horizontalAlignment = HorizontalAlignment.CENTER)
@HeadStyle(horizontalAlignment = HorizontalAlignment.CENTER)
@ContentRowHeight(75)
public class ExVoOrder extends ExVoProduct {

    /**
     * 订单编号
     */
    @ExcelProperty(value = "订单编号")
    @ColumnWidth(20)
    private String orderNumber;


    /**
     * 订单状态
     */
    @ExcelProperty(value = "订单状态")
    @ColumnWidth(12)
    private String orderState;

    /**
     * 订单类型
     */
    @ExcelProperty(value = "订单类型")
    @ColumnWidth(12)
    private String orderType;

    /**
     * 销售人员
     */
    @ExcelProperty(value = "销售人员")
    @ColumnWidth(12)
    private String saleUser;

    /**
     * 销售数量
     */
    @ExcelProperty(value = "销售数量")
    @ColumnWidth(12)
    private String saleNum;

    /**
     * 成交价(元)
     */
    @ExcelProperty(value = "成交价(元)")
    @ColumnWidth(12)
    private String finishPrice;

    /**
     * 销售时间
     */
    @ExcelProperty(value = "销售时间")
    @ColumnWidth(20)
    private String saleTime;

    /**
     * 开单时间
     */
    @ExcelProperty(value = "开单时间")
    @ColumnWidth(20)
    private String orderInsertTime;


    /**
     * 订单备注
     */
    @ExcelProperty(value = "订单备注")
    @ColumnWidth(50)
    private String orderRemark;

}
