package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.shp.ShpUserRoleRef;
import com.luxuryadmin.vo.shp.VoUserRoleRef;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 */
@Mapper
public interface ShpUserRoleRefMapper extends BaseMapper {

    /**
     * 重新授权时, 需要对之前的角色进行清空
     *
     * @param shopId
     * @param userId
     * @return
     */
    int deleteShpUserRoleByShopIdUserId(@Param("shopId") int shopId, @Param("userId") int userId);

    /**
     * 获取用户所在店铺拥有的角色
     *
     * @param shopId
     * @param userId
     * @return
     */
    List<VoUserRoleRef> listUserRoleRefByUserId(@Param("shopId") int shopId, @Param("userId") int userId);

    /**
     * 删除用户对应的角色
     *
     * @param shopId
     * @param userId
     * @param roleId
     */
    void deleteUserRoleRef(@Param("shopId") int shopId,
                           @Param("userId") int userId, @Param("roleId") int roleId);

    /**
     * 删除某个用户的所有角色
     * @param id
     */
    void deleteByUserId(Integer id);
}