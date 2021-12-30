package com.luxuryadmin.mapper.org;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.org.OrgTempSeat;
import com.luxuryadmin.vo.org.VoTempSeatList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 店铺机构仓排序位置分组表
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Mapper
public interface OrgTempSeatMapper  extends BaseMapper {

    /**
     * 根据店铺id删除排序
     * @param shopId
     */
    void deleteByShopId(@Param("shopId")Integer shopId);

    /**
     * 店铺机构仓排序位置分组集合显示
     * @param shopId
     * @return
     */
    List<VoTempSeatList> getShopTempSeat(@Param("shopId")Integer shopId);

    /**
     * 店铺机构仓排序位置详情
     * @param name
     * @return
     */
    OrgTempSeat getTempSeatByName(@Param("name")String name,@Param("shopId")Integer shopId);
}