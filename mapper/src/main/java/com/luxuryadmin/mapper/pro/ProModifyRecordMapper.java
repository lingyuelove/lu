package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProModifyRecord;
import com.luxuryadmin.param.pro.ParamModifyRecordSearch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/**
 * 商品修改记录表 dao
 *
 * @author zhangsai
 * @Date 2021-06-03 22:01:25
 */
@Mapper
public interface ProModifyRecordMapper extends BaseMapper {


    /**
     * 查询商品修改记录
     *
     * @param shopId
     * @param proId
     * @return
     */
    List<ProModifyRecord> listProModifyRecord(ParamModifyRecordSearch modifyRecordSearch);


}
