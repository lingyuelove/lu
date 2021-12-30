package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.ShpUsualFunction;
import com.luxuryadmin.mapper.shp.ShpUsualFunctionMapper;
import com.luxuryadmin.service.shp.ShpUsualFunctionService;
import com.luxuryadmin.vo.shp.VoUsualFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author monkey king
 * @date 2020-06-15 17:26:53
 */
@Slf4j
@Service
public class ShpUsualFunctionServiceImpl implements ShpUsualFunctionService {

    @Resource
    private ShpUsualFunctionMapper shpUsualFunctionMapper;

    @Override
    public List<VoUsualFunction> listShpUsualFunction(int shopId, int userId) {

        return shpUsualFunctionMapper.listShpUsualFunction(shopId, userId);
    }

    @Override
    public List<VoUsualFunction> listAllFunction(int shopId, int userId) {
        return shpUsualFunctionMapper.listAllFunction(shopId, userId);
    }

    @Override
    public List<VoUsualFunction> listBossAllFunction() {
        return shpUsualFunctionMapper.listBossAllFunction();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetUsualFunction(int shopId, int userId, List<ShpUsualFunction> list) {
        try {
            shpUsualFunctionMapper.deleteUsualFunction(shopId, userId);
            if (!LocalUtils.isEmptyAndNull(list)) {
                shpUsualFunctionMapper.saveBatch(list);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            //对于try catch的方法里面; 事务要回滚必须显性操作回滚;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new MyException("设置常用功能失败!");
        }
    }

    @Override
    public void removeFuncAndPermIdNotExists(int shopId, String permIds) {
        shpUsualFunctionMapper.removeFuncAndPermIdNotExists(shopId, permIds);
    }
}
