package com.luxuryadmin.service.org.impl;

import com.luxuryadmin.entity.org.OrgTempSeat;
import com.luxuryadmin.mapper.org.OrgTempSeatMapper;
import com.luxuryadmin.param.org.ParamTempSeatAddOrUpdate;
import com.luxuryadmin.service.org.OrgTempSeatService;
import com.luxuryadmin.vo.org.VoTempSeatList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 店铺机构仓排序位置分组表
 *
 * @author zhangSai
 * @date 2021/04/21 13:57:59
 */
@Slf4j
@Service
public class OrgTempSeatServiceImpl implements OrgTempSeatService {

    @Resource
    private OrgTempSeatMapper tempSeatMapper;

    @Override
    public Integer addTempSeat(ParamTempSeatAddOrUpdate paramTempSeatAddOrUpdate) {
        OrgTempSeat tempSeat = new OrgTempSeat();
        tempSeat.setInsertTime(new Date());
        tempSeat.setInsertAdmin(paramTempSeatAddOrUpdate.getInsertAdmin());
        tempSeat.setFkShpShopId(paramTempSeatAddOrUpdate.getShopId());
        tempSeat.setName(paramTempSeatAddOrUpdate.getName());
        Integer result =tempSeatMapper.saveObject(tempSeat);
        return tempSeat.getId();
    }

    @Override
    public Integer deleteTempSeat(Integer id) {
        return tempSeatMapper.deleteObject(id);
    }

    @Override
    public List<VoTempSeatList> getShopTempSeat(Integer shopId) {

        return tempSeatMapper.getShopTempSeat(shopId);
    }

    @Override
    public OrgTempSeat getByName(String name,Integer shopId) {
        return tempSeatMapper.getTempSeatByName(name,shopId);
    }
}