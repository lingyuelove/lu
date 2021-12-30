package com.luxuryadmin.service.shp;

import com.luxuryadmin.entity.shp.ShpNumberRecord;
import com.luxuryadmin.enums.shp.EnumNumberRecordType;

/**
 * 用户编号随机号码池;<br/>
 * 用户编号在此业务中获取;
 *
 * @author monkey king
 * @date 2019-12-19 16:37:38
 */
public interface ShpNumberRecordService {

    /**
     * 批量插入新生成未使用的随机用户编号;<br/>
     * 增加号码池容量;
     *
     * @param numberTypeEnum {@link EnumNumberRecordType}
     * @return
     */
    ShpNumberRecord getLastGenerateRecord(EnumNumberRecordType numberTypeEnum);

    /**
     * 添加记录
     * @param shpNumberRecord
     * @return
     */
    int SaveShpNumberRecord(ShpNumberRecord shpNumberRecord);
}
