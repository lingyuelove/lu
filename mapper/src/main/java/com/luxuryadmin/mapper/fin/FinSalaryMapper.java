package com.luxuryadmin.mapper.fin;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.param.fin.ParamSalaryQuery;
import com.luxuryadmin.vo.fin.VoFinSalary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 */
@Mapper
public interface FinSalaryMapper extends BaseMapper {

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
     * 获取该月份已经创建好薪资的userId
     *
     * @param shopId
     * @param startDate
     * @param endDate
     * @return
     */
    List<Integer> listAlreadyCreateSalaryUserId(
            @Param("shopId") int shopId, @Param("startDate") String startDate,
            @Param("endDate") String endDate);

    /**
     * 根据条件获取薪资ID
     *
     * @param shopId
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    List<Integer> listFinSalaryId(@Param("shopId") int shopId,
                                  @Param("startDateTime") String startDateTime,
                                  @Param("endDateTime") String endDateTime);


    /**
     * 物理删除薪资; 用in查询
     *
     * @param finSalaryIds
     * @return
     */
    int deleteFinSalary(String finSalaryIds);

    /**
     * 删除整个店铺的薪资
     *
     * @param shopId
     * @return
     */
    int deleteFinSalaryByShopId(int shopId);
}