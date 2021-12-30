package com.luxuryadmin.service.shp;

import com.luxuryadmin.entity.shp.ShpUserShopRef;
import com.luxuryadmin.param.shp.ParamEmployee;
import com.luxuryadmin.vo.shp.VoEmployee;

import java.util.List;
import java.util.Map;

/**
 * 店铺--用户与店铺对应关系(多对多)
 *
 * @author monkey king
 * @date 2019-12-25 14:47:35
 */
public interface ShpUserShopRefService {

    /**
     * 添加实体;
     *
     * @param userId       用户id
     * @param shopId       店铺id
     * @param name         对于店铺所显示的姓名
     * @param userTypeId   身份id
     * @param updateUserId 操作这条记录的userId; 一般直接controller层直接获取getUserId()
     * @return
     */
    int saveShpUserShopRef(int userId, int shopId, String name, int userTypeId, int updateUserId);


    /**
     * 该员工是否存在
     *
     * @param shopId
     * @param userId
     * @return
     */
    boolean existsShpUserShopRef(int shopId, Integer userId);


    /**
     * 根据店铺id获取该店铺的销售人员
     *
     * @param shopId
     * @return
     */
    List<VoEmployee> getSalesmanByShopId(int shopId);

    /**
     * 获取店铺的所有在职员工
     *
     * @param paramEmployee 实体参数 shopId和state 必填;
     * @return
     */
    List<VoEmployee> listUserShopRefByShopId(ParamEmployee paramEmployee);

    /**
     * 删除员工,把引用记录的state改成0
     *
     * @param shopId
     * @param userId
     * @param refId
     * @param state
     * @return
     */
    int modifyEmployeeState(int shopId, int userId, int refId, String state);

    /**
     * 更新用户与店铺的关系
     *
     * @param shpUserShopRef
     * @return
     */
    int updateUserShopRef(ShpUserShopRef shpUserShopRef);

    /**
     * 根据店铺id或用户id,获取该用户在该店铺得角色
     *
     * @param shopId
     * @param userId
     * @return
     */

    String getUserTypeByShopIdAndUserId(int shopId, int userId);


    /**
     * 获取店铺的入库人员(在职员工);
     *
     * @param shopId
     * @return
     */
    List<VoEmployee> listUploadUser(int shopId);

    /**
     * 查找该店铺是否存在此员工记录
     *
     * @param shopId
     * @param userId
     * @return
     */
    ShpUserShopRef getShpUserShopRefByUserId(int shopId, int userId);


    /**
     * 查找在职员工; state=1;
     *
     * @param shopId
     * @param userId
     * @return
     */
    ShpUserShopRef getShpUserShopRefOnJobByUserId(int shopId, int userId);

    /**
     * 统计,在职员工和离职员工总数
     *
     * @param shopId
     * @return
     */
    Map<String, String> countEmployee(int shopId);

    /**
     * 修改店铺与员工的状态
     *
     * @param shpUserShopRef
     */
    void updateUserShopRefState(ShpUserShopRef shpUserShopRef);


    /**
     * 获取用户在该店铺的名称
     *
     * @param shopId
     * @param userId
     * @return
     */
    String getNameFromShop(int shopId, Integer userId);

    /**
     * 获取关联此身份的userId
     *
     * @param shopId
     * @param userTypeId
     * @return
     */
    List<Integer> listUserIdByUserTypeId(int shopId, int userTypeId);

    /**
     * 员工数量是否超上限
     *
     * @param shopId
     */
    void isEmployeeLimit(int shopId);

    /**
     * 更新旧版本经营者的userTypeId
     *
     * @param shopId
     * @param userId
     */
    void updateBossUserShopRef(int shopId, int userId);

    /**
     * 物理删除店铺与员工的关系;<br/>
     * 注销店铺时调用
     *
     * @param shopId
     */
    void deleteUserShopRefByShopId(int shopId);

    /**
     * 获得所有在职员工的userId
     *
     * @param shopId
     * @param state  状态  0：禁用(或辞职)   1：正常
     * @return
     */
    List<Integer> listAllUserIdByShopId(int shopId, String state);

    /**
     * 获取用户店铺角色名称
     *
     * @param shopId
     * @param userId
     * @return
     */
    String getUserTypeRoleNameByShopIdAndUserId(int shopId, Integer userId);

    /**
     * 获取所用
     *
     * @param shopId
     * @param state  状态  0：禁用(或辞职)   1：正常
     * @return
     */
    List<String> listAllUserNameByShopId(int shopId, String state);

    /**
     * 更新实体
     *
     * @param shpUserShopRef
     */
    void updateObject(ShpUserShopRef shpUserShopRef);


    /**
     * 查找在职的所有员工(不包含boss);
     *
     * @return
     */
    List<ShpUserShopRef> listAllUserShopRefExceptBoss();

}
