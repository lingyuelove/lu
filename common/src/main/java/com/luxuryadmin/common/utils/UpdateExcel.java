package com.luxuryadmin.common.utils;

import com.luxuryadmin.common.encrypt.DESEncrypt;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**读取excel表格内容
 * @author monkey king
 */
public class UpdateExcel {
    private POIFSFileSystem fs;
    private Workbook wb;
    private Sheet sheet;
    private Row row;

    /**
     * 读取Excel内容：用户投资行为流水表
     */
    public void readExcelUserInvest() {
        try {
            String filePath = "C:\\Users\\Administrator\\Desktop\\火钱理财\\export_sql_1926213_用户投资行为流水表\\sqlResult_1926213.xlsx";
            InputStream is = new FileInputStream(filePath);
            wb = new XSSFWorkbook(is);
            sheet = wb.getSheetAt(0);
            int totalRow = sheet.getLastRowNum();
            System.out.println("行数： " + totalRow);
            for (int i = 1; i <= totalRow; i++) {
                row = sheet.getRow(i);
                Cell cell1 = row.getCell(1);
                Cell cell3 = row.getCell(3);
                String phone = getCellFormatValue(cell1);
                String idCard = getCellFormatValue(cell3);
                String jmPhone = DESEncrypt.decodeUsername(phone);
                String jmIdCard = DESEncrypt.decodeUsername(idCard);

                if (cell1 != null) {
                    cell1.setCellValue(jmPhone);
                }
                if (cell3 != null) {
                    cell3.setCellValue(jmIdCard);
                }
                System.out.println(i + "\t" + jmPhone + "\t" + jmIdCard);
            }
            FileOutputStream outputStream = new FileOutputStream(filePath);
            wb.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取Excel内容：标的信息表
     */
    public void readExcelProduct() {
        try {
            String filePath = "C:\\Users\\Administrator\\Desktop\\火钱理财\\export_sql_1926217_标的信息表\\sqlResult_1926217.xlsx";
            InputStream is = new FileInputStream(filePath);
            wb = new XSSFWorkbook(is);
            sheet = wb.getSheetAt(0);
            int totalRow = sheet.getLastRowNum();
            System.out.println("行数： " + totalRow);
            for (int i = 1; i <= totalRow; i++) {
                row = sheet.getRow(i);
                Cell cell6 = row.getCell(6);
                Cell cell7 = row.getCell(7);
                String phone = getCellFormatValue(cell6);
                String idCard = getCellFormatValue(cell7);
                String jmPhone = DESEncrypt.decodeUsername(phone);
                String jmIdCard = DESEncrypt.decodeUsername(idCard);

                if (cell6 != null) {
                    cell6.setCellValue(jmPhone);
                }
                if (cell7 != null) {
                    cell7.setCellValue(jmIdCard);
                }
                System.out.println(i + "\t" + jmPhone + "\t" + jmIdCard);
            }
            FileOutputStream outputStream = new FileOutputStream(filePath);
            wb.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取Excel内容：用未还款用户
     */
    public void readExcelNoReturnMoney() {
        try {
            String filePath = "C:\\Users\\Administrator\\Desktop\\火钱理财\\export_sql_1926224_未还款用户情况\\sqlResult_1926224.xlsx";
            InputStream is = new FileInputStream(filePath);
            wb = new XSSFWorkbook(is);
            sheet = wb.getSheetAt(0);
            int totalRow = sheet.getLastRowNum();
            System.out.println("行数： " + totalRow);
            for (int i = 1; i <= totalRow; i++) {
                row = sheet.getRow(i);
                Cell cell1 = row.getCell(1);
                Cell cell3 = row.getCell(3);
                String phone = getCellFormatValue(cell1);
                String idCard = getCellFormatValue(cell3);
                String jmPhone = DESEncrypt.decodeUsername(phone);
                String jmIdCard = DESEncrypt.decodeUsername(idCard);
                if (cell1 != null) {
                    cell1.setCellValue(jmPhone);
                }
                if (cell3 != null) {
                    cell3.setCellValue(jmIdCard);
                }
                System.out.println(i + "\t" + jmPhone + "\t" + jmIdCard);
            }
            FileOutputStream outputStream = new FileOutputStream(filePath);
            wb.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


	public void readExcelDaEFenQi() {
		try {
            System.out.println("==========加载excel文件===========");
			String filePath = "E:\\daefenqi.xlsx";
			InputStream is = new FileInputStream(filePath);
			wb = new XSSFWorkbook(is);
			sheet = wb.getSheetAt(0);
			int totalRow = sheet.getLastRowNum();
			System.out.println("行数： " + totalRow);
            System.out.println("==========加载excel文件完成,开始遍历===========");
            System.out.println("================================");
            System.out.println("================================");
			for (int i = 4; i <= totalRow; i++) {
				row = sheet.getRow(i);
				Cell 机构 = row.getCell(0);
                Cell 机构名称 = row.getCell(1);
                Cell 卡号 = row.getCell(2);
                Cell 姓名 = row.getCell(3);
				Cell 卡种类 = row.getCell(4);
                Cell 证件号码 = row.getCell(5);
                Cell 开卡日 = row.getCell(6);
                Cell 销卡日 = row.getCell(7);
                Cell 卡状态 = row.getCell(8);
                Cell 逾期类型 = row.getCell(9);
                Cell 账单日 = row.getCell(10);
                Cell 推广人员 = row.getCell(11);
                Cell 推广人姓名 = row.getCell(12);
                Cell 联系电话 = row.getCell(13);
                Cell 授信额度 = row.getCell(14);
                Cell 透支余额 = row.getCell(15);
                Cell 透支本金 = row.getCell(16);
                Cell 利息余额 = row.getCell(17);
                Cell 费用余额 = row.getCell(18);
                Cell 分期付款原始本金 = row.getCell(19);
                Cell 分期付款待摊本金 = row.getCell(20);
                Cell 分期付款原始费用 = row.getCell(2);
                Cell 分期付款待摊费用 = row.getCell(22);
                Cell 未还款额 = row.getCell(23);
                Cell 专案代码 = row.getCell(24);
                Cell 分期总额 = row.getCell(25);
                Cell 年日均 = row.getCell(26);
                System.out.println(姓名.toString());
                if(i == 2000){
                    break;
                }
			}
			//FileOutputStream outputStream = new FileOutputStream(filePath);
			//wb.write(outputStream);
            System.out.println("===========遍历结束=================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



    /**
     * 根据HSSFCell类型设置数据
     *
     * @param cell
     * @return
     */
    public String getCellFormatValue(Cell cell) {
        try {
            String cellvalue = "";
            if (cell != null) {
                // 判断当前Cell的Type
                switch (cell.getCellType()) {
                    // 如果当前Cell的Type为NUMERIC
                    case HSSFCell.CELL_TYPE_NUMERIC:
                    case HSSFCell.CELL_TYPE_FORMULA: {
                        // 判断当前的cell是否为Date
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            // 如果是Date类型则，转化为Data格式
                            // 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                            // cellvalue = cell.getDateCellValue().toLocaleString();

                            // 方法2：这样子的data格式是不带带时分秒的：2011-10-12
                            Date date = cell.getDateCellValue();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            cellvalue = sdf.format(date);

                        } else {
                            // 如果是纯数字
                            // 这里直接将数据改成 string类型；防止科学计数法；如：将手机号码转为科学计数法的数据；
                            // cellvalue = String.valueOf(cell.getNumericCellValue());
                            cell.setCellType(Cell.CELL_TYPE_STRING);// 转为string类型并赋值；
                            cellvalue = cell.getStringCellValue();
                        }
                        break;
                    }
                    // 如果当前Cell的Type为STRIN
                    case HSSFCell.CELL_TYPE_STRING:
                        // 取得当前的Cell字符串
                        cellvalue = cell.getRichStringCellValue().getString();
                        break;
                    // 默认的Cell值
                    default:
                        cellvalue = " ";
                }
            } else {
                cellvalue = "";
            }
            return cellvalue;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        long st = System.currentTimeMillis();
        UpdateExcel update = new UpdateExcel();
        update.readExcelDaEFenQi();
        long et = System.currentTimeMillis();
        System.out.println(et-st);
    }

}