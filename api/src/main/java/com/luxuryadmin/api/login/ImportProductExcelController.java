package com.luxuryadmin.api.login;

import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ThreadUtils;
import com.luxuryadmin.common.utils.aliyun.OSSUtil;
import com.luxuryadmin.entity.pro.ProDetail;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.mapper.pro.ProDetailMapper;
import com.luxuryadmin.mapper.pro.ProProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.jsoup.helper.DataUtil;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * 该类为额外操作类;与本项目的业务无关;
 *
 * @author monkey king
 * @date 2019-12-16 11:31:36
 */
@Slf4j
@RestController
@RequestMapping(value = "/tool", method = RequestMethod.GET)
@ApiIgnore
public class ImportProductExcelController {

    @Resource
    private ProProductMapper proProductMapper;

    @Resource
    private ProDetailMapper proDetailMapper;


    @RequestMapping("/importProductExcel")
    public BaseResult importProductExcel(@RequestParam String excelPath, @RequestParam String shopId) {
        if (!new File(excelPath).exists()) {
            return BaseResult.defaultErrorWithMsg(excelPath + " 路径不存在");
        }
        ThreadUtils.getInstance().executorService.execute(() -> {
            long st = System.currentTimeMillis();
            readExcel(excelPath, shopId);
            long et = System.currentTimeMillis();
            log.info("导入文件: {},耗时: {}", excelPath, (et - st));
        });
        return BaseResult.okResult();
    }

    @RequestMapping("/importDxlExcel")
    public BaseResult importDxlExcel(@RequestParam String excelPath, @RequestParam String shopId) {
        long st = System.currentTimeMillis();
        if (!new File(excelPath).exists()) {
            return BaseResult.defaultErrorWithMsg("路径不存在");
        }
        readDxlExcel(excelPath, shopId);
        long et = System.currentTimeMillis();
        System.out.println("耗时: " + (et - st));
        return BaseResult.okResult();
    }


    private void readExcel(String excelPath, String shopId) {
        Workbook wb;
        Sheet sheet;

        try {

            log.info("==========加载excel文件===========");
            //String filePath = "E:\\奢当家导入商品模板(1).xlsx";
            InputStream is = new FileInputStream(excelPath);
            wb = new XSSFWorkbook(is);
            foreachCell(wb.getSheetAt(0), Integer.parseInt(shopId));
            //foreachCell(wb.getSheetAt(1), daEFenQiKaList);

            log.info("================================");
            log.info("================================");
            wb.close();

            log.info("===========遍历结束=================");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void readDxlExcel(String excelPath, String shopId) {
        Workbook wb;
        Sheet sheet;

        try {

            System.out.println("==========加载excel文件===========");
            //String filePath = "E:\\奢当家导入商品模板(1).xlsx";
            InputStream is = new FileInputStream(excelPath);
            wb = new XSSFWorkbook(is);
            foreachDxlCell(wb.getSheetAt(0), Integer.parseInt(shopId));
            //foreachCell(wb.getSheetAt(1), daEFenQiKaList);

            System.out.println("================================");
            System.out.println("================================");
            wb.close();

            System.out.println("===========遍历结束=================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void foreachCell(Sheet sheet, int shopId) {
        Row row;

        int totalRow = sheet.getLastRowNum();
        log.info("==========加载excel文件完成,开始遍历===========");
        Map<String, List<PictureData>> maplist = getPictures2((XSSFSheet) sheet);

        log.info("行数： " + totalRow);
        //用户填的数据从第8行开始
        for (int i = 7; i <= totalRow; i++) {
            try {
                row = sheet.getRow(i);
                if (LocalUtils.isEmptyAndNull(row)) {
                    continue;
                }

                //Cell 序号 = row.getCell(0);
                Cell nameCell = row.getCell(1);
                String proName = LocalUtils.isEmptyAndNull(nameCell) ? "" : nameCell.toString();
                Cell descriptionCell = row.getCell(2);
                if (LocalUtils.isEmptyAndNull(descriptionCell) || LocalUtils.isEmptyAndNull(descriptionCell.toString())) {
                    //如果商品描述没填写,商品名称也没填写,则此条记录作废;
                    if (LocalUtils.isEmptyAndNull(proName)) {
                        continue;
                    }
                }
                Cell proAttrCell = row.getCell(3);
                Cell proClassifyCell = row.getCell(4);
                Cell numCell = row.getCell(5);
                Cell initPriceCell = row.getCell(6);
                Cell tradePriceCell = row.getCell(7);
                Cell agencyPriceCell = row.getCell(8);
                Cell salePriceCell = row.getCell(9);
                Cell unicodeCell = row.getCell(10);
                Cell remarkCell = row.getCell(11);
                Cell isShareCell = row.getCell(12);
                String temp = "(暂未填写信息)";

                String[] imgArray = printImg(maplist, i, shopId);
                String description = descriptionCell.toString();
                if (LocalUtils.isEmptyAndNull(proName)) {
                    //不足20个字符,则全部作为名称
                    proName = description;
                }
                proName = proName.substring(0, Math.min(proName.length(), 20));
                description = description.substring(0, Math.min(description.length(), 250));
                //商品属性
                String proAttr = LocalUtils.returnEmptyStringOrString(proAttrCell + "");
                if (proAttr.contains("自")) {
                    proAttr = "10";
                } else if (proAttr.contains("寄")) {
                    proAttr = "20";
                } else if (proAttr.contains("质")) {
                    proAttr = "30";
                } else {
                    proAttr = "40";
                }


                //商品属性
                String classify = LocalUtils.returnEmptyStringOrString(proClassifyCell + "");
                if (classify.contains("腕表") || classify.contains("手表")) {
                    classify = "WB";
                } else if (classify.contains("珠宝")) {
                    classify = "ZB";
                } else if (classify.contains("鞋靴")) {
                    classify = "XX";
                } else if (classify.contains("箱包")) {
                    classify = "XB";
                } else if (classify.contains("配饰")) {
                    classify = "PS";
                } else {
                    classify = "QT";
                }

                int num = 1;
                try {
                    num = LocalUtils.isEmptyAndNull(numCell) ? 1 : Integer.parseInt(numCell.toString().trim());
                } catch (NumberFormatException ignored) {
                }

                BigDecimal initPrice = new BigDecimal(0);
                try {
                    initPrice = LocalUtils.isEmptyAndNull(initPriceCell) ? initPrice
                            : LocalUtils.calcNumber(initPriceCell, "*", 100);
                } catch (NumberFormatException ignored) {
                }

                BigDecimal tradePrice = new BigDecimal(0);
                try {
                    tradePrice = LocalUtils.isEmptyAndNull(tradePriceCell) ? tradePrice
                            : LocalUtils.calcNumber(tradePriceCell, "*", 100);
                } catch (NumberFormatException ignored) {
                }

                BigDecimal agencyPrice = new BigDecimal(0);
                try {
                    agencyPrice = LocalUtils.isEmptyAndNull(agencyPriceCell) ? agencyPrice
                            : LocalUtils.calcNumber(agencyPriceCell, "*", 100);
                } catch (NumberFormatException ignored) {
                }

                BigDecimal salePrice = new BigDecimal(0);
                try {
                    salePrice = LocalUtils.isEmptyAndNull(salePriceCell) ? salePrice
                            : LocalUtils.calcNumber(salePriceCell, "*", 100);
                } catch (NumberFormatException ignored) {
                }

                String unicode = LocalUtils.isEmptyAndNull(unicodeCell) ? "" : unicodeCell.toString();

                String remark = LocalUtils.isEmptyAndNull(remarkCell) ? "" : remarkCell.toString();

                remark = remark.substring(0, Math.min(remark.length(), 250));

                String isShare = LocalUtils.isEmptyAndNull(isShareCell) ? "10" : isShareCell.toString();

                if ("yes".equalsIgnoreCase(isShare)) {
                    isShare = "22";
                } else {
                    isShare = "10";
                }


                String bizId = LocalUtils.getUUID();
                Date date = new Date();
                ProProduct pro = new ProProduct();
                pro.setBizId(bizId);
                pro.setName(proName);
                pro.setFkShpShopId(shopId);
                pro.setDescription(description);
                pro.setFkProAttributeCode(proAttr);
                pro.setFkProClassifyCode(classify);
                pro.setTotalNum(num);
                pro.setInitPrice(initPrice);
                pro.setTradePrice(tradePrice);
                pro.setAgencyPrice(agencyPrice);
                pro.setSalePrice(salePrice);
                pro.setRemark(remark);
                pro.setInsertAdmin(-7);
                pro.setInsertTime(date);
                pro.setFkProStateCode(10);
                pro.setShare(isShare);
                pro.setSmallImg(imgArray[0]);
                proProductMapper.saveObject(pro);

                ProDetail det = new ProDetail();
                det.setInsertTime(date);
                det.setFkProProductId(pro.getId());
                det.setFkShpShopId(shopId);
                det.setUniqueCode(unicode);
                det.setProductImg(imgArray[1]);
                proDetailMapper.saveObject(det);

            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    private void foreachDxlCell(Sheet sheet, int shopId) {
        Row row;
        try {
            int totalRow = sheet.getLastRowNum();
            System.out.println("==========加载excel文件完成,开始遍历===========");
            // Map<String, List<PictureData>> maplist = getPictures2((XSSFSheet) sheet);
            //printImg(maplist);
            log.info("行数： " + totalRow);
            //段小狸得数据从第二行开始,索引为1
            for (int i = 1; i <= totalRow; i++) {
                row = sheet.getRow(i);
                if (LocalUtils.isEmptyAndNull(row)) {
                    continue;
                }
                //Cell 序号 = row.getCell(0);
                Cell nameCell = row.getCell(1);
                Cell descriptionCell = row.getCell(2);
                Cell proAttrCell = row.getCell(3);
                Cell proClassifyCell = row.getCell(4);
                Cell numCell = row.getCell(5);
                Cell initPriceCell = row.getCell(6);
                Cell tradePriceCell = row.getCell(7);
                Cell agencyPriceCell = row.getCell(8);
                Cell salePriceCell = row.getCell(9);
                Cell unicodeCell = row.getCell(10);
                Cell remarkCell = row.getCell(11);
                Cell isShareCell = row.getCell(12);
                String temp = "(暂未填写信息)";
                String proName = LocalUtils.isEmptyAndNull(nameCell) ? "" : nameCell.toString();
                String description = LocalUtils.isEmptyAndNull(descriptionCell) ? temp : descriptionCell.toString();

                if (LocalUtils.isEmptyAndNull(proName) && !LocalUtils.isEmptyAndNull(description)) {
                    int length = description.length();
                    //不足20个字符,则全部作为名称
                    proName = description.substring(0, Math.min(length, 20));
                }
                //商品属性
                String proAttr = LocalUtils.returnEmptyStringOrString(proAttrCell + "");
                if (proAttr.contains("自")) {
                    proAttr = "10";
                } else if (proAttr.contains("寄")) {
                    proAttr = "20";
                } else if (proAttr.contains("质")) {
                    proAttr = "30";
                } else {
                    proAttr = "40";
                }


                //商品属性
                String classify = LocalUtils.returnEmptyStringOrString(proClassifyCell + "");
                if (classify.contains("腕表")) {
                    classify = "WB";
                } else if (classify.contains("珠宝")) {
                    classify = "ZB";
                } else if (classify.contains("鞋靴")) {
                    classify = "XX";
                } else if (classify.contains("箱包")) {
                    classify = "XB";
                } else if (classify.contains("配饰")) {
                    classify = "PS";
                } else {
                    classify = "QT";
                }

                int num = 1;
                try {
                    num = LocalUtils.isEmptyAndNull(numCell) ? 1 : Integer.parseInt(numCell.toString().trim());
                } catch (NumberFormatException ignored) {
                }

                BigDecimal initPrice = new BigDecimal(0);
                try {
                    initPrice = LocalUtils.isEmptyAndNull(initPriceCell) ? initPrice
                            : LocalUtils.calcNumber(initPriceCell, "*", 100);
                } catch (NumberFormatException ignored) {
                }

                BigDecimal tradePrice = new BigDecimal(0);
                try {
                    tradePrice = LocalUtils.isEmptyAndNull(tradePriceCell) ? tradePrice
                            : LocalUtils.calcNumber(tradePriceCell, "*", 100);
                } catch (NumberFormatException ignored) {
                }

                BigDecimal agencyPrice = new BigDecimal(0);
                try {
                    agencyPrice = LocalUtils.isEmptyAndNull(agencyPriceCell) ? agencyPrice
                            : LocalUtils.calcNumber(agencyPriceCell, "*", 100);
                } catch (NumberFormatException ignored) {
                }

                BigDecimal salePrice = new BigDecimal(0);
                try {
                    salePrice = LocalUtils.isEmptyAndNull(salePriceCell) ? salePrice
                            : LocalUtils.calcNumber(salePriceCell, "*", 100);
                } catch (NumberFormatException ignored) {
                }

                String unicode = LocalUtils.isEmptyAndNull(unicodeCell) ? "" : unicodeCell.toString();

                String remark = LocalUtils.isEmptyAndNull(remarkCell) ? "" : remarkCell.toString();

                String isShare = LocalUtils.isEmptyAndNull(isShareCell) ? "10" : isShareCell.toString();

                if ("yes".equalsIgnoreCase(isShare)) {
                    isShare = "22";
                } else {
                    isShare = "10";
                }


                String bizId = LocalUtils.getUUID();
                Date date = new Date();
                ProProduct pro = new ProProduct();
                pro.setBizId(bizId);
                pro.setName(proName);
                pro.setFkShpShopId(shopId);
                pro.setDescription(description);
                pro.setFkProAttributeCode(proAttr);
                pro.setFkProClassifyCode(classify);
                pro.setTotalNum(num);
                pro.setInitPrice(initPrice);
                pro.setTradePrice(tradePrice);
                pro.setAgencyPrice(agencyPrice);
                pro.setSalePrice(salePrice);
                pro.setRemark(remark);
                pro.setInsertAdmin(-7);
                pro.setInsertTime(date);
                pro.setFkProStateCode(10);
                pro.setShare(isShare);
                pro.setSmallImg("/default/defaultProductImg.png");
                proProductMapper.saveObject(pro);

                ProDetail det = new ProDetail();
                det.setInsertTime(date);
                det.setFkProProductId(pro.getId());
                det.setFkShpShopId(shopId);
                det.setUniqueCode(unicode);
                det.setProductImg("/default/defaultProductImg.png");
                proDetailMapper.saveObject(det);

            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    /**
     * 获取图片和位置 (xlsx)
     *
     * @param sheet
     * @return
     * @throws IOException
     */
    private Map<String, List<PictureData>> getPictures2(XSSFSheet sheet) {
        HashMap<String, List<PictureData>> map = new HashMap<>(16);
        List<POIXMLDocumentPart> list = sheet.getRelations();
        List<PictureData> listData;
        for (POIXMLDocumentPart part : list) {
            if (part instanceof XSSFDrawing) {
                XSSFDrawing drawing = (XSSFDrawing) part;
                List<XSSFShape> shapes = drawing.getShapes();
                for (XSSFShape shape : shapes) {
                    XSSFPicture picture = (XSSFPicture) shape;
                    XSSFClientAnchor anchor = picture.getPreferredSize();
                    CTMarker marker = anchor.getFrom();
                    int row = marker.getRow();
                    //用户填的数据从第8行开始
                    if (row < 7) {
                        continue;
                    }
                    String key = row + "";

                    listData = map.get(key);
                    if (LocalUtils.isEmptyAndNull(listData)) {
                        listData = new ArrayList<>();
                    }
                    //                //文件上传七牛
                    //QiNiuUtils.uploadOneObject(data,"111_"+picName + "." + ext);
                    //图片保存路径
                    //try {
                    //    String filePath = "D:\\img\\pic" + row + ".jpeg";
                    //    System.out.println(filePath);
                    //    FileOutputStream out = new FileOutputStream(filePath);
                    //    out.write(picture.getPictureData().getData());
                    //    out.close();
                    //} catch (Exception e) {
                    //    e.printStackTrace();
                    //}
                    listData.add(picture.getPictureData());

                    map.put(key, listData);
                }
            }
        }
        return map;
    }


    /**
     * 图片写出
     *
     * @param sheetList
     * @throws Exception
     */
    private String[] printImg(Map<String, List<PictureData>> sheetList, int rows, int shopId) throws Exception {
        String smallImgUrl = "";
        String proDetailImgUrl;
        StringBuilder proDetailSb = new StringBuilder();
        String defaultImgUrl = "/default/defaultProductImg.png";
        if (!LocalUtils.isEmptyAndNull(sheetList)) {
            String today = DateUtil.format(new Date(), "yyyyMMdd");
            // 获取图片流
            List<PictureData> picList = sheetList.get(rows + "");
            if (!LocalUtils.isEmptyAndNull(picList)) {
                for (PictureData pic : picList) {
                    // 获取图片格式
                    String ext = pic.suggestFileExtension();
                    byte[] data = pic.getData();
                    String fileName = "product/picture/" + shopId + "/" + today + "/-7/" + System.currentTimeMillis() + "." + ext;
                    if ("".equals(smallImgUrl)) {
                        smallImgUrl = fileName;
                    }
                    proDetailSb.append("/").append(fileName).append(";");
                    OSSUtil.uploadBytesFile(fileName, data);
                }
            }
        }
        smallImgUrl = LocalUtils.isEmptyAndNull(smallImgUrl) ? defaultImgUrl : "/" + smallImgUrl;
        proDetailImgUrl = proDetailSb.length() > 0 ? proDetailSb.substring(0, proDetailSb.length() - 1) : defaultImgUrl;
        return new String[]{smallImgUrl, proDetailImgUrl};
    }

    public static void main(String[] args) {
        String a = "";
        System.out.println(a.substring(0, 0));

    }


}
