package com.luxuryadmin.mapper;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.MpVisitorRecord;
import com.luxuryadmin.param.visitor.ParamVisitorRecordList;
import com.luxuryadmin.vo.visitor.VOVisitorRecordList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 访客记录 dao
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:01
 */
@Mapper
public interface MpVisitorRecordMapper extends BaseMapper<MpVisitorRecord> {


    /**
     * 后台查询访客记录
     *
     * @param param
     * @return
     */
    List<VOVisitorRecordList> getVisitorRecord(ParamVisitorRecordList param);

    /**
     * 获取访问人次o
     *
     * @param param
     * @return
     */
    List<Integer> getVisitorPersonNum(ParamVisitorRecordList param);

}
