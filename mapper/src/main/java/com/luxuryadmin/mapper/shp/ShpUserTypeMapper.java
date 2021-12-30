package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProClassify;
import com.luxuryadmin.entity.shp.ShpUserType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author monkey king
 * @date 2019-12-27 20:34:47
 */
@Mapper
public interface ShpUserTypeMapper extends BaseMapper {

    /**
     * 获取用户类型列表; 不包含【经营者】角色
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
    int initShpUserTypeByShopIdAndUserId(@Param("shopId") int shopId, @Param("userId") int userId);


    /**
     * 是否存在此身份名称
     *
     * @param shopId
     * @param name
     * @return
     */
    int existsUserType(@Param("shopId") int shopId, @Param("name") String name);


    /**
     * 删除身份模板
     *
     * @param shopId
     * @param userTypeId
     */
    void deleteUserType(@Param("shopId") int shopId, @Param("userTypeId") int userTypeId);


    /**
     * @param shopId
     * @param userTypeId
     * @return
     */
    ShpUserType selectShpUserTypeByShopIdAndTypeId(@Param("shopId") int shopId, @Param("id") int userTypeId);


    /**
     * 排序 身份
     *
     * @param list
     * @param shopId
     * @return
     */
    int sortUserType(@Param("list") List<ShpUserType> list, @Param("shopId") int shopId);
}
