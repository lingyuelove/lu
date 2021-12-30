package com.luxuryadmin.service.fin;

import com.alibaba.fastjson.JSONObject;
import com.luxuryadmin.entity.fin.FinSalaryDetail;
import com.luxuryadmin.param.fin.*;
import com.luxuryadmin.param.fin.ParamBillSearch;
import com.luxuryadmin.vo.fin.*;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 帐单表
 *
 * @author zhangSai
 * @date 2021/04/23 14:19:52
 */
public interface FinBillService {
    /**
     * 新增账单
     * @param billCreate
     * @return
     */
    Integer addBill(ParamBillCreate billCreate);

    /**
     * 修改对账单
     * @param billUpdate
     * @return
     */
    Integer updateBill(ParamBillUpdate billUpdate);

    /**
     * 删除对账单
     * @param billUpdate
     * @return
     */
    Integer deleteBill( ParamBillUpdate billUpdate);

    /**
     * 对账单集合显示
     * @param billSearch
     * @return
     */
    VoBillByApp getBillPageByApp(ParamBillSearch billSearch);

    /**
     * 对账单详情
     * @param billDaySearch
     * @return
     */
    VoBillDetailByApp getBillDetailByApp(ParamBillDaySearch billDaySearch);

    /**
     * 获取各个商品的商品总价
     * @param shopId
     * @param userId
     * @return
     */
    VoBillProductByApp getBillProductMoneyByApp(Integer shopId,Integer userId);

    /**
     * 获取薪资
     * @param types
     * @param shopId
     * @return
     */
    BigDecimal getProductMoneyForTypeByApp(String types, Integer shopId);
}
