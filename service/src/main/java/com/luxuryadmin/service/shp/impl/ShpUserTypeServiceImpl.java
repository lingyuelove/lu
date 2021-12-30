package com.luxuryadmin.service.shp.impl;

import com.alibaba.fastjson.JSON;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.ThreadUtils;
import com.luxuryadmin.entity.shp.ShpUserType;
import com.luxuryadmin.mapper.shp.ShpUserTypeMapper;
import com.luxuryadmin.service.shp.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author monkey king
 * @date 2019-12-27 20:41:31
 */
@Slf4j
@Service
public class ShpUserTypeServiceImpl implements ShpUserTypeService {

    @Resource
    private ShpUserTypeMapper shpUserTypeMapper;

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    @Autowired
    private ShpUserPermissionRefService shpUserPermissionRefService;

    @Autowired
    private ShpUserPermissionTplService shpUserPermissionTplService;

    @Autowired
    private ShpPermUserRefService shpPermUserRefService;


    @Autowired
    private ShpPermTplService shpPermTplService;



    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<ShpUserType> listShpUserTypeByShopId(int shopId) {
        return shpUserTypeMapper.listShpUserTypeByShopId(shopId);
    }

    @Override
    public int initShpUserTypeByShopIdAndUserId(int shopId, int userId) {
        return shpUserTypeMapper.initShpUserTypeByShopIdAndUserId(shopId, userId);
    }

    @Override
    public Integer saveOrUpdateShpUserTypeAndReturnId(ShpUserType shpUserType) {
        Integer id = shpUserType.getId();
        if (LocalUtils.isEmptyAndNull(id)) {
            shpUserTypeMapper.saveObject(shpUserType);
            id = shpUserType.getId();
        } else {
            shpUserTypeMapper.updateObject(shpUserType);
        }
        return id;
    }

    @Override
    public boolean existsUserType(int shopId, String name) {
        return shpUserTypeMapper.existsUserType(shopId, name) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserType(int shopId, int userTypeId) {
        try {
            //1.先找到shp_user_shop_ref关联此身份的userId;
            List<Integer> userIdList = shpUserShopRefService.listUserIdByUserTypeId(shopId, userTypeId);
            if (!LocalUtils.isEmptyAndNull(userIdList)) {
                String userIds = LocalUtils.packString(userIdList.toArray());
                //2.根据步骤1的userId和shopId为条件,从shp_user_permission_ref中删除相应的数据;
                shpUserPermissionRefService.deleteUserPermissionByUserId(shopId, userIds);
            }
            //3.根据user_type_id和shopId为条件,从shp_user_permission_tlp中删除相应的数据;
            shpUserPermissionTplService.deleteShpUserPermissionTpl(shopId, userTypeId);
            //4.根据user_type_id和shopId为条件,从shp_user_type中删除相应的数据;
            shpUserTypeMapper.deleteUserType(shopId, userTypeId);
            //5.通知相关用户更新权限;把用户权限缓存里的need_update_perm改为1;后台线程更新;
            ThreadUtils.getInstance().executorService.execute(() -> {
                //2.把用户的缓存权限取出来,更改need_update_perm的值为1
                if (!LocalUtils.isEmptyAndNull(userIdList)) {
                    for (Integer userId : userIdList) {
                        String userPermKey = ConstantRedisKey.getPermKeyByShopIdUserId(shopId, userId);
                        String permJson = redisUtil.get(userPermKey);
                        if (!LocalUtils.isEmptyAndNull(permJson) && !ConstantCommon.NO_PERM.equals(permJson)) {
                            Map<String, Object> hashMap = (Map<String, Object>) JSON.parse(permJson);
                            hashMap.put(ConstantRedisKey.NEED_UPDATE_PERM, "1");
                            //重置有效时间
                            redisUtil.setEx(userPermKey, JSON.toJSONString(hashMap), 15);
                        }
                    }
                }
            });
        } catch (Exception e) {
            log.error("=======【删除身份模板】====: " + e.getMessage(), e);
            //对于try catch的方法里面; 事务要回滚必须显性操作回滚;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new MyException("删除身份模板: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserTypeNew(int shopId, int userTypeId) {
        try {
            //1.先找到shp_user_shop_ref关联此身份的userId;
            List<Integer> userIdList = shpUserShopRefService.listUserIdByUserTypeId(shopId, userTypeId);
            if (!LocalUtils.isEmptyAndNull(userIdList)) {
                String userIds = LocalUtils.packString(userIdList.toArray());
                //2.根据步骤1的userId和shopId为条件,从shp_user_permission_ref中删除相应的数据;
                shpPermUserRefService.deleteShpPermUserRefByUserId(shopId, userIds);
            }
            //3.根据user_type_id和shopId为条件,从shp_user_permission_tlp中删除相应的数据;
            shpPermTplService.deleteShpUserPermTpl(shopId, userTypeId);
            //4.根据user_type_id和shopId为条件,从shp_user_type中删除相应的数据;
            shpUserTypeMapper.deleteUserType(shopId, userTypeId);
            //5.通知相关用户更新权限;把用户权限缓存里的need_update_perm改为1;后台线程更新;
            ThreadUtils.getInstance().executorService.execute(() -> {
                //2.把用户的缓存权限取出来,更改need_update_perm的值为1
                if (!LocalUtils.isEmptyAndNull(userIdList)) {
                    for (Integer userId : userIdList) {
                        String userPermKey = ConstantRedisKey.getPermKeyByShopIdUserId(shopId, userId);
                        String permJson = redisUtil.get(userPermKey);
                        if (!LocalUtils.isEmptyAndNull(permJson) && !ConstantCommon.NO_PERM.equals(permJson)) {
                            Map<String, Object> hashMap = (Map<String, Object>) JSON.parse(permJson);
                            hashMap.put(ConstantRedisKey.NEED_UPDATE_PERM, "1");
                            //重置有效时间
                            redisUtil.setEx(userPermKey, JSON.toJSONString(hashMap), 15);
                        }
                    }
                }
            });
        } catch (Exception e) {
            log.error("=======【删除身份模板(新)】====: " + e.getMessage(), e);
            //对于try catch的方法里面; 事务要回滚必须显性操作回滚;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new MyException("删除身份模板(新): " + e.getMessage());
        }
    }

    @Override
    public ShpUserType packShpUserTypeToSave(int shopId, int insertUserId, String name, String description, int sort, String remark) {
        ShpUserType userType = new ShpUserType();
        userType.setName(name);
        userType.setFkShpShopId(shopId);
        userType.setDescription(description);
        userType.setType("0");
        userType.setState(1);
        userType.setSort(sort);
        userType.setInsertTime(new Date());
        userType.setInsertAdmin(insertUserId);
        userType.setRemark(remark);
        return userType;
    }

    @Override
    public ShpUserType getShpUserTypeByShopIdAndTypeId(int shopId, int userTypeId) {
        return shpUserTypeMapper.selectShpUserTypeByShopIdAndTypeId(shopId, userTypeId);
    }

    @Override
    public void sortUserType(List<ShpUserType> list, int shopId) {
        shpUserTypeMapper.sortUserType(list, shopId);
    }
}
