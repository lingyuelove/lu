package com.luxuryadmin.service.shp;

import com.luxuryadmin.entity.shp.ShpUserPermissionRef;
import com.luxuryadmin.vo.shp.VoUserPermission;

import java.util.List;

/**
 * 用户→权限 关联表
 *
 * @author monkey king
 * @date 2020-09-04 15:34:05
 */
public interface ShpUserPermissionRefService {

    /**
     * 批量添加授权记录;(新添加人员授权;添加与店铺的关系)
     *
     * @param userId
     * @param shopId
     * @param name
     * @param userTypeId
     * @param list
     * @param updateUserId 操作这条记录的userId; 一般直接controller层直接获取getUserId()
     * @return
     */
    int saveOrUpdateBatchShpUserPermissionRef(int userId, int shopId, String name, int userTypeId,
                                              List<ShpUserPermissionRef> list, int updateUserId);


    /**
     * 获取用户对应的权限
     *
     * @param shopId
     * @param userId
     * @return
     */
    List<VoUserPermission> listUserPermByShopIdAndUserId(int shopId, int userId);

    /**
     * 获取用户对应的细粒度权限
     *
     * @param shopId
     * @param userId
     * @return
     */
    List<String> listUserPermission(int shopId, int userId);


    /**
     * 从redis获取用户的细粒度权限<br/>
     * 用作与shiro认证, 在更新用户角色和权限时, 请调用此方法;
     *
     * @param shopId
     * @param userId
     * @param isUpdateRedis 是否更新缓存; true:从数据库更新缓存; false:不更新缓存; 直接取redis值;
     * @return
     */
    List<String> listUserPermissionByRedis(int shopId, int userId, boolean isUpdateRedis);

    /**
     * 查询[指定店铺]拥有[指定权限]的用户ID
     *
     * @param shopId     店铺ID
     * @param permission 权限
     * @return
     */
    //List<Integer> listShopUserIdWithPermission(int shopId, String permission);

    /**
     * 删除用户相应的权限, 用户id用英文逗号拼接
     *
     * @param shopId
     * @param userIds
     * @return
     */
    int deleteUserPermissionByUserId(int shopId, String userIds);


    /**
     * 封装实体入库
     *
     * @param shopId
     * @param userId
     * @param permId
     * @param insertUserId
     * @return
     */
    ShpUserPermissionRef packShpUserPermissionRef(int shopId, int userId, int permId, int insertUserId);


    /**
     * 初始化老用户的权限<br/>
     * 1.找到此用户之前所关联到的角色;<br/>
     * 2.找到此角色对应的权限permId;(原权限表shp_role_permission_ref)<br/>
     * 3.把permId存到新权限表(shp_user_permission_ref)<br/>
     * 4.旧数据, 暂时不删除;
     *
     * @param shopId
     * @param userId
     */
    void initOldUserPermission(int shopId, int userId);

    /**
     * 删除店铺权限
     *
     * @param shopId
     */
    void deleteShpUserPermissionRefByShopId(int shopId);

    /**
     * 拥有该模块下的多少个权限;<br/>
     * 一般用于判断是否拥有该模块下的权限,返回结果大于0则代表拥有该模块下的其中权限
     *
     * @param shopId
     * @param userId
     * @param moduleName
     * @return
     */
    int countModulePermission(int shopId, Integer userId, String moduleName);

    /**
     * 变更经营者时,需要把新经营者之前的权限赋予给旧经营者;
     *
     * @param shopId
     * @param oldUserId 旧经营者的用户id;
     * @param newUserId 新经营者的用户id;
     */
    void changeUserPermission(int shopId, int oldUserId, int newUserId);

    /**
     * 更新实体
     *
     * @param shpUserPermissionRef
     */
    void updateObject(ShpUserPermissionRef shpUserPermissionRef);


    /**
     * 给所有用户加上此权限;(boss除外)<br/>
     * 此方法仅临时使用;后期删除;<br/>
     * 1.找到店铺的员工关系;除去经营者和除去已经离职的;<br/>
     * 2.判断该员工是否已经拥有此权限(newPerm);<br/>
     * 3.如果步骤1a的结果是false,则查询该员工在此店铺是否拥有hasPerm权限,用in(${hasPerm})来查询; 反之亦然;<br/>
     * 4.如果有步骤2的权限,则给此员工增加newPerm权限,并更新redisPerm;<br/>
     * 5.用线程操作;<br/>
     *
     * @param hasPerm 已经拥有的权限
     * @param newPerm 即将要添加的权限
     */
    void addPermForAll(String hasPerm, String newPerm);


}
