package com.luxuryadmin.common.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname ExcelUtils
 * @Description TODO
 * @Date 2020/6/30 17:58
 * @Created by Administrator
 */
@Slf4j
public class ExcelUtils {

    /**
     * Excel 导出
     * @param response
     * @param list 要导出的实体类列表
     * @param fileName
     * @throws IOException
     */
    public static void downExcel(HttpServletResponse response, List list,String fileName) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list,true);
        //宽度自适应
        writer.autoSizeColumnAll();
        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileName,"UTF-8"));
        writer.flush(response.getOutputStream());
        writer.close();
        IoUtil.close(response.getOutputStream());
    }

    /**
     *
     * @param response
     * @param head 导出文件头信息中，实体类属性名 key 与 映射名字 value
     * @param list
     * @param fileName
     * @throws IOException
     */
    public static void downExcel(HttpServletResponse response, Map<String, String> head, List list, String fileName) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.setHeaderAlias(head);
        writer.write(list,true);
        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileName,"UTF-8"));
        writer.flush(response.getOutputStream());
        writer.close();
        IoUtil.close(response.getOutputStream());
    }

    public static <T> List<T> parseExcelByFile(File file, Class<T> c) throws Exception{
        List<T> list = new ArrayList<T>();
        InputStream inputStream = null;
        String fileName = null;
        Workbook wb = null;
        try{
            inputStream = new FileInputStream(file);
            fileName = file.getName();
            if(fileName.endsWith(".xls") || fileName.endsWith(".xlsx")){
                //如果是2003版本
                if(fileName.endsWith(".xls")){
                    //1.先解析文件
                    POIFSFileSystem fs = new POIFSFileSystem(inputStream);
                    wb = new HSSFWorkbook(fs);
                }else if( fileName.endsWith(".xlsx")){//如果是2007以上版本
                    wb = new XSSFWorkbook(inputStream);
                }else{
                    return null;
                }
            }
        }catch(IOException e){
            log.error("" + e);
        }
        return getListByWorkBook(c, list, wb);
    }

    public static <T> List<T> parseExcelByUrl(String path, Class<T> c) throws Exception{
        List<T> list = new ArrayList<T>();
        String type = path.substring(path.lastIndexOf(".") + 1);
        Workbook wb;
        //根据文件后缀（xls/xlsx）进行判断
        InputStream input = new URL(path).openStream();
        if ("xls".equals(type)) {
            //文件流对象
            wb = new HSSFWorkbook(input);
        } else if ("xlsx".equals(type)) {
            wb = new XSSFWorkbook(input);
        } else {
            throw new Exception("文件 类型错误");
        }
        return getListByWorkBook(c, list, wb);
    }

    private static <T> List<T> getListByWorkBook(Class<T> c, List<T> list, Workbook wb) throws Exception{
        Sheet sheet = wb.getSheetAt(0);
        //获取第一行（标题行）
        Row row1 = sheet.getRow(0);
        //总列数
        int colNum = row1.getPhysicalNumberOfCells();
        //总行数
        int rowNum = sheet.getLastRowNum();
        //将标题行一一放入数组
        String[] titles = new String[colNum];
        for(int i = 0 ; i < colNum ; i++){
            Cell cell = row1.getCell(i);
            cell.setCellType(CellType.STRING);
            titles[i] = cell.getStringCellValue();
        }
        //获取指定对象所有的字段
        try {
            Map<String, Field> fieldMap = new HashMap<String, Field>();
            if (c.equals(String.class) || c.equals(Integer.class) || c.equals(Long.class)) {
                for (int i = 1; i < rowNum + 1; i++) {
                    Cell cell = sheet.getRow(i).getCell(0);
                    cell.setCellType(CellType.STRING);
                    String value = String.valueOf(cell);
                    list.add((T) value);
                }
            } else {
                Field fields[] = c.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    fieldMap.put(fields[i].getName(), fields[i]);
                }
                //使用反射机制，将值存入对应对象中
                for (int i = 1; i < rowNum + 1; i++) {
                    T t = c.newInstance();
                    for (int j = 0; j < titles.length; j++) {
                        //当excel中有这个字段
                        if (fieldMap.containsKey(titles[j])) {
                            String fieldName = titles[j];
                            String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                            //调用该字段对应的set方法
                            Class cc = fieldMap.get(titles[j]).getType();
                            Method method = c.getMethod(methodName, cc);
                            String value = String.valueOf(sheet.getRow(i).getCell(j));
                            method.invoke(t, parseValue(value, cc));
                        }
                    }
                    list.add(t);
                }
            }
        }catch (Exception e){
            log.error("" + e);
        }
        return list;
    }

    /**
     * 将字符串转化为指定类型的对象
     * @param s----要转化的字符串
     * @param c----目标对象类型
     * @return
     */
    private static Object parseValue(String s,Class c){
        Object obj = null;
        String className = c.getName();
        //excel中的数字解析之后可能末尾会有.0，需要去除
        if(s.endsWith(".0")){
            s = s.substring(0, s.length()-2);
        }
        if("java.lang.Integer".equals(className)){
            obj =  new Integer(s);
        }else if(className.equals("int")){
            obj = (int)Integer.parseInt(s);
        }else if(className.equals("java.lang.String")){
            obj = s;
        }else if(className.equals("java.lang.Double")){
            obj = new Double(s);
        }else if(className.equals("double")){
            obj = (double)new Double(s);
        }else if(className.equals("java.lang.Float")){
            obj = new Float(s);
        }else if(className.equals("float")){
            obj = (float)new Float(s);
        }else if(className.equals("java.util.Date")){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                obj = sdf.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else if(className.equals("long")){
            obj = Long.parseLong(s);
        }else if(className.equals("java.util.Long")){
            obj = new Long(s);
        }
        return obj;
    }


    public static void main(String[] args) {
        String excelUrl = "http://file-test.luxuryadmin.com/push/excel/443719519823088.xlsx";
        try {
            List<String> phoneList = parseExcelByUrl(excelUrl,String.class);
            int index = 0;
            for (String phone : phoneList) {
                index ++;
                System.out.println("phone【"+index+"】:"+phone);
            }
        } catch (Exception e) {
            System.out.println("" + e);
        }
    }

}
