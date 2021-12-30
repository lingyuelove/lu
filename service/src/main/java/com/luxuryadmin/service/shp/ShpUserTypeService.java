package com.luxuryadmin.service.shp;

import com.luxuryadmin.entity.shp.ShpUserType;

import java.util.List;

/**
 * 用户类型表;(支持用户自定义名称)
 *
 * @author monkey king
 * @date 2019-12-09 15:31:19
 */
public interface ShpUserTypeService {

    /**
     * 获取用户类型列表;不包含【经营者】角色
     *
     * @param shopId
     * @return
     */
    List<ShpUserType> listShpUserTypeByShopId(int shopId);

    /**
     * 根据店铺id 初始化用户类型表;从模板中初始化
     *
     * @param shopId
     * @param userId
     * @return
     */
    int initShpUserTypeByShopIdAndUserId(int shopId, int userId);

    /**
     * 添加或更新身份
     *
     * @param shpUserType
     * @return
     */
    Integer saveOrUpdateShpUserTypeAndReturnId(ShpUserType shpUserType);

    /**
     * 是否存在此身份名称
     *
     * @param shopId
     * @param name
     * @return
     */
    boolean existsUserType(int shopId, String name);


    /**
     * v2.6.6(含)以下的接口调用;
     * 删除身份模板并删除相应关联的数据<br/>
     * 思路如下: <br/>
     * 1.先找到shp_user_shop_ref关联此身份的userId;<br/>
     * 2.根据步骤1的userId和shopId为条件,从shp_user_permission_ref中删除相应的数据;<br/>
     * 3.根据user_type_id和shopId为条件,从shp_user_permission_tlp中删除相应的数据;<br/>
     * 4.根据user_type_id和shopId为条件,冲shp_user_type中删除相应的数据;<br/>
     * 5.通知相关用户更新权限;<br/>
     *
     * @param shopId
     * @param userTypeId
     */
    void deleteUserType(int shopId, int userTypeId);


    /**
     * v2.6.7(含)以上的接口调用;
     * 删除身份模板并删除相应关联的数据<br/>
     * 思路如下: <br/>
     * 1.先找到shp_user_shop_ref关联此身份的userId;<br/>
     * 2.根据步骤1的userId和shopId为条件,从shp_user_permission_ref中删除相应的数据;<br/>
     * 3.根据user_type_id和shopId为条件,从shp_user_permission_tlp中删除相应的数据;<br/>
     * 4.根据user_type_id和shopId为条件,冲shp_user_type中删除相应的数据;<br/>
     * 5.通知相关用户更新权限;<br/>
     *
     * @param shopId
     * @param userTypeId
     */
    void deleteUserTypeNew(int shopId, int userTypeId);

    /**
     * 封装要入库的实体
     *
     * @param shopId
     * @param insertUserId
     * @param name
     * @param description
     * @param sort
     * @param remark
     * @return
     */
    ShpUserType packShpUserTypeToSave(int shopId, int insertUserId, String name, String description, int sort, String remark);

    /**
     * 根据【店铺ID】和【typeId】获取ShpUserType
     *
     * @param shopId
     * @param userTypeId
     * @return
     */
    ShpUserType getShpUserTypeByShopIdAndTypeId(int shopId, int userTypeId);

    /**
     * 排序 身份
     *
     * @param list
     * @param shopId
     * @return
     */
    void sortUserType(List<ShpUserType> list, int shopId);
}
