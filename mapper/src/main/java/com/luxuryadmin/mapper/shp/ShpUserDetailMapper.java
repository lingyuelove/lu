package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.shp.ShpUserDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShpUserDetailMapper  extends BaseMapper {

    /**
     * 根据用户id查询用户身份信息
     * @param id
     * @return
     */
    ShpUserDetail getByUserId(String id);
}