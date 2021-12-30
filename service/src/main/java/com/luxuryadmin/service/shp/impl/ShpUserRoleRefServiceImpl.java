package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.ShpUserRoleRef;
import com.luxuryadmin.mapper.shp.ShpUserRoleRefMapper;
import com.luxuryadmin.param.shp.ParamShpUserInfo;
import com.luxuryadmin.service.shp.ShpUserRoleRefService;
import com.luxuryadmin.vo.shp.VoUserRoleRef;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author monkey king
 * @date 2019-12-30 22:05:51
 */
@Slf4j
@Service
public class ShpUserRoleRefServiceImpl implements ShpUserRoleRefService {

    @Resource
    private ShpUserRoleRefMapper shpUserRoleRefMapper;

    @Override
    public List<VoUserRoleRef> listUserRoleRefByUserId(int shopId, int userId) {
        return shpUserRoleRefMapper.listUserRoleRefByUserId(shopId, userId);
    }

    @Override
    public void saveUserRoleRef(ShpUserRoleRef shpUserRoleRef) {
        shpUserRoleRefMapper.saveObject(shpUserRoleRef);
    }

    @Override
    public void deleteUserRoleRef(int shopId, int userId, int roleId) {
        shpUserRoleRefMapper.deleteUserRoleRef(shopId, userId, roleId);
    }

    @Override
    public void deleteByUserId(Integer id) {
        shpUserRoleRefMapper.deleteByUserId(id);
    }

    @Override
    public void saveUserRoleRefs(ParamShpUserInfo paramShpUserInfo) {
        List<String> roles = paramShpUserInfo.getRoles();
        for (String role : roles) {
            ShpUserRoleRef shpUserRoleRef = new ShpUserRoleRef();
            shpUserRoleRef.setFkShpUserId(paramShpUserInfo.getId());
            shpUserRoleRef.setFkShpRoleId(LocalUtils.strParseInt(role));
            shpUserRoleRef.setDel(ConstantCommon.DEL_OFF);

        }
    }
}
