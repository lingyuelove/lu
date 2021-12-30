package com.luxuryadmin.service;

import com.luxuryadmin.param.visitor.ParamVisitorRecordList;
import com.luxuryadmin.vo.visitor.VOVisitorRecord;

/**
 * 访客记录 service
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:01
 */
public interface MpVisitorRecordService {

    /**
     * 后台查询访客记录
     *
     * @param param
     * @return
     */
    VOVisitorRecord getVisitorRecord(ParamVisitorRecordList param);
}
