package com.luxuryadmin.mapper.fin;

import com.luxuryadmin.entity.fin.FinShopRecord;
import com.luxuryadmin.param.fin.ParamFinShopRecordQuery;
import com.luxuryadmin.vo.fin.VoFinShopRecordHomePageTop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FinShopRecordMapper {
    int deleteObject(Integer id);

    int insert(FinShopRecord record);

    int saveObject(FinShopRecord record);

    FinShopRecord getObjectById(Integer id);

    int updateObject(FinShopRecord record);

    int updateByPrimaryKey(FinShopRecord record);

    FinShopRecord selectFinShopRecordById(@Param("shopId") Integer shopId, @Param("id") int id);

    /**
     * 根据店铺ID查询记账流水列表
     *
     * @param paramFinShopRecordQuery
     * @return
     */
    List<FinShopRecord> listFinShopRecordByShopId(ParamFinShopRecordQuery paramFinShopRecordQuery);

    /**
     * 根据店铺ID查询记账流水首页顶部数据
     *
     * @param paramFinShopRecordQuery
     * @return
     */
    VoFinShopRecordHomePageTop selectFinShopRecordHomePageTop(ParamFinShopRecordQuery paramFinShopRecordQuery);

    /**
     * 删除指定时间范围内的店铺账单记录
     * @param shopId
     * @param startTime
     * @param endTime
     */
    void delFinShopRecordByDateRange(@Param("shopId") Integer shopId, @Param("startTime") String startTime, @Param("endTime") String endTime);
}