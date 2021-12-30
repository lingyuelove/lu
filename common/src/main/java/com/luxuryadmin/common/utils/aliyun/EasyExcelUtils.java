package com.luxuryadmin.common.utils.aliyun;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: EasyExcelUtils
 * @Description: EasyExcel工具类(替换com.example.utils.easyexcel)
 * @Author: jiangbeiping
 * @Data: 2021/3/18 16:46
 * @Version: 1.0
 */
@Slf4j
public class EasyExcelUtils {

    /**
     * 每个sheet的容量，即超过60000时就会把数据分sheet
     */
    private static final int PAGE_SIZE = 60000;


    /**
     * 导出报表(使用注解方式设置Excel头数据)
     *
     * @param response   响应请求
     * @param data       报表数据
     * @param fileName   文件名字
     * @param excelClass 报表实体类的Class（根据该Class的属性来设置Excel的头属性）
     */
//    public static void exportByExcel(HttpServletResponse response, List<?> data, String fileName, Class<?> excelClass) throws IOException {
//        @Cleanup ByteArrayOutputStream os = new ByteArrayOutputStream();
////        ServletOutputStream os = response.getOutputStream();
//        long exportStartTime = System.currentTimeMillis();
//        ;
//        log.info("报表导出Size: " + data.size() + "条。");
//
//        // 把查询到的数据按设置的sheet的容量进行切割
//        List<? extends List<?>> lists = SplitList.splitList(data, PAGE_SIZE);
//        // 设置响应头
//        EasyExcel.setHead(response, fileName);
//        // 格式化Excel数据
//        // EasyExcelBasic.formatExcel()：设置Excel的格式
//        // EasyExcelBasic.ExcelWidthStyleStrategy():设置头部单元格宽度
//        ExcelWriter excelWriter = EasyExcel.write(os, excelClass).registerWriteHandler(com.example.utils.easyexcel.EasyExcel.formatExcel()).registerWriteHandler(new com.example.utils.easyexcel.EasyExcel.ExcelWidthStyleStrategy()).build();
//
//        // 浏览器访问url直接下载文件的方式
//        //ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), excelClass).registerWriteHandler(com.example.utils.easyexcel.EasyExcel.formatExcel()).registerWriteHandler(new com.example.utils.easyexcel.EasyExcel.ExcelWidthStyleStrategy()).build();
//
//        ExcelWriterSheetBuilder excelWriterSheetBuilder;
//        WriteSheet writeSheet;
//        for (int i = 1; i <= lists.size(); ++i) {
//            excelWriterSheetBuilder = new ExcelWriterSheetBuilder(excelWriter);
//            excelWriterSheetBuilder.sheetNo(i).sheetName("sheet" + i);
//            writeSheet = excelWriterSheetBuilder.build();
//            excelWriter.write(lists.get(i - 1), writeSheet);
//        }
//        // 必须要finish才会写入，不finish只会创建empty的文件
//        excelWriter.finish();
//
//        byte[] content = os.toByteArray();
//        @Cleanup InputStream is = new ByteArrayInputStream(content);
//
//        // 文件落地，用来测试文件的格式和数据的完整性
//        // @Cleanup:lombok关流注解
//        //FileOutputStream fileOutputStream = new FileOutputStream("/data/logs/" + fileName + ".xlsx");
//        //@Cleanup BufferedInputStream bis = new BufferedInputStream(is);
//        //@Cleanup BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
//        //byte[] buff = new byte[2048];
//        //int bytesRead;
//        //while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
//        //    bos.write(buff, 0, bytesRead);
//        // }
//        //log.info("文件落地磁盘");
//
//
//        // 文件上传到OSS
//        FileUploadUtil_Test.upLocalFileToOss(is, fileName);
//
//
//        System.out.println("报表导出结束时间:" + new Date() + ";导出耗时: " + (System.currentTimeMillis() - exportStartTime) + "ms");
//
//    }


}

