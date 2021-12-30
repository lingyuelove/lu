package com.luxuryadmin.apiadmin.login;

import com.alibaba.fastjson.JSONObject;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.aliyun.CellphoneRegisterLocationUtil;
import com.luxuryadmin.entity.op.DaEFenQiKa;
import com.luxuryadmin.mapper.op.DaEFenQiKaMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class ImportExcelController {

    @Resource
    private DaEFenQiKaMapper daEFenQiKaMapper;

    @RequestMapping("/importExcel")
    public BaseResult importExcel() {
        long st = System.currentTimeMillis();
        ArrayList<DaEFenQiKa> daEFenQiKaList = readExcelDaEFenQi();
       /* if(!LocalUtils.isEmptyAndNull(daEFenQiKaList)){
            daEFenQiKaList.forEach(daEFenQiKa -> daEFenQiKaMapper.saveObject(daEFenQiKa));
        }*/
        System.out.println("===============添加数据入库==============");
        daEFenQiKaMapper.saveBatch(daEFenQiKaList);
        System.out.println("===============添加数据入库===结束===========");
        long et = System.currentTimeMillis();
        System.out.println("耗时: " + (et - st));
        return BaseResult.okResult();
    }


    public ArrayList<DaEFenQiKa> readExcelDaEFenQi() {
        Workbook wb;
        Sheet sheet;

        ArrayList<DaEFenQiKa> daEFenQiKaList = new ArrayList<>();
        try {

            System.out.println("==========加载excel文件===========");
            String filePath = "E:\\daefenqi.xlsx";
            InputStream is = new FileInputStream(filePath);
            wb = new XSSFWorkbook(is);
            // sheet = wb.getSheetAt(0);
            foreachCell(wb.getSheetAt(0), daEFenQiKaList);
            foreachCell(wb.getSheetAt(1), daEFenQiKaList);

            System.out.println("================================");
            System.out.println("================================");


            System.out.println("===========遍历结束=================");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return daEFenQiKaList;
    }

    public ArrayList<DaEFenQiKa> foreachCell(Sheet sheet, ArrayList<DaEFenQiKa> daEFenQiKaList) {
        Row row;
        int totalRow = sheet.getLastRowNum();
        System.out.println("==========加载excel文件完成,开始遍历===========");
        System.out.println("行数： " + totalRow);
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
            Cell 推广人编号 = row.getCell(11);
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

            DaEFenQiKa daEFenQiKa = new DaEFenQiKa();
            //daEFenQiKa.setId(i-3);
            daEFenQiKa.set机构(机构.toString());
            daEFenQiKa.set机构名称(机构名称.toString());
            daEFenQiKa.set卡号(卡号.toString());
            daEFenQiKa.set姓名(姓名.toString());
            daEFenQiKa.set卡种类(卡种类.toString());
            daEFenQiKa.set证件号码(证件号码.toString());
            daEFenQiKa.set开卡日期(开卡日.toString());
            daEFenQiKa.set逾期类型(逾期类型.toString());
            daEFenQiKa.set账单日(账单日.toString());
            daEFenQiKa.set推广人编号(推广人编号.toString());
            daEFenQiKa.set推广人姓名(推广人姓名.toString());
            daEFenQiKa.set联系电话(联系电话.toString());
            daEFenQiKa.set授信额度(Double.parseDouble(授信额度.toString()));
            daEFenQiKa.set透支余额(Double.parseDouble(透支余额.toString()));
            daEFenQiKa.set透支本金(Double.parseDouble(透支本金.toString()));
            daEFenQiKa.set利息余额(Double.parseDouble(利息余额.toString()));
            daEFenQiKa.set费用余额(Double.parseDouble(费用余额.toString()));
            daEFenQiKa.set分期付款原始本金(Double.parseDouble(分期付款原始本金.toString()));
            daEFenQiKa.set分期付款待摊本金(Double.parseDouble(分期付款待摊本金.toString()));
            daEFenQiKa.set分期付款原始费用(Double.parseDouble(分期付款原始费用.toString()));
            daEFenQiKa.set分期付款待摊费用(Double.parseDouble(分期付款待摊费用.toString()));
            daEFenQiKa.set未还款额(Double.parseDouble(未还款额.toString()));
            daEFenQiKa.set专案代码(专案代码.toString());
            daEFenQiKa.set分期总额(分期总额.toString());
            daEFenQiKa.set年日均(年日均.toString());
            daEFenQiKa.setInsertTime(new Date());
            daEFenQiKa.setUpdateAdmin(0);
            daEFenQiKaList.add(daEFenQiKa);
        }
        return daEFenQiKaList;
    }

    @RequestMapping("/getLocation")
    public BaseResult getLocation() {
        //getCellphoneLocation();
        //getCellphoneLocationTest();
        return BaseResult.okResult();
    }

    public void getCellphoneLocation() {
        log.info("=========更新开始=========");
        int pageSize = 10000;//1万条/页
        int offset = 0;
        //9页
        int j = 1;
        for (int i = 1; i <= 9; i++) {
            offset = (i - 1) * pageSize;
            List<DaEFenQiKa> list = daEFenQiKaMapper.getCellphoneLocation(offset, pageSize);
            //updateCellphoneLocation(list);
            List<List<DaEFenQiKa>> cellphoneList = createList(list, 20);
            for (List<DaEFenQiKa> daEFenQiKaList : cellphoneList) {
                j++;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        updateCellphoneLocation(daEFenQiKaList);
                    }
                }).start();
            }
        }
        System.out.println("======总共启动  " + j + "  个线程===");
    }

    /**
     * 获取手机归属地
     *
     * @param list
     */
    public void updateCellphoneLocation(List<DaEFenQiKa> list) {
        if (!LocalUtils.isEmptyAndNull(list)) {

            for (DaEFenQiKa daEFenQiKa : list) {
                String phone = "";
                String json = "";
                try {
                    phone = daEFenQiKa.get联系电话().trim();
                    boolean isPhone = LocalUtils.isPhoneNumber(phone);
                    if (isPhone) {
                        json = CellphoneRegisterLocationUtil.getCellPhoneRegisterLocation(phone);
                        JSONObject jsonObject = JSONObject.parseObject(json);
                        String province = jsonObject.get("province").toString();
                        String city = jsonObject.get("city").toString();
                        String company = jsonObject.get("company").toString();
                        daEFenQiKa.set联系电话(phone);
                        daEFenQiKa.set省份(province);
                        daEFenQiKa.set城市(city);
                        daEFenQiKa.set运营商(company);
                    } else {
                        daEFenQiKa.setRemark("手机格式错误false");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("updateCellphoneLocation: 手机号: " + phone + " " + e.getMessage(), e + "; 返回结果: " + json);
                    daEFenQiKa.setRemark("手机格式错误");
                }
            }

            daEFenQiKaMapper.updateDaEFenQiKaList(list);
        }
        log.info("=========更新完成=========");
    }

    public void getCellphoneLocationTest() {

        List<DaEFenQiKa> list = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            DaEFenQiKa daEFenQiKa = new DaEFenQiKa();
            daEFenQiKa.setId(i);
            daEFenQiKa.set省份("省份" + i);
            daEFenQiKa.set城市("城市" + i);
            daEFenQiKa.set运营商("运营商" + i);
            list.add(daEFenQiKa);
        }
        daEFenQiKaMapper.updateDaEFenQiKaList(list);
    }


    /**
     * @param targe 要拆分的集合
     * @param size  拆分数量; 多少条为一个集合;
     * @return
     */
    public static List<List<DaEFenQiKa>> createList(List<DaEFenQiKa> targe, int size) {
        List<List<DaEFenQiKa>> listArr = new ArrayList<List<DaEFenQiKa>>();
        //获取被拆分的数组个数
        int arrSize = targe.size() % size == 0 ? targe.size() / size : targe.size() / size + 1;
        for (int i = 0; i < arrSize; i++) {
            List<DaEFenQiKa> sub = new ArrayList<>();
            //把指定索引数据放入到list中
            for (int j = i * size; j <= size * (i + 1) - 1; j++) {
                if (j <= targe.size() - 1) {
                    sub.add((DaEFenQiKa) targe.get(j));
                }
            }
            listArr.add(sub);
        }
        return listArr;
    }
}
