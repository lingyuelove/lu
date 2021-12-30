package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.entity.shp.ShpAfterSaleGuarantee;
import com.luxuryadmin.vo.shp.VoShpAfterSaleGuarantee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ShpAfterSaleGuaranteeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShpAfterSaleGuarantee record);

    int insertSelective(ShpAfterSaleGuarantee record);

    ShpAfterSaleGuarantee selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShpAfterSaleGuarantee record);

    int updateByPrimaryKey(ShpAfterSaleGuarantee record);

    /**
     * 根据店铺ID和售后保障ID查询售后保障记录
     *
     * @param params
     * @return
     */
    ShpAfterSaleGuarantee selectByShopIdAndGuaranteeId(Map<String, Object> params);

    /**
     * 根据店铺ID查询【售后保障】列表
     *
     * @param shopId
     * @return
     */
    List<VoShpAfterSaleGuarantee> selectGuaranteeListByShopId(Integer shopId);

    /**
     * 获取所有需要初始化售后保障标签的店铺ID
     *
     * @return
     */
    List<Integer> selectAllNeedInitShopId();

    /**
     * 删除店铺标签
     *
     * @param shopId
     * @return
     */
    int deleteShpAfterSaleGuaranteeByShopId(int shopId);
}