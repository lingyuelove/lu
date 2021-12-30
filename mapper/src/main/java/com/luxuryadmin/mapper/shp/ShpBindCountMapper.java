package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.shp.ShpBindCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author monkey king
 * @date 2020-08-07 18:15:48
 */
@Mapper
public interface ShpBindCountMapper extends BaseMapper {


    /**
     * 是否存在已绑定的用户
     *
     * @param type
     * @param username
     * @return
     */
    Integer existsBindCount(@Param("type") String type, @Param("username") String username);

    /**
     * 获取实体
     *
     * @param type   类型; wx:微信
     * @param openId 对应类型的唯一标识符
     * @return
     */
    ShpBindCount getBindCountByOpenId(
            @Param("type") String type, @Param("openId") String openId);

    /**
     * 获取实体
     *
     * @param type     类型; wx:微信
     * @param username 绑定的手机号
     * @return
     */
    ShpBindCount getBindCountByUsername(
            @Param("type") String type, @Param("username") String username);
}
