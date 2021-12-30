package com.luxuryadmin.mapper.shp;
import com.luxuryadmin.common.base.BaseMapper;

import com.luxuryadmin.vo.shp.VoServiceRecordCost;
import org.apache.ibatis.annotations.Mapper;
import com.luxuryadmin.entity.shp.ShpServiceRecordCost;

import java.util.List;


/**
 *服务成本表 dao
 *@author zhangsai
 *@Date 2021-10-19 14:48:46
 */
@Mapper
public interface ShpServiceRecordCostMapper  extends BaseMapper<ShpServiceRecordCost> {

    /**
     * 根据店铺服务记录ID获取此次记录的每个详细的服务成本
     * @param serviceRecordId
     * @return
     */
    List<VoServiceRecordCost> listCostForServiceRecordId(Integer serviceRecordId);

    void deleteByServiceRecordId(Integer serviceRecordId);
}
