package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.ShpPermission;
import com.luxuryadmin.mapper.shp.ShpPermissionMapper;
import com.luxuryadmin.mapper.shp.ShpRolePermissionRefMapper;
import com.luxuryadmin.service.shp.ShpPermissionService;
import com.luxuryadmin.vo.shp.VoShpPermission;
import com.luxuryadmin.vo.shp.VoUsualFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author monkey king
 * @date 2019-12-30 16:45:56
 */
@Slf4j
@Service
public class ShpPermissionServiceImpl implements ShpPermissionService {

    @Resource
    private ShpPermissionMapper shpPermissionMapper;

    @Resource
    private ShpRolePermissionRefMapper shpRolePermissionRefMapper;

    @Override
    public int saveShpPermission(ShpPermission shpPermission) {
        shpPermissionMapper.saveObject(shpPermission);
        return shpPermission.getId();
    }

    @Override
    public int updateShpPermission(ShpPermission shpPermission) {
        shpPermissionMapper.updateObject(shpPermission);
        return shpPermission.getId();
    }

    @Override
    public boolean existsShpPermission(int parentId, String name) {
        return shpPermissionMapper.existsShpPermission(parentId, name) > 0;
    }

    @Override
    public boolean existsShpPermissionCode(String code) {
        return shpPermissionMapper.existsShpPermissionCode(code) > 0;
    }

    @Override
    public List<VoShpPermission> listShpPermission(String version99) {
        return shpPermissionMapper.listAppShpPermission(version99);
    }

    @Override
    public ShpPermission getShpPermissionById(Integer id) {
        return (ShpPermission) shpPermissionMapper.getObjectById(id);
    }

    @Override
    public List<VoShpPermission> groupByShpPermission(String version99) {
        List<VoShpPermission> voShpPermissions = listShpPermission(version99);
        //获取一级权限
        List<VoShpPermission> firstPermList = new ArrayList<>();
        for (VoShpPermission allPerm : voShpPermissions) {
            int firstParent = allPerm.getParentId();
            if (firstParent == 0) {
                firstPermList.add(allPerm);
            }
        }

        //遍历一级权限;找到其子菜单;即二级菜单
        for (VoShpPermission firstPerm : firstPermList) {
            List<VoShpPermission> secondPermList = new ArrayList<>();
            //一级菜单的id,即为二级菜单的父节点
            Integer firstPermId = firstPerm.getId();
            HashMap<String, Object> secondMap = new HashMap<>(16);
            //遍历所有权限;
            for (VoShpPermission allPerm : voShpPermissions) {
                int secondParentId = allPerm.getParentId();
                if (firstPermId == secondParentId) {
                    secondPermList.add(allPerm);
                }
            }

            for (VoShpPermission secondPerm : secondPermList) {
                List<VoShpPermission> thirdPermList = new ArrayList<>();
                //二级菜单的id,即为三级菜单的父节点
                Integer secondPermId = secondPerm.getId();
                HashMap<String, Object> thirdMap = new HashMap<>(16);
                //遍历所有权限;
                for (VoShpPermission allPerm : voShpPermissions) {
                    int thirdParentId = allPerm.getParentId();
                    if (secondPermId == thirdParentId) {
                        thirdPermList.add(allPerm);
                    }
                }
                if (!LocalUtils.isEmptyAndNull(thirdPermList)) {
                    secondPerm.setPermissionList(thirdPermList);
                }
            }

            if (!LocalUtils.isEmptyAndNull(secondPermList)) {
                firstPerm.setPermissionList(secondPermList);
            }
        }

        return firstPermList;
    }

    @Override
    public List<VoUsualFunction> listShpPermissionByIds(String ids) {
        return shpPermissionMapper.listShpPermissionByIds(ids);
    }

    @Override
    public List<Integer> listAppShpPermissionId() {
        return shpPermissionMapper.listAppShpPermissionId();
    }

    @Override
    public int deleteShpPermissions(String inSql) {
        shpPermissionMapper.deleteObjects(inSql);
        return deleteShpPermissionRole();
    }

    @Override
    public int deleteShpPermissionRole() {
        return shpRolePermissionRefMapper.deleteObjects();
    }

    @Override
    public boolean existsPerms(String[] permission) {
        return shpPermissionMapper.existsPerms(LocalUtils.packString(permission)) > 0;
    }

    @Override
    public ShpPermission getShpPermissionByPermission(String permission) {
        return shpPermissionMapper.getShpPermissionByPermission(permission);
    }
}
