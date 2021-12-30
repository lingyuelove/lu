package com.luxuryadmin.service.org;

import com.luxuryadmin.entity.org.OrgTempSeat;
import com.luxuryadmin.param.org.ParamTempSeatAddOrUpdate;
import com.luxuryadmin.vo.org.VoTempSeatList;

import java.util.List;

/**
 * 店铺机构仓排序位置分组表
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
public interface OrgTempSeatService {

    /**
     * 机构临时仓排序位置新增/修改
     * @param paramTempSeatAddOrUpdate
     */
    Integer addTempSeat(ParamTempSeatAddOrUpdate paramTempSeatAddOrUpdate);

    Integer deleteTempSeat(Integer id);

    /**
     * 机构临时仓排序位置集合显示
     * @param shopId
     * @return
     */
    List<VoTempSeatList> getShopTempSeat(Integer shopId);

    OrgTempSeat getByName(String name,Integer shopId);
}