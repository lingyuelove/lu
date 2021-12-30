package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.entity.shp.ShpRolePermissionRef;
import com.luxuryadmin.entity.shp.ShpUserPermissionRef;
import com.luxuryadmin.mapper.shp.ShpRolePermissionRefMapper;
import com.luxuryadmin.service.shp.ShpRolePermissionRefService;
import com.luxuryadmin.vo.shp.VoRolePermissionRel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author monkey king
 * @date 2019-12-30 20:53:38
 */
@Slf4j
@Service
public class ShpRolePermissionRefServiceImpl implements ShpRolePermissionRefService {

    @Resource
    private ShpRolePermissionRefMapper shpRolePermissionRefMapper;

    @Override
    public int saveBatchShpRolePermissionRef(List<ShpRolePermissionRef> list) {
        return shpRolePermissionRefMapper.saveBatch(list);
    }

    @Override
    public List<VoRolePermissionRel> listRolePermsRelByRoleId(int shopId, int roleId) {
        return shpRolePermissionRefMapper.listVoRolePermsRelByShopIdRoleId(shopId, roleId);
    }

    @Override
    public int deleteShpRolePermissionRef(int shopId, int roleId) {
        return shpRolePermissionRefMapper.deleteShpRolePermissionRef(shopId, roleId);
    }

    @Override
    public List<String> getUserPermission(int shopId, int userId) {
        return shpRolePermissionRefMapper.getUserPermission(shopId, userId);
    }

}
