package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProCheckProduct;
import com.luxuryadmin.param.pro.ParamCheckProductAddList;
import com.luxuryadmin.param.pro.ParamCheckProductListForApiBySearch;
import com.luxuryadmin.vo.pro.VoCheckProductDetailByApi;
import com.luxuryadmin.vo.pro.VoCheckProductListForApi;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProCheckProductMapper extends BaseMapper {

    /**
     * 新增多个盘点商品
     *
     * @param checkProductAddList
     */
    void addCheckProductList(@Param("checkProductAddList") ParamCheckProductAddList checkProductAddList);

    /**
     * 新增临时仓商品
     * @param checkProductAddList
     */
    void addCheckTempProductList(@Param("checkProductAddList") ParamCheckProductAddList checkProductAddList);
    /**
     * 更新
     *
     * @param id
     * @param checkState
     * @param checkType
     * @param remark
     */

    void updateCheckProduct(@Param("id") Integer id, @Param("checkState") String checkState, @Param("checkType") String checkType, @Param("remark") String remark, @Param("userId") Integer userId);

    /**
     * 查看详情
     *
     * @param id
     * @return
     */
    ProCheckProduct getCheckProductById(@Param("id") Integer id);

    /**
     * 手机端集合分页显示
     *
     * @param checkProductListForApiBySearch
     * @return
     */
    List<VoCheckProductListForApi> getCheckProductListForApi(@Param("checkProductListForApiBySearch") ParamCheckProductListForApiBySearch checkProductListForApiBySearch);

    /**
     * 查看详情
     *
     * @param id
     * @return
     */
    VoCheckProductDetailByApi getCheckProductDetailByApi(@Param("id") Integer id);

    /**
     * 根据盘点id和商品id获取详情
     *
     * @param bizId
     * @param fkProCheckId
     * @return
     */
    VoCheckProductListForApi getCheckProductForApi(@Param("bizId") String bizId, @Param("fkProCheckId") Integer fkProCheckId);

    /**
     * 根据盘点id和状态
     *
     * @param fkProCheckId
     * @param checkType
     * @param checkState
     * @return
     */
    Integer getCountBySearch(@Param("fkProCheckId") Integer fkProCheckId, @Param("checkType") String checkType, @Param("checkState") String checkState, @Param("fkProClassifyCode") String fkProClassifyCode);


    /**
     * 根据盘点id和状态获取临时仓盘点的数量
     *
     * @param fkProCheckId
     * @param checkType
     * @param checkState
     * @return
     */
    Integer getTempCountBySearch(@Param("fkProCheckId") Integer fkProCheckId, @Param("checkType") String checkType, @Param("checkState") String checkState, @Param("tempId") Integer tempId);

    /**
     * 删除此盘点下的商品
     *
     * @param fkProCheckId
     */
    void deleteByCheckId(@Param("fkProCheckId") Integer fkProCheckId);

    /**
     * 删除盘点数据(用于注销店铺时的操作)
     *
     * @param shopId
     * @return
     */
    int deleteProCheckProductByShopId(int shopId);
}