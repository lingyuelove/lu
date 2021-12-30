package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProCheck;
import com.luxuryadmin.param.pro.ParamCheckListForApiBySearch;
import com.luxuryadmin.vo.pro.VoCheckListForApi;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProCheckMapper extends BaseMapper {

    /**
     * 集合显示
     * @param checkListForApiBySearch
     * @return
     */
    List<VoCheckListForApi> getCheckListForApi(@Param("checkListForApiBySearch")ParamCheckListForApiBySearch checkListForApiBySearch);

    /**
     * 更新
     * @param id
     * @param checkState
     */

    void updateCheck(@Param("id")Integer id,@Param("checkState")String checkState);

    /**
     * 查看详情
     * @param id
     * @return
     */
    ProCheck getById(@Param("id")Integer id);

    /**
     * 删除盘点
     * @param id
     */
    void deleteCheck(@Param("id")Integer id);

    /**
     * 删除盘点数据(用于注销店铺)
     * @param shopId
     * @return
     */
    int deleteProCheckByShopId(Integer shopId);

    /**
     * 修改此店铺已过期的盘点
     * @param shopId
     */
    void updateCheckState(@Param("shopId")Integer shopId);

    /**
     * 根据店铺id获取已过期盘点
     * @param shopId
     * @return
     */
    ProCheck getByShopId(@Param("shopId")Integer shopId);
}