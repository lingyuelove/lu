package com.luxuryadmin.mapper.op;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.op.OpPlatform;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OpPlatformMapper  extends BaseMapper {

    //查询所有平台ID
    List<OpPlatform> selectAllOpPlatform();
}