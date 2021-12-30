package com.luxuryadmin.service.shp;

import com.luxuryadmin.entity.shp.ShpRolePermissionRef;
import com.luxuryadmin.entity.shp.ShpUserPermissionRef;
import com.luxuryadmin.vo.shp.VoRolePermissionRel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author monkey king
 * @date 2019-12-30 20:52:38
 */
public interface ShpRolePermissionRefService {

    /**
     * 批量添加角色对应的权限;
     *
     * @param list
     * @return 受影响的行数
     */
    int saveBatchShpRolePermissionRef(List<ShpRolePermissionRef> list);

    /**
     * 根据店铺id和角色id获取该角色所拥有的权限;
     *
     * @param shopId
     * @param roleId
     * @return
     */
    List<VoRolePermissionRel> listRolePermsRelByRoleId(int shopId, int roleId);

    /**
     * 删除角色所连带的权限;
     *
     * @param shopId
     * @param roleId
     * @return
     */
    int deleteShpRolePermissionRef(int shopId, int roleId);

    /**
     * 获取用户的权限(最小一级权限,目前为第三集权限)
     *
     * @param shopId
     * @param userId
     * @return
     */
    List<String> getUserPermission(int shopId, int userId);
}
