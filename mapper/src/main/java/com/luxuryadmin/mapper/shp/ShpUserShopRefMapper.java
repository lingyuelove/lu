package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.shp.ShpUserShopRef;
import com.luxuryadmin.param.shp.ParamEmployee;
import com.luxuryadmin.vo.shp.VoEmployee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author monkey king
 */
@Mapper
public interface ShpUserShopRefMapper extends BaseMapper {

    /**
     * 该员工是否存在
     *
     * @param shopId
     * @param userId
     * @return
     */
    int existsShpUserShopRef(@Param("shopId") int shopId, @Param("userId") int userId);

    /**
     * 根据店铺id获取该店铺的销售人员;只查有开单权限的人员;
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
     * 获得所有在职员工的userId
     *
     * @param shopId
     * @param state  状态  0：禁用(或辞职)   1：正常
     * @return
     */
    List<Integer> listAllUserIdByShopId(@Param("shopId") int shopId,
                                        @Param("state") String state);


    /**
     * 删除员工,把引用记录的state改成0;
     *
     * @param userShopRef
     * @return
     */
    int fireEmployee(ShpUserShopRef userShopRef);


    /**
     * 更新用户与店铺的关系
     *
     * @param shpUserShopRef
     * @return
     */
    int updateUserShopRef(ShpUserShopRef shpUserShopRef);

    /**
     * 更新用户与店铺的状态
     *
     * @param shpUserShopRef
     * @return
     */
    int updateUserShopRefState(ShpUserShopRef shpUserShopRef);

    /**
     * 根据店铺id或用户id,获取该用户在该店铺得角色
     *
     * @param shopId
     * @param userId
     * @return
     */

    String getUserTypeByShopIdAndUserId(@Param("shopId") int shopId, @Param("userId") int userId);


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
    ShpUserShopRef getShpUserShopRefByUserId(@Param("shopId") int shopId, @Param("userId") int userId);


    /**
     * 统计,在职员工和离职员工总数
     *
     * @param shopId
     * @return
     */
    Map<String, String> countEmployee(int shopId);

    /**
     * 根据店铺ID和员工类型查询用户ID
     *
     * @param params
     * @return
     */
    List<Integer> selectUserIdListByTypeList(Map<String, Object> params);

    /**
     * 获取用户在该店铺的名称
     *
     * @param shopId
     * @param userId
     * @return
     */
    String getNameFromShop(@Param("shopId") int shopId, @Param("userId") Integer userId);

    /**
     * 获取关联此身份的userId
     *
     * @param shopId
     * @param userTypeId
     * @return
     */
    List<Integer> listUserIdByUserTypeId(@Param("shopId") int shopId, @Param("userTypeId") int userTypeId);

    /**
     * 获取身份名称
     *
     * @param shopId
     * @param userId
     * @return
     */
    String getUserTypeRoleNameByShopIdAndUserId(@Param("shopId") int shopId, @Param("userId") int userId);

    /**
     * 物理删除店铺与员工的关系;<br/>
     * 注销店铺时调用
     *
     * @param shopId
     * @return
     */
    int deleteUserShopRefByShopId(int shopId);

    /**
     * 获取员工的用户名
     *
     * @param shopId
     * @param state
     * @return
     */
    List<String> listAllUserNameByShopId(@Param("shopId") int shopId,
                                         @Param("state") String state);

    /**
     * 查找在职员工; state=1;
     *
     * @param shopId
     * @param userId
     * @return
     */
    ShpUserShopRef getShpUserShopRefOnJobByUserId(
            @Param("shopId") int shopId, @Param("userId") int userId);

    /**
     * 查找在职的所有员工(不包含boss);
     *
     * @return
     */
    List<ShpUserShopRef> listAllUserShopRefExceptBoss();

}