package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.entity.shp.ShpWechat;
import com.luxuryadmin.vo.shp.VoShpWechat;
import com.luxuryadmin.vo.shp.VoShpWechatByShow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShpWechatMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShpWechat record);

    int insertSelective(ShpWechat record);

    ShpWechat selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShpWechat record);

    int updateByPrimaryKey(ShpWechat record);

    /**
     * 根据店铺ID查询店铺微信列表
     * @param shopId
     * @return
     */
    List<VoShpWechat> selectShpWechatByShopId(Integer shopId);

    /**
     * 逻辑删除【店铺微信】
     * @param id
     * @param shopId
     * @return
     */
    Integer deleteShpWechat(@Param("id")String id,@Param("fkShopId") Integer shopId);

    /**
     * 获取店铺联系人
     * @param shopId
     * @return
     */
    VoShpWechat getWechatByShow(Integer shopId);
}