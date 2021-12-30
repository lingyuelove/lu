package com.luxuryadmin.mapper.stat;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.stat.StatProdSaleClassify;
import com.luxuryadmin.vo.stat.VoStateProdSellClassify;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StatProdSaleClassifyMapper extends BaseMapper {

    /**
     * 根据classifyCode查询销售分类数据
     * @param classifyCode
     * @return
     */
    StatProdSaleClassify selectStatProdSaleByClassifyCode(String classifyCode);

    /**
     * 查询所有分类销售情况
     * @return
     */
    List<VoStateProdSellClassify> selectProdSellClassifyList(String showField);
}