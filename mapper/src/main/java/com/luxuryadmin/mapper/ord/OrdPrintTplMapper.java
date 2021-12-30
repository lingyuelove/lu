package com.luxuryadmin.mapper.ord;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.ord.OrdPrintTpl;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 *订单打印模板表 dao
 *@author zhangsai
 *@Date 2021-09-26 16:00:53
 */
@Mapper
public interface OrdPrintTplMapper extends BaseMapper<OrdPrintTpl> {
    /**
     * 订单打印模板
     * @param shopId
     * @return
     */
    OrdPrintTpl getTplByShopId(Integer shopId);
    /**
     * 订单打印模板 系统加本店
     * @param shopId
     * @return
     */
    List<OrdPrintTpl> listTplByShopId(Integer shopId);
}
