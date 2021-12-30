package com.luxuryadmin.service.shp;

import com.luxuryadmin.entity.shp.ShpPermIndex;
import com.luxuryadmin.vo.shp.VoShpPermIndex;
import com.luxuryadmin.vo.shp.VoUsualFunction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 店铺权限业务逻辑层--改店铺模块的权限. 对所有店铺通用;
 *
 * @author monkey king
 * @date 2019-12-30 16:40:22
 */
public interface ShpPermIndexService {

    /**
     * 添加权限;返回该实体id
     *
     * @param shpPermission
     * @return
     */
    int saveShpPermIndex(ShpPermIndex shpPermission);

    /**
     * 更新权限;返回该实体id
     *
     * @param shpPermission
     * @return
     */
    int updateShpPermIndex(ShpPermIndex shpPermission);

    /**
     * 该权限是否存在
     *
     * @param parentId
     * @param name
     * @return
     */
    boolean existsShpPermIndex(int parentId, String name);

    /**
     * code是否存在,h5除外;
     *
     * @param code
     * @return
     */
    boolean existsShpPermIndexCode(String code);

    /**
     * 根据ID获取实体
     *
     * @param id 主键id
     * @return
     */
    ShpPermIndex getShpPermIndexById(Integer id);

    /**
     * 对查询出来的权限进行分组归类
     *
     * @param showOnApp  null:代表在后台显示 | 有值,代表在app端上显示
     * @param platform   ios | android
     * @param appVersion 线上app版本号
     * @return
     */
    List<VoShpPermIndex> groupByShpPermIndex(String showOnApp, String platform, String appVersion) throws Exception;

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
     */
    void deleteShpPermIndex(String inSql);

    /**
     * 删除不存在关联权限的角色权限
     *
     * @param
     */
    void deleteShpPermissionRole();

    /**
     * 是否已存在权限标识符
     *
     * @param permission
     * @return
     */
    boolean existsPerms(String permission);


    /**
     * 根据permission获取实体
     *
     * @param permission 权限
     * @return
     */
    ShpPermIndex getShpPermissionByPermission(String permission);

    /**
     * 获取权限列表里面可跳转的功能模块
     *
     * @return
     */
    List<VoShpPermIndex> listAppFunction();

    /**
     * 根据权限编码获取权限id
     *
     * @param permissions
     * @return
     */
    List<String> listPermIndexByPermission(String[] permissions);
}
