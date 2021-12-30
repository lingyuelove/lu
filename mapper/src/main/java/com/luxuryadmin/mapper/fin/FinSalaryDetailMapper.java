package com.luxuryadmin.mapper.fin;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.fin.FinSalaryDetail;
import com.luxuryadmin.vo.fin.VoCreateSalaryDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author Administrator
 */
@Mapper
public interface FinSalaryDetailMapper extends BaseMapper {


    /**
     * 获取薪资明细
     *
     * @param shopId
     * @param userId
     * @param startDate
     * @return
     */
    FinSalaryDetail getFinSalaryDetail(@Param("shopId") int shopId, @Param("userId") int userId,
                                       @Param("startDate") String startDate);

    /**
     * 根据月份查询该用户的薪资明细
     *
     * @param shopId
     * @param userId
     * @param startDate
     * @return
     */
    VoCreateSalaryDetail getVoCreateSalaryDetail(@Param("shopId") int shopId, @Param("userId") Integer userId,
                                                 @Param("startDate") String startDate);

    /**
     * 物理删除薪资明细; 用in查询
     *
     * @param finSalaryIds
     * @return
     */
    int deleteFinSalaryDetail(String finSalaryIds);


    /**
     * 删除薪资明细(用于注销店铺)
     *
     * @param shopId
     * @return
     */
    int deleteFinSalaryDetailByShopId(int shopId);

}