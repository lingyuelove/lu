package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.shp.ShpPermIndex;
import com.luxuryadmin.entity.shp.ShpPermTpl;
import com.luxuryadmin.entity.shp.ShpPermUserRef;
import com.luxuryadmin.entity.shp.ShpPermission;
import com.luxuryadmin.vo.shp.VoShpPermIndex;
import com.luxuryadmin.vo.shp.VoUsualFunction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * shp-权限索引
 * 2021-12-02 02:03:35
 *
 * @author Administrator
 */
@Mapper
public interface ShpPermIndexMapper extends BaseMapper {
    /**
     * 同一个父级下是否已经存在该权限;
     *
     * @param parentId
     * @param name
     * @return
     */
    int existsShpPermission(@Param("parentId") int parentId, @Param("name") String name);

    /**
     * code是否存在,h5除外;
     *
     * @param code
     * @return
     */
    int existsShpPermissionCode(String code);

    /**
     * 获取店铺模块的所有权限-所有店铺通用</br>
     * 已经归类好
     * ORDER BY type asc,sort ASC,parentId asc,id ASC
     * -1:对所有人开放;不需要配置权限;-9:权限列表里面,只对经营者展示;
     *
     * @param showOnApp  -1:代表不在app权限列表显示
     * @param platform
     * @param appVersion
     * @return
     */
    List<VoShpPermIndex> listAppShpPermission(
            @Param("showOnApp") String showOnApp,
            @Param("platform") String platform, @Param("appVersion") String appVersion);

    /**
     * 获取所有权限的id,只获取id一列
     *
     * @return
     */
    List<Integer> listAppShpPermissionId();

    /**
     * shopId=10001,roleId=10064的模板, v2.2改版之后将弃用;
     *
     * @return
     */
    List<Integer> listAppEmployeeShpPermissionId();

    /**
     * shopId=10001,roleId=10065的模板, v2.2改版之后将弃用;
     *
     * @return
     */
    List<Integer> listAppAgencyShpPermissionId();


    /**
     * in查询权限
     *
     * @param ids
     * @return
     */
    List<VoUsualFunction> listShpPermissionByIds(String ids);

    /**
     * 删除多个 根据id
     *
     * @param inSql
     * @return
     */
    int deleteObjects(String inSql);

    /**
     * 是否已存在权限标识符
     *
     * @param permission
     * @return
     */
    int existsPerms(String permission);


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
     * 获取员工旧权限
     *
     * @param shopId
     * @param userId
     * @param oldPerms
     * @return
     */
    List<ShpPermUserRef> listOldUserPermTurnNewPerm(
            @Param("shopId") int shopId,
            @Param("userId") int userId, @Param("oldPerms") String oldPerms);

    /**
     * 获取模板的旧权限
     *
     * @param shopId
     * @param userTypeId
     * @param oldPerms
     * @return
     */
    List<ShpPermTpl> listOldPermTplTurnNewPermTpl(
            @Param("shopId") int shopId,
            @Param("userTypeId") int userTypeId, @Param("oldPerms") String oldPerms);

    /**
     * 根据权限编码获取权限id
     *
     * @param perms
     * @return
     */
    List<String> listPermIndexByPermission(String perms);

    /**
     * 根据权限id获取权限授权码
     *
     * @param ids
     * @return
     */
    List<String> listPermCodeByPermIds(String ids);
}