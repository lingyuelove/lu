package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.entity.shp.*;
import com.luxuryadmin.mapper.shp.ShpPermIndexMapper;
import com.luxuryadmin.mapper.shp.ShpPermTplMapper;
import com.luxuryadmin.mapper.shp.ShpPermissionMapper;
import com.luxuryadmin.mapper.shp.ShpUserPermissionTplMapper;
import com.luxuryadmin.service.shp.ShpPermTplService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.service.shp.ShpUserPermissionTplService;
import com.luxuryadmin.service.shp.ShpUserTypeService;
import com.luxuryadmin.service.sys.SysEnumService;
import com.luxuryadmin.vo.shp.VoUserPermission;
import com.luxuryadmin.vo.shp.VoUserShopBase;
import com.luxuryadmin.vo.sys.VoSysEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author monkey king
 * @date 2020-09-04 15:38:22
 */
@Slf4j
@Service
public class ShpPermTplServiceImpl implements ShpPermTplService {

    @Resource
    private ShpPermTplMapper shpPermTplMapper;

    @Resource
    private ShpUserPermissionTplMapper shpUserPermissionTplMapper;

    @Autowired
    private SysEnumService sysEnumService;

    @Autowired
    private ShpUserTypeService shpUserTypeService;

    @Autowired
    private ShpShopService shpShopService;

    @Resource
    private ShpPermIndexMapper shpPermIndexMapper;

    @Resource
    private ShpPermissionMapper shpPermissionMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<VoUserPermission> listPermTplByTplName(int shopId, int userTypeId) {
        return shpPermTplMapper.listPermTplByUserTypeId(shopId, userTypeId);
    }

    @Override
    public void saveShpPermTpl(int shopId, int userTypeId, List<ShpPermTpl> list) {
        //清空模板所有权限;重置
        shpPermTplMapper.deleteShpPermTplByShopIdAndUserId(shopId, userTypeId);
        if (!LocalUtils.isEmptyAndNull(list)) {
            shpPermTplMapper.saveBatch(list);
        }
    }

    @Override
    public void initShopSystemPerm(int shopId, int userId) {
        List<VoSysEnum> permissionList = sysEnumService.listVoSysEnum("shp_perm_index");
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
    public int deleteShpUserPermTpl(int shopId, int userTypeId) {
        return shpPermTplMapper.deleteShpUserPermTpl(shopId, userTypeId);
    }

    @Override
    public void deleteShpPermTplByShopId(int shopId) {
        shpPermTplMapper.deleteShpPermTplByShopId(shopId);
    }

    /**
     * 找到店铺的所有在用的身份,再根据身份id去获取模板,去获取对应的旧权限,再把旧权限对应的新权限id给新的模板;
     */
    @Override
    public void initNewPermTplFromOldPermTpl() {
        long st = System.currentTimeMillis();
        List<VoUserShopBase> shopList = shpShopService.listAllShopIdAndUserId();
        for (VoUserShopBase shop : shopList) {
            Integer shopId = shop.getShopId();
            String tempKey = "shp:movePermTpl:" + shopId;
            String tempValue = redisUtil.get(tempKey);
            log.info("======{}======", tempKey);
            if (!LocalUtils.isEmptyAndNull(tempValue)) {
                //已经迁移过了权限, 不需要再次执行
                continue;
            }
            redisUtil.set(tempKey, DateUtil.format(new Date()));
            List<ShpUserType> userTypeList = shpUserTypeService.listShpUserTypeByShopId(shopId);
            if (LocalUtils.isEmptyAndNull(userTypeList)) {
                continue;
            }
            for (ShpUserType userType : userTypeList) {
                //获取模板旧权限id
                Integer userTypeId = userType.getId();
                List<Integer> permIdList = shpUserPermissionTplMapper.listTplPermIdByUserTypeId(shopId, userTypeId);
                if (LocalUtils.isEmptyAndNull(permIdList)) {
                    continue;
                }
                String oldPermIds = LocalUtils.packString(permIdList.toArray());
                //获取模板对应的旧版权限code
                List<String> permCodeList = shpPermissionMapper.listPermCodeByPermIds(oldPermIds);
                if (LocalUtils.isEmptyAndNull(permCodeList)) {
                    continue;
                }
                String oldPermCodes = LocalUtils.packString(permCodeList.toArray());
                //获取旧版权限code对应的新版权限id
                List<ShpPermTpl> shpPermTplList = shpPermIndexMapper.listOldPermTplTurnNewPermTpl(shopId, userTypeId, oldPermCodes);
                //为模板赋予新版权限id
                saveShpPermTpl(shopId, userTypeId, shpPermTplList);
            }
        }
        long et = System.currentTimeMillis();
        log.info("=======初始化所有新版权限结束====耗时：{}", et - st);
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
        List<ShpPermTpl> tplList = new ArrayList<>();
        if (!LocalUtils.isEmptyAndNull(permIdList)) {
            for (Integer permId : permIdList) {
                ShpPermTpl tpl = new ShpPermTpl();
                tpl.setFkShpUserTypeId(userTypeId);
                tpl.setFkShpPermIndexId(permId);
                tpl.setFkShpShopId(shopId);
                tpl.setInsertTime(date);
                tpl.setInsertAdmin(0);
                tplList.add(tpl);
            }
            //为模板赋予权限
            shpPermTplMapper.saveBatch(tplList);
        }
    }
}

