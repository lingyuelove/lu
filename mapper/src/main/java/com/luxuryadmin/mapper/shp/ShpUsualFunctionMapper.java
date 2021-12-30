package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.shp.ShpUsualFunction;
import com.luxuryadmin.vo.shp.VoUsualFunction;
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
public interface ShpUsualFunctionMapper extends BaseMapper {

    /**
     * 获取用户的常用功能
     *
     * @param shopId
     * @param userId
     * @return
     */
    List<VoUsualFunction> listShpUsualFunction(@Param("shopId") int shopId, @Param("userId") int userId);


    /**
     * 获取用户拥有的全部功能
     *
     * @param shopId
     * @param userId
     * @return
     */
    List<VoUsualFunction> listAllFunction(@Param("shopId") int shopId, @Param("userId") int userId);

    /**
     * 获取经营者拥有的全部功能
     *
     * @return
     */
    List<VoUsualFunction> listBossAllFunction();


    /**
     * 删除具体用户在该店铺的常用功能
     *
     * @param shopId
     * @param userId
     * @return
     */
    int deleteUsualFunction(@Param("shopId") int shopId, @Param("userId") int userId);

    /**
     * 根据permIds参数对比,把不存在于permIds里面的常用功能给去掉
     *
     * @param shopId
     * @param permIds
     * @return
     */
    int removeFuncAndPermIdNotExists(@Param("shopId") int shopId, @Param("permIds") String permIds);
}
