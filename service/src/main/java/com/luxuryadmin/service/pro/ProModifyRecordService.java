package com.luxuryadmin.service.pro;


import com.luxuryadmin.entity.pro.ProModifyRecord;
import com.luxuryadmin.param.pro.ParamModifyRecordSearch;
import com.luxuryadmin.vo.pro.VoModifyRecordByPage;

import java.util.Date;
import java.util.List;

/**
 * 商品修改记录表 service
 *
 * @author zhangsai
 * @Date 2021-06-03 22:01:25
 */
public interface ProModifyRecordService {

    /**
     * 查询商品修改记录
     *
     * @param shopId
     * @param proId
     * @return
     */
    List<ProModifyRecord> listProModifyRecord(ParamModifyRecordSearch modifyRecordSearch);


    /**
     * 保存实体
     *
     * @param proModifyRecord
     */
    void saveProModifyRecord(ProModifyRecord proModifyRecord);

    VoModifyRecordByPage getModifyRecordByPage(ParamModifyRecordSearch modifyRecordSearch);
}
