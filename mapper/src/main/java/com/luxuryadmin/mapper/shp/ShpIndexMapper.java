package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.vo.shp.VoShpIndex;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 常用功能Mapper
 *
 * @author monkey king
 * @date 2020-06-15 17:18:45
 */
@Mapper
public interface ShpIndexMapper extends BaseMapper {


    /**
     * 获取用户在该店铺拥有的全部权限
     *
     * @param platform
     * @param appVersion
     * @param shopId
     * @param userId
     * @return
     */
    List<VoShpIndex> listAllPermByShopIdUserId(
            @Param("platform") String platform, @Param("appVersion") Integer appVersion,
            @Param("shopId") int shopId, @Param("userId") int userId);


    /**
     * 获取app首页的功能列表
     *
     * @param platform   平台; ios或者android; null 代表所有
     * @param appVersion 线上版本号 null 代表所有
     * @return
     */
    List<VoShpIndex> listAppIndexFunction(
            @Param("platform") String platform, @Param("appVersion") Integer appVersion);

    /**
     * 否已经存在该功能名称;
     *
     * @param name
     * @return
     */
    int existsShpIndex(@Param("name") String name);

    /**
     * code是否存在,h5除外;
     *
     * @param code
     * @return
     */
    int existsShpIndexCode(String code);

    /**
     * 是否已存在权限标识符
     *
     * @param permission
     * @return
     */
    int existsShpIndexPerms(String permission);


    /**
     * 获取所有功能点
     *
     * @return
     */
    List<VoShpIndex> listAllShpIndex();

    /**
     * 根据id批量删除
     *
     * @param ids
     * @return
     */
    int batchDelete(String ids);
}
