package com.luxuryadmin.mapper.biz;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.biz.BizLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author monkey king
 * @date 2020-01-12 18:01:03
 */
@Mapper
public interface BizLogMapper extends BaseMapper {

    /**
     * 获取商务模块的操作日志
     *
     * @param shopId
     * @return
     */
    List<BizLog> listBizLogByShopId(int shopId);

    /**
     * 删除已删除店铺的友商日志
     * @param shopId
     */
    void deleteBizLogByShop(int shopId);
}
