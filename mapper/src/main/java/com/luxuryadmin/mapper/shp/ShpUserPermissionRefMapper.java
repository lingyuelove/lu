package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.vo.shp.VoUserPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 */
@Mapper
public interface ShpUserPermissionRefMapper extends BaseMapper {

    /**
     * 获取用户对应的权限
     *
     * @param shopId
     * @param userId
     * @return
     */
    List<VoUserPermission> listUserPermByShopIdAndUserId(
            @Param("shopId") int shopId, @Param("userId") int userId);

    /**
     * 清空用户对应的权限
     *
     * @param shopId
     * @param userId
     * @return
     */
    int deleteUserPermRefByShopIdAndUserId(@Param("shopId") int shopId, @Param("userId") int userId);

    /**
     * 获取用户对应的细粒度权限
     *
     * @param shopId
     * @param userId
     * @return
     */
    List<String> listUserPermission(@Param("shopId") int shopId, @Param("userId") int userId);

    /**
     * 获取所有细粒度权限,适用于经营者
     *
     * @return
     */
    List<String> listBossPermission();

    /**
     * 查询[指定店铺]拥有[指定权限]的用户ID
     *
     * @param shopId
     * @param permission
     * @return
     */
    List<Integer> listShopUserIdWithPermission(@Param("shopId") int shopId, @Param("permission") String permission);

    /**
     * 删除用户相应的权限, 用户id用英文逗号拼接
     *
     * @param shopId
     * @param userIds
     * @return
     */
    int deleteUserPermissionByUserId(@Param("shopId") int shopId, @Param("userIds") String userIds);

    /**
     * 用in查询是否存在权限
     *
     * @param shopId
     * @param userId
     * @param queryPermIds
     * @return
     */
    int existsShpUserPermissionRef(@Param("shopId") int shopId, @Param("userId") int userId, @Param("queryPermIds") String queryPermIds);

    /**
     * 删除店铺权限
     *
     * @param shopId
     * @return
     */
    int deleteShpUserPermissionRefByShopId(int shopId);

    /**
     * 拥有该模块下的多少个权限;<br/>
     * 一般用于判断是否拥有该模块下的权限,返回结果大于0则代表拥有该模块下的其中权限
     *
     * @param shopId
     * @param userId
     * @param moduleName
     * @return
     */
    int countModulePermission(
            @Param("shopId") int shopId,
            @Param("userId") Integer userId,
            @Param("moduleName") String moduleName);

    /**
     * 变更经营者时,需要把新经营者之前的权限赋予给旧经营者;
     *
     * @param shopId
     * @param oldUserId 旧经营者的用户id;
     * @param newUserId 新经营者的用户id;
     */
    void changeUserPermission(
            @Param("shopId") int shopId,
            @Param("oldUserId") int oldUserId,
            @Param("newUserId") int newUserId);


}