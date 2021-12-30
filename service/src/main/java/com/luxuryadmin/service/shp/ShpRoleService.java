package com.luxuryadmin.service.shp;

import com.luxuryadmin.entity.shp.ShpRole;
import com.luxuryadmin.vo.shp.VoShpRole;

import java.util.List;

/**
 * 店铺角色业务逻辑层
 *
 * @author monkey king
 * @date 2019-12-30 15:05:40
 */
public interface ShpRoleService {

    /**
     * 添加角色;返回该实体id
     *
     * @param shpRole
     * @param permIds 权限id; 为null时,则代表不添加权限
     * @param isSave  true:新增; false:修改
     * @return
     */
    int saveOrUpdateShpRole(ShpRole shpRole, Integer[] permIds, boolean isSave);

    /**
     * 根据实体更新ShpRole;<br/>
     * 该实体必须包含id;
     *
     * @param shpRole
     * @return
     */
    int updateShpRole(ShpRole shpRole);

    /**
     * 获取店铺下的所有角色
     *
     * @param shopId
     * @return
     */
    List<VoShpRole> listShpRole(int shopId);

    /**
     * 删除店铺角色,附带把角色所对应的权限也要删除;
     *
     * @param shopId
     * @param userId
     * @param roleId
     * @return
     */
    int deleteShopRole(int shopId, int userId, int roleId);

    /**
     * 获取该店铺添加的角色总数(上限100个)
     *
     * @param shopId
     * @return
     */
    int countRoleNum(int shopId);

    /**
     * 该店铺是否存在此角色
     *
     * @param shopId
     * @param roleId
     * @return
     */
    boolean existsShpRole(int shopId, int roleId);

    /**
     * 获取拥有此角色的所有用户id
     *
     * @param shopId
     * @param roleId
     * @return
     */
    List<Integer> listUserIdByRoleId(int shopId, int roleId);

    /**
     * 删除系统默认角色
     *
     * @param shopId
     */
    void deleteSysDefaultRole(int shopId);

    /**
     * 获取该店铺所有系统自动生成的角色id
     *
     * @param shopId
     * @return roleId
     */
    List<Integer> listRoleIdByShopId(int shopId);

    /**
     * 是否系统创建的角色
     *
     * @param shopId
     * @param roleId
     * @return
     */
    boolean isSysCreateRole(int shopId, int roleId);

}
