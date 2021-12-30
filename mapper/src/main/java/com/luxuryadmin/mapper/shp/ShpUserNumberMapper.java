package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.shp.ShpUserNumber;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author monkey king
 */
@Mapper
public interface ShpUserNumberMapper extends BaseMapper {

    /**
     * 获取最后一个编号;
     *
     * @return
     */
    Integer getLastNumber();

    /**
     * 获取比当前id大的ShpUserNumber
     * @param id
     * @return
     */
    ShpUserNumber getShpUserNumberOverId(int id);
}