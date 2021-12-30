package com.luxuryadmin.service.biz.impl;

import com.luxuryadmin.entity.biz.BizLog;
import com.luxuryadmin.mapper.biz.BizLogMapper;
import com.luxuryadmin.service.biz.BizLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author monkey king
 * @date 2020-01-12 18:12:38
 */
@Slf4j
@Service
public class BizLogServiceImpl implements BizLogService {

    @Resource
    private BizLogMapper bizLogMapper;

    @Override
    public int saveBizLog(int shopId, int userId, String type, String remake) {
        BizLog bizLog = new BizLog();
        bizLog.setFkShpShopId(shopId);
        bizLog.setType(type);
        bizLog.setInsertTime(new Date());
        bizLog.setInsertAdmin(userId);
        bizLog.setRemark(remake);
        return bizLogMapper.saveObject(bizLog);
    }

    @Override
    public void deleteBizLogByShop(int shopId) {
        bizLogMapper.deleteBizLogByShop(shopId);
    }
}
