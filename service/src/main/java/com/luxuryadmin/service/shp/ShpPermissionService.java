package com.luxuryadmin.service.shp;

import com.luxuryadmin.entity.shp.ShpPermission;
import com.luxuryadmin.vo.shp.VoShpPermission;
import com.luxuryadmin.vo.shp.VoUsualFunction;

import java.util.List;

/**
 * 店铺权限业务逻辑层--改店铺模块的权限. 对所有店铺通用;
 *
 * @author monkey king
 * @date 2019-12-30 16:40:22
 */
public interface ShpPermissionService {

    /**
     * 添加权限;返回该实体id
     *
     * @param shpPermission
     * @return
     */
    int saveShpPermission(ShpPermission shpPermission);

    /**
     * 更新权限;返回该实体id
     *
     * @param shpPermission
     * @return
     */
    int updateShpPermission(ShpPermission shpPermission);

    /**
     * 该权限是否存在
     *
     * @param parentId
     * @param name
     * @return
     */
    boolean existsShpPermission(int parentId, String name);

    /**
     * 该code值是否存在
     *
     * @param code
     * @return
     */
    boolean existsShpPermissionCode(String code);

    /**
     * 获取店铺模块的所有角色-所有店铺通用</br>
     * 已经归类好
     * ORDER BY type asc,sort ASC,parentId asc,id ASC
     *
     * @return
     */
    List<VoShpPermission> listShpPermission(String version99);

    /**
     * 根据ID获取实体
     *
     * @param id 主键id
     * @return
     */
    ShpPermission getShpPermissionById(Integer id);

    /**
     * 对查询出来的权限进行分组归类
     *
     * @return
     */
    List<VoShpPermission> groupByShpPermission(String version99);

    /**
     * in查询权限
     *
     * @param ids
     * @return
     */
    List<VoUsualFunction> listShpPermissionByIds(String ids);

    /**
     * 获取所有权限id
     *
     * @return
     */
    List<Integer> listAppShpPermissionId();

    /**
     * 权限删除 - 删除多个
     *
     * @param inSql
     * @return
     */
    int deleteShpPermissions(String inSql);

    /**
     * 删除不存在关联权限的角色权限
     *
     * @param
     * @return
     */
    int deleteShpPermissionRole();

    /**
     * 是否已存在权限标识符
     *
     * @param permission
     * @return
     */
    boolean existsPerms(String[] permission);


    /**
     * 根据permission获取实体
     *
     * @param permission 权限
     * @return
     */
    ShpPermission getShpPermissionByPermission(String permission);
}
