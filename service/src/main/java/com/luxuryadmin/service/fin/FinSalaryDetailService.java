package com.luxuryadmin.service.fin;

import com.alibaba.fastjson.JSONObject;
import com.luxuryadmin.entity.fin.FinSalaryDetail;
import com.luxuryadmin.param.fin.ParamCreateSalaryQuery;
import com.luxuryadmin.param.fin.ParamSalaryDetailQuery;
import com.luxuryadmin.vo.fin.VoCreateSalaryDetail;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 薪资管理
 *
 * @author monkey king
 * @date 2020-09-23 23:56:29
 */
public interface FinSalaryDetailService {


    /**
     * 获取员工已经生成的薪资明细记录
     *
     * @param shopId
     * @param userId
     * @param startDate
     * @return
     */
    VoCreateSalaryDetail getCreateSalaryDetail(int shopId, int userId, String startDate);

    /**
     * 获取员工已经生成的薪资明细记录
     *
     * @param shopId
     * @param userId
     * @param startDate
     * @return
     */
    FinSalaryDetail getFinSalaryDetail(int shopId, int userId, String startDate);


    /**
     * 获取该员工已经生成的最新一条薪资明细记录
     *
     * @param shopId
     * @param userId
     * @return
     */
    VoCreateSalaryDetail getOwnLastCreateSalaryDetail(int shopId, int userId);

    /**
     * 获取该店铺已经生成的最新一条薪资明细记录
     *
     * @param shopId
     * @return
     */
    VoCreateSalaryDetail getShopLastCreateSalaryDetail(int shopId);

    /**
     * 创建薪资
     *
     * @param param
     * @param result
     * @return
     * @throws Exception
     */
    BigDecimal createSalary(ParamCreateSalaryQuery param, BindingResult result, HttpServletRequest request) throws Exception;

    /**
     * 计算薪资总额
     *
     * @param param
     * @param result
     * @return
     * @throws Exception
     */
    Map<String, Object> calcTotalSalaryMoney(ParamCreateSalaryQuery param, BindingResult result) throws Exception;

    /**
     * 删除薪资明细
     *
     * @param shopId
     * @param userId
     * @param startDate
     */
    void deleteSalaryDetail(int shopId, int userId, String startDate,HttpServletRequest request);

    /**
     * 获取员工的薪资明细表的各种提成统计(页面的上半部分内容),<br/>
     * 该接口, 薪资明细和创建薪资都需要调用
     *
     * @param param
     * @param result
     * @return
     * @throws Exception
     */
    JSONObject getSalaryDetail(ParamSalaryDetailQuery param, BindingResult result) throws Exception;
}
