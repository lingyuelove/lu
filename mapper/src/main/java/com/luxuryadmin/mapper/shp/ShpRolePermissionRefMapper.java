package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.vo.shp.VoRolePermissionRel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 */
@Mapper
public interface ShpRolePermissionRefMapper extends BaseMapper {

    /**
     * 根据店铺id和角色id获取该角色所拥有的权限;
     *
     * @param shopId
     * @param roleId
     * @return
     */
    List<VoRolePermissionRel> listVoRolePermsRelByShopIdRoleId(
            @Param("shopId") int shopId, @Param("roleId") int roleId);

    /**
     * 删除角色所连带的权限;
     *
     * @param shopId
     * @param roleId
     * @return
     */
    int deleteShpRolePermissionRef(
            @Param("shopId") int shopId, @Param("roleId") int roleId);

    /**
     * 获取用户的权限(最小一级权限,目前为第三集权限)
     *
     * @param shopId
     * @param userId
     * @return
     */
    List<String> getUserPermission(
            @Param("shopId") int shopId, @Param("userId") int userId);

    /**
     * 删除不存在的的角色权限关联
     * @return
     */
    int deleteObjects();
}