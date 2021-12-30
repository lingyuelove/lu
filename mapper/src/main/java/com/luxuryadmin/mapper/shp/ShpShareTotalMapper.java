package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.shp.ShpShareTotal;
import com.luxuryadmin.vo.shp.VoShopMemberAddHour;
import com.luxuryadmin.vo.shp.VoShpShareTotal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 *商铺分享进度时长累计表 dao
 *@author Mong
 *@Date 2021-05-31 16:44:37
 */
@Mapper
public interface ShpShareTotalMapper extends BaseMapper {

    /**
     * 获取当天分享时长
     * @param code
     * @param fkShpShopId
     * @return
     */
    VoShpShareTotal getShareTotalByToDay(@Param("code") String code, @Param("fkShpShopId")Integer fkShpShopId);

    /**
     * 获取个人中心会员信息
     * @param code
     * @param fkShpShopId
     * @return
     */
    VoShopMemberAddHour getByShopId(@Param("code") String code, @Param("fkShpShopId")Integer fkShpShopId);

    /**
     * 获取昨天添加
     * @return
     */
    List<ShpShareTotal> getShareTotalByYest();
}
