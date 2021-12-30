package com.luxuryadmin.api.login;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ThreadUtils;
import com.luxuryadmin.common.utils.aliyun.OSSUtil;
import com.luxuryadmin.entity.pro.ProPublic;
import com.luxuryadmin.service.pro.ProPublicService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 该类为额外操作类;与本项目的业务无关;
 *
 * @author monkey king
 * @date 2019-12-16 11:31:36
 */
@Slf4j
@RestController
@RequestMapping(value = "/tool")
@Api(tags = {"P001.【基础】模块"})
public class ImportProPublicExcelController extends BaseController {

    @Autowired
    private ProPublicService proPublicService;


    @GetMapping("/importPublicProductExcel")
    public BaseResult importPublicProductExcel(@RequestParam String excelPath) {
        if (!new File(excelPath).exists()) {
            return BaseResult.defaultErrorWithMsg(excelPath + " 路径不存在");
        }
        ThreadUtils.getInstance().executorService.execute(() -> {
            long st = System.currentTimeMillis();
            readExcel(excelPath);
            long et = System.currentTimeMillis();
            log.info("导入文件: {},耗时: {}", excelPath, (et - st));
        });
        return BaseResult.okResult();
    }

    private void readExcel(String excelUrl) {
        Workbook wb;
        Sheet sheet;

        try {
            log.info("==========加载excel文件===========");
            //String filePath = "E:\\奢当家导入商品模板(1).xlsx";
            InputStream is = new FileInputStream(excelUrl);
            wb = new XSSFWorkbook(is);
            foreachCell(wb.getSheetAt(0));
            log.info("================================");
            log.info("================================");
            wb.close();

            log.info("===========遍历结束=================");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void foreachCell(Sheet sheet) {
        Row row;

        int totalRow = sheet.getLastRowNum();
        long st = System.currentTimeMillis();
        log.info("==========加载excel文件完成,开始遍历===========");

        //和数据库的数据去重;
        HashMap<String,String> map = new HashMap<>(16);
        List<ProPublic> dbList = proPublicService.listAllProPublic();
        for (ProPublic proPublic : dbList) {
            String key = proPublic.getThirdClassify() + proPublic.getTypeNo() + proPublic.getSerialNo();
            map.put(key, "1");
        }
        log.info("=================获取数据库所有数据完毕==================条数:{}",map.size());

        log.info("行数： " + totalRow);
        List<ProPublic> list = new ArrayList<>();
        //第一行是列名; 从第二行开始
        for (int i = 1; i <= totalRow; i++) {
            try {
                row = sheet.getRow(i);
                if (LocalUtils.isEmptyAndNull(row)) {
                    continue;
                }

                //型号
                Cell typeNoCell = row.getCell(0);
                String typeNo = getStringCellValue(typeNoCell);

                if (LocalUtils.isEmptyAndNull(typeNo)) {
                    continue;
                }

                //公价图地址
                Cell smallImgCell = row.getCell(1);

                //公价
                Cell publicPriceCell = row.getCell(2);
                //系列
                Cell serialNoCell = row.getCell(3);
                //机芯类型
                Cell watchCoreTypeCell = row.getCell(4);
                //表壳材质
                Cell watchcaseCell = row.getCell(5);
                //表盘直径
                Cell watchcaseSizeCell = row.getCell(6);
                //材质
                Cell materialCell = row.getCell(7);
                //尺寸
                Cell objectSizeCell = row.getCell(8);
                //尺码
                Cell clothesSizeCell = row.getCell(9);
                //品牌名称
                Cell nameCell = row.getCell(10);
                //分类
                Cell thirdClassifyCell = row.getCell(11);
                BigDecimal initPrice = new BigDecimal(0);
                try {
                    initPrice = LocalUtils.isEmptyAndNull(publicPriceCell) ? initPrice
                            : LocalUtils.calcNumber(getStringCellValue(publicPriceCell), "*", 1);
                } catch (NumberFormatException e) {
                    log.error(e.getMessage(), e);
                }
                Date date = new Date();
                ProPublic pub = new ProPublic();
                pub.setState(1);
                pub.setClassifyCode("");
                pub.setThirdClassify(getStringCellValue(thirdClassifyCell));
                pub.setName(getStringCellValue(nameCell));
                pub.setTypeNo(typeNo);
                pub.setSerialNo(getStringCellValue(serialNoCell));
                pub.setWatchCoreType(getStringCellValue(watchCoreTypeCell));
                pub.setWatchcase(getStringCellValue(watchcaseCell));
                pub.setWatchcaseSize(getStringCellValue(watchcaseSizeCell));
                pub.setMaterial(getStringCellValue(materialCell));
                pub.setObjectSize(getStringCellValue(objectSizeCell));
                pub.setClothesSize(getStringCellValue(clothesSizeCell));
                pub.setPublicPrice(initPrice.intValue());
                pub.setSmallImg(getStringCellValue(smallImgCell));
                pub.setInsertTime(date);
                pub.setUpdateTime(date);
                pub.setInsertAdmin(-1);
                pub.setUpdateAdmin(null);
                pub.setVersions(1);
                pub.setRemark("excel导入");

                String key = pub.getThirdClassify() + pub.getTypeNo() + pub.getSerialNo();
                String s = map.get(key);
                if (LocalUtils.isEmptyAndNull(s)) {
                    list.add(pub);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                break;
            }
        }
        long et = System.currentTimeMillis();
        log.info("============遍历完毕,条数:{},耗时:{}ms====================", list.size(), (et - st));
        //20000行做一次批量插入;
        List<List<ProPublic>> groupList = splitList(list, 20000);

        for (int i = 0; i < groupList.size(); i++) {
            log.info("==========第{}批插入,条数为:{}==========", (i + 1), groupList.size());
            proPublicService.saveBatch(groupList.get(i));
        }
    }

    /**
     * 按一定的数量大小切割list
     *
     * @param list
     * @param groupSize
     * @return
     */
    private List<List<ProPublic>> splitList(List list, int groupSize) {
        int length = list.size();
        // 计算可以分成多少组
        int num = (length + groupSize - 1) / groupSize;
        List<List<ProPublic>> newList = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            // 开始位置
            int fromIndex = i * groupSize;
            // 结束位置
            int toIndex = Math.min((i + 1) * groupSize, length);
            newList.add(list.subList(fromIndex, toIndex));
        }
        return newList;
    }


    //校验数值型单元格
    private String getStringCellValue(Cell cell) throws Exception {
        if (LocalUtils.isEmptyAndNull(cell)) {
            return "";
        }
        cell.setCellType(CellType.STRING);//转为String类型
        return cell.getStringCellValue();
    }

    public static void main(String[] args) {
        String a = "";
        System.out.println(a.substring(0, 0));

    }


}
