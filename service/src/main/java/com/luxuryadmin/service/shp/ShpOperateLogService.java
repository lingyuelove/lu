package com.luxuryadmin.service.shp;

import com.luxuryadmin.param.shp.ParamAddShpOperateLog;
import com.luxuryadmin.param.shp.ParamShpOperateLogQuery;
import com.luxuryadmin.vo.shp.VoInitShopOperateLog;
import com.luxuryadmin.vo.shp.VoShpOperateLogRecord;

import java.util.List;

/**
 * 操作日志Service
 * @author dingxin
 */
public interface ShpOperateLogService {

    /**
     * 根据店铺ID获取初始化查询参数
     *
     * @param shopId
     * @return
     */
    VoInitShopOperateLog getInitOperateLogQueryData(Integer shopId);

    /**
     * 保存店铺操作日志
     *
     * @param paramAddShpOperateLog
     * @return
     */
    Integer saveShpOperateLog(ParamAddShpOperateLog paramAddShpOperateLog);

    /**
     * 分页查询操作日志
     *
     * @param paramShpOperateLogQuery
     * @return
     */
    List<VoShpOperateLogRecord> listShpOperateLog(ParamShpOperateLogQuery paramShpOperateLogQuery);

    /**
     * 删除店铺所有操作日志(用于操作店铺注销)
     *
     * @param shopId
     * @return
     */
    int deleteShpOperateLogByShopId(int shopId);
}
