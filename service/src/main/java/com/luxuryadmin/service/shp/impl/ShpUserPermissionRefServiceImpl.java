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
            int row = 0;
            //清空用户所有权限;重置
            shpUserPermissionRefMapper.deleteUserPermRefByShopIdAndUserId(shopId, userId);
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
                //旧版权限更新时,去掉新版权限;让新版权限重新更新;
                shpPermUserRefMapper.deletePermUserRefByShopIdAndUserId(shopId, userId);
                String tempKey = "shp:movePerm:" + shopId + ":" + userId;
                redisUtil.delete(tempKey);
            }
            //更新权限缓存;
            listUserPermissionByRedis(shopId, userId, true);
            return row;
        } catch (Exception e) {
            log.error("=======【人员授权失败】====: " + e.getMessage(), e);
            //对于try catch的方法里面; 事务要回滚必须显性操作回滚;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new MyException("注册用户失败: " + e.getMessage());
        }
    }

    @Override
    public List<String> listUserPermissionByRedis(int shopId, int userId, boolean isUpdateRedis) {
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
                List<String> newPermList;
                //此处需要考虑空权限的处理;(把之前的权限全部处理)
                if (EnumShpUserType.BOSS.getCode().intValue() == userShopRef.getFkShpUserTypeId().intValue()) {
                    //经营者拥有所有权限
                    permList = shpUserPermissionRefMapper.listBossPermission();
                } else {
                    permList = listUserPermission(shopId, userId);
                }
                ////获取之前的权限
                //if (!LocalUtils.isEmptyAndNull(shpUserPermValue)) {
                //    hashMap = (Map<String, Object>) JSON.parse(shpUserPermValue);
                //    Object permObj = hashMap.get("permList");
                //    if (!ConstantCommon.NO_PERM.equals(permObj)) {
                //        permList = (List<String>) permObj;
                //        //把旧版权限剥离出来;只更新旧版权限;
                //        //permList.removeIf(perm -> !perm.startsWith("9"));
                //    }
                //}
                ////新旧权限,做合并去重处理;v267;迭代若干版本之后,可以去掉此代码;不需要合并去重;
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
        //重置有效时间
        redisUtil.setEx(shpUserPermKey, JSON.toJSONString(hashMap), 15);
        return permList;
    }

    //@Override
    //public List<Integer> listShopUserIdWithPermission(int shopId, String permission) {
    //    List<Integer> pushUserIdList = shpUserPermissionRefMapper.listShopUserIdWithPermission(shopId, permission);
    //    //经营者用户ID
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
                        List<ShpUserPermissionRef> refList = new ArrayList<>();
                        ArrayList<Object> queryPermIdList = new ArrayList<>();
                        for (VoRolePermissionRel perm : permIdList) {
                            Integer permId = perm.getId();
                            Object existsPermId = map.get(permId);
                            //权限去重;
                            if (LocalUtils.isEmptyAndNull(existsPermId)) {
                                map.put(permId, permId);
                                queryPermIdList.add(permId);
                                // 3.把permId存到新权限表(shp_user_permission_ref)
                                ShpUserPermissionRef shpUserPerm = packShpUserPermissionRef(shopId, userId, permId, 0);
                                refList.add(shpUserPerm);
                            }
                        }
                        //绑定权限
                        if (!LocalUtils.isEmptyAndNull(refList)) {
                            String queryPermIds = LocalUtils.packString(queryPermIdList.toArray());
                            int rows = shpUserPermissionRefMapper.existsShpUserPermissionRef(shopId, userId, queryPermIds);
                            if (rows == 0) {
                                shpUserPermissionRefMapper.saveBatch(refList);
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
        log.info("===================执行赋予所有用户权限=======开始");
        //1.找到店铺的员工关系;除去经营者和除去已经离职的;
        List<ShpUserShopRef> listUser = shpUserShopRefService.listAllUserShopRefExceptBoss();
        List<ShpUserShopRef> userIdList = new ArrayList<>();
        List<ShpUserPermissionRef> refList = new ArrayList<>();
        for (ShpUserShopRef userShopRef : listUser) {
            Integer shopId = userShopRef.getFkShpShopId();
            Integer userId = userShopRef.getFkShpUserId();
            //2.判断该员工是否已经拥有新加的权限(newPerm);
            int newPermRows = shpUserPermissionRefMapper.existsShpUserPermissionRef(shopId, userId, newPerm);
            if (newPermRows == 0) {
                //3.查询该员工在此店铺是否拥有hasPerm权限,用in(${hasPerm})来查询;如果拥有,则符合添加新权限;
                int hasPermRows = shpUserPermissionRefMapper.existsShpUserPermissionRef(shopId, userId, hasPerm);
                if (hasPermRows > 0) {
                    //4.如果有步骤2的权限,则给此员工增加newPerm权限,并更新redisPerm;
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
        //将剩下的用户权限一次性保存
        if (refList.size() > 0) {
            shpUserPermissionRefMapper.saveBatch(refList);
        }
        //用户所有权限更新到数据库之后,再更新缓存;
        if (!LocalUtils.isEmptyAndNull(userIdList)) {
            for (ShpUserShopRef user : userIdList) {
                servicesUtil.updateRedisUserPerm(user.getFkShpShopId(), user.getFkShpUserId());
            }
        }
        long et = System.currentTimeMillis();
        log.info("===================执行赋予所有用户权限=======结束,耗时:{}", (et - st));
    }
}
