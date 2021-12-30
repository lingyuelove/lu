package com.luxuryadmin.mapper.op;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.op.OpChannel;
import com.luxuryadmin.vo.op.VoOpChannel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OpChannelMapper extends BaseMapper {
    OpChannel selectByChannelCode(String channelCode);

    /**
     * 查询所有渠道
     *
     * @return
     */
    List<VoOpChannel> selectAllOpChannel();

    /**
     * 根据outsideCode来获取渠道
     *
     * @param outsideCode
     * @return
     */
    OpChannel getOpChannelByOutsideCode(String outsideCode);
}