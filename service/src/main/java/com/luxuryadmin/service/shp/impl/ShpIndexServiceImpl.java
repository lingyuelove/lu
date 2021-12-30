package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.ShpIndex;
import com.luxuryadmin.entity.shp.ShpPermUserRef;
import com.luxuryadmin.mapper.shp.ShpIndexMapper;
import com.luxuryadmin.mapper.shp.ShpPermIndexMapper;
import com.luxuryadmin.mapper.shp.ShpPermUserRefMapper;
import com.luxuryadmin.service.shp.ShpIndexService;
import com.luxuryadmin.service.shp.ShpPermUserRefService;
import com.luxuryadmin.vo.shp.VoShpIndex;
import com.luxuryadmin.vo.shp.VoShpPermIndex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author monkey king
 * @date 2020-06-15 17:26:53
 */
@Slf4j
@Service
public class ShpIndexServiceImpl implements ShpIndexService {

    @Resource
    private ShpIndexMapper shpIndexMapper;

    @Override
    public ShpIndex getShpIndexById(Integer id) {
        return (ShpIndex) shpIndexMapper.getObjectById(id);
    }

    @Override
    public void saveShpPermIndex(ShpIndex shpIndex) {
        shpIndexMapper.saveObject(shpIndex);
    }


    @Override
    public void updateShpIndex(ShpIndex shpIndex) {
        shpIndexMapper.updateObject(shpIndex);
    }

    @Override
    public void deleteShpIndex(String ids) {
        shpIndexMapper.batchDelete(ids);
    }

    @Override
    public List<VoShpIndex> listAllPermByShopIdUserId(String platform, Integer appVersion, int shopId, int userId) {
        return shpIndexMapper.listAllPermByShopIdUserId(platform, appVersion, shopId, userId);
    }


    @Override
    public List<VoShpIndex> listAppIndexFunction(String platform, Integer appVersion) {
        return shpIndexMapper.listAppIndexFunction(platform, appVersion);
    }

    @Override
    public boolean existsShpIndex(String name) {
        return shpIndexMapper.existsShpIndex(name) > 0;
    }

    @Override
    public List<VoShpIndex> groupByShpPermIndex() {
        List<VoShpIndex> voShpPermissions = shpIndexMapper.listAllShpIndex();
        //获取一级权限
        List<VoShpIndex> firstPermList = new ArrayList<>();
        for (VoShpIndex allPerm : voShpPermissions) {
            int firstParent = allPerm.getParentId();
            if (firstParent == 0) {
                firstPermList.add(allPerm);
            }
        }
        //遍历一级权限;找到其子菜单;即二级菜单
        for (VoShpIndex firstPerm : firstPermList) {
            List<VoShpIndex> secondPermList = new ArrayList<>();
            //一级菜单的id,即为二级菜单的父节点
            Integer firstPermId = firstPerm.getId();
            HashMap<String, Object> secondMap = new HashMap<>(16);
            //遍历所有权限;
            for (VoShpIndex allPerm : voShpPermissions) {
                int secondParentId = allPerm.getParentId();
                if (firstPermId == secondParentId) {
                    secondPermList.add(allPerm);
                }
            }
            for (VoShpIndex secondPerm : secondPermList) {
                List<VoShpIndex> thirdPermList = new ArrayList<>();
                //二级菜单的id,即为三级菜单的父节点
                Integer secondPermId = secondPerm.getId();
                HashMap<String, Object> thirdMap = new HashMap<>(16);
                //遍历所有权限;
                for (VoShpIndex allPerm : voShpPermissions) {
                    int thirdParentId = allPerm.getParentId();
                    if (secondPermId == thirdParentId) {
                        thirdPermList.add(allPerm);
                    }
                }
                if (!LocalUtils.isEmptyAndNull(thirdPermList)) {
                    secondPerm.setPermIndexList(thirdPermList);
                }
            }
            if (!LocalUtils.isEmptyAndNull(secondPermList)) {
                firstPerm.setPermIndexList(secondPermList);
            }
        }
        return firstPermList;
    }
}
