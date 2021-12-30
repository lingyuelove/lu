package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProProductClassify;
import com.luxuryadmin.param.pro.ParamClassifyTypeSearch;
import com.luxuryadmin.vo.pro.VoClassifyTypeList;
import com.luxuryadmin.vo.pro.VoClassifyTypeSonList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 *商品补充信息关联表 dao
 *@author zhangsai
 *@Date 2021-08-03 10:45:15
 */
@Mapper
public interface ProProductClassifyMapper  extends BaseMapper<ProProductClassify> {

    /**
     * 商品补充信息集合显示 废弃
     * @param classifyTypeSearch
     * @return
     */
//    List<VoClassifyTypeList> getClassifyTypeList(ParamClassifyTypeSearch classifyTypeSearch);

    /**
     * 商品补充信息二级集合显示
     * @param classifyTypeSearch
     * @return
     */
    List<VoClassifyTypeSonList> getClassifyTypeSonList(ParamClassifyTypeSearch classifyTypeSearch);

    /**
     * 根基商品id 假删
     * @param productId
     */
    void updateDelByProductId(Integer productId);

    /**
     * 根基商品id 真删 编辑商品后使用删除无用值
     * @param productId
     */
    void delByProductId(Integer productId);

    /**
     * 根据商品id,补充信息一级id,补充信息二级名称查重
     * @param productId
     * @param classifyTypeId
     * @param classifyTypeDetailName
     * @return
     */
    ProProductClassify getProductClassifyBySearch(@Param("productId") Integer productId,@Param("classifyTypeId") Integer classifyTypeId,@Param("classifyTypeDetailName") String classifyTypeDetailName);
}
