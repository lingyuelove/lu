package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.common.utils.ThreadUtils;
import com.luxuryadmin.entity.shp.ShpRole;
import com.luxuryadmin.entity.shp.ShpRolePermissionRef;
import com.luxuryadmin.mapper.shp.ShpRoleMapper;
import com.luxuryadmin.service.shp.*;
import com.luxuryadmin.vo.shp.VoShpRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author monkey king
 * @date 2019-12-30 15:06:39
 */
@Slf4j
@Service
public class ShpRoleServiceImpl implements ShpRoleService {

    @Resource
    private ShpRoleMapper shpRoleMapper;

    @Autowired
    private ShpRolePermissionRefService shpRolePermissionRefService;

    @Autowired
    private ShpPermissionService shpPermissionService;

    @Autowired
    private ShpUserRoleRefService shpUserRoleRefService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ServicesUtil servicesUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveOrUpdateShpRole(ShpRole shpRole, Integer[] permIds, boolean isSave) {
        try {
            Integer roleId = shpRole.getId();
            int shopId = shpRole.getFkShpShopId();
            if (!isSave) {
                shpRoleMapper.updateObject(shpRole);
                //修改角色权限之前, 先删除之前的权限;
                shpRolePermissionRefService.deleteShpRolePermissionRef(shopId, roleId);
            } else {
                shpRoleMapper.saveObject(shpRole);
            }
            if (!LocalUtils.isEmptyAndNull(permIds)) {
                //角色附带权限;
                List<ShpRolePermissionRef> list = new ArrayList<>();
                for (Integer permId : permIds) {
                    ShpRolePermissionRef rolePermRef = new ShpRolePermissionRef();
                    rolePermRef.setFkShpRoleId(shpRole.getId());
                    rolePermRef.setFkShpPermissionId(permId);
                    rolePermRef.setFkShpShopId(shopId);
                    rolePermRef.setInsertTime(new Date());
                    rolePermRef.setInsertAdmin(shpRole.getInsertAdmin());
                    rolePermRef.setVersions(1);
                    rolePermRef.setDel("0");
                    list.add(rolePermRef);
                }
                shpRolePermissionRefService.saveBatchShpRolePermissionRef(list);
            }

            if (!isSave) {
                //1.找到拥有此角色的用户;
                List<Integer> userIdList = shpRoleMapper.listUserIdByRoleId(shopId, roleId);
                //启用多线程去执行,不影响前端操作
                //ThreadUtils.getInstance().executorService.execute(() -> {
                //    //更新所有有此角色的用户的常用功能;
                //    if (!LocalUtils.isEmptyAndNull(userIdList)) {
                //        //2.查看此用户的全部功能;
                //        for (Integer userId : userIdList) {
                //            //3.全部功能和常用功能对比,把不存在于全部功能里面的常用功能给剔除;
                //            String allFuncPermId = shpUsualFunctionService.listAllFunctionPermId(shopId, userId);
                //            if (!LocalUtils.isEmptyAndNull(allFuncPermId)) {
                //                shpUsualFunctionService.removeFuncAndPermIdNotExists(shopId, allFuncPermId);
                //            }
                //
                //        }
                //    }
                //});
                //更新用户缓存;把用户权限缓存里的need_update_perm改为1;后台线程更新;
                ThreadUtils.getInstance().executorService.execute(() -> {
                    //2.把用户的缓存权限取出来,更改need_update_perm的值为1
                    if (!LocalUtils.isEmptyAndNull(userIdList)) {
                        for (Integer userId : userIdList) {
                            servicesUtil.updateRedisUserPerm(shopId, userId);
                        }
                    }

                });

            }
            return shpRole.getId();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            //对于try catch的方法里面; 事务要回滚必须显性操作回滚;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new MyException("创建角色失败!");
        }
    }

    @Override
    public int updateShpRole(ShpRole shpRole) {
        return shpRoleMapper.updateObject(shpRole);
    }

    @Override
    public List<VoShpRole> listShpRole(int shopId) {
        return shpRoleMapper.listShpRole(shopId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteShopRole(int shopId, int userId, int roleId) {
        try {
            //1.删除角色拥有的权限
            shpRolePermissionRefService.deleteShpRolePermissionRef(shopId, roleId);
            //2.删除角色与用户的关系
            shpUserRoleRefService.deleteUserRoleRef(shopId, userId, roleId);
            //3.删除角色
            return shpRoleMapper.deleteShopRole(shopId, roleId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            //对于try catch的方法里面; 事务要回滚必须显性操作回滚;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new MyException("删除角色失败!");
        }
    }

    @Override
    public int countRoleNum(int shopId) {
        return shpRoleMapper.countRoleNum(shopId);
    }

    @Override
    public boolean existsShpRole(int shopId, int roleId) {
        return shpRoleMapper.existsShpRole(shopId, roleId) > 0;
    }

    @Override
    public List<Integer> listUserIdByRoleId(int shopId, int roleId) {
        return shpRoleMapper.listUserIdByRoleId(shopId, roleId);
    }

    @Override
    public void deleteSysDefaultRole(int shopId) {
        shpRoleMapper.deleteSysDefaultRole(shopId);
    }

    @Override
    public List<Integer> listRoleIdByShopId(int shopId) {
        return shpRoleMapper.listRoleIdByShopId(shopId);
    }

    @Override
    public boolean isSysCreateRole(int shopId, int roleId) {
        return shpRoleMapper.isSysCreateRole(shopId, roleId) > 0;
    }
}
