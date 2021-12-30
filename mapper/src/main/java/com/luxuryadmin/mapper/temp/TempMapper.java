package com.luxuryadmin.mapper.temp;

import com.luxuryadmin.entity.pro.ProDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author qwy
 */
@Mapper
public interface TempMapper {


    /**
     * 获取商品详情里面的图片地址;
     *
     * @param startId 开始的id(含)
     * @param endId   结束的id(含)
     * @return
     */
    List<ProDetail> listProDetail(int startId, int endId);


    /**
     * 获取用户注册数和店铺注册数
     *
     * @return
     */
    List<HashMap<String, String>> listRegisterNum();

    /**
     * 统计在售商品,成本>0
     *
     * @return
     */
    HashMap<String, String> countOnSaleInitPriceNotNull();


    /**
     * 统计在售商品(包含未填写成本的商品)
     *
     * @return
     */
    HashMap<String, String> countOnSale();


    /**
     * 获取活跃用户
     *
     * @param st       开始时间;
     * @param startNum 起始数量
     * @param endNum   结束数量
     * @return
     */
    List<HashMap<String, String>> listActivityUser(
            @Param("st") String st,
            @Param("startNum") int startNum, @Param("endNum") int endNum);

    /**
     * 统计注册用户和注册店铺
     *
     * @return
     */
    HashMap<String, String> countRegisterUserAndShop();

    /**
     * 统计在售商品各分类成本
     *
     * @param isInitPriceNotNull
     * @return
     */
    List<HashMap<String, String>> countOnSaleProClassify(boolean isInitPriceNotNull);


}
