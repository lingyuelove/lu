package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.entity.shp.ShpServiceType;
import com.luxuryadmin.vo.shp.VoShpServiceType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShpServiceTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShpServiceType record);

    int insertSelective(ShpServiceType record);

    ShpServiceType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShpServiceType record);

    int updateByPrimaryKey(ShpServiceType record);

    /**
     * 查询所有需要初始化的店铺ID
     *
     * @return
     */
    List<Integer> selectAllNeedInitShopId();

    /**
     * 根据店铺ID获取服务类型列表
     *
     * @param shopId
     * @return
     */
    List<VoShpServiceType> selectAllServiceTypeByShopId(Integer shopId);


    /**
     * 根据服务ID获取类型名称
     *
     * @param shpServiceTypeId
     * @return
     */
    String selectTypeNameById(Integer shpServiceTypeId);

    /**
     * 删除店铺服务类型
     *
     * @param shopId
     * @return
     */
    int deleteShpServiceTypeByShopId(int shopId);

    List<ShpServiceType> getServiceType(Integer shopId);
}