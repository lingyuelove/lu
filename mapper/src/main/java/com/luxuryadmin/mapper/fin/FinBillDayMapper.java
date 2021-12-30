package com.luxuryadmin.mapper.fin;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.fin.FinBill;
import com.luxuryadmin.vo.fin.VoBillDayPageByApp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 帐单日统计表
 *
 * @author zhangSai
 * @date   2021/04/23 14:19:52
 */
@Mapper
public interface FinBillDayMapper extends BaseMapper {

    /**
     * 账单每日统计集合显示
     * @param shopId
     * @param startTime
     * @param endTime
     * @return
     */
    List<VoBillDayPageByApp> getBillDayPageByApps(@Param("shopId")Integer shopId,@Param("billId")Integer billId,@Param("startTime") String startTime, @Param("endTime")String endTime);

    /**
     * 账单每日新增定时任务
     * @return
     */
    List<FinBill> getBillList(@Param("shopId")Integer shopId);

}