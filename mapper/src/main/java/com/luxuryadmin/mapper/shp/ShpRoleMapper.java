package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.vo.shp.VoShpRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author monkey king
 */
@Mapper
public interface ShpRoleMapper extends BaseMapper {


    /**
     * 获取店铺下的所有角色
     *
     * @param shopId
     * @return
     */
    List<VoShpRole> listShpRole(int shopId);

    /**
     * 删除店铺角色
     *
     * @param shopId
     * @param roleId
     * @return
     */
    int deleteShopRole(@Param("shopId") int shopId, @Param("roleId") int roleId);


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
    int existsShpRole(@Param("shopId") int shopId, @Param("roleId") int roleId);

    /**
     * 获取拥有此角色的所有用户id
     *
     * @param shopId
     * @param roleId
     * @return
     */
    List<Integer> listUserIdByRoleId(@Param("shopId")int shopId, @Param("roleId") Integer roleId);


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
     * @param shopId
     * @param roleId
     * @return
     */
    int isSysCreateRole(@Param("shopId")int shopId, @Param("roleId") int roleId);
}