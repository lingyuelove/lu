package com.luxuryadmin.common.utils.aliyun;


import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 描述:
 * EasyExcel的基础封装
 *
 * @author XueGuCheng
 * @create 2021-03-19 23:01
 */
@Slf4j
public class MyEasyExcel {


    /**
     * 设置响应头
     *
     * @param response 回应的请求数据
     * @param fileName 文件名字
     */
    public static void setHead(HttpServletResponse response, String fileName) {

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("编码异常");
        }
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

    }

    /**
     * 设置Excel的格式
     *
     * @return 格式化后的Excel
     */
    public static HorizontalCellStyleStrategy formatExcel() {
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 10);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 10);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 设置自动换行
        contentWriteCellStyle.setWrapped(true);
        // 设置垂直居中
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);

        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

    }

    /**
     * 设置头部单元格宽度
     */
    public static class ExcelWidthStyleStrategy extends AbstractColumnWidthStyleStrategy {

        @Override
        protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<CellData> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {
            // 设置宽度
            Sheet sheet = writeSheetHolder.getSheet();
            sheet.setColumnWidth(cell.getColumnIndex(), 5000);
        }
    }


}

