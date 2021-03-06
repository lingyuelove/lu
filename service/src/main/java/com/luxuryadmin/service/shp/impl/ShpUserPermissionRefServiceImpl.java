package com.luxuryadmin.service.shp.impl;

import com.alibaba.fastjson.JSON;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.entity.shp.*;
import com.luxuryadmin.enums.shp.EnumShpUserType;
import com.luxuryadmin.mapper.shp.ShpPermUserRefMapper;
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
public class ShpUserPermissionRefServiceImpl implements ShpUserPermissionRefService {

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    @Autowired
    private ShpUserService shpUserService;

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private ShpUserPermissionRefMapper shpUserPermissionRefMapper;

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
    private ShpPermUserRefMapper shpPermUserRefMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveOrUpdateBatchShpUserPermissionRef(
            int userId, int shopId, String name, int userTypeId,
            List<ShpUserPermissionRef> list, int updateUserId) {
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
            int row = 0;
            //????????????????????????;??????
            shpUserPermissionRefMapper.deleteUserPermRefByShopIdAndUserId(shopId, userId);
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
                        for (ShpUserPermissionRef s : list) {
                            if (s.getFkShpPermissionId().intValue() == sp.getId().intValue()) {
                                hasPerm = true;
                                break;
                            }
                        }
                        if (!hasPerm) {
                            ShpUserPermissionRef spr = list.get(0);
                            ShpUserPermissionRef newSpr = new ShpUserPermissionRef();
                            BeanUtils.copyProperties(spr, newSpr);
                            newSpr.setFkShpPermissionId(sp.getId());
                            list.add(newSpr);
                        }
                    }
                }
                row = shpUserPermissionRefMapper.saveBatch(list);
                //?????????????????????,??????????????????;???????????????????????????;
                shpPermUserRefMapper.deletePermUserRefByShopIdAndUserId(shopId, userId);
                String tempKey = "shp:movePerm:" + shopId + ":" + userId;
                redisUtil.delete(tempKey);
            }
            //??????????????????;
            listUserPermissionByRedis(shopId, userId, true);
            return row;
        } catch (Exception e) {
            log.error("=======????????????????????????====: " + e.getMessage(), e);
            //??????try catch???????????????; ???????????????????????????????????????;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new MyException("??????????????????: " + e.getMessage());
        }
    }

    @Override
    public List<String> listUserPermissionByRedis(int shopId, int userId, boolean isUpdateRedis) {
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
                List<String> newPermList;
                //????????????????????????????????????;(??????????????????????????????)
                if (EnumShpUserType.BOSS.getCode().intValue() == userShopRef.getFkShpUserTypeId().intValue()) {
                    //???????????????????????????
                    permList = shpUserPermissionRefMapper.listBossPermission();
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
                //        //permList.removeIf(perm -> !perm.startsWith("9"));
                //    }
                //}
                ////????????????,?????????????????????;v267;????????????????????????,?????????????????????;?????????????????????;
                //permList = (List<String>) CollectionUtils.union(permList, newPermList);
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

    //@Override
    //public List<Integer> listShopUserIdWithPermission(int shopId, String permission) {
    //    List<Integer> pushUserIdList = shpUserPermissionRefMapper.listShopUserIdWithPermission(shopId, permission);
    //    //???????????????ID
    //    Integer ownerUserId = shpShopService.getBossUserIdByShopId(shopId);
    //    if (!pushUserIdList.contains(ownerUserId)) {
    //        pushUserIdList.add(ownerUserId);
    //    }
    //    return pushUserIdList;
    //}

    @Override
    public List<VoUserPermission> listUserPermByShopIdAndUserId(int shopId, int userId) {
        return shpUserPermissionRefMapper.listUserPermByShopIdAndUserId(shopId, userId);
    }

    @Override
    public List<String> listUserPermission(int shopId, int userId) {
        return shpUserPermissionRefMapper.listUserPermission(shopId, userId);
    }

    @Override
    public int deleteUserPermissionByUserId(int shopId, String userIds) {
        return shpUserPermissionRefMapper.deleteUserPermissionByUserId(shopId, userIds);
    }

    @Override
    public ShpUserPermissionRef packShpUserPermissionRef(int shopId, int userId, int permId, int insertUserId) {

        ShpUserPermissionRef ref = new ShpUserPermissionRef();
        ref.setFkShpUserId(userId);
        ref.setFkShpPermissionId(permId);
        ref.setFkShpShopId(shopId);
        ref.setInsertTime(new Date());
        ref.setInsertAdmin(insertUserId);
        ref.setVersions(1);
        ref.setDel("0");
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
                        List<ShpUserPermissionRef> refList = new ArrayList<>();
                        ArrayList<Object> queryPermIdList = new ArrayList<>();
                        for (VoRolePermissionRel perm : permIdList) {
                            Integer permId = perm.getId();
                            Object existsPermId = map.get(permId);
                            //????????????;
                            if (LocalUtils.isEmptyAndNull(existsPermId)) {
                                map.put(permId, permId);
                                queryPermIdList.add(permId);
                                // 3.???permId??????????????????(shp_user_permission_ref)
                                ShpUserPermissionRef shpUserPerm = packShpUserPermissionRef(shopId, userId, permId, 0);
                                refList.add(shpUserPerm);
                            }
                        }
                        //????????????
                        if (!LocalUtils.isEmptyAndNull(refList)) {
                            String queryPermIds = LocalUtils.packString(queryPermIdList.toArray());
                            int rows = shpUserPermissionRefMapper.existsShpUserPermissionRef(shopId, userId, queryPermIds);
                            if (rows == 0) {
                                shpUserPermissionRefMapper.saveBatch(refList);
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
        shpUserPermissionRefMapper.deleteShpUserPermissionRefByShopId(shopId);
    }

    @Override
    public int countModulePermission(int shopId, Integer userId, String moduleName) {
        return shpUserPermissionRefMapper.countModulePermission(shopId, userId, moduleName);
    }

    @Override
    public void changeUserPermission(int shopId, int oldUserId, int newUserId) {
        shpUserPermissionRefMapper.changeUserPermission(shopId, oldUserId, newUserId);
    }

    @Override
    public void updateObject(ShpUserPermissionRef shpUserPermissionRef) {
        shpUserPermissionRefMapper.updateObject(shpUserPermissionRef);
    }

    @Override
    public void addPermForAll(String hasPerm, String newPerm) {
        long st = System.currentTimeMillis();
        log.info("===================??????????????????????????????=======??????");
        //1.???????????????????????????;???????????????????????????????????????;
        List<ShpUserShopRef> listUser = shpUserShopRefService.listAllUserShopRefExceptBoss();
        List<ShpUserShopRef> userIdList = new ArrayList<>();
        List<ShpUserPermissionRef> refList = new ArrayList<>();
        for (ShpUserShopRef userShopRef : listUser) {
            Integer shopId = userShopRef.getFkShpShopId();
            Integer userId = userShopRef.getFkShpUserId();
            //2.????????????????????????????????????????????????(newPerm);
            int newPermRows = shpUserPermissionRefMapper.existsShpUserPermissionRef(shopId, userId, newPerm);
            if (newPermRows == 0) {
                //3.???????????????????????????????????????hasPerm??????,???in(${hasPerm})?????????;????????????,????????????????????????;
                int hasPermRows = shpUserPermissionRefMapper.existsShpUserPermissionRef(shopId, userId, hasPerm);
                if (hasPermRows > 0) {
                    //4.???????????????2?????????,?????????????????????newPerm??????,?????????redisPerm;
                    ShpUserPermissionRef shpUserPerm = packShpUserPermissionRef(shopId, userId, Integer.parseInt(newPerm), -9);
                    refList.add(shpUserPerm);
                    userIdList.add(userShopRef);
                }
            }
            if (refList.size() == 2000) {
                shpUserPermissionRefMapper.saveBatch(refList);
                refList.clear();
            }
        }
        //???????????????????????????????????????
        if (refList.size() > 0) {
            shpUserPermissionRefMapper.saveBatch(refList);
        }
        //??????????????????????????????????????????,???????????????;
        if (!LocalUtils.isEmptyAndNull(userIdList)) {
            for (ShpUserShopRef user : userIdList) {
                servicesUtil.updateRedisUserPerm(user.getFkShpShopId(), user.getFkShpUserId());
            }
        }
        long et = System.currentTimeMillis();
        log.info("===================??????????????????????????????=======??????,??????:{}", (et - st));
    }
}
