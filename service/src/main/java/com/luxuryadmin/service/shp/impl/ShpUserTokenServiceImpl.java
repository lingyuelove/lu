package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.entity.shp.ShpUserToken;
import com.luxuryadmin.mapper.shp.ShpUserTokenMapper;
import com.luxuryadmin.service.shp.ShpUserTokenService;
import com.luxuryadmin.vo.admin.shp.VoShpUserToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author monkey king
 * @date 2019-12-09 15:33:10
 */
@Slf4j
@Service
public class ShpUserTokenServiceImpl implements ShpUserTokenService {
    @Resource
    private ShpUserTokenMapper shpUserTokenMapper;

    @Override
    public ShpUserToken existsToken(String token) {
        return shpUserTokenMapper.getShpUserTokenByToken(token);
    }

    @Override
    public void saveShpUserToken(ShpUserToken shpUserToken) {
        shpUserTokenMapper.saveObject(shpUserToken);
    }

    @Override
    public ShpUserToken getShpUserTokenByUserId(Integer userId) {
        return shpUserTokenMapper.getShpUserTokenByUserId(userId);
    }

    @Override
    public void updateShpUserTokenState(Integer userId) {
        shpUserTokenMapper.updateShpUserTokenState(userId);
    }

    @Override
    public List<VoShpUserToken> listShpUserToken(Integer userId) {
        return shpUserTokenMapper.listShpUserToken(userId);
    }
}
