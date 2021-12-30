package com.luxuryadmin.mapper.op;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.op.DaEFenQiKa;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DaEFenQiKaMapper extends BaseMapper {

    //int saveBatch(List<DaEFenQiKa> list);

    List<DaEFenQiKa> getCellphoneLocation(int offset,int pageSize);

    int updateDaEFenQiKaList(List<DaEFenQiKa> list);

}