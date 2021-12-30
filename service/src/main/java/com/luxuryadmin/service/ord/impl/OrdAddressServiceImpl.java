package com.luxuryadmin.service.ord.impl;

import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.ord.OrdAddress;
import com.luxuryadmin.mapper.ord.OrdAddressMapper;
import com.luxuryadmin.service.ord.OrdAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author monkey king
 * @Date 2019/12/26 3:36
 */
@Slf4j
@Service
public class OrdAddressServiceImpl implements OrdAddressService {

    @Resource
    private OrdAddressMapper ordAddressMapper;

    @Override
    public int saveOrdAddress(OrdAddress ordAddress) {
        if (!LocalUtils.isEmptyAndNull(ordAddress.getId()) ){
            ordAddressMapper.updateObject(ordAddress);
        }else {
            ordAddressMapper.saveObject(ordAddress);
        }

        return ordAddress.getId();
    }
}
