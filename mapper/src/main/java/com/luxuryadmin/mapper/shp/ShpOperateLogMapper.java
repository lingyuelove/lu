package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.param.shp.ParamShpOperateLogQuery;
import com.luxuryadmin.vo.shp.VoShpOperateLogRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author qwy
 */
@Mapper
public interface ShpOperateLogMapper extends BaseMapper {

    /**
     * 分页查询操作日志
     *
     * @param paramShpOperateLogQuery
     * @return
     */
    List<VoShpOperateLogRecord> selectShpOperateLogList(ParamShpOperateLogQuery paramShpOperateLogQuery);


    /**
     * 删除店铺操作日志
     *
     * @param shopId
     */
    int deleteShpOperateLogByShopId(int shopId);
}