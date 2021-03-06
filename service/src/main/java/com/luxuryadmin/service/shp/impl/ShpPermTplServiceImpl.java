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
        //????????????????????????;??????
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
     * ????????????????????????????????????,???????????????id???????????????,???????????????????????????,?????????????????????????????????id???????????????;
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
                //????????????????????????, ?????????????????????
                continue;
            }
            redisUtil.set(tempKey, DateUtil.format(new Date()));
            List<ShpUserType> userTypeList = shpUserTypeService.listShpUserTypeByShopId(shopId);
            if (LocalUtils.isEmptyAndNull(userTypeList)) {
                continue;
            }
            for (ShpUserType userType : userTypeList) {
                //?????????????????????id
                Integer userTypeId = userType.getId();
                List<Integer> permIdList = shpUserPermissionTplMapper.listTplPermIdByUserTypeId(shopId, userTypeId);
                if (LocalUtils.isEmptyAndNull(permIdList)) {
                    continue;
                }
                String oldPermIds = LocalUtils.packString(permIdList.toArray());
                //?????????????????????????????????code
                List<String> permCodeList = shpPermissionMapper.listPermCodeByPermIds(oldPermIds);
                if (LocalUtils.isEmptyAndNull(permCodeList)) {
                    continue;
                }
                String oldPermCodes = LocalUtils.packString(permCodeList.toArray());
                //??????????????????code?????????????????????id
                List<ShpPermTpl> shpPermTplList = shpPermIndexMapper.listOldPermTplTurnNewPermTpl(shopId, userTypeId, oldPermCodes);
                //???????????????????????????id
                saveShpPermTpl(shopId, userTypeId, shpPermTplList);
            }
        }
        long et = System.currentTimeMillis();
        log.info("=======?????????????????????????????????====?????????{}", et - st);
    }

    /**
     * ??????????????????
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
            //?????????????????????
            shpPermTplMapper.saveBatch(tplList);
        }
    }
}

