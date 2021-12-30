package com.luxuryadmin.mapper.stat;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.stat.StatTotal;
import com.luxuryadmin.param.stat.ParamRegDataQuery;
import com.luxuryadmin.vo.stat.VoStateDataTotalAll;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface StatTotalMapper extends BaseMapper {

    /**
     * 根据统计日期查询【运营统计数据】-【全盘】
     * @param countDate
     * @return
     */
    StatTotal selectStatTotalByCountDate(Date countDate);

    /**
     * 查询所有日期的汇总数据
     * @return
     */
    VoStateDataTotalAll selectAllCountDate();

    /**
     * 根据条件查询注册数据
     * @param paramRegDataQuery
     * @return
     */
    List<StatTotal> selectStatTotalByRangeDate(ParamRegDataQuery paramRegDataQuery);
}