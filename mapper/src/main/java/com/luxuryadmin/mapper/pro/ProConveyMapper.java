package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProConvey;
import com.luxuryadmin.param.pro.ParamConveyQuery;
import com.luxuryadmin.vo.pro.VoConvey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 *商品传送表 dao
 *@author zhangsai
 *@Date 2021-11-22 15:12:44
 */
@Mapper
public interface ProConveyMapper  extends BaseMapper<ProConvey> {

    /**
     * 寄卖传送集合显示
     * @param paramConveyQuery
     * @return
     */
    List<VoConvey> listConvey(ParamConveyQuery paramConveyQuery);

    /**
     * 根据编号获取寄卖传送详情
     * @param number
     * @return
     */
    ProConvey getByNumber(String number);
}
