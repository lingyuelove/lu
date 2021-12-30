package com.luxuryadmin.mapper.sys;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.shp.ShpPermission;
import com.luxuryadmin.entity.sys.SysPermission;
import com.luxuryadmin.vo.shp.VoShpPermission;
import com.luxuryadmin.vo.sys.VoSysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 后天--菜单权限
 *
 * @author Administrator
 */
@Mapper
public interface SysPermissionMapper extends BaseMapper {

    /**
     * 获取店铺模块的所有角色-所有店铺通用</br>
     * 已经归类好
     * ORDER BY type asc,sort ASC,parentId asc,id ASC
     *
     * @return
     */
    List<VoSysPermission> listAppSysPermission();
    /**
     * 根据当前用户id和路径查询是否拥有此权限
     * @param userId

     * @return
     */
    List<String> getPermissionByUserAndUrl(@Param("userId") String userId);
    /**
     * 根据用户id获取该用户拥有的权限</br>
     * 已经归类好
     * ORDER BY type asc,sort ASC,parentId asc,id ASC
     *
     * @return
     */
    List<VoSysPermission> queryByUser(int username);
    /**
     * 删除多个 根据id
     * @param inSql
     * @return
     */
    int deleteObjects(String inSql);

    /**
     * 该权限是否存在
     *
     * @param parentId
     * @param name
     * @return
     */
    int existsSysPermissionName(@Param("parentId") int parentId, @Param("name") String name, @Param("id")int id);

    /**
     * 该code值是否存在
     *
     * @param code
     * @return
     */
    int existsSysPermissionCode(@Param("code")String code,@Param("id")int id);

    /**
     * 该权限是否存在
     * @param permission
     * @param id
     * @return
     */
    int existsSysPermission(@Param("permission")String permission,@Param("id")int id);
    /**
     * 查询具体某个接口的权限
     * @param url
     * @return
     */
    List<SysPermission> listByPath(String url);

    /**
     * 获取该用户所拥有的权限
     * @param id
     */
    List<SysPermission> listByUser(Integer id);

    /**
     * 根据父id查询子id
     * @param parentId
     * @return
     */
    List<SysPermission> getByParentId(Integer parentId);

    /**
     * 根据父id查询子id数量
     * @param parentId
     * @return
     */
    Integer getCountByParentId(Integer parentId);

    /**
     * 根据父id删除整个子id
     * @param parentId
     * @return
     */
    int deleteByParentId(Integer parentId);
}