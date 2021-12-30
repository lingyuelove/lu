package com.luxuryadmin.service.fin;

import com.github.pagehelper.PageInfo;
import com.luxuryadmin.entity.fin.FinShopRecord;
import com.luxuryadmin.enums.fin.EnumFinShopRecordType;
import com.luxuryadmin.param.fin.ParamFinShopRecordAdd;
import com.luxuryadmin.param.fin.ParamFinShopRecordQuery;
import com.luxuryadmin.param.fin.ParamFinShopRecordUpdate;
import com.luxuryadmin.vo.fin.VoFinShopRecord;
import com.luxuryadmin.vo.fin.VoFinShopRecordDetail;
import com.luxuryadmin.vo.fin.VoFinShopRecordHomePageTop;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 财务店铺流水记录Service
 */
public interface FinShopRecordService {

    /**
     * 新增一条流水记录
     * @param shopId
     * @param userId
     * @param paramFinShopRecordAdd
     * @return
     */
    FinShopRecord addFinShopRecord(Integer shopId, Integer userId, ParamFinShopRecordAdd paramFinShopRecordAdd,
                                   EnumFinShopRecordType enumType,String imgUrl,String fkOrderId);


    /**
     * 修改一条流水记录
     * @param paramFinShopRecordUpdate
     * @return
     */
    FinShopRecord updateFinShopRecord(ParamFinShopRecordUpdate paramFinShopRecordUpdate, HttpServletRequest request);

    /**
     * 逻辑删除一条流水记录
     * @param shopId
     * @param userId
     * @param recordId
     * @return
     */
    Integer deleteFinShopRecord(Integer shopId, Integer userId, int recordId, HttpServletRequest request);

    /**
     * 查询记账流水列表
     * @param shopId
     * @param paramFinShopRecordQuery
     * @return
     */
    List<List<VoFinShopRecord>> listFinShopRecordByShopId(Integer shopId, ParamFinShopRecordQuery paramFinShopRecordQuery) throws Exception;


    /**
     * 查询记账流水列表 用户pc端
     * @param shopId
     * @param paramFinShopRecordQuery
     * @return
     */
    PageInfo listFinShopRecordByShopIdForAdmin(Integer shopId, ParamFinShopRecordQuery paramFinShopRecordQuery) throws Exception;

    List<VoFinShopRecord> listFinShopRecordByShopIdForExp(ParamFinShopRecordQuery paramFinShopRecordQuery);

    /**
     * 获取记账流水首页顶部数据
     * @param shopId
     * @param paramFinShopRecordQuery
     * @return
     */
    VoFinShopRecordHomePageTop getFinShopRecordHomePageTop(Integer shopId, ParamFinShopRecordQuery paramFinShopRecordQuery) throws Exception;

    /**
     * 根据ID获取流水详情
     * @param shopId
     * @param id
     * @return
     */
    VoFinShopRecordDetail getFinShopRecordDetailById(Integer shopId, int id);

    /**
     * 根据日期范围删除店铺账单
     * @param shopId
     * @param startTime
     * @param endTime
     */
    void deleteFinShopRecordByDateRange(Integer shopId,String startTime,String endTime);
}
