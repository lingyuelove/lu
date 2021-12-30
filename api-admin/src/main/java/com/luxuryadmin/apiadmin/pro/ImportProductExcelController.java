package com.luxuryadmin.apiadmin.pro;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.common.utils.ThreadUtils;
import com.luxuryadmin.common.utils.aliyun.AliHttpUtils;
import com.luxuryadmin.common.utils.aliyun.OSSUtil;
import com.luxuryadmin.entity.pro.ProDetail;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.mapper.pro.ProDetailMapper;
import com.luxuryadmin.mapper.pro.ProProductMapper;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.pro.ParamProductUpload;
import com.luxuryadmin.service.pro.ProProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
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
@RequestMapping(value = "/shop/admin/pro")
@Api(tags = {"C003.【商品】模块"})
public class ImportProductExcelController extends BaseController {

    @Resource
    private ProProductMapper proProductMapper;

    @Resource
    private ProDetailMapper proDetailMapper;

    @Autowired
    private ServicesUtil servicesUtil;


    /**
     * 上传excel商品
     *
     * @param paramToken
     * @return
     * @throws Exception
     */
    @ApiOperation(
            value = "上传excel商品;",
            notes = "上传excel商品;",
            httpMethod = "POST")
    @PostMapping("/uploadProductExcel")
    public BaseResult uploadProductExcel(
            @Valid ParamToken paramToken, BindingResult result,
            @RequestParam("file") MultipartFile file) {
        servicesUtil.validControllerParam(result);
        if (file.isEmpty()) {
            return BaseResult.errorResult("请上传文件!");
        }
        if (file.getSize() > 300 * 1024 * 1024) {
            return BaseResult.errorResult("上传文件大小超出限制300M.");
        }
        String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if (!"xlsx".equals(fileType)) {
            return BaseResult.errorResult("文件各式必须为xlsx");
        }
        StringBuffer dirName = new StringBuffer();
        dirName.append("product/importExcel/").append(getShopId());
        dirName.append("/").append(DateUtil.format(new Date(), "YYYYMMdd"));
        dirName.append("/").append(getUserId()).append("/");
        String fileName = System.currentTimeMillis() + "." + fileType;
        String relativePath = "/" + dirName + fileName;
        try {
            String serverPath = OSSUtil.uploadBytesFile(dirName.toString() + fileName, file.getBytes());
            String key = "init:uploadAdmin:" + getShopId() + ":" + getUserId();
            redisUtil.setExMINUTES(key, serverPath, 60);
            log.info("上传成功: {}", relativePath);
            return BaseResult.defaultOkResultWithMsg("文件上传成功,请在1小时之内点击导入!");
            // return BaseResult.okResult(relativePath);
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return BaseResult.errorResult();
    }

    /**
     * 批量导入商品
     *
     * @param param
     * @return
     * @throws Exception
     */
    @ApiOperation(
            value = "批量导入商品;",
            notes = "批量导入商品;",
            httpMethod = "POST")
    @PostMapping("/importProductExcel")
    public BaseResult importProductExcel(
            @Valid ParamToken param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        final String shopId = getShopId() + "";
        String key = "init:uploadAdmin:" + shopId + ":" + getUserId();
        String excelUrl = redisUtil.get(key);
        if (LocalUtils.isEmptyAndNull(excelUrl) || !OSSUtil.exists(excelUrl)) {
            return BaseResult.defaultErrorWithMsg("资源不存在,或已过期,请重新上传!");
        }
        ThreadUtils.getInstance().executorService.execute(() -> {
            long st = System.currentTimeMillis();
            readExcel(excelUrl, shopId);
            long et = System.currentTimeMillis();
            redisUtil.delete(key);
            log.info("导入文件: {},耗时: {}", excelUrl, (et - st));
        });
        return BaseResult.defaultOkResultWithMsg("商品导入正在后台执行,请稍候在仓库查看!");
    }


    private void readExcel(String excelUrl, String shopId) {
        Workbook wb;
        Sheet sheet;

        try {
            log.info("==========加载excel文件===========");
            //String filePath = "E:\\奢当家导入商品模板(1).xlsx";
            //InputStream is = new FileInputStream(excelUrl);
            wb = new XSSFWorkbook(OSSUtil.downloadInputStream(excelUrl));
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
                    //不足50个字符,则全部作为名称
                    proName = description;
                }
                proName = proName.substring(0, Math.min(proName.length(), 50));
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
                    num = LocalUtils.isEmptyAndNull(numCell) ? 1 : Integer.parseInt(getStringCellValue(numCell));
                } catch (NumberFormatException e) {
                    log.error(e.getMessage(), e);
                }

                BigDecimal initPrice = new BigDecimal(0);
                try {
                    initPrice = LocalUtils.isEmptyAndNull(initPriceCell) ? initPrice
                            : LocalUtils.calcNumber(initPriceCell, "*", 100);
                } catch (NumberFormatException e) {
                    log.error(e.getMessage(), e);
                }

                BigDecimal tradePrice = new BigDecimal(0);
                try {
                    tradePrice = LocalUtils.isEmptyAndNull(tradePriceCell) ? tradePrice
                            : LocalUtils.calcNumber(tradePriceCell, "*", 100);
                } catch (NumberFormatException e) {
                    log.error(e.getMessage(), e);
                }

                BigDecimal agencyPrice = new BigDecimal(0);
                try {
                    agencyPrice = LocalUtils.isEmptyAndNull(agencyPriceCell) ? agencyPrice
                            : LocalUtils.calcNumber(agencyPriceCell, "*", 100);
                } catch (NumberFormatException e) {
                    log.error(e.getMessage(), e);
                }

                BigDecimal salePrice = new BigDecimal(0);
                try {
                    salePrice = LocalUtils.isEmptyAndNull(salePriceCell) ? salePrice
                            : LocalUtils.calcNumber(salePriceCell, "*", 100);
                } catch (NumberFormatException e) {
                    log.error(e.getMessage(), e);
                }

                String unicode = LocalUtils.isEmptyAndNull(unicodeCell) ? "" : getStringCellValue(unicodeCell);

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
                pro.setUpdateTime(date);
                //质押商品的初始状态是12
                pro.setFkProStateCode("30".equals(proAttr) ? 12 : 10);
                pro.setShare(isShare);
                pro.setSmallImg(imgArray[0]);
                proProductMapper.saveObject(pro);

                ProDetail det = new ProDetail();
                det.setInsertTime(date);
                det.setUpdateTime(date);
                det.setFkProProductId(pro.getId());
                det.setFkShpShopId(shopId);
                det.setUniqueCode(unicode);
                det.setProductImg(imgArray[1]);
                long autoNumber = LocalUtils.calcNumber(pro.getId(), "+", 1000000).longValue();
                det.setAutoNumber(autoNumber + "");


                proDetailMapper.saveObject(det);

            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    //校验数值型单元格
    private static String getStringCellValue(Cell cell) throws Exception {
        cell.setCellType(CellType.STRING);//转为String类型
        return cell.getStringCellValue();
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

    }


}

