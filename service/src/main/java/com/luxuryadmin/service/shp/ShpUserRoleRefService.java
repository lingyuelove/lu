package com.luxuryadmin.service.shp;

import com.luxuryadmin.entity.shp.ShpUserPermissionRef;
import com.luxuryadmin.entity.shp.ShpUserRoleRef;
import com.luxuryadmin.param.shp.ParamShpUserInfo;
import com.luxuryadmin.vo.shp.VoUserRoleRef;

import java.util.List;

/**
 * 店铺人员授权--业务逻辑层
 *
 * @author monkey king
 * @date 2019-12-30 22:04:01
 */
public interface ShpUserRoleRefService {


    /**
     * 获取用户所在店铺拥有的角色
     *
     * @param shopId
     * @param userId
     * @return
     */
    List<VoUserRoleRef> listUserRoleRefByUserId(int shopId, int userId);


    /**
     * 保存实体
     *
     * @param shpUserRoleRef
     */
    void saveUserRoleRef(ShpUserRoleRef shpUserRoleRef);

    /**
     * 删除用户对应的角色
     *
     * @param shopId
     * @param userId
     * @param roleId
     */
    void deleteUserRoleRef(int shopId, int userId, int roleId);



    /**
     * 删除user对应的角色
     *
     * @param id
     */
    void deleteByUserId(Integer id);

    /**
     * 用户增加多个角色
     *
     * @param paramShpUserInfo
     */
    void saveUserRoleRefs(ParamShpUserInfo paramShpUserInfo);

}
