package com.luxuryadmin.mapper.op;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.op.OpNotice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Administrator
 */
@Mapper
public interface OpNoticeMapper extends BaseMapper {

    /**
     * 加载系统消息;面对所有用户
     *
     * @return
     */
    List<OpNotice> listOpVoNotice();

    /**
     * 加载店铺消息
     *
     * @param shopId
     * @return
     */
    List<OpNotice> listOpVoNoticeByShopId(int shopId);

    /**
     * 根据id获取OpNotice
     *
     * @param id
     * @return
     */
    OpNotice getNoticeById(String id);
}