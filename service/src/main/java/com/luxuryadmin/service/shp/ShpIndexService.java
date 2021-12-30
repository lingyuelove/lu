package com.luxuryadmin.service.shp;

import com.luxuryadmin.entity.shp.ShpIndex;
import com.luxuryadmin.vo.shp.VoShpIndex;

import java.util.List;

/**
 * 首页列表
 *
 * @author monkey king
 * @date 2021-12-02 19:20:36
 */
public interface ShpIndexService {


    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    ShpIndex getShpIndexById(Integer id);

    /**
     * 添加权限;返回该实体id
     *
     * @param shpIndex
     */
    void saveShpPermIndex(ShpIndex shpIndex);

    /**
     * 更新权限;返回该实体id
     *
     * @param shpIndex
     * @return
     */
    void updateShpIndex(ShpIndex shpIndex);

    /**
     * 删除首页app功能
     *
     * @param ids
     */
    void deleteShpIndex(String ids);

    /**
     * 获取用户拥有的全部功能
     *
     * @param platform
     * @param appVersion
     * @param shopId
     * @param userId
     * @return
     */
    List<VoShpIndex> listAllPermByShopIdUserId(String platform, Integer appVersion, int shopId, int userId);


    /**
     * 获取app首页的功能列表
     *
     * @param platform   平台; ios或者android; null 代表所有
     * @param appVersion 线上版本号 null 代表所有
     * @return
     */
    List<VoShpIndex> listAppIndexFunction(String platform, Integer appVersion);


    /**
     * 该功能名称是否存在
     *
     * @param name
     * @return
     */
    boolean existsShpIndex(String name);

    /**
     * 对查询出来的权限进行分组归类
     *
     * @return
     */
    List<VoShpIndex> groupByShpPermIndex();

}
