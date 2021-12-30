package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.entity.shp.ShpUserDetail;
import com.luxuryadmin.mapper.shp.ShpUserDetailMapper;
import com.luxuryadmin.service.shp.ShpUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author monkey king
 * @date 2019-12-05 14:06:31
 */
@Slf4j
@Service
public class ShpUserDetailServiceImpl implements ShpUserDetailService {

    @Resource
    private ShpUserDetailMapper shpUserDetailMapper;
    @Autowired
    private RedisUtil redisUtil;



    @Override
    public void saveShpUserDetail(int shpUserId) {
        ShpUserDetail shpUserDetail = new ShpUserDetail();
        shpUserDetail.setFkShpUserId(shpUserId);
        shpUserDetail.setInsertTime(new Date());
        shpUserDetailMapper.saveObject(shpUserDetail);
    }

    @Override
    public ShpUserDetail selectByUserId(String id) {
        return shpUserDetailMapper.getByUserId(id);
    }

}
