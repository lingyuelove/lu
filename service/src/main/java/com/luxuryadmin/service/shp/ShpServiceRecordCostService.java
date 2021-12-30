package com.luxuryadmin.service.shp;


import com.luxuryadmin.entity.shp.ShpServiceRecordCost;
import com.luxuryadmin.param.shp.ParamServiceRecordCostAddOrUp;
import com.luxuryadmin.param.shp.ParamShpServiceRecordAdd;
import com.luxuryadmin.vo.shp.VoServiceRecordCost;

import java.util.List;

/**
 * 服务成本表 service
 *
 * @author zhangsai
 * @Date 2021-10-19 14:48:46
 */
public interface ShpServiceRecordCostService {
    /**
     * 新增/编辑服务成本
     * @param serviceRecordAdd
     */
    void addOrUpdate(ParamShpServiceRecordAdd serviceRecordAdd);
    /**
     * 根据店铺服务记录ID获取此次记录的每个详细的服务成本
     * @param serviceRecordId
     * @return
     */
    List<VoServiceRecordCost> listCostForServiceRecordId(Integer serviceRecordId);
}
