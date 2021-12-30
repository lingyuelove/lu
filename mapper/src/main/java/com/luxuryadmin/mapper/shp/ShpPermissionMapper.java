package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.shp.ShpPermission;
import com.luxuryadmin.vo.shp.VoShpPermission;
import com.luxuryadmin.vo.shp.VoUsualFunction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author monkey king
 */
@Mapper
public interface ShpPermissionMapper extends BaseMapper {
    /**
     * 该权限是否存在
     *
     * @param parentId
     * @param name
     * @return
     */
    int existsShpPermission(@Param("parentId") int parentId, @Param("name") String name);

    /**
     * 该code值是否存在
     *
     * @param code
     * @return
     */
    int existsShpPermissionCode(String code);

    /**
     * 获取店铺模块的所有权限-所有店铺通用</br>
     * 已经归类好
     * ORDER BY type asc,sort ASC,parentId asc,id ASC
     *
     * @return
     */
    List<VoShpPermission> listAppShpPermission(String version99);

    /**
     * 获取所有权限的id,只获取id一列
     *
     * @return
     */
    List<Integer> listAppShpPermissionId();


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
    ShpPermission getShpPermissionByPermission(String permission);

    /**
     * 根据授权码获取权限id
     *
     * @param permCodes
     * @return
     */
    List<Integer> listPermIdByPermCode(String permCodes);


    /**
     * 根据授权码获取权限id
     *
     * @param ids
     * @return
     */
    List<String> listPermCodeByPermIds(String ids);
}