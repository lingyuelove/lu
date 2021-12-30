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
            //新增员工之前,查找该员工是否曾在此店就职过;确保每个人在每个店只有一条记录;
            ShpUserShopRef userShopRef = shpUserShopRefService.getShpUserShopRefByUserId(shopId, userId);
            if (LocalUtils.isEmptyAndNull(userShopRef)) {
                //新增员工
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
            //清空用户所有权限;重置
            shpPermUserRefMapper.deletePermUserRefByShopIdAndUserId(shopId, userId);
            if (!LocalUtils.isEmptyAndNull(list)) {
                ShpShop shpShop = shpShopService.getShpShopById(shopId + "");


                //隐藏的权限;v2.6.1;商家联盟
                String userPerms = servicesUtil.getUserPerms(shopId, userId);
                String unionPerm = "shop:union:show";
                //排除经营者本身在操作,如果是经营者在操作, 则按照正常流程走;因为经营者允许看到商家联盟权限
                if (shpShop.getFkShpUserId() != updateUserId && userPerms.contains(unionPerm)) {
                    ShpPermission sp = shpPermissionService.getShpPermissionByPermission(unionPerm);
                    if (null != sp) {
                        //端上已经为其赋值则不再需要重新赋值;
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
            //更新权限缓存;
            listPermUserByRedis(shopId, userId, true);
        } catch (Exception e) {
            log.error("=======【人员授权失败】====: " + e.getMessage(), e);
            //对于try catch的方法里面; 事务要回滚必须显性操作回滚;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception();
        }
    }

    @Override
    public void refreshOldPerm(int shopId, int userId, List<ShpPermUserRef> list) {
        //新版权限更新时,去掉旧版权限;让旧权限重新更新;
        shpUserPermissionRefMapper.deleteUserPermRefByShopIdAndUserId(shopId, userId);
        //1.根据新版权限的id,找到新版权限的授权码;
        List<Integer> permIdList = new ArrayList<>();
        for (ShpPermUserRef ref : list) {
            permIdList.add(ref.getFkShpPermIndexId());
        }
        String newPermIds = LocalUtils.packString(permIdList.toArray());
        List<String> permCodeList = shpPermIndexMapper.listPermCodeByPermIds(newPermIds);
        //2.根据新版权限的授权码,去查找旧版权限的权限id
        if (!LocalUtils.isEmptyAndNull(permCodeList)) {
            Object[] codeArray = permCodeList.toArray();

            List<Integer> oldPermIds = shpPermissionMapper.listPermIdByPermCode(LocalUtils.packString(codeArray));
            if (!LocalUtils.isEmptyAndNull(oldPermIds)) {
                List<ShpUserPermissionRef> oldList = new ArrayList<>();
                //3.把旧版权限的id重新添加到旧版权限的引用关系表里;
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
        //用户权限缓存
        String shpUserPermKey = ConstantRedisKey.getPermKeyByShopIdUserId(shopId, userId);
        //全局缓存版本号
        String permVersionKey = ConstantRedisKey.SHP_PERM_VERSION;
        Object permVersionValue = redisUtil.get(permVersionKey);

        String needUpdateKey = ConstantRedisKey.NEED_UPDATE_PERM;
        Map<String, Object> hashMap = new HashMap<>(16);
        List<String> permList = new ArrayList<>();
        String shpUserPermValue = redisUtil.get(shpUserPermKey);
        if (!isUpdateRedis && !LocalUtils.isEmptyAndNull(shpUserPermValue)) {
            //json数据格式开头
            if (shpUserPermValue.startsWith("{")) {
                hashMap = (Map<String, Object>) JSON.parse(shpUserPermValue);
                Object ownPermVersion = hashMap.get(permVersionKey);
                Object needUpdateValue = hashMap.get(needUpdateKey);
                //校验版本号;全局店铺版本号,个人更新版本号; 版本号一致,则不需要更新缓存
                if (permVersionValue.equals(ownPermVersion) && ConstantCommon.ZERO.equals(needUpdateValue)) {
                    //有权限
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
        //需要更新权限缓存
        if (isUpdateRedis) {
            ShpUserShopRef userShopRef = shpUserShopRefService.getShpUserShopRefByUserId(shopId, userId);
            if (!LocalUtils.isEmptyAndNull(userShopRef)) {
                List<String> permList2;
                if (EnumShpUserType.BOSS.getCode().intValue() == userShopRef.getFkShpUserTypeId().intValue()) {
                    //经营者拥有所有权限
                    permList = shpPermUserRefMapper.listBossPermission();
                } else {
                    permList = listUserPermission(shopId, userId);
                }
                ////获取之前的权限
                //if (!LocalUtils.isEmptyAndNull(shpUserPermValue)) {
                //    hashMap = (Map<String, Object>) JSON.parse(shpUserPermValue);
                //    Object permObj = hashMap.get("permList");
                //    if (!ConstantCommon.NO_PERM.equals(permObj)) {
                //        permList = (List<String>) permObj;
                //        //把新版权限剥离出来;只更新新版权限;
                //        //permList.removeIf(perm -> perm.startsWith("9"));
                //    }
                //}
                ////新旧权限,做合并去重处理;v267;迭代若干版本之后,可以去掉此代码;不需要合并去重;
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
        //重置有效时间
        redisUtil.setEx(shpUserPermKey, JSON.toJSONString(hashMap), 15);
        return permList;
    }

    @Override
    public List<Integer> listShopUserIdWithPermission(int shopId, String permission) {
        List<Integer> pushUserIdList = shpPermUserRefMapper.listShopUserIdWithPermission(shopId, permission);
        //经营者用户ID
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
            // 1.找到此用户之前所关联到的角色;
            List<VoUserRoleRef> userRoleList = shpUserRoleRefService.listUserRoleRefByUserId(shopId, userId);
            String moveUser = shopId + "_" + userId;
            if (movePermUserValue.contains(moveUser)) {
                return;
            }
            movePermUserValue += moveUser + ",";
            redisUtil.set(movePermUserKey, movePermUserValue);
            if (!LocalUtils.isEmptyAndNull(userRoleList)) {
                log.info("==============迁移权限====shopId: {}, userId: {}=========", shopId, userId);
                HashMap<Object, Object> map = new HashMap<>(16);
                String userTypeName = "";
                String tempName = "临时";
                int userTypeNum;
                int maxNum = 0;
                for (VoUserRoleRef userRoleRef : userRoleList) {
                    Integer roleId = userRoleRef.getRoleId();
                    String roleName = userRoleRef.getRoleName();
                    //如果是系统默认模板则使用默认名称;
                    switch (roleName) {
                        case "店长":
                            userTypeNum = 3;
                            break;
                        case "员工":
                            userTypeNum = 2;
                            break;
                        case "代理":
                            userTypeNum = 1;
                            break;
                        default:
                            userTypeNum = 0;
                    }
                    if (maxNum <= userTypeNum) {
                        maxNum = userTypeNum;
                        switch (maxNum) {
                            case 3:
                                userTypeName = "店长";
                                break;
                            case 2:
                                userTypeName = "员工";
                                break;
                            case 1:
                                userTypeName = "代理";
                                break;
                            default:
                                userTypeName = tempName;
                        }
                    }
                    // 2.找到此角色对应的权限permId;(原权限表shp_role_permission_ref)
                    List<VoRolePermissionRel> permIdList = shpRolePermissionRefService.listRolePermsRelByRoleId(shopId, roleId);
                    if (!LocalUtils.isEmptyAndNull(permIdList)) {
                        List<ShpPermUserRef> refList = new ArrayList<>();
                        ArrayList<Object> queryPermIdList = new ArrayList<>();
                        for (VoRolePermissionRel perm : permIdList) {
                            Integer permId = perm.getId();
                            Object existsPermId = map.get(permId);
                            //权限去重;
                            if (LocalUtils.isEmptyAndNull(existsPermId)) {
                                map.put(permId, permId);
                                queryPermIdList.add(permId);
                                // 3.把permId存到新权限表(shp_user_permission_ref)
                                ShpPermUserRef shpUserPerm = packShpPermUserRef(shopId, userId, permId, 0);
                                refList.add(shpUserPerm);
                            }
                        }
                        //绑定权限
                        if (!LocalUtils.isEmptyAndNull(refList)) {
                            String queryPermIds = LocalUtils.packString(queryPermIdList.toArray());
                            int rows = shpPermUserRefMapper.existsShpUserPermissionRef(shopId, userId, queryPermIds);
                            if (rows == 0) {
                                shpPermUserRefMapper.saveBatch(refList);
                            }
                        }
                    }
                }
                //查找该用户在店铺的关系
                ShpUserShopRef shpUserShopRef = shpUserShopRefService.getShpUserShopRefByUserId(shopId, userId);
                int userTypeId;
                //把旧身份迁移到新身份
                List<ShpUserType> userTypeList = shpUserTypeService.listShpUserTypeByShopId(shopId);
                boolean isSame = false;
                ShpUserType dbUserType = null;
                if (!LocalUtils.isEmptyAndNull(userTypeList)) {
                    for (ShpUserType userType : userTypeList) {
                        String dbUserTypeName = userType.getName();
                        if (dbUserTypeName.equals(userTypeName)) {
                            //用户旧身份和新身份一致,则直接更新到新身份id
                            isSame = true;
                            dbUserType = userType;
                            break;
                        }
                    }
                }
                if (isSame) {
                    userTypeId = dbUserType.getId();
                } else {
                    //为店铺新添加一个叫"临时"的身份
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
            log.error("======新旧权限替换====={}", e.getMessage(), e);
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
                    //添加分享模块
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
                        //赋予仓库权限;
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
            //清空用户所有权限;重置
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
            log.info("=========第{}个{}========", i, tempKey);
        }
        long et = System.currentTimeMillis();
        log.info("=======初始化所有新版权限结束====耗时：{}", et - st);
    }


    /**
     * 新旧权限合并,新权限为第一个,多个权限用逗号隔开;
     *
     * @return
     */
    private void turnOldPermToNewPerm(int shopId, int userId) {
        String tempKey = "shp:movePerm:" + shopId + ":" + userId;
        String tempValue = redisUtil.get(tempKey);
        log.info("======{}======", tempKey);
        if (!LocalUtils.isEmptyAndNull(tempValue)) {
            //已经迁移过了权限, 不需要再次执行
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
            //更新权限缓存;
            listPermUserByRedis(shopId, userId, true);
        }
    }
}
