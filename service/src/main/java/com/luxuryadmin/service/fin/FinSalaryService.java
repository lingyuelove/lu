package com.luxuryadmin.service.fin;

import com.luxuryadmin.entity.fin.FinSalary;
import com.luxuryadmin.entity.fin.FinSalaryDetail;
import com.luxuryadmin.param.fin.ParamSalaryQuery;
import com.luxuryadmin.vo.fin.VoFinSalary;

import java.util.Date;
import java.util.List;

/**
 * 薪资管理
 *
 * @author monkey king
 * @date 2020-09-23 23:56:29
 */
public interface FinSalaryService {

    /**
     * 获取店铺的工资条
     *
     * @param salaryQuery
     * @return
     */
    List<VoFinSalary> listFinSalaryByDate(ParamSalaryQuery salaryQuery);

    /**
     * 统计薪资总额(分)
     *
     * @param salaryQuery
     * @return
     */
    Long countSalaryMoney(ParamSalaryQuery salaryQuery);


    /**
     * 创建工资条<br/>
     * 工资条和工资明细是一个事务
     *
     * @param finSalary
     * @param finSalaryDetail
     */
    void createSalary(FinSalary finSalary, FinSalaryDetail finSalaryDetail);

    /**
     * 更新薪资
     *
     * @param finSalary
     * @param finSalaryDetail
     */
    void updateSalary(FinSalary finSalary, FinSalaryDetail finSalaryDetail);

    /**
     * 获取该月份已经创建好薪资的userId
     *
     * @param shopId
     * @param startDate
     * @param endDate
     * @return
     */
    List<Integer> listAlreadyCreateSalaryUserId(int shopId, String startDate, String endDate);

    /**
     * 删除薪资;包括薪资明细
     *
     * @param shopId
     * @param startDateTime
     * @param endDateTime
     */
    void deleteFinSalary(int shopId, String startDateTime, String endDateTime);

    /**
     * 刷新薪资
     *
     * @param shopId
     * @param userId
     * @param localUserId
     * @param startDate
     */
    void refreshFinSalary(int shopId, int userId, int localUserId, Date startDate);

    /**
     * 初始化薪资,如果用户id为null;则初始化整个店铺员工的薪资<br/>
     * 只初始化当月
     *
     * @param shopId
     * @param userId
     * @param startDate
     */
    void initFinSalary(int shopId, Integer userId, Date startDate);

    /**
     * 实时更新薪资
     *
     * @param shopId
     * @param startDate
     * @param endDate
     */
    void realTimeFinSalary(int shopId, String startDate, String endDate);

    /**
     * 删除薪资;(注销店铺)<br/>
     * 删除薪资相关的表数据,包括;薪资各种明细;
     *
     * @param shopId
     */
    void deleteFinSalaryForDestroyShop(int shopId);
}
