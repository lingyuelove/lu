package com.luxuryadmin.service.op.impl;

import com.luxuryadmin.entity.op.OpNotice;
import com.luxuryadmin.mapper.op.OpNoticeMapper;
import com.luxuryadmin.service.op.OpNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author monkey king
 * @date 2020-01-16 20:49:06
 */
@Slf4j
@Service
public class OpNoticeServiceImpl implements OpNoticeService {

    @Resource
    private OpNoticeMapper opNoticeMapper;


    @Override
    public List<OpNotice> listOpNotice() {
        return opNoticeMapper.listOpVoNotice();
    }

    @Override
    public List<OpNotice> listOpNoticeByShopId(int shopId) {
        return opNoticeMapper.listOpVoNoticeByShopId(shopId);
    }

    @Override
    public OpNotice getNoticeById(String id) {
        return opNoticeMapper.getNoticeById(id);
    }
}
