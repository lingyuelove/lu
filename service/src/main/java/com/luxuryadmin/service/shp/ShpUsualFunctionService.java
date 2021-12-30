package com.luxuryadmin.service.shp;

import com.luxuryadmin.entity.shp.ShpUsualFunction;
import com.luxuryadmin.vo.shp.VoUsualFunction;

import java.util.List;

/**
 * 常用功能
 *
 * @author monkey king
 * @date 2020-06-15 17:24:21
 */
public interface ShpUsualFunctionService {

    /**
     * 获取用户的常用功能
     *
     * @param shopId
     * @param userId
     * @return
     */
    List<VoUsualFunction> listShpUsualFunction(int shopId, int userId);


    /**
     * 获取用户拥有的全部功能
     *
     * @param shopId
     * @param userId
     * @return
     */
    List<VoUsualFunction> listAllFunction(int shopId, int userId);

    /**
     * 获取经营者拥有的全部功能
     *
     * @return
     */
    List<VoUsualFunction> listBossAllFunction();

    /**
     * 重置常用功能
     *
     * @param shopId
     * @param userId
     * @param list
     * @return
     */
    void resetUsualFunction(int shopId, int userId, List<ShpUsualFunction> list);

    /**
     * 根据permIds参数对比,把不存在于permIds里面的常用功能给去掉
     *
     * @param shopId
     * @param permIds
     */
    void removeFuncAndPermIdNotExists(int shopId, String permIds);


}
