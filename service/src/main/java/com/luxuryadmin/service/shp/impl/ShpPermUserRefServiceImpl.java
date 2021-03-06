package com.luxuryadmin.service.shp.impl;

import com.alibaba.fastjson.JSON;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.entity.shp.*;
import com.luxuryadmin.enums.shp.EnumShpUserType;
import com.luxuryadmin.mapper.shp.ShpPermIndexMapper;
import com.luxuryadmin.mapper.shp.ShpPermUserRefMapper;
import com.luxuryadmin.mapper.shp.ShpPermissionMapper;
import com.luxuryadmin.mapper.shp.ShpUserPermissionRefMapper;
import com.luxuryadmin.service.shp.*;
import com.luxuryadmin.vo.shp.VoRolePermissionRel;
import com.luxuryadmin.vo.shp.VoUserPermission;
import com.luxuryadmin.vo.shp.VoUserRoleRef;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author monkey king
 * @date 2020-09-04 15:34:23
 */
@Slf4j
@Service
public class ShpPermUserRefServiceImpl implements ShpPermUserRefService {

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    @Autowired
    private ShpUserService shpUserService;

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private ShpPermUserRefMapper shpPermUserRefMapper;

    @Resource
    private ShpShopService shpShopService;

    @Autowired
    private ShpUserRoleRefService shpUserRoleRefService;

    @Autowired
    private ShpRolePermissionRefService shpRolePermissionRefService;

    @Autowired
    private ShpUserTypeService shpUserTypeService;

    @Autowired
    private ServicesUtil servicesUtil;

    @Autowired
    private ShpPermissionService shpPermissionService;


    @Resource
    private ShpPermIndexMapper shpPermIndexMapper;

    @Autowired
    private ShpPermIndexService shpPermIndexService;

    @Resource
    private ShpUserPermissionRefMapper shpUserPermissionRefMapper;

    @Resource
    private ShpPermissionMapper shpPermissionMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateBatchShpPermUserRef(
            int userId, int shopId, String name, int userTypeId,
            List<ShpPermUserRef> list, int updateUserId) throws Exception {
        try {
            //??????????????????,??????????????????????????????????????????;?????????????????????????????????????????????;
            ShpUserShopRef userShopRef = shpUserShopRefService.getShpUserShopRefByUserId(shopId, userId);
            if (LocalUtils.isEmptyAndNull(userShopRef)) {
                //????????????
                shpUserShopRefService.saveShpUserShopRef(userId, shopId, name, userTypeId, updateUserId);
                ShpUser shpUser = shpUserService.packShpUserForLogin(userId, shopId, null);
                shpUserService.updateShpUser(shpUser);
            } else {
                userShopRef.setState("1");
                userShopRef.setFkShpUserTypeId(userTypeId);
                userShopRef.setUpdateTime(new Date());
                userShopRef.setUpdateAdmin(updateUserId);
                userShopRef.setName(name);
                shpUserShopRefService.updateUserShopRef(userShopRef);
            }
            //????????????????????????;??????
            shpPermUserRefMapper.deletePermUserRefByShopIdAndUserId(shopId, userId);
            if (!LocalUtils.isEmptyAndNull(list)) {
                ShpShop shpShop = shpShopService.getShpShopById(shopId + "");


                //???????????????;v2.6.1;????????????
                String userPerms = servicesUtil.getUserPerms(shopId, userId);
                String unionPerm = "shop:union:show";
                //??????????????????????????????,???????????????????????????, ????????????????????????;?????????????????????????????????????????????
                if (shpShop.getFkShpUserId() != updateUserId && userPerms.contains(unionPerm)) {
                    ShpPermission sp = shpPermissionService.getShpPermissionByPermission(unionPerm);
                    if (null != sp) {
                        //???????????????????????????????????????????????????;
                        boolean hasPerm = false;
                        for (ShpPermUserRef s : list) {
                            if (s.getFkShpPermIndexId().intValue() == sp.getId().intValue()) {
                                hasPerm = true;
                                break;
                            }
                        }
                        if (!hasPerm) {
                            ShpPermUserRef spr = list.get(0);
                            ShpPermUserRef newSpr = new ShpPermUserRef();
                            BeanUtils.copyProperties(spr, newSpr);
                            newSpr.setFkShpPermIndexId(sp.getId());
                            list.add(newSpr);
                        }
                    }
                }
                shpPermUserRefMapper.saveBatch(list);
                refreshOldPerm(shopId, userId, list);
            }
            //??????????????????;
            listPermUserByRedis(shopId, userId, true);
        } catch (Exception e) {
            log.error("=======????????????????????????====: " + e.getMessage(), e);
            //??????try catch???????????????; ???????????????????????????????????????;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception();
        }
    }

    @Override
    public void refreshOldPerm(int shopId, int userId, List<ShpPermUserRef> list) {
        //?????????????????????,??????????????????;????????????????????????;
        shpUserPermissionRefMapper.deleteUserPermRefByShopIdAndUserId(shopId, userId);
        //1.?????????????????????id,??????????????????????????????;
        List<Integer> permIdList = new ArrayList<>();
        for (ShpPermUserRef ref : list) {
            permIdList.add(ref.getFkShpPermIndexId());
        }
        String newPermIds = LocalUtils.packString(permIdList.toArray());
        List<String> permCodeList = shpPermIndexMapper.listPermCodeByPermIds(newPermIds);
        //2.??????????????????????????????,??????????????????????????????id
        if (!LocalUtils.isEmptyAndNull(permCodeList)) {
            Object[] codeArray = permCodeList.toArray();

            List<Integer> oldPermIds = shpPermissionMapper.listPermIdByPermCode(LocalUtils.packString(codeArray));
            if (!LocalUtils.isEmptyAndNull(oldPermIds)) {
                List<ShpUserPermissionRef> oldList = new ArrayList<>();
                //3.??????????????????id????????????????????????????????????????????????;
                for (Integer oldPermId : oldPermIds) {
                    ShpUserPermissionRef newSpr = new ShpUserPermissionRef();
                    newSpr.setFkShpPermissionId(oldPermId);
                    newSpr.setFkShpShopId(shopId);
                    newSpr.setFkShpUserId(userId);
                    newSpr.setInsertAdmin(userId);
                    newSpr.setInsertTime(new Date());
                    oldList.add(newSpr);
                }
                shpUserPermissionRefMapper.saveBatch(oldList);
            }
        }
    }

    @Override
    public List<String> listPermUserByRedis(int shopId, int userId, boolean isUpdateRedis) {
        //??????????????????
        String shpUserPermKey = ConstantRedisKey.getPermKeyByShopIdUserId(shopId, userId);
        //?????????????????????
        String permVersionKey = ConstantRedisKey.SHP_PERM_VERSION;
        Object permVersionValue = redisUtil.get(permVersionKey);

        String needUpdateKey = ConstantRedisKey.NEED_UPDATE_PERM;
        Map<String, Object> hashMap = new HashMap<>(16);
        List<String> permList = new ArrayList<>();
        String shpUserPermValue = redisUtil.get(shpUserPermKey);
        if (!isUpdateRedis && !LocalUtils.isEmptyAndNull(shpUserPermValue)) {
            //json??????????????????
            if (shpUserPermValue.startsWith("{")) {
                hashMap = (Map<String, Object>) JSON.parse(shpUserPermValue);
                Object ownPermVersion = hashMap.get(permVersionKey);
                Object needUpdateValue = hashMap.get(needUpdateKey);
                //???????????????;?????????????????????,?????????????????????; ???????????????,????????????????????????
                if (permVersionValue.equals(ownPermVersion) && ConstantCommon.ZERO.equals(needUpdateValue)) {
                    //?????????
                    Object permObj = hashMap.get("permList");
                    if (!ConstantCommon.NO_PERM.equals(permObj)) {
                        permList = (List<String>) permObj;
                    }
                } else {
                    isUpdateRedis = true;
                }
            }
        } else {
            isUpdateRedis = true;
        }
        //????????????????????????
        if (isUpdateRedis) {
            ShpUserShopRef userShopRef = shpUserShopRefService.getShpUserShopRefByUserId(shopId, userId);
            if (!LocalUtils.isEmptyAndNull(userShopRef)) {
                List<String> permList2;
                if (EnumShpUserType.BOSS.getCode().intValue() == userShopRef.getFkShpUserTypeId().intValue()) {
                    //???????????????????????????
                    permList = shpPermUserRefMapper.listBossPermission();
                } else {
                    permList = listUserPermission(shopId, userId);
                }
                ////?????????????????????
                //if (!LocalUtils.isEmptyAndNull(shpUserPermValue)) {
                //    hashMap = (Map<String, Object>) JSON.parse(shpUserPermValue);
                //    Object permObj = hashMap.get("permList");
                //    if (!ConstantCommon.NO_PERM.equals(permObj)) {
                //        permList = (List<String>) permObj;
                //        //???????????????????????????;?????????????????????;
                //        //permList.removeIf(perm -> perm.startsWith("9"));
                //    }
                //}
                ////????????????,?????????????????????;v267;????????????????????????,?????????????????????;?????????????????????;
                //permList = (List<String>) CollectionUtils.union(permList, permList2);
            }
        }
        if (!LocalUtils.isEmptyAndNull(permList)) {
            hashMap.put("permList", permList);
        } else {
            hashMap.put("permList", ConstantCommon.NO_PERM);
            permList = new ArrayList<>();
        }
        hashMap.put(permVersionKey, permVersionValue);
        hashMap.put(needUpdateKey, ConstantCommon.ZERO);
        //??????????????????
        redisUtil.setEx(shpUserPermKey, JSON.toJSONString(hashMap), 15);
        return permList;
    }

    @Override
    public List<Integer> listShopUserIdWithPermission(int shopId, String permission) {
        List<Integer> pushUserIdList = shpPermUserRefMapper.listShopUserIdWithPermission(shopId, permission);
        //???????????????ID
        Integer ownerUserId = shpShopService.getBossUserIdByShopId(shopId);
        if (!pushUserIdList.contains(ownerUserId)) {
            pushUserIdList.add(ownerUserId);
        }
        return pushUserIdList;
    }

    @Override
    public List<VoUserPermission> listUserPermByShopIdAndUserId(int shopId, int userId) {
        return shpPermUserRefMapper.listUserPermByShopIdAndUserId(shopId, userId);
    }

    @Override
    public List<String> listUserPermission(int shopId, int userId) {
        return shpPermUserRefMapper.listUserPermission(shopId, userId);
    }

    @Override
    public int deleteShpPermUserRefByUserId(int shopId, String userIds) {
        return shpPermUserRefMapper.deleteShpPermUserRefByUserId(shopId, userIds);
    }

    @Override
    public ShpPermUserRef packShpPermUserRef(int shopId, int userId, int permId, int insertUserId) {

        ShpPermUserRef ref = new ShpPermUserRef();
        ref.setFkShpUserId(userId);
        ref.setFkShpPermIndexId(permId);
        ref.setFkShpShopId(shopId);
        ref.setInsertTime(new Date());
        ref.setInsertAdmin(insertUserId);
        return ref;
    }

    @Override
    public void initOldUserPermission(int shopId, int userId) {
        try {
            String movePermUserKey = "_movePermShopUser";
            String movePermUserValue = redisUtil.get(movePermUserKey);
            movePermUserValue = LocalUtils.isEmptyAndNull(movePermUserValue) ? "" : movePermUserValue;
            // 1.??????????????????????????????????????????;
            List<VoUserRoleRef> userRoleList = shpUserRoleRefService.listUserRoleRefByUserId(shopId, userId);
            String moveUser = shopId + "_" + userId;
            if (movePermUserValue.contains(moveUser)) {
                return;
            }
            movePermUserValue += moveUser + ",";
            redisUtil.set(movePermUserKey, movePermUserValue);
            if (!LocalUtils.isEmptyAndNull(userRoleList)) {
                log.info("==============????????????====shopId: {}, userId: {}=========", shopId, userId);
                HashMap<Object, Object> map = new HashMap<>(16);
                String userTypeName = "";
                String tempName = "??????";
                int userTypeNum;
                int maxNum = 0;
                for (VoUserRoleRef userRoleRef : userRoleList) {
                    Integer roleId = userRoleRef.getRoleId();
                    String roleName = userRoleRef.getRoleName();
                    //????????????????????????????????????????????????;
                    switch (roleName) {
                        case "??????":
                            userTypeNum = 3;
                            break;
                        case "??????":
                            userTypeNum = 2;
                            break;
                        case "??????":
                            userTypeNum = 1;
                            break;
                        default:
                            userTypeNum = 0;
                    }
                    if (maxNum <= userTypeNum) {
                        maxNum = userTypeNum;
                        switch (maxNum) {
                            case 3:
                                userTypeName = "??????";
                                break;
                            case 2:
                                userTypeName = "??????";
                                break;
                            case 1:
                                userTypeName = "??????";
                                break;
                            default:
                                userTypeName = tempName;
                        }
                    }
                    // 2.??????????????????????????????permId;(????????????shp_role_permission_ref)
                    List<VoRolePermissionRel> permIdList = shpRolePermissionRefService.listRolePermsRelByRoleId(shopId, roleId);
                    if (!LocalUtils.isEmptyAndNull(permIdList)) {
                        List<ShpPermUserRef> refList = new ArrayList<>();
                        ArrayList<Object> queryPermIdList = new ArrayList<>();
                        for (VoRolePermissionRel perm : permIdList) {
                            Integer permId = perm.getId();
                            Object existsPermId = map.get(permId);
                            //????????????;
                            if (LocalUtils.isEmptyAndNull(existsPermId)) {
                                map.put(permId, permId);
                                queryPermIdList.add(permId);
                                // 3.???permId??????????????????(shp_user_permission_ref)
                                ShpPermUserRef shpUserPerm = packShpPermUserRef(shopId, userId, permId, 0);
                                refList.add(shpUserPerm);
                            }
                        }
                        //????????????
                        if (!LocalUtils.isEmptyAndNull(refList)) {
                            String queryPermIds = LocalUtils.packString(queryPermIdList.toArray());
                            int rows = shpPermUserRefMapper.existsShpUserPermissionRef(shopId, userId, queryPermIds);
                            if (rows == 0) {
                                shpPermUserRefMapper.saveBatch(refList);
                            }
                        }
                    }
                }
                //?????????????????????????????????
                ShpUserShopRef shpUserShopRef = shpUserShopRefService.getShpUserShopRefByUserId(shopId, userId);
                int userTypeId;
                //??????????????????????????????
                List<ShpUserType> userTypeList = shpUserTypeService.listShpUserTypeByShopId(shopId);
                boolean isSame = false;
                ShpUserType dbUserType = null;
                if (!LocalUtils.isEmptyAndNull(userTypeList)) {
                    for (ShpUserType userType : userTypeList) {
                        String dbUserTypeName = userType.getName();
                        if (dbUserTypeName.equals(userTypeName)) {
                            //?????????????????????????????????,???????????????????????????id
                            isSame = true;
                            dbUserType = userType;
                            break;
                        }
                    }
                }
                if (isSame) {
                    userTypeId = dbUserType.getId();
                } else {
                    //???????????????????????????"??????"?????????
                    ShpUserType userType = shpUserTypeService.packShpUserTypeToSave(shopId, 0, tempName, tempName, 9, "");
                    userTypeId = shpUserTypeService.saveOrUpdateShpUserTypeAndReturnId(userType);
                }
                if (!LocalUtils.isEmptyAndNull(shpUserShopRef)) {
                    shpUserShopRef.setFkShpUserTypeId(userTypeId);
                    shpUserShopRef.setUpdateTime(new Date());
                    shpUserShopRefService.updateUserShopRef(shpUserShopRef);
                }
            }
        } catch (Exception e) {
            log.error("======??????????????????====={}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteShpUserPermissionRefByShopId(int shopId) {
        shpPermUserRefMapper.deleteShpUserPermissionRefByShopId(shopId);
    }

    @Override
    public int countModulePermission(int shopId, Integer userId, String moduleName) {
        return shpPermUserRefMapper.countModulePermission(shopId, userId, moduleName);
    }

    @Override
    public void changeShpPermUserRef(int shopId, int oldUserId, int newUserId) {
        shpPermUserRefMapper.changeShpPermUserRef(shopId, oldUserId, newUserId);
    }

    @Override
    public void updateObject(ShpPermUserRef shpUserPermissionRef) {
        shpPermUserRefMapper.updateObject(shpUserPermissionRef);
    }

    @Override
    public void turnOldPermToNewPerm(int shopId, int userId, List<String> oldUserPermList) {
        if (LocalUtils.isEmptyAndNull(oldUserPermList)) {
            return;
        }
        String oldUserPerm = LocalUtils.packString(oldUserPermList.toArray());
        List<ShpPermUserRef> shpPermUserRefList = shpPermIndexMapper.listOldUserPermTurnNewPerm(shopId, userId, oldUserPerm);
        if (!LocalUtils.isEmptyAndNull(shpPermUserRefList)) {


            int rows = shpPermUserRefMapper.existsShpUserPermissionRef(shopId, userId, "10010");
            if (rows == 0) {

                boolean a = false;
                for (ShpPermUserRef shpPermUserRef : shpPermUserRefList) {
                    if (10010 == shpPermUserRef.getFkShpPermIndexId()) {
                        a = true;
                        break;
                    }
                }
                if (!a) {
                    //??????????????????
                    ShpPermUserRef ref = new ShpPermUserRef();
                    ref.setFkShpShopId(shopId);
                    ref.setFkShpUserId(userId);
                    ref.setFkShpPermIndexId(10010);
                    ref.setInsertTime(new Date());
                    ref.setInsertAdmin(-9);
                    shpPermUserRefList.add(ref);
                }
            }


            String[] storePerms = new String[]{"pro:check:ownProduct", "pro:check:entrustProduct", "pro:check:otherProduct"};
            if (shpPermissionService.existsPerms(storePerms)) {

                int rows2 = shpPermUserRefMapper.existsShpUserPermissionRef(shopId, userId, "10010");
                if (rows2 == 0) {
                    boolean a = false;
                    for (ShpPermUserRef shpPermUserRef : shpPermUserRefList) {
                        if (10018 == shpPermUserRef.getFkShpPermIndexId()) {
                            a = true;
                            break;
                        }
                    }
                    if (!a) {
                        //??????????????????;
                        ShpPermUserRef ref2 = new ShpPermUserRef();
                        ref2.setFkShpShopId(shopId);
                        ref2.setFkShpUserId(userId);
                        ref2.setFkShpPermIndexId(10018);
                        ref2.setInsertTime(new Date());
                        ref2.setInsertAdmin(-9);
                        shpPermUserRefList.add(ref2);
                    }
                }
            }
            //????????????????????????;??????
            shpPermUserRefMapper.deletePermUserRefByShopIdAndUserId(shopId, userId);
            shpPermUserRefMapper.saveBatch(shpPermUserRefList);
        }
    }

    @Override
    public void initNewPermFromOldPerm() {
        long st = System.currentTimeMillis();
        List<ShpUserShopRef> listUser = shpUserShopRefService.listAllUserShopRefExceptBoss();
        if (LocalUtils.isEmptyAndNull(listUser)) {
            return;
        }
        for (int i = 0; i < listUser.size(); i++) {
            ShpUserShopRef userShopRef = listUser.get(i);
            Integer shopId = userShopRef.getFkShpShopId();
            Integer userId = userShopRef.getFkShpUserId();
            turnOldPermToNewPerm(shopId, userId);
            String tempKey = "shp:movePerm:" + shopId + ":" + userId;
            log.info("=========???{}???{}========", i, tempKey);
        }
        long et = System.currentTimeMillis();
        log.info("=======?????????????????????????????????====?????????{}", et - st);
    }


    /**
     * ??????????????????,?????????????????????,???????????????????????????;
     *
     * @return
     */
    private void turnOldPermToNewPerm(int shopId, int userId) {
        String tempKey = "shp:movePerm:" + shopId + ":" + userId;
        String tempValue = redisUtil.get(tempKey);
        log.info("======{}======", tempKey);
        if (!LocalUtils.isEmptyAndNull(tempValue)) {
            //????????????????????????, ?????????????????????
            return;
        }
        String userPerms = servicesUtil.getUserPerms(shopId, userId);
        redisUtil.set(tempKey, userPerms);
        if (LocalUtils.isEmptyAndNull(userPerms)) {
            return;
        }
        Map<String, Object> hashMap = (Map<String, Object>) JSON.parse(userPerms);
        Object permObj = hashMap.get("permList");
        if (!ConstantCommon.NO_PERM.equals(permObj)) {
            List<String> oldPermList = (List<String>) permObj;
            turnOldPermToNewPerm(shopId, userId, oldPermList);
            //??????????????????;
            listPermUserByRedis(shopId, userId, true);
        }
    }
}
