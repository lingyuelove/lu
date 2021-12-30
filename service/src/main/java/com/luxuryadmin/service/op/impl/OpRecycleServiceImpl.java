package com.luxuryadmin.service.op.impl;

import com.luxuryadmin.mapper.op.OpRecycleMapper;
import com.luxuryadmin.param.op.ParamOpRecycleQuery;
import com.luxuryadmin.service.op.OpRecycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 回收归集逻辑层
 * mong
 */
@Service
public class OpRecycleServiceImpl implements OpRecycleService {


    @Resource
    private OpRecycleMapper opRecycleMapper;

    @Override
    public void addRecycle(ParamOpRecycleQuery paramOpRecycleQuery) {
        opRecycleMapper.saveObject(paramOpRecycleQuery);
    }



}
