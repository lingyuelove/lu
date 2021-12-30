package com.luxuryadmin.service.shp;

import com.luxuryadmin.entity.shp.ShpPermTpl;
import com.luxuryadmin.entity.shp.ShpUserPermissionTpl;
import com.luxuryadmin.vo.shp.VoUserPermission;

import java.util.List;

/**
 * 身份权限模板
 *
 * @author Administrator
 */
public interface ShpPermTplService {


    /**
     * 根据模板名称去获取对应的权限
     *
     * @param shopId
     * @param userTypeId
     * @return
     */
    List<VoUserPermission> listPermTplByTplName(int shopId, int userTypeId);

    /**
     * 保存身份模板, 保存之前先清空对应的模板权限
     *
     * @param shopId
     * @param userTypeId
     * @param list
     */
    void saveShpPermTpl(int shopId, int userTypeId, List<ShpPermTpl> list);

    /**
     * 初始化店铺模板权限
     *
     * @param shopId
     * @param userId 用户id(用户类型为经营者)
     */
    void initShopSystemPerm(int shopId, int userId);

    /**
     * 删除对应的权限模板
     *
     * @param shopId
     * @param userTypeId
     * @return
     */
    int deleteShpUserPermTpl(int shopId, int userTypeId);

    /**
     * 删除店铺权限模板
     *
     * @param shopId
     */
    void deleteShpPermTplByShopId(int shopId);


    /**
     * 从旧权限模板自动同步到新权限模板；
     */
    void initNewPermTplFromOldPermTpl();
}