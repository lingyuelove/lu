package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.vo.shp.VoShpShareType;
import org.apache.ibatis.annotations.Mapper;


/**
 *商铺添加时长类型表 dao
 *@author Mong
 *@Date 2021-05-31 17:27:11
 */
@Mapper
public interface ShpShareTypeMapper extends BaseMapper {


    VoShpShareType getShareTypeByCode(String code);
}
