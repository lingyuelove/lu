package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.*;
import com.luxuryadmin.mapper.shp.ShpUserPermissionRefMapper;
import com.luxuryadmin.mapper.shp.ShpUserPermissionTplMapper;
import com.luxuryadmin.service.shp.ShpPermissionService;
import com.luxuryadmin.service.shp.ShpUserPermissionTplService;
import com.luxuryadmin.service.shp.ShpUserTypeService;
import com.luxuryadmin.service.sys.SysEnumService;
import com.luxuryadmin.vo.shp.VoUserPermission;
import com.luxuryadmin.vo.sys.VoSysEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author monkey king
 * @date 2020-09-04 15:38:22
 */
@Slf4j
@Service
public class ShpUserPermissionTplServiceImpl implements ShpUserPermissionTplService {

    @Resource
    private ShpUserPermissionTplMapper shpUserPermissionTplMapper;

    @Resource
    private ShpUserPermissionRefMapper shpUserPermissionRefMapper;

    @Autowired
    private SysEnumService sysEnumService;

    @Autowired
    private ShpUserTypeService shpUserTypeService;

    @Autowired
    private ShpPermissionService shpPermissionService;

    @Override
    public List<VoUserPermission> listUserPermissionTplByTplName(int shopId, int userTypeId) {
        return shpUserPermissionTplMapper.listUserPermissionTplByUserTypeId(shopId, userTypeId);
    }

    @Override
    public void saveShpUserPermissionTpl(int shopId, int userTypeId, List<ShpUserPermissionTpl> list) {
        //清空模板所有权限;重置
        shpUserPermissionTplMapper.deleteUserPermTplByShopIdAndUserId(shopId, userTypeId);
        if (!LocalUtils.isEmptyAndNull(list)) {
            shpUserPermissionTplMapper.saveBatch(list);
        }
    }

    @Override
    public void initShopSystemPerm(int shopId, int userId) {
        List<VoSysEnum> permissionList = sysEnumService.listVoSysEnum("shp_permission");
        if (!LocalUtils.isEmptyAndNull(permissionList)) {
            HashMap<String, List<Integer>> hashMap = new HashMap<>(16);
            List<String> tplNameList = new ArrayList<>();
            for (VoSysEnum perm : permissionList) {
                String tplName = perm.getName();
                List<Integer> permIdList = hashMap.get(tplName);
                if (LocalUtils.isEmptyAndNull(permIdList)) {
                    permIdList = new ArrayList<>();
                    tplNameList.add(tplName);
                }
                permIdList.add(Integer.parseInt(perm.getCode()));
                hashMap.put(tplName, permIdList);
            }
            int i = 0;
            for (String tplName : tplNameList) {
                List<Integer> permIdList = hashMap.get(tplName);
                ShpUserType userType = shpUserTypeService.packShpUserTypeToSave(shopId, 0, tplName, tplName, ++i, "");
                Integer userTypeId = shpUserTypeService.saveOrUpdateShpUserTypeAndReturnId(userType);
                bindUserPermission(permIdList, shopId, userTypeId);
            }
        }
    }

    @Override
    public int deleteShpUserPermissionTpl(int shopId, int userTypeId) {
        return shpUserPermissionTplMapper.deleteShpUserPermissionTpl(shopId, userTypeId);
    }

    @Override
    public void deleteShpUserPermissionTplByShopId(int shopId) {
        shpUserPermissionTplMapper.deleteShpUserPermissionTplByShopId(shopId);
    }

    /**
     * 绑定模板权限
     *
     * @param permIdList
     * @param shopId
     * @param userTypeId
     */
    private void bindUserPermission(List<Integer> permIdList, int shopId, int userTypeId) {
        Date date = new Date();
        List<ShpUserPermissionTpl> tplList = new ArrayList<>();
        if (!LocalUtils.isEmptyAndNull(permIdList)) {
            for (Integer permId : permIdList) {
                ShpUserPermissionTpl tpl = new ShpUserPermissionTpl();
                tpl.setFkShpUserTypeId(userTypeId);
                tpl.setFkShpPermissionId(permId);
                tpl.setFkShpShopId(shopId);
                tpl.setInsertTime(date);
                tpl.setInsertAdmin(0);
                tpl.setVersions(1);
                tpl.setDel("0");
                tplList.add(tpl);
            }
            //为模板赋予权限
            shpUserPermissionTplMapper.saveBatch(tplList);
        }
    }
}

