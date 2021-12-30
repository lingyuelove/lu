package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.shp.ShpPermTpl;
import com.luxuryadmin.vo.shp.VoUserPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * shp-权限模板
 * 2021-12-02 02:03:35
 *
 * @author Administrator
 */
@Mapper
public interface ShpPermTplMapper extends BaseMapper {
    /**
     * 根据模板名称去获取对应的权限
     *
     * @param shopId
     * @param userTypeId
     * @return
     */
    List<VoUserPermission> listPermTplByUserTypeId(
            @Param("shopId") int shopId, @Param("userTypeId") int userTypeId);

    /**
     * 只返回该模板的权限id集合
     *
     * @param shopId
     * @param userTypeId
     * @return
     */
    List<Integer> listTplPermIdByUserTypeId(
            @Param("shopId") int shopId, @Param("userTypeId") int userTypeId);

    /**
     * 删除模板权限
     *
     * @param shopId
     * @param userTypeId
     */
    void deleteShpPermTplByShopIdAndUserId(@Param("shopId") int shopId, @Param("userTypeId") int userTypeId);

    /**
     * 删除对应的权限模板
     *
     * @param shopId
     * @param userTypeId
     * @return
     */
    int deleteShpUserPermTpl(@Param("shopId") int shopId, @Param("userTypeId") int userTypeId);

    /**
     * 删除店铺权限模板
     *
     * @param shopId
     * @return
     */
    int deleteShpPermTplByShopId(int shopId);
}