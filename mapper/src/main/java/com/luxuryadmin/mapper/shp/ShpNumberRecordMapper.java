package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.shp.ShpNumberRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author monkey king
 */
@Mapper
public interface ShpNumberRecordMapper extends BaseMapper {

    /**
     * 根据类型获取最后一条生成的记录;
     * @param type
     * @return
     */
    ShpNumberRecord getLastRecordByType(String type);
}