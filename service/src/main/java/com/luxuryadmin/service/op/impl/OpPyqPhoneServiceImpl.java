package com.luxuryadmin.service.op.impl;

import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.aliyun.OSSUtil;
import com.luxuryadmin.entity.op.OpPyqPhone;
import com.luxuryadmin.entity.pro.ProDetail;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.mapper.op.OpPyqPhoneMapper;
import com.luxuryadmin.service.op.OpPyqPhoneService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author monkey king
 * @date 2021-07-21 2:32:26
 */
@Service
@Slf4j
public class OpPyqPhoneServiceImpl implements OpPyqPhoneService {

    @Resource
    private OpPyqPhoneMapper opPyqPhoneMapper;


    @Override
    public void saveBatch(List<OpPyqPhone> list) {
        opPyqPhoneMapper.saveBatch(list);
    }

    @Override
    public List<OpPyqPhone> readExcel(String excelUrl, int userId, String remark, String batch) {
        Workbook wb;
        List<OpPyqPhone> list = new ArrayList<>();
        try {
            log.info("==========加载excel文件===========");
            wb = new XSSFWorkbook(OSSUtil.downloadInputStream(excelUrl));
            list.addAll(foreachCell(wb.getSheetAt(0), userId, remark, batch));
            log.info("================================");
            wb.close();
            log.info("===========遍历结束=================");
            if (!LocalUtils.isEmptyAndNull(list)) {
                opPyqPhoneMapper.saveBatch(list);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    private List<OpPyqPhone> foreachCell(Sheet sheet, int userId, String remark, String batch) {
        Row row;
        int totalRow = sheet.getLastRowNum();
        log.info("==========加载excel文件完成,开始遍历===========");
        List<OpPyqPhone> list = new ArrayList<>();
        log.info("行数： " + totalRow);
        //用户填的数据从第8行开始
        Date date = new Date();
        for (int i = 1; i <= totalRow; i++) {
            try {
                row = sheet.getRow(i);
                if (LocalUtils.isEmptyAndNull(row)) {
                    continue;
                }
                //店铺名称
                Cell shopNameCell = row.getCell(0);
                Cell phoneCell = row.getCell(1);
                Cell addressCell = row.getCell(2);
                Cell gpsCell = row.getCell(3);
                Cell industryCell = row.getCell(4);
                String phone = getStringCellValue(phoneCell);
                if (LocalUtils.isEmptyAndNull(phone)) {
                    continue;
                }
                phone = phone.replaceAll("；", ";");
                phone = phone.replaceAll("/", ";");
                String[] split = phone.split(";");
                phone = "";
                for (String s : split) {
                    if (LocalUtils.isPhoneNumber(s)) {
                        phone = s;
                        break;
                    }
                }
                if (LocalUtils.isEmptyAndNull(phone)) {
                    phone = split[0];
                    if (phone.length() > 20) {
                        phone = phone.substring(0, 11);
                    }
                }
                String type = LocalUtils.isPhoneNumber(phone) ? "1" : "3";
                String shopName = getStringCellValue(shopNameCell);
                String address = getStringCellValue(addressCell);
                String gps = getStringCellValue(gpsCell);
                String industry = getStringCellValue(industryCell);
                OpPyqPhone opPyqPhone = new OpPyqPhone();
                opPyqPhone.setPhone(phone);
                opPyqPhone.setBatch(batch);
                opPyqPhone.setType(type);
                opPyqPhone.setProvince("");
                opPyqPhone.setCity("");
                opPyqPhone.setAddress(address);
                opPyqPhone.setShopName(shopName);
                opPyqPhone.setGps(gps);
                opPyqPhone.setIndustry(industry);
                opPyqPhone.setIsUser("0");
                opPyqPhone.setInsertTime(date);
                opPyqPhone.setInsertAdmin(userId);
                opPyqPhone.setRemark(remark);
                list.add(opPyqPhone);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return list;
    }


    /**
     * 校验数值型单元格
     *
     * @param cell
     * @return
     * @throws Exception
     */
    private String getStringCellValue(Cell cell) throws Exception {
        //转为String类型
        if (LocalUtils.isEmptyAndNull(cell)) {
            return "";
        }
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }
}
