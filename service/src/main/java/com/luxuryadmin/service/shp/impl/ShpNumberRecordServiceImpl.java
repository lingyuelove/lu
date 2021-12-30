package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.entity.shp.ShpNumberRecord;
import com.luxuryadmin.enums.shp.EnumNumberRecordType;
import com.luxuryadmin.mapper.shp.ShpNumberRecordMapper;
import com.luxuryadmin.service.shp.ShpNumberRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author monkey king
 * @date 2019-12-19 16:49:09
 */
@Slf4j
@Service
public class ShpNumberRecordServiceImpl implements ShpNumberRecordService {

    @Resource
    private ShpNumberRecordMapper shpNumberRecordMapper;

    @Override
    public ShpNumberRecord getLastGenerateRecord(EnumNumberRecordType numberTypeEnum) {
        return (ShpNumberRecord)shpNumberRecordMapper.getLastRecordByType(numberTypeEnum.getCode());
    }

    @Override
    public int SaveShpNumberRecord(ShpNumberRecord shpNumberRecord) {
        return shpNumberRecordMapper.saveObject(shpNumberRecord);
    }
}
