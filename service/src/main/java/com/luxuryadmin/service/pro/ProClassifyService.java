package com.luxuryadmin.service.pro;

import com.luxuryadmin.entity.pro.ProClassify;
import com.luxuryadmin.param.pro.ParamProClassifySubQuery;
import com.luxuryadmin.vo.pro.VoProClassify;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品分类表模板;<br/>
 * 系统默认模板;腕表、珠宝、鞋靴、佩饰、其它
 *
 * @author monkey king
 * @date 2019-12-09 15:31:19
 */
public interface ProClassifyService {

    /**
     * 根据状态来获取分类;
     *
     * @param shopId
     * @param state
     * @return
     */
    List<VoProClassify> listProClassifyByState(int shopId, String state);

    /**
     * 获取系统生成分类;
     *
     * @param state
     * @return
     */
    List<VoProClassify> listSysProClassifyByState(String state);

    /**
     * 根据店铺id 初始化分类表;
     *
     * @param shopId
     * @param userId
     * @return
     */
    int initProClassifyByShopIdAndUserId(int shopId, int userId);


    /**
     * 加载友商店铺分类
     *
     * @param leaguerShopId
     * @return
     */
    List<VoProClassify> listLeaguerProClassifyByState(int leaguerShopId);

    /**
     * 修改店铺分类
     *
     * @param list
     * @param shopId
     * @return
     */
    int updateProClassify(List<ProClassify> list, int shopId);

    /**
     * 删除店铺分类(注销时调用)
     *
     * @param shopId
     * @return
     */
    int deleteProClassifyByShopId(int shopId);


}
