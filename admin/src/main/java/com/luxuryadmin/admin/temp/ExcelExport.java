package com.luxuryadmin.admin.temp;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author：Mong
 * @Date：2019-09-21 11:21
 * @Description：Excel导出工具类，依赖于ClassUtil工具类
 */
public final class ExcelExport {

    /**
     * 将传入的数据导出excel表并下载
     * @param response 返回的HttpServletResponse
     * @param importlist 要导出的对象的集合
     * @param attributeNames 含有每个对象属性在excel表中对应的标题字符串的数组（请按对象中属性排序调整字符串在数组中的位置）
     */
    public static void export(HttpServletResponse response, List<?> importlist, String[] attributeNames,String fileName) {
        //获取数据集
        List<?> datalist = importlist;

        //声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        //生成一个表格
        HSSFSheet sheet = workbook.createSheet();
        //设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 18);

        //获取字段名数组
        String[] tableAttributeName = attributeNames;



        //获取对象属性
        Field[] fields = ClassUtil.getClassAttribute(importlist.get(0));
        //获取对象get方法
        List<Method> methodList = ClassUtil.getMethodGet(importlist.get(0));

        //循环字段名数组，创建标题行
        Row row = sheet.createRow(0);
        // 设置字体
        CellStyle cellStyle = workbook.createCellStyle();
        for (int j = 0; j< tableAttributeName.length; j++){
            //创建列
            Cell cell = row.createCell(j);

            HSSFFont font = workbook.createFont();
            //颜色
            // redFont.setColor(Font.COLOR_RED);
            //设置字体大小
            font.setFontHeightInPoints((short) 18);
            //字体
            font.setFontName("宋体");
            font.setBold(true);
            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cell.setCellStyle(cellStyle);

            //设置单元类型为String
            cell.setCellType(CellType.STRING);
            cell.setCellValue(transCellType(tableAttributeName[j]));
        }
        HSSFFont font1 = workbook.createFont();
        CellStyle cellStyle1 = workbook.createCellStyle();
        HSSFFont font2 = workbook.createFont();
        CellStyle cellStyle2 = workbook.createCellStyle();
        //创建普通行
        for (int i = 0;i<datalist.size();i++){
            //因为第一行已经用于创建标题行，故从第二行开始创建
            row = sheet.createRow(i+1);
            //如果是第一行就让其为标题行
            Object targetObj = datalist.get(i);

            for (int j = 0;j<fields.length;j++){
                //创建列
                Cell cell1 = row.createCell(j);
                cell1.setCellType(CellType.STRING);
                if (j==1 || j==2){
                    //颜色
                    // redFont.setColor(Font.COLOR_RED);
                    //设置字体大小
                    font1.setFontHeightInPoints((short) 14);
                    //字体
                    font1.setFontName("宋体");
                    font1.setBold(true);
                    cellStyle1.setFont(font1);
                    cellStyle1.setAlignment(HorizontalAlignment.CENTER);
                    cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
                    cellStyle1.setWrapText(true);

                    cell1.setCellStyle(cellStyle1);
                }else {
                    //颜色
                    // redFont.setColor(Font.COLOR_RED);
                    //设置字体大小
                    font2.setFontHeightInPoints((short) 14);
                    //字体
                    font2.setFontName("宋体");
                    font2.setBold(false);
                    cellStyle2.setFont(font2);
                    cellStyle2.setWrapText(true);
                    cellStyle2.setAlignment(HorizontalAlignment.CENTER);
                    cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
                    cell1.setCellStyle(cellStyle2);
                }

                //
                try {
                    Object value = methodList.get(j).invoke(targetObj, new Object[]{});
                    cell1.setCellValue(transCellType(value));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        response.setContentType("application/octet-stream");
        //默认Excel名称
        response.setHeader("Content-Disposition", "attachment;fileName="+fileName+".xls");

        try {
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String transCellType(Object value){
        String str = null;
        if (value instanceof Date){
            Date date = (Date) value;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            str = sdf.format(date);
        }else{
            str = String.valueOf(value);
            if (str == "null"){
                str = "";
            }
        }

        return str;
    }

}
