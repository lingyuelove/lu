package com.luxuryadmin.mapper;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.MpAddVipTime;
import com.luxuryadmin.vo.award.VOAwardRecordList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 追加时长表 dao
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:00
 */
@Mapper
public interface MpAddVipTimeMapper extends BaseMapper<MpAddVipTime> {

    /**
     * 查询用户奖励记录
     *
     * @param userId
     * @return
     */
    List<VOAwardRecordList> listAddVipTime(@Param("userId") Integer userId);

    /**
     * 查询用户总奖励天数
     *
     * @param userId
     * @return
     */
    Integer getTotalAddDay(@Param("userId") Integer userId);
}
