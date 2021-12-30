package com.luxuryadmin.mapper.sys;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.sys.SysSalt;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysSaltMapper extends BaseMapper {

    /**
     * 根据用户id和类型; 更新盐值
     * @param sysSalt
     * @return
     */
    SysSalt getSysSaltByUserIdAndType(SysSalt sysSalt);

}