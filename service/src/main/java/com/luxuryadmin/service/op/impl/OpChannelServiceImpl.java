package com.luxuryadmin.service.op.impl;

import com.luxuryadmin.entity.op.OpChannel;
import com.luxuryadmin.mapper.op.OpChannelMapper;
import com.luxuryadmin.service.op.OpChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author monkey king
 * @date 2021-04-01 23:15:04
 */
@Slf4j
@Service
public class OpChannelServiceImpl implements OpChannelService {

    @Resource
    private OpChannelMapper opChannelMapper;

    @Override
    public OpChannel getOpChannelByOutsideCode(String outsideCode) {
        return opChannelMapper.getOpChannelByOutsideCode(outsideCode);
    }
}
