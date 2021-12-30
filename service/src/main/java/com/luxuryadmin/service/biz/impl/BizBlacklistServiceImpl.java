package com.luxuryadmin.service.biz.impl;

import com.luxuryadmin.entity.biz.BizBlacklist;
import com.luxuryadmin.mapper.biz.BizBlacklistMapper;
import com.luxuryadmin.service.biz.BizBlacklistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author monkey king
 * @date 2020-01-12 17:40:30
 */
@Slf4j
@Service
public class BizBlacklistServiceImpl implements BizBlacklistService {

    @Resource
    private BizBlacklistMapper bizBlacklistMapper;

    @Override
    public int saveBizBlacklist(BizBlacklist bizBlacklist) {
        return bizBlacklistMapper.saveObject(bizBlacklist);
    }
}
