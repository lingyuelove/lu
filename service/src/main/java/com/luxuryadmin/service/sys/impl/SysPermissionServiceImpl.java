package com.luxuryadmin.service.sys.impl;

import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.op.OpSmsRecord;
import com.luxuryadmin.entity.shp.ShpPermission;
import com.luxuryadmin.entity.sys.SysPermission;
import com.luxuryadmin.entity.sys.SysRolePermissionRef;
import com.luxuryadmin.mapper.sys.SysConfigMapper;
import com.luxuryadmin.mapper.sys.SysPermissionMapper;
import com.luxuryadmin.mapper.sys.SysRolePermissionRefMapper;
import com.luxuryadmin.param.sys.ParamSysPermDelete;
import com.luxuryadmin.service.sys.SysPermissionService;
import com.luxuryadmin.vo.shp.VoShpPermission;
import com.luxuryadmin.vo.sys.VoSysPermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.Permission;
import java.util.*;

/**
 * 后台--权限菜单
 *
 * @author monkey king
 * @date 2020-05-25 16:57:30
 */
@Slf4j
@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    @Resource
    private SysRolePermissionRefMapper sysRolePermissionRefMapper;

    @Override
    public int saveSysPermission(SysPermission sysPermission) {
        sysPermissionMapper.saveObject(sysPermission);
        SysRolePermissionRef permissionRef = new SysRolePermissionRef();
        permissionRef.setFkSysRoleId(10001);
        permissionRef.setFkSysPermissionId(sysPermission.getId());
        permissionRef.setDel("0");
        permissionRef.setVersions(0);
        permissionRef.setInsertAdmin(sysPermission.getInsertAdmin());
        sysRolePermissionRefMapper.saveObject(permissionRef);
        return sysPermission.getId();
    }

    @Override
    public int updateSysPermission(SysPermission sysPermission) {

        sysPermissionMapper.updateObject(sysPermission);
        return sysPermission.getId();
    }

    @Override
    public List<SysPermission> selectListByPath(String requestUrl) {
        return sysPermissionMapper.listByPath(requestUrl);
    }

    @Override
    public List<SysPermission> selectListByUser(Integer id) {
        return sysPermissionMapper.listByUser(id);
    }

    @Override
    public boolean existsSysPermissionName(int parentId, String name,int id) {
        return sysPermissionMapper.existsSysPermissionName(parentId, name,id) > 0;
    }

    @Override
    public boolean existsSysPermissionCode(String code,int id) {
        return sysPermissionMapper.existsSysPermissionCode(code,id) > 0;
    }

    @Override
    public boolean existsSysPermission(String permission, int id) {
        return  sysPermissionMapper.existsSysPermission(permission,id) > 0;
    }

    @Override
    public SysPermission getSysPermissionById(Integer id) {
        return (SysPermission) sysPermissionMapper.getObjectById(id);
    }

    @Override
    public int saveOrUpdateSysPermission(SysPermission sysPermission) {
        if (sysPermission.getId() != null) {
            sysPermissionMapper.updateObject(sysPermission);
        } else {
            sysPermissionMapper.saveObject(sysPermission);
        }
        return sysPermission.getId();
    }

    @Override
    public List<VoSysPermission> listSysPermission() {
        return sysPermissionMapper.listAppSysPermission();
    }

    @Override
    public int deleteSysPermissions(String inSql) {
        return sysPermissionMapper.deleteObjects(inSql);
    }

    @Override
    public int deleteSysPerm(ParamSysPermDelete sysPermDelete) {

        List<String> idList = Arrays.asList(sysPermDelete.getId().split(","));
        if (LocalUtils.isEmptyAndNull(idList) && idList.size() <= 0) {
            return 0;
        }
        idList.forEach(s -> {
            SysPermission sysPermission = (SysPermission) sysPermissionMapper.getObjectById(Integer.parseInt(s));
            if (sysPermission != null) {
                sysPermissionMapper.deleteObject(Integer.parseInt(s));
                // 该节点可能是子节点但也可能是其它节点的父节点,所以需要级联删除
                this.removeChildrenBy(sysPermission.getId());
                //删除角色授权表
                sysRolePermissionRefMapper.deleteRoleByPermissionId(Integer.parseInt(s));
            }
        });

        return 0;
    }

    /**
     * 根据父id删除其关联的子节点数据
     *
     * @return
     */
    public void removeChildrenBy(Integer parentId) {

        // 封装查询条件parentId为主键,
        // 查出该主键下的所有子级
        List<SysPermission> permissionList = sysPermissionMapper.getByParentId(parentId);
        if (LocalUtils.isEmptyAndNull(permissionList) || permissionList.size() <= 0) {
            return;
        }
        // id
        // 查出的子级数量
        int num = 0;
        // 如果查出的集合不为空, 则先删除所有
        sysPermissionMapper.deleteByParentId(parentId);
        // 再遍历刚才查出的集合, 根据每个对象,查找其是否仍有子级
        for (int i = 0, len = permissionList.size(); i < len; i++) {
            Integer id = permissionList.get(i).getId();
            //删除角色授权表
            sysRolePermissionRefMapper.deleteRoleByPermissionId(id);
            num =  sysPermissionMapper.getCountByParentId(id);
            // 如果有, 则递归
            if (num > 0) {
                this.removeChildrenBy(id);
            }
        }
    }

    @Override
    public  Boolean getPermissionByUserAndUrl(String userName,String[] strings, String logical ) {
//        int userId = servicesUtil.getShpUserIdByToken(token);

        List<String> shpPermission = sysPermissionMapper.getPermissionByUserAndUrl(userName);
        //防止以后业务逻辑的新增 就在此之后进行
        if (shpPermission == null && shpPermission.size() <=0) {
            return true;
        }
        Boolean permission = true;
        if (logical.equals("AND")) {
            for (String string : strings) {
                if (!shpPermission.contains(string)) {
                    permission = false;
                    break;
                }
            }
        } else {
            permission = false;
            for (String string : strings) {
                if (shpPermission.contains(string)) {
                    permission =true;
                    break;
                }
            }
        }
        if (!permission) {
            throw new MyException(EnumCode.ERROR_NO_PERMISSION);
        }
        return Boolean.TRUE;
    }

    @Override
    public List<VoSysPermission> queryByUser(String username) {
        List<VoSysPermission> voSysPermissions = sysPermissionMapper.queryByUser(Integer.parseInt(username));
        //获取一级权限
        List<VoSysPermission> firstPermList = new ArrayList<>();
        for (VoSysPermission allPerm : voSysPermissions) {
            int firstParent = allPerm.getParentId();
            if (firstParent == 0) {
                firstPermList.add(allPerm);
            }
        }
        //遍历一级权限;找到其子菜单;即二级菜单
        for (VoSysPermission firstPerm : firstPermList) {
            List<VoSysPermission> secondPermList = new ArrayList<>();
            //一级菜单的id,即为二级菜单的父节点
            Integer firstPermId = firstPerm.getId();
            HashMap<String, Object> secondMap = new HashMap<>(16);
            //遍历所有权限;
            for (VoSysPermission allPerm : voSysPermissions) {
                int secondParentId = allPerm.getParentId();
                if (firstPermId == secondParentId) {
                    secondPermList.add(allPerm);
                }
            }

            for (VoSysPermission secondPerm : secondPermList) {
                List<VoSysPermission> thirdPermList = new ArrayList<>();
                //二级菜单的id,即为三级菜单的父节点
                Integer secondPermId = secondPerm.getId();
                HashMap<String, Object> thirdMap = new HashMap<>(16);
                //遍历所有权限;
                for (VoSysPermission allPerm : voSysPermissions) {
                    int thirdParentId = allPerm.getParentId();
                    if (secondPermId == thirdParentId) {
                        thirdPermList.add(allPerm);
                    }
                }
                if (!LocalUtils.isEmptyAndNull(thirdPermList)) {
                    secondPerm.setPermissionList(thirdPermList);
                }
            }

            if (!LocalUtils.isEmptyAndNull(secondPermList)) {
                firstPerm.setPermissionList(secondPermList);
            }
        }
        return firstPermList;
    }

    @Override
    public List<VoSysPermission> groupBySysPermission() {
        List<VoSysPermission> voSysPermissions = listSysPermission();
        //获取一级权限
        List<VoSysPermission> firstPermList = new ArrayList<>();
        for (VoSysPermission allPerm : voSysPermissions) {
            int firstParent = allPerm.getParentId();
            if (firstParent == 0) {
                firstPermList.add(allPerm);
            }
        }

        //遍历一级权限;找到其子菜单;即二级菜单
        for (VoSysPermission firstPerm : firstPermList) {
            List<VoSysPermission> secondPermList = new ArrayList<>();
            //一级菜单的id,即为二级菜单的父节点
            Integer firstPermId = firstPerm.getId();
            HashMap<String, Object> secondMap = new HashMap<>(16);
            //遍历所有权限;
            for (VoSysPermission allPerm : voSysPermissions) {
                int secondParentId = allPerm.getParentId();
                if (firstPermId == secondParentId) {
                    secondPermList.add(allPerm);
                }
            }

            for (VoSysPermission secondPerm : secondPermList) {
                List<VoSysPermission> thirdPermList = new ArrayList<>();
                //二级菜单的id,即为三级菜单的父节点
                Integer secondPermId = secondPerm.getId();
                HashMap<String, Object> thirdMap = new HashMap<>(16);
                //遍历所有权限;
                for (VoSysPermission allPerm : voSysPermissions) {
                    int thirdParentId = allPerm.getParentId();
                    if (secondPermId == thirdParentId) {
                        thirdPermList.add(allPerm);
                    }
                }
                if (!LocalUtils.isEmptyAndNull(thirdPermList)) {
                    secondPerm.setPermissionList(thirdPermList);
                }
            }

            if (!LocalUtils.isEmptyAndNull(secondPermList)) {
                firstPerm.setPermissionList(secondPermList);
            }
        }

        return firstPermList;
    }
}
