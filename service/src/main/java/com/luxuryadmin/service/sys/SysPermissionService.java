package com.luxuryadmin.service.sys;

import com.luxuryadmin.entity.shp.ShpPermission;
import com.luxuryadmin.entity.sys.SysPermission;
import com.luxuryadmin.param.sys.ParamSysPermDelete;
import com.luxuryadmin.vo.shp.VoShpPermission;
import com.luxuryadmin.vo.sys.VoSysPermission;

import java.security.Permission;
import java.util.List;

/**
 * 后台--权限菜单
 *
 * @author monkey king
 * @date 2020-05-25 16:55:36
 */
public interface SysPermissionService {

    /**
     * 新增或者更新实体;根据ID;
     * @param sysPermission
     * @return 返回ID;
     */
    int saveOrUpdateSysPermission(SysPermission sysPermission);
    /**
     * 根据当前用户id和路径查询是否拥有此权限
     * @param userId
     * @param url
     * @return
     */
    Boolean getPermissionByUserAndUrl(String userName ,String[] strings, String logical );

    /**
     * 查询当前用户所拥有的权限
     * @param username
     * @return
     */
    List<VoSysPermission> queryByUser(String username);
    /**
     * 对查询出来的权限进行分组归类
     *
     * @return
     */
    List<VoSysPermission> groupBySysPermission();

    /**
     * 查询权限列表
     * @return
     */
    List<VoSysPermission> listSysPermission();

    /**
     * 权限删除 - 删除多个
     * @param inSql
     * @return
     */
    int deleteSysPermissions(String inSql);

    /**
     * 新权限删除 - 删除多个
     * @param inSql
     * @return
     */
    int deleteSysPerm(ParamSysPermDelete sysPermDelete);
    /**
     * 该权限是否存在
     *
     * @param parentId
     * @param name
     * @return
     */
    boolean existsSysPermissionName(int parentId, String name,int id);

    /**
     * 该code值是否存在
     * @param code
     * @param id
     * @return
     */
    boolean existsSysPermissionCode(String code,int id);

    /**
     * 该权限是否存在
     * @param permission
     * @param id
     * @return
     */
    boolean existsSysPermission(String permission,int id);
    /**
     * 添加权限;返回该实体id
     *
     * @param perm
     * @return
     */
    int saveSysPermission(SysPermission perm);

    /**
     * 根据ID获取实体
     *
     * @param id 主键id
     * @return
     */
    SysPermission getSysPermissionById(Integer id);

    /**
     * 更新权限;返回该实体id
     *
     * @param perm
     * @return
     */
    int updateSysPermission(SysPermission perm);

    /**
     * 查询具体某个接口的权限
     * @param requestUrl
     * @return
     */
    List<SysPermission> selectListByPath(String requestUrl);

    /**
     * 获取该用户所拥有的权限
     * @param id
     * @return
     */
    List<SysPermission> selectListByUser(Integer id);
}
