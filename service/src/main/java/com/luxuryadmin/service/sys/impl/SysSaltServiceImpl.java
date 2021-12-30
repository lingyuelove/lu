package com.luxuryadmin.service.sys.impl;

import java.util.Date;

import com.luxuryadmin.entity.sys.SysSalt;
import com.luxuryadmin.enums.sys.EnumSaltType;
import com.luxuryadmin.mapper.sys.SysSaltMapper;
import com.luxuryadmin.service.sys.SysSaltService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author monkey king
 * @date 2019-12-05 15:19:13
 */
@Slf4j
@Service
public class SysSaltServiceImpl implements SysSaltService {

    @Resource
    private SysSaltMapper sysSaltMapper;

    @Override
    public SysSalt getSysSaltByUserIdAndType(int userId, EnumSaltType saltType) {
        SysSalt sysSalt = createSysSalt(userId, saltType, null);
        return sysSaltMapper.getSysSaltByUserIdAndType(sysSalt);
    }

    @Override
    public SysSalt createSysSalt(int userId, EnumSaltType saltType, String salt) {
        SysSalt sysSalt = new SysSalt();
        sysSalt.setUserId(userId);
        sysSalt.setType(saltType.getCode());
        sysSalt.setSalt(salt);
        sysSalt.setInsertTime(new Date());
        return sysSalt;
    }

    @Override
    public void saveSysSalt(SysSalt sysSalt) {
        sysSaltMapper.saveObject(sysSalt);
    }

    @Override
    public int updateSysSalt(SysSalt sysSalt) {
        return sysSaltMapper.updateObject(sysSalt);
    }
}
